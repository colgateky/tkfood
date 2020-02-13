package com.tk.center.util;

import com.magic.utils.JsonUtils;
import com.magic.utils.http.HttpUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Wang Zhe on 2015/8/11.
 */
public class IpHelper {
    private static String ipLink = "http://ips.lg8888.org/";

    public static class IpInfo {
        public String province;
        public String city;
        public String country;
        public String isp;
    }

    public static IpInfo resolve(String ip) {
        HttpUtils httpUtils = new HttpUtils();
        String content = httpUtils.getAsString(ipLink + "ips?ips=" + ip, null ,null);
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        Map m = JsonUtils.parseJson(content, Map.class);
        if (m == null) {
            return null;
        }
        Object obj = m.get(ip);
        if (obj == null) {
            return null;
        }
        m = (Map)obj;
        IpInfo ret = new IpInfo();
        ret.province = (String)m.get("province");
        ret.city = (String)m.get("city");
        ret.country = (String)m.get("country");
        ret.isp = (String)m.get("isp");
        if (ret.province != null && ret.province.equals("台湾")) {
            ret.country = "台湾";
        }
        return ret;
    }

    public static String getIp(HttpServletRequest req){
        String ret;
        // 落地页传过来的真实ip优先
        String landPageIp = req.getHeader("X-Landpage-Real-IP");
        if (!StringUtils.isEmpty(landPageIp)) {
            return landPageIp;
        }
        String forwardIp = req.getHeader("X-Forwarded-For");
        if (forwardIp != null) {
            String[] items = forwardIp.trim().split(",");
            if (items.length > 0) {
                String ip = items[0].trim();
                if (StringUtils.isEmpty(ip) == false) {
                    return ip;
                }
            }
        }
        String realIp = req.getHeader("X-Real-IP");
        if (realIp != null && realIp.length() > 4) {
            ret = realIp.trim();
        } else {
            ret = req.getRemoteAddr();
        }
        return ret;
    }
}
