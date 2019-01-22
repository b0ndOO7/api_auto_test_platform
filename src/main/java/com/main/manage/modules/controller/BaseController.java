package com.main.manage.modules.controller;

import com.main.manage.RestTemplate.Result;
import com.main.manage.utils.ResultCode;

public class BaseController {
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
