package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Servidor {
    private static final int CONNECTION_PORT = 1200;
    private final Queue<Socket> queueOE = new LinkedBlockingQueue<>();
    private final Queue<Socket> queueEO = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        Servidor server = new Servidor();
        server.start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(CONNECTION_PORT)) {
            System.out.println("Esclusas listas");

            while (true) {
                Socket connection = serverSocket.accept();
                new ManejadorEsclusas(this, connection).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Queue<Socket> getQueueOeste() {
        return this.queueOE;
    }

    public Queue<Socket> getQueueEste() {
        return this.queueEO;
    }
}
