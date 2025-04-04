package com.example.be12hrimimhrbe.domain.department.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class DepartmentDto {

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class DepartmentListResponse{
        private List<Department> departments;
    }
}
