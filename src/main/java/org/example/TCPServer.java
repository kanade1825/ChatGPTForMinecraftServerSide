package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TCPServer {
    public static final int TEST_PORT = 8001;
    private static final int THREAD_POOL_SIZE = 10;
    private static final int TASK_QUEUE_SIZE = 100;
    static final ExecutorService executorService = new ThreadPoolExecutor(
            THREAD_POOL_SIZE,
            THREAD_POOL_SIZE,
            60L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(TASK_QUEUE_SIZE)
    );

    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(TEST_PORT);
            System.out.println("接続待ち受け中");
            while (true) {
                Socket socket = serverSocket.accept();
                new TCPThread(socket, executorService).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                    System.out.println("接続待ち受け終了");
                }
            } catch (IOException e) {
            }
        }
    }
}