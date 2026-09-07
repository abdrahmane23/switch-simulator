package org.example.Command;

import org.example.Model.InterfaceInfo;
import org.example.Model.VlanInfo;

public class CommandParser {
    public static InterfaceInfo getInterfaceByNameWithIndex(String command,int index) {
        String interfaceName = command.split("\\s+")[index];
        return new InterfaceInfo(interfaceName);

    }

    public static VlanInfo getVlanByIdWithIndex(String command, int index) {
        String vlanId = command.split("\\s+")[index];
        return new VlanInfo(Integer.parseInt(vlanId));
    }

    public static VlanInfo getVlanByNameWithIndex(String command,int index) {
        String vlanName= command.split("\\s+")[index];

        return new VlanInfo(vlanName);
    }

    public static String getStringWithIndex(String command, int i) {
        return command.split("\\s+")[i];
    }
}
