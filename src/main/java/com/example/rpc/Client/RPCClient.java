package com.example.rpc.Client;

import com.example.rpc.entity.Request;
import com.example.rpc.entity.Response;

/**
 * @Author：wuwei
 * @name：ClientSend
 * @Date：2022/12/12 15:14
 */
public interface RPCClient {

    Response sendRequest(Request response);
}
