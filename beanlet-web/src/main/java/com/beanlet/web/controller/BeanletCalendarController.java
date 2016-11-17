package com.beanlet.web.controller;

import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import com.beanlet.web.service.BeanletCalendar;
import com.beanlet.web.service.BeanletCalendarService;
import com.beanlet.web.service.BeanletService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BeanletCalendarController {

  @Autowired
  private BeanletCalendarService beanletCalendarService;

  @GetMapping("/beanlets/{beanletId}/calendar")
  public BeanletCalendar countIt(@PathVariable EntityId<Beanlet> beanletId,
                                 @RequestParam(required = false) DateTimeZone timeZone,
                                 @RequestParam(required = false) Integer year,
                                 @RequestParam(required = false) Integer month,
                                 @AuthenticationPrincipal User user) {
    BeanletCalendar beanletCalendar;

    if (year != null && month != null) {
      beanletCalendar = beanletCalendarService.getBeanletCalendar(year, month);
    } else if (timeZone != null) {
      DateTime localDateTime = new DateTime(timeZone);
      beanletCalendar = beanletCalendarService.getBeanletCalendar(
        localDateTime.getYear(),
        localDateTime.getMonthOfYear());
    } else {
      beanletCalendar = new BeanletCalendar.Builder().getCalendar();
    }

    return beanletCalendar;
  }

}