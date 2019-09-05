package com.yanglf.payment.service;

import com.yanglf.payment.utils.Constant;
import com.yanglf.payment.utils.HttpUtils;
import com.yanglf.payment.utils.SignUtil;
import com.yanglf.payment.utils.XmlUtil;
import com.yanglf.payment.vo.WxPaySignVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author yanglf
 */
@Component
@Slf4j
public class WeixinAppPayService {

    @Value("${define.weixin.app.appid}")
    protected String appid;
    @Value("${define.weixin.app.mch_id}")
    protected String mchId;
    @Value("${define.weixin.app.key}")
    protected String key;

    /**
     * 获取支付签名
     * 流程参照 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1
     *
     * @param paySeqNo  商户订单号
     * @param orderId   附加参数-订单ID
     * @param orderAmt  支付金额
     * @param notifyUrl 回调URL
     * @return
     */
    public WxPaySignVO appPay(String paySeqNo,
                              String orderId,
                              BigDecimal orderAmt,
                              String msg,
                              String notifyUrl) {
        WxPaySignVO vo = new WxPaySignVO();
        SortedMap<String, Object> prePayTrans = new TreeMap<>();
        //应用ID
        prePayTrans.put("appid", appid);
        //附加数据 (订单ID)
        prePayTrans.put("attach", orderId);
        //商品描述
        prePayTrans.put("body", msg);
        //商户号
        prePayTrans.put("mch_id", mchId);
        //随机字符串
        prePayTrans.put("nonce_str", UUID.randomUUID().toString().replace("-", ""));
        //通知地址
        prePayTrans.put("notify_url", notifyUrl);
        //商户订单号（支付流水号），不可重复
        prePayTrans.put("out_trade_no", paySeqNo);
        //总金额 Int 订单总金额，单位为分
        prePayTrans.put("total_fee", orderAmt.multiply(new BigDecimal(100)).intValue());
        // 调用微信支付API的机器IP
        prePayTrans.put("spbill_create_ip","192.168.015.12");
        //交易类型
        prePayTrans.put("trade_type", "APP");
        //报文签名  签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
        prePayTrans.put("sign_type", "MD5");
        String prePaySign = SignUtil.transSign(prePayTrans, key);
        prePayTrans.put("sign", prePaySign);
        //报文转换为xml
        String transXml = XmlUtil.payMapToXml(prePayTrans);
        System.out.println("发送微信预支付报文:" + transXml);
        vo.setReqMsg(transXml);
        // 发送微信预支付
        String wxPayResp = HttpUtils.httpRequest(Constant.UNIFIED_ORDER,
                "POST", transXml);
        log.info("接收微信预支付报文:[{}]", wxPayResp);
        vo.setRespMsg(wxPayResp);
        Map<String, String> result;
        try {
            result = XmlUtil.doXMLParse(wxPayResp);
        } catch (Exception e) {
            vo.setCode("0");
            vo.setMsg("微信支付通讯失败");
            return vo;
        }

        if (StringUtils.isEmpty(result)) {
            vo.setCode("0");
            vo.setMsg("微信支付通讯失败");
            return vo;
        }

        //通讯校验
        if (!(result.get("return_code")).equalsIgnoreCase("SUCCESS")) {
            vo.setCode("0");
            vo.setMsg("微信支付通讯失败");
            return vo;
        }
        //业务操作校验
        if (!(result.get("result_code")).equalsIgnoreCase("SUCCESS")) {
            vo.setCode("0");
            vo.setMsg("生成微信支付预定单失败");
            return vo;
        }
        vo.setCode("1");

        String prepayId = result.get("prepay_id");

        //生成给APP的预定单报文
        SortedMap<String, Object> payTrans = new TreeMap<>();
        payTrans.put("appid", appid);
        payTrans.put("partnerid", mchId);
        payTrans.put("prepayid", prepayId);
        payTrans.put("package", "Sign=WXPay");
        String nonceStr = UUID.randomUUID().toString().replace("-", "");
        payTrans.put("noncestr", nonceStr);
        String timeStamp = Long.toString(System.currentTimeMillis() / 1000L);
        payTrans.put("timestamp", timeStamp);
        //对报文签名
        String sign = SignUtil.transSign(payTrans, key);

        vo.setAppid(appid);
        vo.setPartnerid(mchId);
        vo.setPrepayid(prepayId);
        vo.setNonceStr(nonceStr);
        vo.setTimeStamp(timeStamp);
        vo.setSign(sign);
        return vo;
    }


}
