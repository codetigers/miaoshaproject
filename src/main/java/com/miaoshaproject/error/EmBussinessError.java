package com.miaoshaproject.error;

import org.omg.CORBA.UNKNOWN;

public enum EmBussinessError implements CommonError {
    //通用错误类型10001,可以用来当校验邮箱，用户名是否合法的时候使用，不同的校验可以传不同的
    // errMsg这就是接口CommonError的作用:来改动errormsg
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),

    //1000x开头为用户信息相关错误定义码
    USER_NOT_EXIST(10002,"用户不存在"),
    UNKNOWN_ERROR(20001,"未知异常"),
    USER_LOGIN_FAIL(20002,"用户手机号或密码错误"),
    USER_NOT_LOGIN(20003,"用户还未登陆"),
    //如果以后要向错误信息中填值就可以在这里添加就行了
    //30000为交易信息错误定义
    STOCK_NOT_ENOUGH(30001,"库存不足");
    ;

    private int errCode;
    private String errMsg;

    EmBussinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg=errMsg;
        return this;
    }
}
