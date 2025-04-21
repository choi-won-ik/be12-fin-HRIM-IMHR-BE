package com.example.be12hrimimhrbe.domain.company;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
@Tag(name = "회사 관리 기능")
public class CompanyController {
    private final CompanyService companyService;


//---------위에는 Company /// 아래는 ESG_Company ----------------------------------------------------------------------------------
}
