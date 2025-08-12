package com.finsight.backend.service;

import com.finsight.backend.dto.request.TradeRequest;
import com.finsight.backend.repository.mapper.HistoryMapper;
import com.finsight.backend.repository.mapper.HoldingsMapper;
import com.finsight.backend.domain.vo.user.HistoryVO;
import com.finsight.backend.domain.vo.user.HoldingsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TradeService {

    @Autowired
    private HoldingsMapper holdingsMapper;

    @Autowired
    private HistoryMapper historyMapper;

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
            long newAmt = holdings.getHoldingsTotalPrice() + (isBuy ? req.getAmount() : -req.getAmount());

            if (newQty < 0 || newAmt < 0) throw new IllegalStateException("보유 수량 또는 금액 부족");

            holdings.setHoldingsTotalQuantity(newQty);
            holdings.setHoldingsTotalPrice(newAmt);
            holdings.setHoldingsStatus(newQty == 0 ? "zero" : "holding");

            holdingsMapper.update(holdings);
        }

        HistoryVO history = new HistoryVO(req, holdings.getHoldingsId(), tradeType);
        historyMapper.insert(history);
    }
}

