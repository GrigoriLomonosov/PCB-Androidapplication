package com.example.tac.boardcommunicator;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for processing data from an inpustream and doing background calculations for the application
 */
public class DataProcessor {

    private Map<String,String> checkMapARP = new HashMap<>();
    //String[] args = {"/system/bin/cat", "/proc/net/arp"};
    String[] args = {"cat", "/proc/net/arp"};

    /**
     * Returns true if the given string is a possible IPv4-string
     * @param ip
     * @return true if correct IPv4 string, false otherwise
     */
    public boolean checkIPFormat(String ip){
        return true;
    }

    /**
     * Returns the first free dynamic IP in the current network
     * @return The first free IP in the network
     */
    public String findFreeIp() {
        String total = "";
        List<String> ipList = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("/proc/net/arp")));
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                ipList.add(line.split("\\s+")[0]);
            }
            System.out.println(total);
        } catch (IOException e) {
            e.printStackTrace();

        }

        // Sort the IP's
        Collections.sort(ipList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String[] ips1 = o1.split("\\.");
                String updatedIp1 = String.format("%3s.%3s.%3s.%3s",
                        ips1[0],ips1[1],ips1[2],ips1[3]);
                String[] ips2 = o2.split("\\.");
                String updatedIp2 = String.format("%3s.%3s.%3s.%3s",
                        ips2[0],ips2[1],ips2[2],ips2[3]);
                return updatedIp1.compareTo(updatedIp2);
            }
        });

        // Find first free one
        if(ipList.isEmpty()){
            return "198.162.1.1";
        }
        return findFirstFreeInList(ipList);
    }

    private String findFirstFreeInList(List<String>list){
        //TODO implement
        return list.get(0);
    }

   /* public Map<String, String> createArpMap() throws IOException, InterruptedException {
        checkMapARP.clear();
        BufferedReader localBufferdReader = new BufferedReader(new FileReader(new File("/proc/net/arp")));
        String line = "";
        while ((line = localBufferdReader.readLine()) == null) {
            localBufferdReader.close();
            Thread.sleep(1000);
            localBufferdReader = new BufferedReader(new FileReader(new File("/proc/net/arp")));
        }
        do {
            String[] ipmac = line.split("[ ]+");
            if (!ipmac[0].matches("IP")) {
                String ip = ipmac[0];
                String mac = ipmac[3];
                if (!checkMapARP.containsKey(ip)) {
                    checkMapARP.put(ip, mac);
                }
            }
        } while ((line = localBufferdReader.readLine()) != null);
        return Collections.unmodifiableMap(checkMapARP);
    }

    public String toRead()
    {
        ProcessBuilder cmd;
        String result="";

        try{
            cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[1024];
            //TESTCODE
            re[0] = 1;
            while(in.read(re) != -1){
                //System.out.println(new String(re,  "UTF-8"));
                System.out.println(re[1]);
                result = result + new String(re);
            }
            in.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }
        return result;
    }*/

    public void methode4(){
        /*String   s_dns1 ;
        String   s_dns2;
        String   s_gateway;
        String   s_ipAddress;
        String   s_leaseDuration;
        String   s_netmask;
        String   s_serverAddress;
        DhcpInfo d;
        WifiManager wifii;

        wifii = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        d = wifii.getDhcpInfo();

        s_dns1 = "DNS 1: " + String.valueOf(d.dns1);
        s_dns2 = "DNS 2: " + String.valueOf(d.dns2);
        s_gateway = "Default Gateway: " + String.valueOf(d.gateway);
        s_ipAddress = "IP Address: " + String.valueOf(d.ipAddress);
        s_leaseDuration = "Lease Time: " + String.valueOf(d.leaseDuration);
        s_netmask = "Subnet Mask: " + String.valueOf(d.netmask);
        s_serverAddress = "Server IP: " + String.valueOf(d.serverAddress);

        String connections = "";
        InetAddress host;
        try
        {
            host = InetAddress.getByName(intToIp(d.dns1));
            byte[] ip = host.getAddress();

            for(int i = 1; i <= 254; i++)
            {
                ip[3] = (byte) i;
                InetAddress address = InetAddress.getByAddress(ip);
                if(address.isReachable(100))
                {
                    System.out.println(address + " machine is turned on and can be pinged");
                    connections+= address+"\n";
                }
                else if(!address.getHostAddress().equals(address.getHostName()))
                {
                    System.out.println(address + " machine is known in a DNS lookup");
                }

            }
        }
        catch(UnknownHostException e1)
        {
            e1.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        System.out.println(connections);*/
    }

    public String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 24) & 0xFF);
    }

    /**
     * Takes a string and transforms it to a byte array.
     * @param hexInput The input string should be in hexadecimal format
     * @return a bytearray for the hexvalues in the hexInput
     */
    public byte[] stringToByte(String hexInput){
        int len = hexInput.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexInput.charAt(i), 16) << 4)
                    + Character.digit(hexInput.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Returns a string holding the ip-address in hex-format
     * @param reqIpAddr the given ip-address should be in the following format xxx.xxx.xxx.xxx
     * @return a string in the following format xxxxxxxx
     */
    public String ip2Hex(String reqIpAddr) {
        String hex = "";
        String[] part = reqIpAddr.split("[\\.,]");
        if (part.length < 4) {
            return "00000000";
        }
        for (int i = 0; i < 4; i++) {
            int decimal = Integer.parseInt(part[i]);
            if (decimal < 16) // Append a 0 to maintian 2 digits for every
            // number
            {
                hex += "0" + String.format("%01X", decimal);
            } else {
                hex += String.format("%01X", decimal);
            }
        }
        return hex;
    }

    /**
     * Returns an IPv4-address based on a given array of bytes
     * @param arr The first 4 bytes in the array are used to construct the ip
     * @return IPv4-address
     */
    public String byteArrayToStringIP(byte[] arr){
        String result = "";
        int first = arr[0];
        int second = arr[1];
        int third = arr[2];
        int fourth = arr[3];
        return String.valueOf(first) + "." + String.valueOf(second) + "." + String.valueOf(third) + "." + String.valueOf(fourth);
    }
}
