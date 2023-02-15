package com.example.rpc.Server;

import com.example.rpc.Service.userServiceImp;
import com.example.rpc.entity.Request;
import com.example.rpc.entity.Response;
import com.example.rpc.entity.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        userServiceImp service = new userServiceImp();
        try {
            ServerSocket serverSocket = new ServerSocket(8008);

            System.out.println("server have been start!");

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() ->{
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                        Request request = (Request) ois.readObject();
                        System.out.println(request.getInterfaceName() + " " + request.getMethodName());

                        Method method =  service.getClass().getDeclaredMethod(request.getMethodName(), request.getParamsTypes());

                        Object o =  method.invoke(service, request.getParams());
                        Response response = Response.success(o);

                        oos.writeObject(response);
                        oos.flush();

                    } catch (IOException | ClassNotFoundException | NoSuchMethodException e) {
                        e.printStackTrace();
                        System.out.println("从IO中读取数据错误");
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
