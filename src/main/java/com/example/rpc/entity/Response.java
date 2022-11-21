package com.example.rpc.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Response implements Serializable {
    private int code;
    private String message;

    private Object data;

    public static Response success(Object data) {
        return Response.builder().code(200).data(data).build();
    }
    public static Response fail() {
        return Response.builder().code(500).message("服务器发生错误").build();
    }
}
