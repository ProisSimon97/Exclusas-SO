package org.example;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ManejadorExclusas extends Thread {

    private final Socket socket;
    private final String direccion;
    private final BlockingQueue<Socket> cola;

    public ManejadorExclusas(Socket socket, String direccion) {
        this.socket = socket;
        this.direccion = direccion;
        this.cola = ("Oeste".equals(direccion)) ? Servidor.colaOE : Servidor.colaEO;
    }

    @Override
    public void run() {
        try {
            String solicitud = recibir(socket);
            System.out.println("Nuevo barco llegó desde el " + solicitud);

            cola.offer(socket);
            System.out.println("Barco esperando en la fila del " + direccion);

            if (cola.size() >= 2) {
                System.out.println("Pasan dos barcos desde el " + direccion);
                Socket barco1 = cola.poll();
                Socket barco2 = cola.poll();
                cruzar(barco1, barco2);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void cruzar(Socket barco1, Socket barco2) {
        try {
            enviar(barco1, "GRANTED");
            enviar(barco2, "GRANTED");

            System.out.println("Esperando a que los barcos terminen de cruzar");

            Thread.sleep(2000);

            String respuesta1 = recibir(barco1);
            String respuesta2 = recibir(barco2);

            if ("RELEASE".equals(respuesta1) && "RELEASE".equals(respuesta2)) {
                System.out.println("Esclusa libre...");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String recibir(Socket socket) {
        try (InputStream inputStream = socket.getInputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            String message = new String(buffer, 0, bytesRead);
            System.out.println("Mensaje recibido: " + message);
            return message;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void enviar(Socket socket, String message) {
        try (OutputStream outputStream = socket.getOutputStream()) {
            outputStream.write(message.getBytes());
            System.out.println("Mensaje enviado: " + message);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}