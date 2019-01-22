package com.main.manage.utils;

import com.main.manage.RestTemplate.Result;

public class ResultUtils {
    public static Result success(Object data) {
        return new Result<>(ResultCode.SUCCESS, data);
    }

    public static Result warn(ResultCode resultCode, String msg) {
        Result<Object> result = new Result<>(resultCode);
        result.setMsg(msg);
        return result;
    }

    public static Result warn(ResultCode resultCode) {
        return new Result(resultCode);
    }
}
