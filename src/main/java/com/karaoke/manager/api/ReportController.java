package com.karaoke.manager.api;

import com.karaoke.manager.entity.support.ResponseApi;
import com.karaoke.manager.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

  private final OrderService orderService;

  // API xem thống kê
  @GetMapping
  public ResponseApi<?> report(@RequestBody Map<String, String> time) throws ParseException {
    String regex =
        "^\\d\\d\\d\\d-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])T(00|[0-9]|1[0-9]|2[0-3]):([0-9]|[0-5][0-9]):([0-9]|[0-5][0-9])$";
    if (!time.containsKey("startTime")) {
      throw new RuntimeException("Unable to find start time.");
    }
    if (!time.containsKey("endTime")) {
      throw new RuntimeException("Unable to find end time.");
    }
    String startTimeStr = time.get("startTime");
    String endTimeStr = time.get("endTime");
    if (!startTimeStr.matches(regex) || !endTimeStr.matches(regex)) {
      throw new RuntimeException("Time format must be yyyy-MM-dd'T'HH:mm:ss");
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    Timestamp startTime = new Timestamp(dateFormat.parse(startTimeStr).getTime());
    Timestamp endTime = new Timestamp(dateFormat.parse(endTimeStr).getTime());
    Double revenue = orderService.revenueAroundTime(startTime, endTime);

    Map<String, String> map =
        Collections.singletonMap("revenue", BigDecimal.valueOf(revenue).toPlainString());
    return new ResponseApi<>(HttpStatus.OK.value(), map);
  }
}
