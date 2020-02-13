package com.magic.springboot.exception;

/**
 * Created by ifuck on 2018/8/21.
 */
public class ApiErrorCode {

    public static void throwError(String msg){
        new ApiException(9999, msg).throwSelf();
    }
}
