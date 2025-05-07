package com.example.be12hrimimhrbe.domain.activitySubject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activitySubject")
public class ActivitySubjectController {
    private final ActivitySubjectService activitySubjectService;
}
