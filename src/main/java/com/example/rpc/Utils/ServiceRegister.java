package com.example.rpc.Utils;

import java.util.List;

/**
 * @Author：wuwei
 * @name：ServerRegister
 * @Date：2023/2/15
 */
public interface ServiceRegister {

    void register(String serviceName, String socketAddress);

    String serviceDiscovery(String serviceName);

    void registerWatch(String servicePath);

}
