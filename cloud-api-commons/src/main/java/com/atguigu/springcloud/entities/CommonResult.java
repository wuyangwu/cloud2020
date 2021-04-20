package com.atguigu.springcloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/1/20 10:52
 */
@Data
@AllArgsConstructor //它是lombok中的注解,作用在类上;使用后添加一个构造函数，该构造函数含有所有已声明字段属性参数
@NoArgsConstructor
public class CommonResult<T> {

    // 404 NOT_FOUND
    private Integer code;
    private String message;
    private T data;


    public CommonResult(Integer code, String message){
        this(code,message,null);
    }

}
