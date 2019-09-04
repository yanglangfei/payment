package com.yanglf.payment.utils;

import com.alibaba.fastjson.JSONObject;
import net.sf.json.xml.XMLSerializer;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * @author yanglf
 */
public class SignUtil {

    /**
     * 报文签名
     *
     * @param trans 报文
     * @return
     */
    public static String transSign(SortedMap<String, Object> trans, String key) {
        StringBuffer sb = new StringBuffer();
        Set es = trans.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry) it.next();
            String k = entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        return SecurityUtil.encryptionWithMd5(sb.toString()).toUpperCase();
    }

}
