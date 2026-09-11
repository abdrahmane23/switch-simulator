package org.example.Model;

import org.example.Command.CommandResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Switch {

    private List<InterfaceInfo> interfaces;
    private List<VlanInfo> vlans;
    private List<MacEntry> macTable;
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();


    public Switch() {
        initialize();
    }

    public void initialize() {

        interfaces = new ArrayList<>();
        vlans = new ArrayList<>();
        macTable = new ArrayList<>();

        // =========================
        // VLANs
        // =========================

        VlanInfo vlan1 =
                new VlanInfo(1, "default", "ACTIVE");

        VlanInfo vlan10 =
                new VlanInfo(10, "USERS", "ACTIVE");

        VlanInfo vlan20 =
                new VlanInfo(20, "SERVERS", "ACTIVE");

        VlanInfo vlan30 =
                new VlanInfo(30, "MANAGEMENT", "ACTIVE");

        vlans.add(vlan1);
        vlans.add(vlan10);
        vlans.add(vlan20);
        vlans.add(vlan30);


        // =========================
        // Interfaces
        // =========================

        InterfaceInfo gi01 =
                new InterfaceInfo(
                        "GigabitEthernet0/1",
                        "UP",
                        "UP",
                        vlan10
                );

        InterfaceInfo gi02 =
                new InterfaceInfo(
                        "GigabitEthernet0/2",
                        "UP",
                        "UP",
                        vlan10
                );

        InterfaceInfo gi03 =
                new InterfaceInfo(
                        "GigabitEthernet0/3",
                        "DOWN",
                        "DOWN",
                        vlan20
                );

        InterfaceInfo gi04 =
                new InterfaceInfo(
                        "GigabitEthernet0/4",
                        "UP",
                        "UP",
                        vlan20
                );

        interfaces.add(gi01);
        interfaces.add(gi02);
        interfaces.add(gi03);
        interfaces.add(gi04);


        vlan10.addInterface(gi01);
        vlan10.addInterface(gi02);

        vlan20.addInterface(gi03);
        vlan20.addInterface(gi04);


        macTable.add(
                new MacEntry(
                        "00:1A:2B:3C:4D:5E",
                        "GigabitEthernet0/1"
                )
        );

        macTable.add(
                new MacEntry(
                        "00:1A:2B:3C:4D:6E",
                        "GigabitEthernet0/4"
                )
        );

        macTable.add(
                new MacEntry(
                        "00:1A:2B:3C:4D:9E",
                        "GigabitEthernet0/2"
                )
        );
    }
    public List<InterfaceInfo> getInterfaces() {
        return interfaces;
    }

    public List<VlanInfo> getVlans() {
        return vlans;
    }

    public List<MacEntry> getMacTable() {
        return macTable;
    }

    public CommandResult executeUpInterfaceCommand(InterfaceInfo interfaceInfo) {
        if (!interfaces.contains(interfaceInfo)){
            return new CommandResult(false, "Interface not found");
        }
        InterfaceInfo foundInterface = interfaces.get(interfaces.indexOf(interfaceInfo));
        if (foundInterface.getStatus()=="UP") {
            return new CommandResult(false, "Interface is already UP");
        }
        foundInterface.setStatus("UP");
        foundInterface.setProtocol("UP");
        return new CommandResult(true);
    }
    public CommandResult executeDownInterfaceCommand(InterfaceInfo interfaceInfo) {
        if (!interfaces.contains(interfaceInfo)){
            return new CommandResult(false, "Interface not found");
        }
        InterfaceInfo foundInterface = interfaces.get(interfaces.indexOf(interfaceInfo));
        if (foundInterface.getStatus().equals("DOWN")) {
            return new CommandResult(false, "Interface is already DOWN");
        }
        makeInterfaceDown(foundInterface);
        return new CommandResult(true);
    }

    public CommandResult executeUpVlanCommand(VlanInfo vlanInfo) {
        Optional<VlanInfo> candidateVlan = getVLanIfExistsById(vlanInfo);
        if(candidateVlan.isEmpty()){
            return new CommandResult(false,"vlan not found");
        }
        VlanInfo foundVlan = candidateVlan.get();
        if (foundVlan.getStatus().equals("ACTIVE")) {
            return new CommandResult(false, "VLAN is already UP");
        }
        foundVlan.setStatus("ACTIVE");
        for (InterfaceInfo interfaceInfo:foundVlan.getInterfaces()){
            executeUpInterfaceCommand(interfaceInfo);
        }
        return new CommandResult(true);
    }
    public CommandResult executeDownVlanCommand(VlanInfo vlanInfo) {
        Optional<VlanInfo> candidateVlan = getVLanIfExistsById(vlanInfo);
        if(candidateVlan.isEmpty()){
            return new CommandResult(false,"vlan not found");
        }
        VlanInfo foundVlan = candidateVlan.get();
        if (foundVlan.getStatus().equals("IN_ACTIVE")) {
            return new CommandResult(false, "VLAN is already DOWN");
        }
        foundVlan.setStatus("IN_ACTIVE");
        for (InterfaceInfo interfaceInfo:foundVlan.getInterfaces()){
            makeInterfaceDown(interfaceInfo);
        }
        return new CommandResult(true);
    }
    public CommandResult executeCreateInterfaceCommand(InterfaceInfo interfaceInfo,VlanInfo vlanInfo) {
        if (interfaces.contains(interfaceInfo)){
            return new CommandResult(false, "Interface already exists");
        }
        Optional<VlanInfo> candidateVlan = getVLanIfExistsById(vlanInfo);
        if(candidateVlan.isEmpty()){
            return new CommandResult(false,"vlan not found");
        }
        VlanInfo foundVlan = candidateVlan.get();
        interfaceInfo.setStatus("UP");
        interfaceInfo.setProtocol("UP");
        interfaceInfo.setVlan(foundVlan);
        foundVlan.addInterface(interfaceInfo);
        interfaces.add(interfaceInfo);
        System.out.println(interfaces.get(interfaces.indexOf(interfaceInfo)).getName());
        return new CommandResult(true);
    }


    public CommandResult executeDeleteInterfaceCommand(InterfaceInfo interfaceInfo) {
        if (!interfaces.contains(interfaceInfo)){
            return new CommandResult(false, "Interface not found");
        }
        InterfaceInfo foundInterface = interfaces.get(interfaces.indexOf(interfaceInfo));
        makeInterfaceDown(foundInterface);
        VlanInfo vlan = foundInterface.getVlan();
        vlan.getInterfaces().remove(foundInterface);
        interfaces.remove(foundInterface);
        return new CommandResult(true);
    }

    public CommandResult executeCreateVlanCommand(VlanInfo vlanInfo) {

        if (checkVlanDuplicationByName(vlanInfo)){
            return new CommandResult(false, "VLAN name already exists");
        }
        int uniqueId = vlans.stream().map(VlanInfo::getId).max(Integer::compareTo).get()+1; // used get directly because we are insuring that the list is not empty because of switch early initialization
        vlanInfo.setId(uniqueId);
        vlanInfo.setStatus("ACTIVE");
        vlans.add(vlanInfo);
        return new CommandResult(true);
    }

    public CommandResult executeDeleteVlanCommand(VlanInfo vlanInfo) {
        Optional<VlanInfo> candidateVlan = getVLanIfExistsById(vlanInfo);
        if(candidateVlan.isEmpty()){
            return new CommandResult(false,"vlan not found");
        }
        VlanInfo foundVlan = candidateVlan.get();
        for (InterfaceInfo interfaceInfo:foundVlan.getInterfaces()){
            makeInterfaceDown(interfaceInfo);
            interfaces.remove(interfaceInfo);
        }
        vlans.remove(foundVlan);
        return new CommandResult(true);
    }

    public CommandResult executeAssignCommand(String macAddress, InterfaceInfo interfaceInfo) {
        System.out.println(interfaces);
        if(!interfaces.contains(interfaceInfo)){
            return new CommandResult(false, "Interface not found");
        }
        InterfaceInfo foundInterface = interfaces.get(interfaces.indexOf(interfaceInfo));
        if (foundInterface.getStatus().equals("DOWN")){
            return new CommandResult(false, "Interface is DOWN");
        }

        for(MacEntry macEntry:macTable){
            if(macEntry.getMacAddress().equals(macAddress)){
                return new CommandResult(false, "MAC address already  to "+ macEntry.getInterfaceName());
            }
            if (macEntry.getInterfaceName().equals(foundInterface.getName())){
                return new CommandResult(false, "Interface already has a MAC address assigned "+ macEntry.getMacAddress());
            }
        }
        macTable.add(new MacEntry(macAddress, foundInterface.getName()));
        return new CommandResult(true);
    }

    public CommandResult executeUnassignCommand(String macAddress, InterfaceInfo interfaceInfo) {
        System.out.println(macTable);
        for (MacEntry macEntry:macTable){
            if(macEntry.getMacAddress().equals(macAddress) && macEntry.getInterfaceName().equals(interfaceInfo.getName())){
                macTable.remove(macEntry);
                return new CommandResult(true);
            }
        }
        return new CommandResult(false, "MAC address not found on the specified interface");
    }
    private Optional<VlanInfo> getVLanIfExistsById(VlanInfo vlanInfo){
        return  vlans.stream()
                .filter(
                        vlan-> vlan.getId() == vlanInfo.getId()).findFirst();
    }

    private boolean checkVlanDuplicationByName (VlanInfo vlanInfo){
        return vlans.stream().anyMatch(v->v.getName().equals(vlanInfo.getName()));
    }

    private void makeInterfaceDown(InterfaceInfo foundInterface) {
        foundInterface.setStatus("DOWN");
        foundInterface.setProtocol("DOWN");
        for (MacEntry macEntry : macTable) {
            if (macEntry.getInterfaceName().equals(foundInterface.getName())) {
                macTable.remove(macEntry);
                break;
            }
        }
    }
}
