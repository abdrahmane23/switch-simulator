package org.example.Model;

import java.util.Objects;

public class MacEntry {

    private final String macAddress;
    private String interfaceName;

    public MacEntry(String macAddress, String interfaceName) {
        this.macAddress = macAddress;
        this.interfaceName = interfaceName;
    }

    public String getMacAddress() {
        return macAddress;
    }



    public String getInterfaceName() {
        return interfaceName;
    }



    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    @Override
    public String toString() {
        return "MacEntry{" +
                "macAddress='" + macAddress + '\'' +
                ", interfaceName='" + interfaceName + '\'' +
                '}';
    }
}
