package org.example.Model;

import java.util.Objects;

public class InterfaceInfo {

    private final String name;
    private String status;
    private String protocol;
    private VlanInfo vlan;

    public InterfaceInfo(
            String name,
            String status,
            String protocol,
            VlanInfo vlan
    ) {
        this.name = name;
        this.status = status;
        this.protocol = protocol;
        this.vlan = vlan;
    }

    public InterfaceInfo(String interfaceName) {
        this.name = interfaceName;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getProtocol() {
        return protocol;
    }

    public VlanInfo getVlan() {
        return vlan;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setVlan(VlanInfo vlan) {
        this.vlan = vlan;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InterfaceInfo that)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


    @Override
    public String toString() {
        return "InterfaceInfo{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", protocol='" + protocol + '\'' +
                ", vlan=" + vlan +
                '}';
    }
}