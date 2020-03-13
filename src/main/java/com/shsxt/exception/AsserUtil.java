package com.shsxt.exception;

public class AsserUtil {
    public static void isTrue(Boolean flag,String msg){
        if(flag){
            throw new ParamsException(msg);
        }
    }


}
