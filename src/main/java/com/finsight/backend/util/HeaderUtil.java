package com.finsight.backend.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
public class HeaderUtil {
    public static Optional<String> refineHeader(HttpServletRequest httpServletRequest, String header, String prefix){
        String unpreparedToken = httpServletRequest.getHeader(header);
        log.info("unpreparedToken : {}", unpreparedToken);

        if(!StringUtils.hasText(unpreparedToken) || !unpreparedToken.startsWith(prefix)){
            return Optional.empty();
        }

        return Optional.of(unpreparedToken.substring(prefix.length()));
    }
}
