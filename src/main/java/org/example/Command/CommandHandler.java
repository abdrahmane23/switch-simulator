package org.example.Command;

import org.example.Model.InterfaceInfo;
import org.example.Model.Switch;
import org.example.Model.VlanInfo;
import static org.example.Command.ResponseBuilder.*;// retreive all utility methods of ResponseBuilder

public class CommandHandler {
    Switch switchInstance;
    public CommandHandler(Switch switchInsatance){
        this.switchInstance = switchInsatance;

    }

    public synchronized String handle(String command) {
        return switch (command) {

            case "terminal length 0"->
                    buildTerminalResponse();
            case "show vlan brief" ->
                    buildVlanResponse(switchInstance.getVlans());
            case "show interfaces status" ->
                    buildInterfaceResponse(switchInstance.getInterfaces());
            case "show mac address-table" ->
                    buildMacTableResponse(switchInstance.getMacTable());
            default -> "Unknown command";
        };
    }
    public synchronized String  handleInstabilityCommands(String command)  {
            if(command.equals("show vlan brief"))
                    return buildVlanResponse(switchInstance.getVlans());
            if(command.equals("show interfaces status"))
                    return buildInterfaceResponse(switchInstance.getInterfaces());
            if(command.equals("show mac address-table"))
                    return buildMacTableResponse(switchInstance.getMacTable());
            if (command.equals("help"))
                    return buildHelpResponse();
            if (command.matches("^UP\\s+interface\\s+.+$")){
                InterfaceInfo interfaceInfo = CommandParser.getInterfaceByNameWithIndex(command,2);
                CommandResult commandResult =switchInstance.executeUpInterfaceCommand(interfaceInfo);
                if(commandResult.result()){
                    return buildSuccessResponse();
                }
                return buildErrorResponse(commandResult.message());
            }
        if (command.matches("^DOWN\\s+interface\\s+.+$")){
            InterfaceInfo interfaceInfo = CommandParser.getInterfaceByNameWithIndex(command,2);
            CommandResult commandResult =switchInstance.executeDownInterfaceCommand(interfaceInfo);
            if(commandResult.result()){
                return buildSuccessResponse();
            }
            return buildErrorResponse(commandResult.message());
        }
        if (command.matches("^CREATE\\s+interface\\s+.+\\s+TO\\s+\\d+$")){
            InterfaceInfo interfaceInfo = CommandParser.getInterfaceByNameWithIndex(command,2);
            System.out.println(interfaceInfo.getName());
            VlanInfo vlan = CommandParser.getVlanByIdWithIndex(command,4);
            System.out.println(vlan.getId());
            CommandResult commandResult =switchInstance.executeCreateInterfaceCommand(interfaceInfo,vlan);
            if(commandResult.result()){
                return buildSuccessResponse();
            }
            return buildErrorResponse(commandResult.message());
        }
        if (command.matches("^DELETE\\s+interface\\s+.+$")){
            InterfaceInfo interfaceInfo = CommandParser.getInterfaceByNameWithIndex(command,2);
            CommandResult commandResult =switchInstance.executeDeleteInterfaceCommand(interfaceInfo);
            if(commandResult.result()){
                return buildSuccessResponse();
            }
            return buildErrorResponse(commandResult.message());
        }
        if (command.matches("^UP\\s+vlan\\s+\\d+$")){
            VlanInfo vlanInfo = CommandParser.getVlanByIdWithIndex(command,2);
            CommandResult commandResult =switchInstance.executeUpVlanCommand(vlanInfo);
            if(commandResult.result()){
                return buildSuccessResponse();
            }
            return buildErrorResponse(commandResult.message());
        }
        if (command.matches("^DOWN\\s+vlan\\s+\\d+$")){
            VlanInfo vlanInfo = CommandParser.getVlanByIdWithIndex(command,2);
            System.out.println(vlanInfo.getId());
            CommandResult commandResult =switchInstance.executeDownVlanCommand(vlanInfo);
            if(commandResult.result()){
                return buildSuccessResponse();
            }
            return buildErrorResponse(commandResult.message());
        }
        if (command.matches("^CREATE\\s+vlan\\s+.+$")){
            VlanInfo vlanInfo = CommandParser.getVlanByNameWithIndex(command,2);
            CommandResult commandResult =switchInstance.executeCreateVlanCommand(vlanInfo);
            if(commandResult.result()){
                return buildSuccessResponse();
            }
            return buildErrorResponse(commandResult.message());
        }
        if (command.matches("^DELETE\\s+vlan\\s+\\d+$")){
            VlanInfo vlanInfo = CommandParser.getVlanByIdWithIndex(command,2);
            CommandResult commandResult =switchInstance.executeDeleteVlanCommand(vlanInfo);
            if(commandResult.result()){
                return buildSuccessResponse();
            }
            return buildErrorResponse(commandResult.message());
        }
        if (command.matches("^ASSIGN\\s+MAC\\s+.+\\s+TO\\s+.+$")){
            String  macAddress = CommandParser.getStringWithIndex(command,2);
            InterfaceInfo interfaceInfo = CommandParser.getInterfaceByNameWithIndex(command,4);
            System.out.println(interfaceInfo.getName());
            CommandResult commandResult =switchInstance.executeAssignCommand(macAddress,interfaceInfo);
            if(commandResult.result()){
                return buildSuccessResponse();
            }
            return buildErrorResponse(commandResult.message());
        }
        if (command.matches("^UNASSIGN\\s+MAC\\s+.+\\s+FROM\\s+.+$")){
            String  macAddress = CommandParser.getStringWithIndex(command,2);
            System.out.println(macAddress);
            InterfaceInfo interfaceInfo = CommandParser.getInterfaceByNameWithIndex(command,4);
            System.out.println(interfaceInfo.getName());
            CommandResult commandResult =switchInstance.executeUnassignCommand(macAddress,interfaceInfo);
            if(commandResult.result()){
                return buildSuccessResponse();
            }
            return buildErrorResponse(commandResult.message());
        }


        return "Unknown command";
        }
    }


