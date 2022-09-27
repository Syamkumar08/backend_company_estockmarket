package com.cts.company.response;

import com.cts.company.model.Company;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CompanyStockResponse extends AbstractResponse {

    private Company company;

    private Stock stock;

}
