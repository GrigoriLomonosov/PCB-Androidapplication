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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for processing data from an inpustream and doing background calculations for the application
 */
public class DataProcessor {

    private Map<String,String> checkMapARP = new HashMap<>();
    //String[] args = {"/system/bin/cat", "/proc/net/arp"};
    String[] args = {"cat", "/proc/net/arp"};

    /**
     * Returns the first free dynamic IP in the current network
     * @return The first free IP in the network
     */
    public String findFreeIp() {
        String total = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("/proc/net/arp")));
            String line;
            while ((line = br.readLine()) != null) {
                total += line + "\n";
            }
            System.out.println(total);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return total;
    }

    public Map<String, String> createArpMap() throws IOException, InterruptedException {
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
    }

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
}