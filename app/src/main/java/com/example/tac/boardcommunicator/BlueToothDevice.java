package com.example.tac.boardcommunicator;

public class BlueToothDevice {

    private String name;
    //The MAC-address
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BlueToothDevice(String name, String address){
        this.name = name;
        this.address = address;
    }
}
