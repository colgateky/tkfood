package com.magic.utils.shell;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by ifuck on 2018/11/15.
 */
public class ShellHelper {
    protected String host;
    protected String user;

    public ShellHelper(String host, String user){
        this.host = host;
        this.user = user;
    }

    public void executeSsh(String command, HttpServletResponse resp){
        try
        {
            resp.setStatus(200);
            resp.addHeader("Content-Type", "text/html;charset=utf-8");
            resp.getOutputStream().write("<html><head><script src='/js/deploy-log.js'></script><link rel='stylesheet' href='/css/deploy-log.css'></head><body>".getBytes());

            resp.getOutputStream().flush();
            Connection conn = new Connection(host);
            conn.connect();
            String userHome = System.getProperty("user.home");
            File keyFile = new File(userHome + "/.ssh/id_rsa");
            String keyFilePass = ""; // will be ignored if not needed
            boolean isAuthenticated = conn.authenticateWithPublicKey(user, keyFile, keyFilePass);
            if (isAuthenticated == false) {
                resp.getOutputStream().write("Authentication failed.".getBytes());
                throw new IOException("Authentication failed.");
            }

            Session sess = conn.openSession();
            sess.execCommand(command);

            new Thread(() -> {
                InputStream stderr = new StreamGobbler(sess.getStderr());
                BufferedReader br1 = new BufferedReader(new InputStreamReader(stderr));
                try {
                    write(br1, resp, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            InputStream stdout = new StreamGobbler(sess.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            this.write(br, resp, false);

            resp.getOutputStream().write("执行结束<br/></body></html>".getBytes());

            br.close();
            sess.close();
            conn.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void execute(String command, HttpServletResponse resp){
        try {
            resp.setStatus(200);
            resp.addHeader("Content-Type", "text/html;charset=utf-8");
            resp.getOutputStream().write("<html><head><script src='/js/deploy-log.js'></script><style>body{padding: 10px 30px 120px 10px;}</style></head><body>".getBytes());
            resp.getOutputStream().flush();
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(command);

            new Thread(() -> {
                InputStream stderr = process.getErrorStream();
                BufferedReader br1 = new BufferedReader(new InputStreamReader(stderr));
                try {
                    write(br1, resp, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            InputStream stdout = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            this.write(br, resp, false);

            resp.getOutputStream().write("执行结束<br/></body></html>".getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void write(BufferedReader br, HttpServletResponse resp, boolean isStdErr) throws IOException{
        while (true)
        {
            String line = br.readLine();
            if (line == null)
                break;
            line = isStdErr ? String.format("<span style='color:red'>%s</span>", line) : line;
            line += "<br/>";
            synchronized (resp) {
                resp.getOutputStream().write(line.getBytes());
                resp.getOutputStream().flush();
            }
        }
    }
}
