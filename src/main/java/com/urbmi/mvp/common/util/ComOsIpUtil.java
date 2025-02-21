package com.urbmi.mvp.common.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;

/**
 * @author nive
 * @class ComOsIpUtil
 * @desc OS와 IP를 추출하는 util
 * @since 2025-01-31
 */
public class ComOsIpUtil {


    public static String getOsInfo(String agent) {
        String os = "";
        if(!Objects.isNull(agent) && !agent.isBlank()){

            if (agent.indexOf("NT 10.0") != -1) os = "Windows 10";
            else if (agent.indexOf("NT 6.3") != -1) os = "Windows 8.1";
            else if (agent.indexOf("NT 6.2") != -1) os = "Windows 8";
            else if (agent.indexOf("NT 6.1") != -1) os = "Windows 7";
            else if (agent.indexOf("NT 6.0") != -1) os = "Windows Vista/Server 2008";
            else if (agent.indexOf("NT 5.2") != -1) os = "Windows Server 2003";
            else if (agent.indexOf("NT 5.1") != -1) os = "Windows XP";
            else if (agent.indexOf("NT 5.0") != -1) os = "Windows 2000";
            else if (agent.indexOf("NT") != -1) os = "Windows NT";
            else if (agent.indexOf("9x 4.90") != -1) os = "Windows Me";
            else if (agent.indexOf("98") != -1) os = "Windows 98";
            else if (agent.indexOf("95") != -1) os = "Windows 95";
            else if (agent.indexOf("Win16") != -1) os = "Windows 3.x";
            else if (agent.indexOf("Windows") != -1) os = "Windows";
            else if (agent.indexOf("Linux") != -1) os = "Linux";
            else if (agent.indexOf("Ubuntu") != -1) os = "Ubuntu";
            else if (agent.indexOf("Fedora") != -1) os = "Fedora";
            else if (agent.indexOf("Debian") != -1) os = "Debian";
            else if (agent.indexOf("CentOS") != -1) os = "CentOS";
            else if (agent.indexOf("Red Hat") != -1) os = "Red Hat";
            else if (agent.indexOf("Macintosh") != -1) os = "Macintosh";
            else if (agent.indexOf("mac os") != -1) os = "Mac";
            else if (agent.indexOf("Android") != -1) os = "Android";
            else if (agent.indexOf("iPhone") != -1 || agent.indexOf("iPad") != -1) os = "iOS";
            else if (agent.indexOf("CrOS") != -1) os = "Chrome OS";
            else if (agent.indexOf("FreeBSD") != -1) os = "FreeBSD";
            else os = ""; // 기본값
        }

        return os;
    }

    public static String getIpAddr(HttpServletRequest request) throws Exception {

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
