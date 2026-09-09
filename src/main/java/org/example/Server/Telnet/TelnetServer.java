package org.example.Server.Telnet;

import org.example.Command.CommandHandler;
import org.example.Server.ConnectionSetup;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import static org.example.Command.ResponseBuilder.buildTelnetlResponse;

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
            if (!handleTelnetAuth(reader,writer)){
                writer.write(System.lineSeparator()+System.lineSeparator()+"% Authentication failed");
                return;
            }

            handleTelnetCommands(writer,reader);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private boolean handleTelnetAuth(BufferedReader reader, BufferedWriter writer) throws IOException {
        writer.write("User Access Verification"+System.lineSeparator()+System.lineSeparator());
        writer.flush();
        for (int i=0 ;i<3;i++ ){
            writer.write("Password: ");
            writer.flush();
            System.out.println(i);
            String enteredPassword = reader.readLine();
            System.out.println(enteredPassword);
            if (enteredPassword.equals("admin"))
                return true;
        }
        return false;
    }
    private void handleTelnetCommands(BufferedWriter writer, BufferedReader reader) throws IOException {
        String command;
        while(true){
            writer.write(System.lineSeparator()+"switch-Core01> ");
            writer.flush();
            command = reader.readLine();
            if(command.equals("exit"))
                return;
            String response=buildTelnetlResponse(command,commandHandler.handle(command));
            writer.write(response);
            writer.flush();
        }
    }
}
