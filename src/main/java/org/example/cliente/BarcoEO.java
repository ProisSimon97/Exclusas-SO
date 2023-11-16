package org.example.cliente;

import java.io.*;
import java.net.Socket;

public class BarcoEO {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT_EO = 1210;

    private final int id;

    public BarcoEO(int id) {
        this.id = id;
    }

    public static void main(String[] args) {
        int numClientes = 4;

        for (int i = 0; i < numClientes; i++) {
            int id = i;
            new Thread(() -> new BarcoEO(id).iniciar()).start();
        }
    }

    public void iniciar() {
        Socket socket = null;

        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT_EO);

            System.out.println("Barco-" + id + " llegó a la esclusa desde el Este");
            enviar(socket, "Este");

            System.out.println("Barco-" + id + " esperando confirmación para cruzar desde el Este");

            String respuesta = recibir(socket);
            if ("GRANTED".equals(respuesta)) {
                System.out.println("Barco-" + id + " cruzando desde el Este");

                // Simula el tiempo que tarda el barco en cruzar
                Thread.sleep(2000);

                System.out.println("Barco-" + id + " terminó de cruzar.");
                enviar(socket, "RELEASE");
            }
        } catch (IOException | InterruptedException e) {
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