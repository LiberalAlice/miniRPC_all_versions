package com.example.rpc.Client.socket;

import com.example.rpc.Client.RPCClient;
import com.example.rpc.entity.Request;
import com.example.rpc.entity.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketRPCclient implements RPCClient {

    private static final String ip = "127.0.0.1";
    private static final int port  = 8008;

    //private static final Logger log = LoggerFactory.getLogger(SocketRPCclient.class);

    @Override
    public Response sendRequest(Request request) {
        try {
            Socket socket = new Socket(ip, port);

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            outputStream.writeObject(request);
            outputStream.flush();

            Response response = (Response) inputStream.readObject();

            return response;
        } catch (IOException | ClassNotFoundException e) {
            //log.warn("socket启动失败");
            return null;
        }
    }
}
