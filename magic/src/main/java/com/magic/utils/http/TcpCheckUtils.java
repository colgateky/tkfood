package com.magic.utils.http;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by jamen on 17/2/18.
 */
public class TcpCheckUtils {
    public static long checkTcpConnectTime(String host, int port, int timeOut) {
        Socket socket = new Socket();
        long start = System.currentTimeMillis();
        long end = -1;
        try {
            socket.setSoTimeout(timeOut);
            socket.connect(new InetSocketAddress(host, port));
            end = System.currentTimeMillis();
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (end < 0) {
                return -1;
            }
            return end - start;
        }
    }
}