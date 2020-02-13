package com.magic.utils.http;


import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by jamen on 17/2/18.
 */
public class WebServerIpResolver {
    private List<String> domains = new ArrayList<>();
    private List<String> rowIps = new ArrayList<>();
    private List<Ip> cache = new ArrayList<>();
    private final int MaxConnectTime = 3000;
    private String name;
    public int port = 80;
    private static Pattern pattern = Pattern.compile("([1-9]|[1-9]//d|1//d{2}|2[0-4]//d|25[0-5])(//.(//d|[1-9]//d|1//d{2}|2[0-4]//d|25[0-5])){3}");
    public WebServerIpResolver(String name, int port, String... hosts) {
        this.name = name;
        this.port = port;
        for (String host : hosts) {
            if (pattern.matcher(host).matches()) {
                rowIps.add(host);
            } else {
                domains.add(host);
            }
        }
    }
    static class Ip {
        public String ip;
        public long connectTimeTakes;
    }

    public void setInvalidIp(String ip) {
        List<Ip> ipcache = cache;
        for (Ip iip : ipcache) {
            if (ip.equals(iip.ip)) {
                iip.connectTimeTakes = -1;
                break;
            }
        }
    }
    private void resolveIps() {
        Set<String> ips = new HashSet<>();
        for (String domain : domains) {
            try {
                InetAddress[] myServers = InetAddress.getAllByName(domain);
                for (InetAddress server : myServers) {
                    ips.add(server.getHostAddress());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (String ip : rowIps) {
            ips.add(ip);
        }
        if (ips.size() > 0) {
            List<Ip> ipList = new ArrayList<>();
            Set<String> has = new HashSet<>();
            for (Ip ip : cache) {
                if (ips.contains(ip.ip) || ip.connectTimeTakes > 0) {
                    ipList.add(ip);
                    has.add(ip.ip);
                }
            }
            for (String ip : ips) {
                if (has.contains(ip) == false) {
                    Ip iip = new Ip();
                    iip.ip = ip;
                    iip.connectTimeTakes = MaxConnectTime;
                    ipList.add(iip);
                }
            }
            cache = ipList;
        }
    }

    private void checkIps() {
        for (Ip ip : cache) {
            long t = TcpCheckUtils.checkTcpConnectTime(ip.ip, port, MaxConnectTime);
            ip.connectTimeTakes = t;
        }
    }

    public String resolveIp() {
        List<Ip> ipcache = cache;
        if (ipcache.size() <= 0) {
            return null;
        }
        List<String> validIps = new ArrayList<>();
        for (Ip ip : ipcache) {
            if (ip.connectTimeTakes > 0) {
                validIps.add(ip.ip);
            }
        }
        if (validIps.size() > 0) {
            return validIps.get((int)(Math.random()*validIps.size()));
        }
        return ipcache.get((int)(Math.random()*ipcache.size())).ip;
    }

    public void runSelfCheck() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    check();
                    try {
                        Thread.sleep(60000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void check() {
        try {
            resolveIps();
            checkIps();

            StringBuffer sb = new StringBuffer();
            List<Ip> ipList = this.cache;
            for (Ip ip : ipList) {
                sb.append(ip.ip + "(" + ip.connectTimeTakes + ") ");
            }
            System.out.println("[domain-resolve] " + name + ":" + port + " " + sb);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
}