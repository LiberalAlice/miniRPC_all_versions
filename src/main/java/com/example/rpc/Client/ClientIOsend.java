package com.example.rpc.Client;

import com.example.rpc.entity.Request;
import com.example.rpc.entity.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientIOsend {

    private static final String ip = "127.0.0.1";
    private static final int port  = 8008;

    public static  Response send(Request request) {
        try {
            Socket socket = new Socket(ip, port);

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            outputStream.writeObject(request);
            outputStream.flush();

            Response response = (Response) inputStream.readObject();

            return response;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println();
            return null;
        }
    }

}
