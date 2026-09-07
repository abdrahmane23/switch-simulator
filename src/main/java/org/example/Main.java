package org.example;

import org.example.Model.Switch;
import org.example.Command.CommandHandler;
import org.example.Server.ConnectionSetup;
import org.example.Server.Ssh.SshServer;
import org.example.Server.Telnet.TelnetServer;
import org.example.Simulation.SwitchInstabilitySimulator;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        Switch switchInstance = new Switch();
        switchInstance.initialize();
        CommandHandler commandHandler =
                new CommandHandler(switchInstance);

        List<ConnectionSetup> connections = List.of(
                new SshServer(commandHandler),
                new TelnetServer(commandHandler)
        );

        SwitchInstabilitySimulator instabilitySimulator =
                new SwitchInstabilitySimulator(commandHandler);

        Runnable instabilityTask = instabilitySimulator::startSimulation;


        for (ConnectionSetup connection : connections) {
            connection.setupConnection();
        }

        Thread instabilityThread = new Thread(instabilityTask);
        instabilityThread.start();
    }
}
