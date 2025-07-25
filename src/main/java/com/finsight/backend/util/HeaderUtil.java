package com.finsight.backend.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class HeaderUtil {
    public static Optional<String> refineHeader(HttpServletRequest httpServletRequest, String header, String prefix){
        String unpreparedToken = httpServletRequest.getHeader(header);

        if(!StringUtils.hasText(unpreparedToken) || !unpreparedToken.startsWith(prefix)){
            return Optional.empty();
        }

        return Optional.of(unpreparedToken.substring(prefix.length()));
    }
}
