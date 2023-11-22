package org.example.cliente;

import org.example.config.SocketConnection;

import java.io.*;
import java.net.Socket;

public class BarcoOE {
    private static final String TYPE = "Oeste";

    public static void main(String[] args) {
        new Thread(() -> new BarcoOE().run()).start();
        new Thread(() -> new BarcoOE().run()).start();
    }

    public void run() {
        try {
            Socket socket = SocketConnection.connection();

            System.out.println("Llego un barco desde el " + TYPE);
            sendMessage(socket, TYPE);

            System.out.println("Esperando confirmacion desde el " + TYPE);

            String message = readMessage(socket);

            if(message.equals("ENTRAR")) {
                Thread.sleep(2000);

                System.out.println("Entro al canal un barco del " + TYPE);

                sendMessage(socket, "PASANDO");

                String response = readMessage(socket);

                if(response.equals("SALIR")) {
                    Thread.sleep(2000);

                    System.out.println("Saliendo del canal, barco del " + TYPE);

                    sendMessage(socket, "AFUERA");
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(Socket socket, String message) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readMessage(Socket socket) {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
