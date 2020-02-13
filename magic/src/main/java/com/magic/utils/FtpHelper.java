package com.magic.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;

public class FtpHelper {


    private FTPClient ftp = null;
    private String server;

    private String uname;

    private String password;

    private int port = 21;

    public FtpHelper(String server, int port, String uname,
                     String password) {
        this.server = server;
        if (this.port > 0) {
            this.port = port;
        }
        this.uname = uname;
        this.password = password;
        ftp = new FTPClient();
        ftp.setControlKeepAliveTimeout(300);
        ftp.setConnectTimeout(60000);
        ftp.setDataTimeout(60000);
        ftp.setDefaultTimeout(60000);
    }

    public FTPClient connectFTPServer() throws Exception {
        try {
            ftp.setControlEncoding("GBK");
            //ftp.setControlEncoding("UTF-8");

            ftp.configure(getFTPClientConfig());
            ftp.connect(this.server, this.port);
            if (!ftp.login(this.uname, this.password)) {
                ftp.logout();
                ftp = null;
                return ftp;
            }

            // 文件类型,默认是ASCII
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

            // 设置被动模式
            ftp.enterLocalPassiveMode();

            ftp.setBufferSize(1024);

            // 响应信息
            int replyCode = ftp.getReplyCode();
            if ((!FTPReply.isPositiveCompletion(replyCode))) {

                // 关闭Ftp连接
                closeFTPClient();

                // 释放空间
                ftp = null;
                throw new Exception("登录FTP服务器失败,请检查![Server:" + server + "、"
                        + "User:" + uname + "、" + "Password:" + password);
            } else {
                return ftp;
            }
        } catch (Exception e) {
            closeFTPClient();
            throw e;
        }
    }


    /**
     * 配置FTP连接参数
     *
     * @return
     * @throws Exception
     */
    public FTPClientConfig getFTPClientConfig() throws Exception {
        String systemKey = FTPClientConfig.SYST_UNIX;//FTPClientConfig.SYST_NT;
        String serverLanguageCode = "zh";
        FTPClientConfig conf = new FTPClientConfig(systemKey);
        conf.setServerLanguageCode(serverLanguageCode);
        conf.setDefaultDateFormatStr("yyyy-MM-dd");
        return conf;
    }


