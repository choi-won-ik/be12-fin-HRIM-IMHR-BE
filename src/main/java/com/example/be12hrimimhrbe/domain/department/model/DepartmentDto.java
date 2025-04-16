package com.example.be12hrimimhrbe.domain.department.model;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class DepartmentDto {

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class DepartmentListResponse{
        private List<Department> departments;
        public static DepartmentListResponse fromDepartments(List<Department> departments){
            return new DepartmentListResponse(departments);
        }
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CDRequest {
        private List<CreateRequest> createRequests;
        private List<DeleteRequest> deleteRequests;
        public List<Department> toCreateEntity(Company company) {
            List<Department> departments = new ArrayList<>();
            for (CreateRequest createRequest : createRequests) {
                departments.add(Department.builder()
                                .name(createRequest.name)
                                .company(company)
                        .build()
                );
            }
            return departments;
        }
        public List<Department> toDeleteEntity() {
            List<Department> departments = new ArrayList<>();
            for (DeleteRequest deleteRequest : deleteRequests) {
                departments.add(Department.builder().idx(deleteRequest.idx).build());
            }
            return departments;
        }
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CreateRequest{
        private String name;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class DeleteRequest{
        private Long idx;
    }
}
