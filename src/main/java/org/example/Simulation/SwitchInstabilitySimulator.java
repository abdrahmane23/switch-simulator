package org.example.Simulation;


import org.example.Command.CommandHandler;
import org.example.Model.Switch;

import java.util.Scanner;

public class SwitchInstabilitySimulator {
    private final CommandHandler commandHandler;


    public SwitchInstabilitySimulator(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public void startSimulation() {
        var scanner = new Scanner(System.in);
        System.out.println("to see intructions type 'help'");
        System.out.println("to exit simulation type 'exit'");
        System.out.println();
        System.out.println("Enter your commands");
        while(true){
            System.out.print("-> ");
            String command = scanner.nextLine().trim();
            if(command.equalsIgnoreCase("exit")){
                System.out.println("Exiting simulation...");
                break;
            }
                String response = commandHandler.handleInstabilityCommands(command);
                System.out.println(response);
        }

    }


}
