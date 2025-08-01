package com.finsight.backend.control;

import com.finsight.backend.service.UserViewLogger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final UserViewLogger logger;

    public NewsController(UserViewLogger logger) {
        this.logger = logger;
    }

    @PostMapping("/click")
    public ResponseEntity<Void> logClick(
            @RequestParam String newsId
    ) {
        // 🔥 로그인된 사용자 ID 추출
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        logger.logNewsClick(userId, newsId);
        return ResponseEntity.ok().build();
    }
}
