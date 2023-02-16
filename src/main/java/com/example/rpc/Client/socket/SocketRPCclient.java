package com.example.rpc.Client.socket;

import com.example.rpc.Client.RPCClient;
import com.example.rpc.Utils.ServiceRegister;
import com.example.rpc.Utils.ZKserviceRegister;
import com.example.rpc.entity.Request;
import com.example.rpc.entity.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class SocketRPCclient implements RPCClient {

    private static final Logger log = LoggerFactory.getLogger(SocketRPCclient.class);

    private ServiceRegister serviceRegister;

    public SocketRPCclient() {
        // 初始化注册中心，建立连接
        this.serviceRegister = new ZKserviceRegister();
        log.info("建立连接成功。。。。。。。。。");
    }

    @Override
    public Response sendRequest(Request request) {
        List<String> urls = serviceRegister.serviceDiscovery(request.getInterfaceName());
        String url = urls.get(0);
        int index = url.indexOf(":");
        String ip = url.substring(0, index);
        int port = Integer.parseInt(url.substring(index + 1));

        try {
            Socket socket = new Socket(ip, port);

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            outputStream.writeObject(request);
            outputStream.flush();

            Response response = (Response) inputStream.readObject();

            return response;
        } catch (IOException | ClassNotFoundException e) {
            log.warn("socket启动失败");
            return null;
        }
    }
}
