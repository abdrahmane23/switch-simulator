package org.example.Server.Telnet;

import org.example.Command.CommandHandler;
import org.example.Server.ConnectionSetup;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TelnetServer implements ConnectionSetup {
    private final CommandHandler commandHandler;
    public TelnetServer(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public  void setupConnection() throws IOException {
        Thread telnetThread = new Thread(() -> {

            try (ServerSocket serverSocket = new ServerSocket(23)) {


                while (true) {

                    Socket socket = serverSocket.accept();

                    new Thread(() -> handleTelnet(socket)).start();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        telnetThread.start();
    }
    private  void handleTelnet(Socket socket) {

        try (
                socket;
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream(),
                                StandardCharsets.UTF_8
                        )
                );
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream(),
                                StandardCharsets.UTF_8
                        )
                )
        ) {

            String command = reader.readLine().trim();
            String response = commandHandler.handle(command);
            writer.write(response);
            writer.newLine();
            writer.flush();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
