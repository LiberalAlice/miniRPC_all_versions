package com.example.rpc.Utils;

import com.example.rpc.Service.userServiceImp;
import com.example.rpc.entity.Request;
import com.example.rpc.entity.Response;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

@AllArgsConstructor
public class WorkThead implements Runnable {

    private Socket socket;
    private Map<String, Object> serviceProvide;

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            Request request = (Request) ois.readObject();
            Response response = getResponse(request);
            oos.writeObject(response);
            oos.flush();

        } catch (IOException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
            System.out.println("从IO中读取数据错误");
        }
    }

    private Response getResponse(Request request) throws InstantiationException {
        System.out.println(request.getInterfaceName() + " " + request.getMethodName());

        Method method;
        Response response = null;
        try {
            Object c = serviceProvide.get(request.getInterfaceName());
            method =  c.getClass().getDeclaredMethod(request.getMethodName(), request.getParamsTypes());
            Object o =  method.invoke(c, request.getParams());

            response = Response.success(o);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException  e) {
            e.printStackTrace();
        }
        return response;
    }


}
