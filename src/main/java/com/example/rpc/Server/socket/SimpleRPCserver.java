package com.example.rpc.Server.socket;

import com.example.rpc.Server.RPCserver;
import com.example.rpc.Utils.ServiceProvider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SimpleRPCserver implements RPCserver {

    private ServiceProvider serviceProvide;

    public SimpleRPCserver(ServiceProvider serviceProvide) {
        this.serviceProvide = serviceProvide;
    }

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;


    ThreadPoolExecutor executor = new ThreadPoolExecutor(CORE_POOL_SIZE,
            MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(QUEUE_CAPACITY), new ThreadPoolExecutor.CallerRunsPolicy());


    @Override
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(serviceProvide.port);
            while (true) {
                Socket socket = serverSocket.accept();
                executor.execute(new WorkThead(socket, serviceProvide.getInterfaceProvider()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {

    }
} 
