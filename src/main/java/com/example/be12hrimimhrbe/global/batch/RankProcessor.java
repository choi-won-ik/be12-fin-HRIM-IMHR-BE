package com.example.be12hrimimhrbe.global.batch;


import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.rank.Rank;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RankProcessor implements ItemProcessor<Company, List<Rank>> {

    private final RankService rankService;


    @Override
    public List<Rank> process(Company item) throws Exception {
        List<Rank> result=rankService.rankAggregater(item);
        return result;
    }
}