    /**
     * 向FTP根目录上传文件
     *
     * @param localFile
     * @param newName   新文件名
     * @throws Exception
     */
    public Boolean uploadFile(String localFile, String newName)
            throws Exception {
        InputStream input = null;
        boolean success = false;
        try {
            File file = null;
            if (checkFileExist(localFile)) {
                file = new File(localFile);
            }
            input = new FileInputStream(file);
            success = ftp.storeFile(newName, input);
            if (!success) {
                throw new Exception("文件上传失败!");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return success;
    }


    /**
     * 向FTP根目录上传文件
     *
     * @param input
     * @param newName 新文件名
     * @throws Exception
     */
    public Boolean uploadFile(InputStream input, String newName)
            throws Exception {
        boolean success = false;
        try {
            success = ftp.storeFile(newName, input);
            if (!success) {
                throw new Exception("文件上传失败!");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return success;
    }


    /**
     * 向FTP指定路径上传文件
     *
     * @param localFile
     * @param newName        新文件名
     * @param remoteFoldPath
     * @throws Exception
     */
    public Boolean uploadFile(String localFile, String newName,
                              String remoteFoldPath) throws Exception {
        InputStream input = null;
        boolean success = false;
        try {
            File file = null;
            if (checkFileExist(localFile)) {
                file = new File(localFile);
            }
            input = new FileInputStream(file);

            // 改变当前路径到指定路径
            if (!this.changeDirectory(remoteFoldPath)) {
                System.out.println("服务器路径不存!");
                return false;
            }
            success = ftp.storeFile(newName, input);
            if (!success) {
                throw new Exception("文件上传失败!");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return success;
    }


    /**
     * 向FTP指定路径上传文件
     *
     * @param input
     * @param newName        新文件名
     * @param remoteFoldPath
     * @throws Exception
     */
    public Boolean uploadFile(InputStream input, String newName,
                              String remoteFoldPath) throws Exception {
        boolean success = false;
        try {
            // 改变当前路径到指定路径
            if (!this.changeDirectory(remoteFoldPath)) {
                System.out.println("服务器路径不存!");
                return false;
            }
            success = ftp.storeFile(newName, input);
            if (!success) {
                throw new Exception("文件上传失败!");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return success;
    }

    /**
     * 从FTP服务器下载文件
     *
     * @param remotePath FTP路径(不包含文件名)
     * @param fileName   下载文件名
     * @param localPath  本地路径
     */
    public Boolean downloadFile(String remotePath, String fileName,
                                String localPath) throws Exception {
        Date begin = new Date();
        BufferedOutputStream output = null;
        boolean success = false;
        try {
            File dir = new File(localPath);
            if (!dir.exists())
                dir.mkdirs();

            // 检查本地路径
            this.checkFileExist(localPath);

            // 改变工作路径
            if (!this.changeDirectory(remotePath)) {
                System.out.println("当前路径 " + ftp.printWorkingDirectory() + " 服务器路径" + remotePath + "不存在");
                return false;
            }

            // 列出当前工作路径下的文件列表
            List<FTPFile> fileList = this.getFileList();
            if (fileList == null || fileList.size() == 0) {
                System.out.println("服务器当前路径下不存在文件！");
                return success;
            }


            File localFilePath = new File(localPath + File.separator
                    + fileName);
            output = new BufferedOutputStream(new FileOutputStream(
                    localFilePath));
            success = ftp.retrieveFile(fileName, output);
            if (!success) {
                System.err.println("文件下载失败:" + remotePath + " " + fileName);
            } else {
                System.out.println("下载成功 (耗时:" + (System.currentTimeMillis() - begin.getTime()) / 1000d + " s)：" + remotePath + "/" + fileName + "-> " + localPath + "/" + fileName);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (output != null) {
                output.close();
            }
        }
        return success;
    }


    /**
     * 从FTP服务器获取文件流
     *
     * @param remoteFilePath
     * @return
     * @throws Exception
     */
    public InputStream downloadFile(String remoteFilePath) throws Exception {


        return ftp.retrieveFileStream(remoteFilePath);
    }

    /**
     * 获取FTP服务器上[指定路径]下的文件列表
     *
     * @param remotePath
     * @return
     * @throws Exception
     */
    public List<FTPFile> getFileList(String remotePath) throws Exception {
        if (remotePath != null && !remotePath.endsWith("/")) {
            remotePath += "/";
        }
        List<FTPFile> ftpfiles = Arrays.asList(ftp.listFiles(remotePath, new FTPFileFilter() {
            @Override
            public boolean accept(FTPFile f) {

                if (".".equals(f.getName()) || "..".equals(f.getName())) {
                    return false;
                }
                return true;
            }
        }));


        return ftpfiles;
    }


    /**
     * 获取FTP服务器[当前工作路径]下的文件列表
     *
     * @return
     * @throws Exception
     */
    public List<FTPFile> getFileList() throws Exception {


        List<FTPFile> ftpfiles = Arrays.asList(ftp.listFiles(null, new FTPFileFilter() {
            @Override
            public boolean accept(FTPFile f) {
                if (!".".equals(f.getName()) && !"..".equals(f.getName())) {
                    return true;
                }
                return false;
            }
        }));

        return ftpfiles;
    }


    /**
     * 改变FTP服务器工作路径
     *
     * @param remoteFoldPath
     */
    public Boolean changeDirectory(String remoteFoldPath) throws Exception {
        return ftp.changeWorkingDirectory(remoteFoldPath);
    }


    /**
     * 删除文件
     *
     * @param remoteFilePath
     * @return
     * @throws Exception
     */
    public Boolean deleteFtpServerFile(String remoteFilePath) throws Exception {
        return ftp.deleteFile(remoteFilePath);
    }


    /**
     * 创建目录
     *
     * @param remoteFoldPath
     * @return
     */
    public boolean createFold(String remoteFoldPath) throws Exception {
        boolean flag = ftp.makeDirectory(remoteFoldPath);
        if (!flag) {
            throw new Exception("创建目录失败");
        }
        return false;
    }


    /**
     * 删除目录
     *
     * @param remoteFoldPath
     * @return
     * @throws Exception
     */
    public boolean deleteFold(String remoteFoldPath) throws Exception {
        return ftp.removeDirectory(remoteFoldPath);
    }


    /**
     * 删除目录以及文件
     *
     * @param remoteFoldPath
     * @return
     */
    public boolean deleteFoldAndsubFiles(String remoteFoldPath)
            throws Exception {
        boolean success = false;
        List<FTPFile> list = this.getFileList(remoteFoldPath);
        if (list == null || list.size() == 0) {
            return deleteFold(remoteFoldPath);
        }
        for (FTPFile ftpFile : list) {

            String name = ftpFile.getName();
            if (ftpFile.isDirectory()) {
                success = deleteFoldAndsubFiles(remoteFoldPath + "/" + name);
                if (!success)
                    break;
            } else {
                success = deleteFtpServerFile(remoteFoldPath + "/" + name);
                if (!success)
                    break;
            }
        }
        if (!success)
            return false;
        success = deleteFold(remoteFoldPath);
        return success;
    }


    /**
     * 检查本地路径是否存在
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public boolean checkFileExist(String filePath) throws Exception {
        boolean flag = false;
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("本地路径不存在,请检查!");
        } else {
            flag = true;
        }
        return flag;
    }

    public long getLocalFileSize(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return 0;
        }
        return file.length();
    }

    /**
     * 关闭FTP连接
     *
     * @param ftp
     * @throws Exception
     */
    public void closeFTPClient(FTPClient ftp) throws Exception {
        try {
            if (ftp != null && ftp.isConnected()) {
                ftp.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 关闭FTP连接
     *
     * @throws Exception
     */
    public void closeFTPClient() throws Exception {
        this.closeFTPClient(this.ftp);
    }

    public FTPClient getFtp() {
        return ftp;
    }


    public String getServer() {
        return server;
    }


    public String getUname() {
        return uname;
    }


    public String getPassword() {
        return password;
    }


    public int getPort() {
        return port;
    }


    public void setFtp(FTPClient ftp) {
        this.ftp = ftp;
    }


    public void setServer(String server) {
        this.server = server;
    }


    public void setUname(String uname) {
        this.uname = uname;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public void setPort(int port) {
        this.port = port;
    }

    public List<String> synFtpFolder(String remotePath, String localPath, boolean checkSize) throws Exception {

        List<String> downList = new ArrayList<>();

        if (remotePath == null) {
            remotePath = "/";
        } else if (!remotePath.startsWith("/")) {
            remotePath = "/" + remotePath;
        }

        List<FTPFile> lst = getFileList(remotePath);
//        System.out.println("同步文件夹：" +remotePath+" "+lst.size());
        for (int i = 0; i < lst.size(); i++) {
            FTPFile f = lst.get(i);
            if (f.getType() == FTPFile.DIRECTORY_TYPE) {
                List<String> downList2 = synFtpFolder((remotePath == "/" ? "" : remotePath) + "/" + f.getName(), localPath + "/" + f.getName(), checkSize);
                downList.addAll(downList2);
            } else if (f.getType() == FTPFile.FILE_TYPE) {
                File dir = new File(localPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                boolean temp = true;
                if (checkSize && f.getSize() <= getLocalFileSize(localPath + "/" + f.getName())) {
                    temp = false;
                }
                if (temp) {
                    downloadFile(remotePath, f.getName(), localPath);
                    downList.add(localPath + "/" + f.getName());
                } else {
//                    System.err.println("文件大小一致,忽略同步:"+remotePath+"/"+f.getName());
                }
            }
        }
        return downList;
    }

    public List<FTPFile> getAllFiles(String remotePath) throws Exception {
        Date begin = new Date();
        if (remotePath == null) {
            remotePath = "/";
        } else if (!remotePath.startsWith("/")) {
            remotePath = "/" + remotePath;
        }
        List<FTPFile> files = new ArrayList<>();
        List<FTPFile> lst = getFileList(remotePath);
        for (int i = 0; i < lst.size(); i++) {
            FTPFile f = lst.get(i);
            if (f.getType() == FTPFile.DIRECTORY_TYPE) {
                files.addAll(getAllFiles((remotePath == "/" ? "" : remotePath) + "/" + f.getName()));
            } else if (f.getType() == FTPFile.FILE_TYPE) {
                files.add(f);
            } else {
                System.out.println("ftp文件 特殊类型" + f.getType());
            }
        }
        System.out.println("遍历" + remotePath + " 耗时 " + (System.currentTimeMillis() - begin.getTime()) / 1000d + " s");
        return files;
    }

}
