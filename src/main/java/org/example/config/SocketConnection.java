package org.example.config;

import java.io.IOException;
import java.net.Socket;

public class SocketConnection {
    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 1200;

    public static Socket connection() throws IOException {
        return new Socket(SERVER_HOST, SERVER_PORT);
    }
}
