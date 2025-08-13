package com.finsight.backend.service;

import com.finsight.backend.dto.request.TradeRequest;
import com.finsight.backend.repository.mapper.HistoryMapper;
import com.finsight.backend.repository.mapper.HoldingsMapper;
import com.finsight.backend.domain.vo.user.HistoryVO;
import com.finsight.backend.domain.vo.user.HoldingsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeService {

    private final HoldingsMapper holdingsMapper;
    private final HistoryMapper historyMapper;

    @Transactional
    public void processTrade(TradeRequest req, String tradeType) {
        boolean isBuy = tradeType.equalsIgnoreCase("buy");

        HoldingsVO holdings = holdingsMapper.findByUserAndProduct(req.getUserId(), req.getProductCode());

        if (holdings == null) {
            if (!isBuy) throw new IllegalArgumentException("보유 내역이 없습니다.");
            holdings = new HoldingsVO(req);
            holdingsMapper.insert(holdings);
        } else {
            int newQty = holdings.getHoldingsTotalQuantity() + (isBuy ? req.getQuantity() : -req.getQuantity());
            BigDecimal newAmt;
            if (isBuy) {
                newAmt = holdings.getHoldingsTotalPrice().add(req.getAmount());
            } else {
                newAmt = holdings.getHoldingsTotalPrice().subtract(req.getAmount());
            }

            // 판매 시에만 보유 수량 체크 (보유하지 않은 상품은 판매 불가)
            if (!isBuy && holdings.getHoldingsTotalQuantity() < req.getQuantity()) {
                throw new IllegalStateException("보유 수량이 부족합니다. 보유: " + holdings.getHoldingsTotalQuantity() + ", 판매요청: " + req.getQuantity());
            }

            holdings.setHoldingsTotalQuantity(newQty);
            holdings.setHoldingsTotalPrice(newAmt);
            holdings.setHoldingsStatus(newQty == 0 ? "zero" : "holding");

            holdingsMapper.update(holdings);
        }

        // History 테이블에 거래 내역 저장 (contractMonths 포함)
        HistoryVO history = new HistoryVO(req, holdings.getHoldingsId(), tradeType);
        historyMapper.insert(history);
        
        log.info("거래 처리 완료: userId={}, productCode={}, tradeType={}, quantity={}, amount={}, contractMonths={}", 
                req.getUserId(), req.getProductCode(), tradeType, req.getQuantity(), req.getAmount(), req.getContractMonths());
    }
}

