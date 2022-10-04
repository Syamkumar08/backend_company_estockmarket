package com.cts.company.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cts.company.model.Company;
import com.cts.company.repository.CompanyRepository;
import com.cts.company.response.CompanySaveResponse;
import com.cts.company.response.CompanyStockResponse;
import com.cts.company.response.StockResponseDTO;
import com.cts.stock.dto.CompanyResponseDTO;
import com.cts.stock.dto.ResponseMessage;
import com.cts.stock.dto.StockResponse;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public CompanySaveResponse register(Company company) {

        CompanySaveResponse companySaveResponse = new CompanySaveResponse();
        try {
            if (getCompany(company.getCompanyCode()).getData() == null) {
                if (company != null && company.getCompanyTurnover() >= 100000000) {
                    Company savedCompany = companyRepository.save(company);
                    if (savedCompany != null) {
                        companySaveResponse.setMessage("Company saved successfully!!");
                        companySaveResponse.setCompany(savedCompany);
                    } else {
                        companySaveResponse.setMessage("Company not saved successfully!!");
                    }
                } else {
                    companySaveResponse.setMessage("Company Turnover must be greater than 10cr");
                }
            } else {
                companySaveResponse.setMessage("Company already exists");

            }
            companySaveResponse.setStatus(HttpStatus.OK.toString());
            companySaveResponse.setHttpstatus(HttpStatus.OK);
        } catch (Exception ex) {
            companySaveResponse.setStatus(HttpStatus.BAD_REQUEST.toString());
            companySaveResponse.setHttpstatus(HttpStatus.BAD_REQUEST);
        }
        return companySaveResponse;
    }

    @Override
    public CompanyStockResponse getCompanyWithStock(String companyCode) {
        Company company = companyRepository.findByCompanyCode(companyCode);
        CompanyStockResponse companyStockResponse = new CompanyStockResponse();
        if (company != null) {
            StockResponseDTO stock = restTemplate.getForObject(
                    "http://localhost:8085/api/v1.0/market/stock/get/stockPrice/" + companyCode,
                    StockResponseDTO.class);
            companyStockResponse.setCompany(company);
            companyStockResponse.setStock(stock);
            return companyStockResponse;
        }
        return companyStockResponse;
    }

    @Override
    public List<CompanyStockResponse> getAllCompaniesWithStocks() {
        List<Company> companyList = companyRepository.findAll();
        List<CompanyStockResponse> companyStockList = new ArrayList<>();
        for (Company company : companyList) {
            CompanyStockResponse companyStock = new CompanyStockResponse();
            companyStock.setCompany(company);
            ResponseEntity<StockResponse> stockResponse = restTemplate.getForEntity(
                    "http://localhost:8085/api/v1.0/market/stock/get/stockPrice/" + company.getCompanyCode(),
                    StockResponse.class);
            StockResponseDTO stock = new StockResponseDTO();
            if (stockResponse.getBody().getData() != null) {
                stock.setCurrentPrice(Double.valueOf(stockResponse.getBody().getData().toString()));
            } else {
                stock.setCurrentPrice(0.0);
            }
            companyStock.setStock(stock);
            companyStockList.add(companyStock);
        }
        return companyStockList;
    }

    @Override
    public void deleteCompanyWithStock(String companyCode) {
        Company company = companyRepository.findByCompanyCode(companyCode);
        if (company != null) {
            companyRepository.delete(company);
            restTemplate.delete("http://localhost:8085/api/v1.0/market/stock/delete" + companyCode);
        }
    }

    @Override
    public CompanyResponseDTO<Company> getCompany(String companyCode) {
        CompanyResponseDTO<Company> companyResponseDTO = new CompanyResponseDTO<>();
        ResponseMessage message = new ResponseMessage();
        try{
            
        Company company = companyRepository.findByCompanyCode(companyCode);
        if (company != null) {
            message.setCode("COMPANY_FOUND");
            message.setDescription("Company found");
            companyResponseDTO.withData(company);
        } else {
            message.setCode("NO_COMPANY_FOUND");
            message.setDescription("No Company found");
             companyResponseDTO.withData(null);
        }
        companyResponseDTO.withMessage(message);
        return companyResponseDTO;
    } catch (Exception ex) {
        message.setCode("FILTER_COMPANY_FAILED");
        message.setDescription("FILTER_COMPANY_FAILED"+ ": " + ex.getMessage());
        companyResponseDTO.withData(null);
        companyResponseDTO.withMessage(message);
        return companyResponseDTO;
    }
       
        
    }

}
