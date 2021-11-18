package com.karaoke.manager.api.support;

import com.karaoke.manager.entity.Order;
import com.karaoke.manager.entity.OrderStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Component
public class OrderControllerSupport {
  public void setDiscount(@RequestBody Map<String, String> discountParam, Order order) {
    if (!order.getStatus().getCodeName().equals(OrderStatus.PENDING)) {
      throw new RuntimeException("Order status must be PENDING.");
    }
    boolean isChange = false;
    if (discountParam.containsKey("discountPercent")) {
      double discountPercent = Double.parseDouble(discountParam.get("discountPercent"));
      if (discountPercent > 100) {
        throw new RuntimeException("Discount percent must be less than or equal to 100.");
      }
      order.setDiscountPercent(discountPercent);
      isChange = true;
    }

    if (discountParam.containsKey("discountMoney")) {
      order.setDiscountMoney(Double.parseDouble(discountParam.get("discountMoney")));
      isChange = true;
    }

    if (!isChange) {
      throw new RuntimeException("Must have at least one of the two discount parameters.");
    }
  }
}
