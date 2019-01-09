package org.big.common;

import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static org.springframework.web.servlet.support.RequestContextUtils.getLocaleResolver;

/**
 * @Author: WangTianshan(王天山)
 * @Description:
 * @Created Date: 2017/11/6 15:56
 * @Modified By:
 * @Last Modified Date:
 */
public class GetLocale {

    public static Locale getLocale (HttpServletRequest request) {
        LocaleResolver localeResolver = getLocaleResolver (request);
        if (localeResolver != null ) {
            return localeResolver.resolveLocale(request);
        }
        else {
            return request.getLocale();
        }
    }
}
