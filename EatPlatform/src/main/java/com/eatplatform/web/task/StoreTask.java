package com.eatplatform.web.task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eatplatform.web.persistence.ReviewMapper;
import com.eatplatform.web.persistence.StoreMapper;

import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class StoreTask {
	
	@Autowired
	StoreMapper storeMapper;
	
	@Autowired
	ReviewMapper reviewMapper;


    // 매 정각 별점 갱신
    @Scheduled(cron = "0 */46 * * * *") 
    public void updateStoreScores() {
        List<Long> storeIds = getAllStoreIds(); 

        for (Long storeId : storeIds) {
            BigDecimal averageRating = reviewMapper.selectAverageStarByStoreId(storeId);
            
            if (averageRating != null) {
                BigDecimal roundedRating = averageRating.setScale(1, RoundingMode.HALF_UP);
                storeMapper.updateStoreScore(storeId, roundedRating);

            }
        }
    }

    private List<Long> getAllStoreIds() {
        return storeMapper.selectAllStoreIds();
    }
}
