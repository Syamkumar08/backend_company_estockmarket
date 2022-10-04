package com.cts.company.response;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockResponseDTO {
    private Long id;

    private long stockcode;

    private String companyCode;

    private double previousPrice;

    private double currentPrice;

    private Date previousDate;

    private Date currentDt;

    private boolean currentFlag;
}
