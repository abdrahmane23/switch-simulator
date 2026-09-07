package org.example.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VlanInfo {

    private  int id;
    private  String name;
    private String status;

    private final List<InterfaceInfo> interfaces= new ArrayList<>();

    public VlanInfo(int id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public VlanInfo(int id) {
        this.id = id;
    }

    public VlanInfo(String name) {
        this.name= name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public List<InterfaceInfo> getInterfaces() {
        return interfaces;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addInterface(InterfaceInfo interfaceInfo) {
        interfaces.add(interfaceInfo);
    }
}