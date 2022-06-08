package com.cp.correioprivadosite.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Slf4j
public class Utils {
    public static String getCookie(HttpServletRequest webRequest, String cookieName) {
        Cookie[] userCookies = webRequest.getCookies();

        if(userCookies != null) {
            log.info("Found {} Cookies", userCookies.length);

            for (Cookie cookie : userCookies)
                if (cookie.getName().equals(cookieName)) {
                    log.info("The cookie {} was found: {}", cookieName, cookie.getValue());
                    return cookie.getValue();
                }
        }

        log.info("The cookie {} was not found");
        return "";
    }
}
