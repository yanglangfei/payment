package com.yanglf.payment.controller;

import com.yanglf.payment.service.WeixinAppPayService;
import com.yanglf.payment.vo.WxPaySignVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RestController
@Slf4j
@RequestMapping("/payment")
public class PayController {

    @Autowired
    private WeixinAppPayService weixinAppPayService;

    @RequestMapping(value = "/weixin", method = RequestMethod.GET)
    public WxPaySignVO orderInit(@RequestParam Long orderId) {
        String paySeqNo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return weixinAppPayService.appPay(paySeqNo,String.valueOf(orderId), BigDecimal.valueOf(12.01),"XX充值中心","http://www.baidyu.com");
    }

}
