package org.example.Command;

import org.example.Model.InterfaceInfo;
import org.example.Model.MacEntry;
import org.example.Model.VlanInfo;

import java.util.List;
import java.util.Random;

public class ResponseBuilder {
    //utility class responsible for creating cisco switch like commands and sucess and failure responses for instability commands


    public static String buildVlanResponse(List<VlanInfo> vlans) {

        StringBuilder response = new StringBuilder();

        response.append(
                "VLAN Name                             Status    Ports\n"
        );

        response.append(
                "---- -------------------------------- --------- -------------------------------\n"
        );

        for (VlanInfo vlan : vlans) {
            if (vlan.getId()==0)
                continue;//to not make trunk appear in vlan response


            response.append(
                    String.format(
                            "%-4d %-32s %-9s ",
                            vlan.getId(),
                            vlan.getName(),
                            vlan.getStatus()
                    )
            );

            for (int i = 0; i < vlan.getInterfaces().size(); i++) {

                InterfaceInfo interfaceInfo =
                        vlan.getInterfaces().get(i);

                response.append(interfaceInfo.getName());

                if (i < vlan.getInterfaces().size() - 1) {
                    response.append(", ");
                }
            }

            response.append("\n");
        }

        return response.toString();
    }

    public static String buildInterfaceResponse(List<InterfaceInfo> interfaces) {

        StringBuilder response = new StringBuilder();

        response.append(String.format(
                "%-10s%-19s%-13s%-11s%-8s%-6s%s%n",
                "Port", "Name", "Status", "Vlan", "Duplex", "Speed", "Type"
        ));

        for (InterfaceInfo interfaceInfo : interfaces) {

            String port = interfaceInfo.getName();
            String status = interfaceInfo.getStatus();

            String vlan =
                    interfaceInfo.getVlan() != null
                            ? interfaceInfo.getVlan().getId() == 0
                            ? "trunk"
                            : String.valueOf(interfaceInfo.getVlan().getId())
                            : "1";

            // arbitrary filler values derived from status/port type,they will be ignored in parser in the client side
            boolean isConnected = "connected".equalsIgnoreCase(status);
            String duplex = isConnected ? "a-full" : "auto";
            String type = port.startsWith("Gi") ? "1000BaseTX" : "10/100BaseTX";
            String speed = isConnected
                    ? (port.startsWith("Gi") ? "a-1000" : "a-100")
                    : "auto";

            response.append(String.format(
                    "%-10s%-19s%-13s%-11s%-8s%-6s%s%n",
                    port,
                    "",
                    status,
                    vlan,
                    duplex,
                    speed,
                    type
            ));
        }


        return response.toString();
    }


    public static String buildMacTableResponse(List<MacEntry> macEntries) {

        StringBuilder response = new StringBuilder();
        Random random = new Random();
        String[] types = {"DYNAMIC", "STATIC"};

        response.append("          Mac Address Table\n");
        response.append("-------------------------------------------\n\n");
        response.append(String.format("%-8s%-19s%-12s%s%n", "Vlan", "Mac Address", "Type", "Ports"));
        response.append(String.format("%-8s%-19s%-12s%s%n", "----", "-----------", "--------", "-----"));

        for (MacEntry entry : macEntries) {
            int vlan = random.nextInt(10) + 1;          // random VLAN 1-10
            String type = types[random.nextInt(types.length)]; // random type

            response.append(
                    String.format(
                            "%-8d%-19s%-12s%s%n",
                            vlan,
                            entry.getMacAddress(),
                            type,
                            entry.getInterfaceName()
                    )
            );
        }

        response.append(String.format("Total Mac Addresses for this criterion: %d%n", macEntries.size()));

        return response.toString();
    }

    public static String buildHelpResponse() {
        String instructions = """
                Instructions to see switch Data:
                
                show vlan brief
                show interfaces status
                show mac address-table
                
                
                Instructions to manipulate switch Data:
                
                DOWN interface {name}       // Brings the specified interface down.
                UP interface {name}         // Brings the specified interface back up.
                
                DOWN vlan {name}            // Deactivates the specified VLAN.
                UP vlan {name}              // Reactivates the specified VLAN.
                
                CREATE interface {name} TO {vlan_id}    // Creates a new interface.
                DELETE interface {name}    // Deletes the specified interface.
                
                CREATE vlan {name}         // Creates a new VLAN.
                DELETE vlan {id}         // Deletes the specified VLAN.
                
                ASSIGN {interface} TO {vlan}       // Assigns interfaces to a VLAN.
                
                ASSIGN MAC {mac} TO {interface}     // Associates a MAC address with an interface.
                UNASSIGN MAC {mac} FROM {interface}       // Removes the MAC address from an interface.
                """;

        return instructions;
    }
    public static String buildSuccessResponse() {
        return "Command executed successfully: ";
    }

    public static String buildErrorResponse(String message) {
        return "Error: " + message;
    }

    public static String buildTerminalResponse(){
        return "";//considered as config command
    }
    public static String buildTelnetlResponse(String command,String response) {
        return command+System.lineSeparator()+response;
    }
}
