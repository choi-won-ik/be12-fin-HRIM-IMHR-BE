package com.example.be12hrimimhrbe.domain.score;

import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.score.model.Score;
import com.example.be12hrimimhrbe.domain.score.model.ScoreDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreService {
    private final ScoreRepository scoreRepository;

    public BaseResponse<ScoreDto.DashBoardRsp> dashboard(Member member) {
        List<Score> scores=scoreRepository.findByMemberIdx(member.getIdx());
        List<ScoreDto.ChangeScoreRsp> list = new ArrayList<>();
        String companyName = null;
        for (Score score : scores) {
            if (companyName==null) {
                companyName= score.getCompany().getName();
            }
            ScoreDto.ChangeScoreRsp dto= new ScoreDto.ChangeScoreRsp(score);
            list.add(dto);
        }

        list.sort(Comparator.comparingInt(ScoreDto.ChangeScoreRsp::getYear));

        ScoreDto.DashBoardRsp result=ScoreDto.DashBoardRsp.builder()
                .companyName(companyName)
                .changeScoreRsp(list)
                .build();
        System.out.println("실행");
        return new BaseResponse<>(BaseResponseMessage.COMPANY_ALL_LIST_SUCCESS, result);
    }
}
