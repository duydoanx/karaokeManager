package com.karaoke.manager.utils;

import com.karaoke.manager.entity.Order;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

@Getter
@Setter
public class OrderPDFExporter {
  private Order order;
  private String brandName;

  public OrderPDFExporter(Order order, String brandName) {
    this.order = order;
    this.brandName = brandName;
  }

  private void writeTableHeader(PdfPTable table) {
    PdfPCell cell = new PdfPCell();
    cell.setBackgroundColor(Color.WHITE);
    cell.setPadding(5);

    Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
    font.setColor(Color.BLACK);

    cell.setPhrase(new Phrase("Tên"));
    cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
    table.addCell(cell);

    cell.setPhrase(new Phrase("SL"));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Giá"));
    table.addCell(cell);

    cell.setPhrase(new Phrase("T.Tiền (VND)"));
    table.addCell(cell);
  }

  private void writeTableData(PdfPTable table) {
    table.addCell("Số giờ");
    Double numberHoursBooked = order.getNumberHoursBooked();
    table.addCell(numberHoursBooked.toString());

    Double price = order.getRoomBooking().getRoom().getRoomType().getPrice();
    table.addCell(price.toString());

    table.addCell(String.format("%,.2f", numberHoursBooked * price) + "");
    order
        .getProductOrderedHistories()
        .forEach(
            productOrderedHistory -> {
              table.addCell(productOrderedHistory.getProduct().getName());
              table.addCell(productOrderedHistory.getQuantity().toString());
              table.addCell(productOrderedHistory.getPrice().toString());
              table.addCell(
                  productOrderedHistory.getPrice() * productOrderedHistory.getQuantity() + "");
            });
  }

  public void export(HttpServletResponse response) throws IOException {
    Document document = new Document(PageSize.A4);
    PdfWriter.getInstance(document, response.getOutputStream());
    document.open();
    Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
    font.setSize(18);
    font.setColor(Color.BLACK);

    Paragraph header = new Paragraph(brandName);
    header.setAlignment(Paragraph.ALIGN_CENTER);

    Paragraph info =
        new Paragraph(
            "\n"
                + "\nMã hoá đơn: "
                + order.getId()
                + "\nNgày tạo: "
                + order.getCreatedAt().toString()
                + "\nNhân viên: "
                + order.getRoomBooking().getStaff().getLastName()
                + " "
                + order.getRoomBooking().getStaff().getFirstName()
                + "\n");
    info.setAlignment(Paragraph.ALIGN_LEFT);

    document.add(header);
    document.add(info);

    PdfPTable table = new PdfPTable(4);
    table.setWidthPercentage(100f);
    table.setWidths(new float[] {3.5f, 1.5f, 3.0f, 3.0f});
    table.setSpacingBefore(10);

    writeTableHeader(table);
    writeTableData(table);

    document.add(table);

    Paragraph belowTable =
        new Paragraph(
            "Giảm giá theo phần trăm: "
                + order.getDiscountPercent()
                + "%"
                + "\nGiảm giá trực tiếp: "
                + order.getDiscountMoney()
                + " VND"
                + "\nTổng tiền cần thanh toán: "
                + String.format("%,.2f", order.getTotal())
                + " VND");
    document.add(belowTable);
    document.close();
  }
}
