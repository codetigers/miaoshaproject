package com.miaoshaproject.response;

/**
 * @program: miaoshaproject
 * @description:
 * @author: Mr.li
 * @create: 2020-04-20 12:55
 **/
public class CommonReturnType {
    //表明对应请求结果的返回处理结果"success"或"fail"
    private String status;
    //若返回结果为"success",则data内返回前端使用的json数据
    //若返回结果为"fail"，则data返回通用的错误码格式
    private Object data;

    //定义一个通用的创建方法
    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }

    public static CommonReturnType create(Object result, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
