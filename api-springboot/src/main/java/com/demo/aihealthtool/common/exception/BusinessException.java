package com.demo.aihealthtool.common.exception;

public class BusinessException extends RuntimeException {
    private final int code;
    
    // 默认错误代码构造函数
    public BusinessException(String message) { 
        super(message); 
        this.code = 40000; // 默认使用40000作为业务错误代码
    }
    
    // 自定义错误代码构造函数
    public BusinessException(int code, String message) { 
        super(message); 
        this.code = code; 
    }
    
    public int getCode() { 
        return code; 
    }
}
