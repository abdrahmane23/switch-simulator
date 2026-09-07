package org.example.Server.Ssh;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.example.Command.CommandHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyCommand implements Command {

    private String command;
    private OutputStream output;
    private ExitCallback exitCallback;
    CommandHandler commandHandler ;

    public MyCommand(String command,CommandHandler commndHandler) {
        this.command = command;
        this.commandHandler = commndHandler;
    }

    @Override
    public void setInputStream(InputStream in) {
    }

    @Override
    public void setOutputStream(OutputStream out) {
        this.output = out;
    }

    @Override
    public void setErrorStream(OutputStream err) {
    }

    @Override
    public void setExitCallback(ExitCallback callback) {
        this.exitCallback = callback;
    }

    @Override
    public void start(ChannelSession channel, Environment env) throws IOException {
        String response =commandHandler.handle(command);
        output.write(response.getBytes());

        output.flush();
        exitCallback.onExit(0);
    }

    @Override
    public void destroy(ChannelSession channel) {
    }
}