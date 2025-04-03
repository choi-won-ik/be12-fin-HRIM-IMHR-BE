package com.example.be12hrimimhrbe.global.response;

import lombok.*;

@Getter
public class BaseResponse<T> {
    private Boolean isSuccess;
    private String message;
    private Integer code;
    private T data;

    public BaseResponse(BaseResponseMessage baseResponseMessage, T data){
        this.isSuccess = baseResponseMessage.getIsSuccess();
        this.message = baseResponseMessage.getMessage();
        this.code = baseResponseMessage.getCode();
        this.data = data;
    }

    public BaseResponse(BaseResponseMessage baseResponseMessage){
        this.isSuccess = baseResponseMessage.getIsSuccess();
        this.message = baseResponseMessage.getMessage();
        this.code = baseResponseMessage.getCode();
    }


}

