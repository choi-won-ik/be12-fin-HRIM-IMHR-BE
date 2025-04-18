package com.example.be12hrimimhrbe.domain.feedback;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.feedback.model.FeedbackDto;
import com.example.be12hrimimhrbe.domain.feedback.model.FeedbackQuestion;
import com.example.be12hrimimhrbe.domain.feedback.model.FeedbackResponse;
import com.example.be12hrimimhrbe.domain.feedback.model.FeedbackTemplate;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackTemplateRepository feedbackTemplateRepository;
    private final FeedbackQuestionRepository feedbackQuestionRepository;
    private final FeedbackChoiceRepository feedbackChoiceRepository;
    private final FeedbackResponseRepository feedbackResponseRepository;
    private final MemberRepository memberRepository;

    public BaseResponse<FeedbackDto.FeedbackTemplateResponse> getFeedbackTemplate(Member customMember) {
        Member member = memberRepository.findById(customMember.getIdx()).orElse(null);
        if (member == null) {
            return new BaseResponse<>(BaseResponseMessage.MEMBER_SEARCH_NOT_FOUND, null);
        }
        List<FeedbackTemplate> templates = feedbackTemplateRepository
                                            .findAllByCompanyOrderByCreatedAtDesc(member.getCompany(), Limit.of(1));
        if (templates.isEmpty()) {
            return new BaseResponse<>(BaseResponseMessage.FEEDBACK_TEMPLATE_NOT_FOUND, null);
        }
        FeedbackTemplate template = templates.get(0);
        List<FeedbackDto.FeedbackQuestionItem> questions = template.getQuestions().stream()
                .map(question -> FeedbackDto.FeedbackQuestionItem.builder()
                        .questionIdx(question.getIdx())
                        .question(question.getQuestionText())
                        .type(question.getType())
                        .sortOrder(question.getSortOrder())
                        .choices(question.getChoices().stream()
                                .map(choice -> FeedbackDto.FeedbackChoiceItem.builder()
                                        .choiceIdx(choice.getIdx())
                                        .text(choice.getChoiceText())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return new BaseResponse<>(BaseResponseMessage.FEEDBACK_TEMPLATE_RETRIEVE_SUCCESS,
                FeedbackDto.FeedbackTemplateResponse.from(template.getIdx(), questions));
    }

    @Transactional
    public BaseResponse<String> submitFeedback(Member from, Member to, FeedbackDto.FeedbackAnswerRequest dto) {
        FeedbackTemplate template = feedbackTemplateRepository.findById(dto.getAnswers().get(0).getTemplateIdx())
                .orElse(null);
        if(template == null) {
            return new BaseResponse<>(BaseResponseMessage.FEEDBACK_TEMPLATE_NOT_FOUND, null);
        }

        dto.getAnswers().forEach(answer -> {
            FeedbackResponse response = FeedbackResponse.builder()
                    .feedback(template)
                    .question(feedbackQuestionRepository.findById(answer.getQuestionIdx())
                            .orElseThrow(() -> new RuntimeException("Question 찾지 못함")))
                    .choice(answer.getChoiceIdx() != null ? feedbackChoiceRepository.findById(answer.getChoiceIdx())
                            .orElseThrow(() -> new RuntimeException("Choice 찾지 못함")) : null)
                    .answerText(answer.getAnswer())
                    .from(from)
                    .to(to)
                    .build();
            feedbackResponseRepository.save(response);
        });

        return new BaseResponse<>(BaseResponseMessage.FEEDBACK_SUBMIT_SUCCESS, "피드백 작성에 성공했습니다.");
    }

    @Transactional
    public BaseResponse<String> modifyFeedbackTemplate(FeedbackDto.FeedbackModifyRequest dto) {
        FeedbackTemplate template = feedbackTemplateRepository.findById(dto.getTemplateIdx())
                .orElseThrow(() -> new RuntimeException("Template not found"));

        // Delete questions
        dto.getDeleteQuestions().forEach(question -> {
            feedbackQuestionRepository.deleteById(question.getQuestionIdx());
        });

        // Add new questions
        if(!dto.getInsertQuestions().isEmpty()) {
            dto.getInsertQuestions().forEach(question -> {
                FeedbackQuestion newQuestion = feedbackQuestionRepository.save(question.toEntity(template));
                if(question.getChoices() != null) {
                    question.getChoices().forEach(choice -> {
                        feedbackChoiceRepository.save(choice.toEntity(newQuestion));
                    });
                }
            });
        }

        return new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, "Template modified successfully");
    }

    @Transactional
    public BaseResponse<String> createFeedbackTemplate(Member customMember, FeedbackDto.FeedbackModifyRequest dto) {
        Member member = memberRepository.findById(customMember.getIdx()).orElse(null);
        if(member == null) {
            return new BaseResponse<>(BaseResponseMessage.MEMBER_SEARCH_NOT_FOUND, null);
        }
        Company company = member.getCompany();
        FeedbackTemplate template = FeedbackTemplate.builder()
                .company(company)
                .createdAt(LocalDateTime.now())
                .build();
        feedbackTemplateRepository.save(template);
        if(!dto.getInsertQuestions().isEmpty()) {
            dto.getInsertQuestions().forEach(question -> {
                FeedbackQuestion newQuestion = feedbackQuestionRepository.save(question.toEntity(template));
                if(question.getChoices() != null) {
                    question.getChoices().forEach(choice -> {
                        feedbackChoiceRepository.save(choice.toEntity(newQuestion));
                    });
                }
            });
        }

        return new BaseResponse<>(BaseResponseMessage.FEEDBACK_TEMPLATE_CREATE_SUCCESS, "피드백 양식 생성 성공");
    }
}