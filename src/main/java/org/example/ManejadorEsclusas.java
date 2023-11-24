package org.example;

import java.io.*;
import java.net.Socket;

public class ManejadorEsclusas {
    private final Servidor servidor;
    private final Socket socket;

    public ManejadorEsclusas(Servidor servidor, Socket socket) {
        this.servidor = servidor;
        this.socket = socket;
    }

    public void run() {
        Socket barco1, barco2;
        String message = readMessage(socket);

        if("Oeste".equals(message)) {
            servidor.getQueueOeste().offer(socket);
        } else {
            servidor.getQueueEste().offer(socket);
        }

        System.out.println("Llego barco desde el " + message);

        if(servidor.getQueueOeste().size() >= 2) {

            System.out.println("Esclusa Oeste bajando");
            barco1 = servidor.getQueueOeste().poll();
            barco2 = servidor.getQueueOeste().poll();
            manejarCruceBarcos(barco1, barco2, message);

        } else if(servidor.getQueueEste().size() >= 2) {

            System.out.println("Esclusa Este bajando");
            barco1 = servidor.getQueueEste().poll();
            barco2 = servidor.getQueueEste().poll();
            manejarCruceBarcos(barco1, barco2, message);
        }
    }

    private void manejarCruceBarcos(Socket barco1, Socket barco2, String message) {
        String message1, message2;
        String salida = (message.equals("Este") ? "Oeste" : "Este");

        sendMessage(barco1, "ENTRAR");
        sendMessage(barco2, "ENTRAR");

        message1 = readMessage(barco1);
        message2 = readMessage(barco2);

        if(message1.equals("PASANDO") && message2.equals("PASANDO")) {
            System.out.println("Sube esclusa " + message + ". Baja esclusa " + salida);
        }

        sendMessage(barco1, "SALIR");
        sendMessage(barco2, "SALIR");

        message1 = readMessage(barco1);
        message2 = readMessage(barco2);

        if(message1.equals("AFUERA") && message2.equals("AFUERA")) {
            System.out.println("Sube esclusa " + salida);
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
