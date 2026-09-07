package org.example.Server.Ssh;


import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.example.Command.CommandHandler;
import org.example.Server.ConnectionSetup;

import java.io.IOException;

public class SshServer implements ConnectionSetup {
    private final CommandHandler commandHandler;

    public SshServer(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void setupConnection() throws IOException {
        // ---------- SSH ----------
        org.apache.sshd.server.SshServer sshServer = org.apache.sshd.server.SshServer.setUpDefaultServer();

        sshServer.setPort(22);

        sshServer.setKeyPairProvider(
                new SimpleGeneratorHostKeyProvider()
        );

        sshServer.setPasswordAuthenticator(
                (username, password, session) ->
                        username.equals("admin")
                                && password.equals("admin")
        );

        sshServer.setCommandFactory((channel, command) -> {
            return new MyCommand(command, commandHandler);
        });
        sshServer.start();
    }

}
