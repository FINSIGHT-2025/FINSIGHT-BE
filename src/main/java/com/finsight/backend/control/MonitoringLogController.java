package com.finsight.backend.control;

import com.finsight.backend.dto.request.MonitoringRequest;
import com.finsight.backend.dto.response.ApiResponse;
import com.finsight.backend.service.MonitoringLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/log")
public class MonitoringLogController {

    private final MonitoringLogService monitoringLogService;

    @PostMapping("/save")
    public ResponseEntity<?> userMonitoring(@Valid @RequestBody MonitoringRequest request){
        try {
            monitoringLogService.saveProductUserLog(request);
            log.info("저장 성공: {}", request.getUserId());
        } catch (Exception e) {
            log.error("MonitoringLog 저장 실패", e);
            throw new RuntimeException("저장 중 오류 발생", e);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(Boolean.TRUE, "모니터링 저장 성공", null));
    }
}
