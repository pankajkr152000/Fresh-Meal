package com.foodies.freshmeal.common.dto;



import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;

    private int httpStatusCode;

    private String httpStatusMessage;

    private String message;

    private List<String> errors;

    private T data;
}
