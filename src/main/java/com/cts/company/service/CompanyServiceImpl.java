package com.cts.company.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cts.company.model.Company;
import com.cts.company.repository.CompanyRepository;
import com.cts.company.response.CompanySaveResponse;
import com.cts.company.response.CompanyStockResponse;
import com.cts.company.response.Stock;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public CompanySaveResponse register(Company company) {
        CompanySaveResponse companySaveResponse = new CompanySaveResponse();
        if (company != null && company.getCompanyTurnover() >= 100000000) {
            Company savedCompany = companyRepository.save(company);
            if (savedCompany != null) {
                companySaveResponse.setMessage("Company saved successfully!!");
                companySaveResponse.setStatus(HttpStatus.OK.toString());
                companySaveResponse.setHttpstatus(HttpStatus.OK);
                companySaveResponse.setCompany(savedCompany);
            } else {
                companySaveResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                companySaveResponse.setHttpstatus(HttpStatus.INTERNAL_SERVER_ERROR);
                companySaveResponse.setMessage("Company not saved successfully!!");
            }
        } else {
            companySaveResponse.setMessage("Company Turnover must be greater than 10cr");
            companySaveResponse.setHttpstatus(HttpStatus.BAD_REQUEST);
            companySaveResponse.setStatus(HttpStatus.BAD_REQUEST.name());
        }
        return companySaveResponse;
    }

    public CompanyStockResponse getCompanyWithStock(String companyCode) {
        Company company = companyRepository.findByCompanyCode(companyCode);
        CompanyStockResponse companyStockResponse = new CompanyStockResponse();
        if (company != null) {
            Stock stock = restTemplate.getForObject("http://localhost:8082/api/v1.0/market/stock/" + companyCode,
                    Stock.class);
            companyStockResponse.setCompany(company);
            companyStockResponse.setStock(stock);
            return companyStockResponse;
        }
        return companyStockResponse;
    }

    public List<CompanyStockResponse> getAllCompaniesWithStocks() {
        List<Company> companyList = companyRepository.findAll();
        List<CompanyStockResponse> companyStockList = new ArrayList<>();
        for (Company company : companyList) {
            CompanyStockResponse companyStock = new CompanyStockResponse();
            companyStock.setCompany(company);
            Stock stock = restTemplate.getForObject(
                    "http://localhost:8082/api/v1.0/market/stock/" + company.getCompanyCode(), Stock.class);
            companyStock.setStock(stock);
            companyStockList.add(companyStock);
        }
        return companyStockList;
    }

    public void deleteCompanyWithStock(String companyCode) {
        Company company = companyRepository.findByCompanyCode(companyCode);
        if (company != null) {
            companyRepository.delete(company);
            restTemplate.delete("http://localhost:8082/api/v1.0/market/stock/delete" + companyCode);
        }
    }

}
