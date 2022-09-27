package com.cts.company.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.company.model.Company;
import com.cts.company.repository.CompanyRepository;
import com.cts.company.response.CompanySaveResponse;
import com.cts.company.response.CompanyStockResponse;
import com.cts.company.service.CompanyService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/v1.0/market/company")
public class CompController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @PostMapping("/register")
    public ResponseEntity<CompanySaveResponse> saveCompany(@RequestBody Company company) {
        log.info("User requested to register the company: " + company.getCompanyName());
        CompanySaveResponse companySaveResponse = companyService.register(company);
        return new ResponseEntity<>(companySaveResponse, companySaveResponse.getHttpstatus());
    }

    @GetMapping("/info/{company_code}")
    public CompanyStockResponse getCompanyWithStock(@PathVariable("company_code") String companyCode) {
        return companyService.getCompanyWithStock(companyCode);
    }

    @GetMapping("/getall")
    public List<Company> getAllCompanyDetails() {
        return companyRepository.findAll();
    }

    @GetMapping("/getAllCompaniesWithStocks")
    public List<CompanyStockResponse> getAllCompaniesWithStocks() {
        return companyService.getAllCompaniesWithStocks();
    }

    @DeleteMapping("/delete/{company_code}")
    public void deleteCompanyWithStock(@PathVariable("company_code") String companyCode) {
        companyService.deleteCompanyWithStock(companyCode);
    }
}
