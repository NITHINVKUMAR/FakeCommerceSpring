package com.example.FakeCommerce.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class APIResponse<T> {
    private boolean success;
    private String message;
    private String error;
    private T data;

    public static <T> APIResponse<T> success(T data,String message){
        return APIResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> APIResponse<T> error(String error,String message){
        return APIResponse.<T>builder()
                .success(true)
                .message(message)
                .error(error)
                .build();
    }
}
