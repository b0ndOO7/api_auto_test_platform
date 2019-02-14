package com.main.manage.utils;

public enum ResultCode {

    SUCCESS(200, "成功"),
    FAIL(10000, "失败"),
    USER_NOT_LOGIN(-1, "用户长时间未操作，请重新登录"),
    TOKEN_ERROR(-2, "用户token不匹配，请重启登录"),
    TOKEN_NULL(-3, "用户token为空"),
    PASSWORD_ERROR(10001, "用户名或密码错误"),
    PARAMETER_ERROR(10100, "参数错误"),
    PARAMETER_NULL(10101, "参数为空"),
    UID_NULL(10102, "UID为空"),
    ADD_FAIL(20001, "新增失败"),
    EDIT_FAIL(20002, "修改失败"),
    SAVE_FAIL(20003, "保存失败");


    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
