package com.example.batchapi.rank;

import com.example.batchapi.rank.model.Rank;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RankProcessor implements ItemProcessor<Rank, Rank> {

    private final RankService rankService;


    @Override
    public Rank process(Rank item) throws Exception {
        Rank rank=rankService.rankAggregater(item);
        return rank;
    }
}
