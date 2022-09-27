package com.cts.company.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AbstractResponse {

    private HttpStatus httpstatus;

    private String status;

    private String message;

}
