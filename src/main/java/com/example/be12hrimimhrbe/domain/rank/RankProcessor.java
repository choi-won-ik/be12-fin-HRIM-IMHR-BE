package com.example.be12hrimimhrbe.domain.rank;

import com.example.be12hrimimhrbe.domain.rank.model.Rank;
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
