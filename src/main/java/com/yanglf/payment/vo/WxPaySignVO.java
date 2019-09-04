package com.yanglf.payment.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPaySignVO {
    /**
     * 请求结果 0=失败;1=成功
     */
    private String code;
    /**
     * 请求返回内容
     */
    private String msg;
    /**
     * 请求报文
     */
    private String reqMsg;
    /**
     * 响应报文
     */
    private String respMsg;


    private String appid;
    private String partnerid;
    private String prepayid;
    private String nonceStr;
    private String timeStamp;
    private String sign;
}
