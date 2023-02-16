package com.example.rpc.Utils;


import java.util.HashMap;
import java.util.Map;

public class ServiceProvider {

    private Map<String, Object> interfaceProvider;

    private ServiceRegister serviceRegister;
    private String host;
    public int port;

    public ServiceProvider(String host, int port) {
        this.interfaceProvider = new HashMap<>();
        this.serviceRegister = new ZKserviceRegister();
        this.host = host;
        this.port = port;
    }

    public Map<String, Object> getInterfaceProvider() {
        return interfaceProvider;
    }

    public void setInterfaceProvider(Object service) {
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for (Class c:interfaces
             ) {
            interfaceProvider.put(c.getName(),service);
            serviceRegister.register(c.getName(), host + ":" + port);
        }
    }

    public Object getInterface(String interfaceName) {
        return interfaceProvider.get(interfaceName);
    }

}
