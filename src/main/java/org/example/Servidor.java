package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Servidor {

    private static final int PUERTO_OE = 1209;
    private static final int PUERTO_EO = 1210;
    public static BlockingQueue<Socket> colaOE = new LinkedBlockingQueue<>();
    public static BlockingQueue<Socket> colaEO = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.iniciar();
    }

    public void iniciar() {
        try (ServerSocket serverSocketOE = new ServerSocket(PUERTO_OE);
             ServerSocket serverSocketEO = new ServerSocket(PUERTO_EO)) {

            System.out.println("Esclusas listas para la llegada de barcos");

            while (true) {
                try {
                    Socket connection;
                    if (Math.random() < 0.5) {
                        connection = serverSocketOE.accept();
                        new ManejadorExclusas(connection, "Oeste").start();
                    } else {
                        connection = serverSocketEO.accept();
                        new ManejadorExclusas(connection, "Este").start();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}