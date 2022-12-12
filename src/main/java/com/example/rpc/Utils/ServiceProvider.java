package com.example.rpc.Utils;


import java.util.HashMap;
import java.util.Map;

public class ServiceProvider {

    private Map<String, Object> interfaceProvider;

    public ServiceProvider() {
        this.interfaceProvider = new HashMap<>();
    }

    public Map<String, Object> getInterfaceProvider() {
        return interfaceProvider;
    }

    public void setInterfaceProvider(Object service) {
        String serviceName = service.getClass().getName();
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for (Class c:interfaces
             ) {
            System.out.println(c.getName());
            interfaceProvider.put(c.getName(),service);
        }
    }

    public Object getInterface(String interfaceName) {
        return interfaceProvider.get(interfaceName);
    }

}
