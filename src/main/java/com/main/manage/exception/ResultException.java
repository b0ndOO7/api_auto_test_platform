package com.main.manage.exception;

import com.main.manage.utils.ResultCode;

public class ResultException extends RuntimeException {
    private ResultCode resultCode;

    public ResultException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
