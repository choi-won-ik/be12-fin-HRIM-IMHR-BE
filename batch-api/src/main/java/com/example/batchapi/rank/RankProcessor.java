package com.example.batchapi.rank;

import com.example.batchapi.company.model.Company;
import com.example.batchapi.rank.model.Rank;
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
