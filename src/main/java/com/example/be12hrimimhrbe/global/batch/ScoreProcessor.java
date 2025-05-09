package com.example.be12hrimimhrbe.global.batch;

import com.example.be12hrimimhrbe.domain.department.DepartmentService;
import com.example.be12hrimimhrbe.domain.department.model.Department;
import com.example.be12hrimimhrbe.domain.department.model.DepartmentScore;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScoreProcessor implements ItemProcessor<Department, DepartmentScore> {

    private final DepartmentService departmentService;

    @Override
    public DepartmentScore process(Department item) throws Exception {
        DepartmentScore result = departmentService.score(item);
        return result;
    }
}
