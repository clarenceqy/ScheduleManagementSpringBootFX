package com.synir.utility;

import ch.ethz.ssh2.*;
import com.jcraft.jsch.*;
import com.jcraft.jsch.Session;
import com.synir.models.POJO.Schedule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;

@Configuration
public class FileUploadUtil {

    @Value("${remoteServer.url}")
    private String url;

    @Value("${remoteServer.password}")
    private String passWord;

    @Value("${remoteServer.username}")
    private String userName;

    @Async
    public ResultEntity uploadFile(File file, String targetPath, String remoteFileName) throws Exception{
        ScpConnectEntity scpConnectEntity=new ScpConnectEntity();
        scpConnectEntity.setTargetPath(targetPath);
        scpConnectEntity.setUrl("47.103.118.75");
        scpConnectEntity.setPassWord("898421aA!");
        scpConnectEntity.setUserName("root");
        String code = null;
        String message = null;
        try {
            if (file == null || !file.exists()) {
                throw new IllegalArgumentException("请确保上传文件不为空且存在！");
            }
            if(remoteFileName==null || "".equals(remoteFileName.trim())){
                throw new IllegalArgumentException("远程服务器新建文件名不能为空!");
            }
            remoteUploadFile(scpConnectEntity, file, remoteFileName);
            code = "ok";
            message = remoteFileName;
        } catch (IllegalArgumentException e) {
            code = "Exception";
            message = e.getMessage();
        } catch (JSchException e) {
            code = "Exception";
            message = e.getMessage();
        } catch (IOException e) {
            code = "Exception";
            message = e.getMessage();
        } catch (Exception e) {
            throw e;
        } catch (Error e) {
            code = "Error";
            message = e.getMessage();
        }
        return new ResultEntity(code, message, null);
    }


    private void remoteUploadFile(ScpConnectEntity scpConnectEntity, File file,
                                  String remoteFileName) throws JSchException, IOException {

        Connection connection = null;
        ch.ethz.ssh2.Session session = null;
        SCPOutputStream scpo = null;
        FileInputStream fis = null;

        try {
            createDir(scpConnectEntity);
        }catch (JSchException e) {
            throw e;
        }
        try {
            connection = new Connection(scpConnectEntity.getUrl());
            connection.connect();

            if(!connection.authenticateWithPassword(scpConnectEntity.getUserName(),scpConnectEntity.getPassWord())){
                throw new RuntimeException("SSH连接服务器失败");
            }

            SCPClient scpClient = new SCPClient(connection);
            scpo = scpClient.put(file.getName(), file.length(), scpConnectEntity.getTargetPath(), null);
            fis = new FileInputStream(file);

            byte[] b = new byte[4096];
            int i;
            while ((i = fis.read(b)) != -1) {
                scpo.write(b, 0, i);
            }
            session= connection.openSession();//打开一个会话
            //远程执行linux命令 因为上传的文件没有读的文件 需要加上才能下载 （如果你上传的文件有）
            String cmd = "chmod +r "+scpConnectEntity.getTargetPath()+file.getName();
            System.out.println("linux命令=="+cmd);
            session.execCommand(cmd);

        } catch (IOException e) {
            throw new IOException("SSH上传文件至服务器出错"+e.getMessage());
        }finally {
            if(null != fis){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != scpo){
                try {
                    scpo.flush();
//                    scpo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != session){
                session.close();
            }
            if(null != connection){
                connection.close();
            }
        }
    }


    private boolean createDir(ScpConnectEntity scpConnectEntity ) throws JSchException {

        JSch jsch = new JSch();
        com.jcraft.jsch.Session sshSession = null;
        Channel channel= null;
        try {
            sshSession = jsch.getSession(scpConnectEntity.getUserName(), scpConnectEntity.getUrl(), 22);
            sshSession.setPassword(scpConnectEntity.getPassWord());
            sshSession.setConfig("StrictHostKeyChecking", "no");
            sshSession.connect();
            channel = sshSession.openChannel("sftp");
            channel.connect();
        } catch (JSchException e) {
            e.printStackTrace();
            throw new JSchException("SFTP连接服务器失败"+e.getMessage());
        }
        ChannelSftp channelSftp=(ChannelSftp) channel;
        if (isDirExist(scpConnectEntity.getTargetPath(),channelSftp)) {
            channel.disconnect();
            channelSftp.disconnect();
            sshSession.disconnect();
            return true;
        }else {
            String pathArry[] = scpConnectEntity.getTargetPath().split("/");
            StringBuffer filePath=new StringBuffer("/");
            for (String path : pathArry) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                try {
                    if (isDirExist(filePath.toString(),channelSftp)) {
                        channelSftp.cd(filePath.toString());
                    } else {
                        // 建立目录
                        channelSftp.mkdir(filePath.toString());
                        // 进入并设置为当前目录
                        channelSftp.cd(filePath.toString());
                    }
                    //System.out.println("路径建立" + filePath.toString());
                } catch (SftpException e) {
                    e.printStackTrace();
                    throw new JSchException("SFTP无法正常操作服务器"+e.getMessage());
                }
            }
        }
        channel.disconnect();
        channelSftp.disconnect();
        sshSession.disconnect();
        return true;
    }

    private boolean isDirExist(String directory,ChannelSftp channelSftp) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = channelSftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }

        return isDirExistFlag;
    }

    public String downloadSchedule(String targetPath,String filename) throws IOException {
        ch.ethz.ssh2.Session session = null;
        SCPInputStream scpi = null;
        Connection connection = null;

        String res = "未命名";
        try {
            ScpConnectEntity scpConnectEntity=new ScpConnectEntity();
            scpConnectEntity.setTargetPath(targetPath);
            scpConnectEntity.setUrl("47.103.118.75");
            scpConnectEntity.setPassWord("898421aA!");
            scpConnectEntity.setUserName("root");
            connection = new Connection(scpConnectEntity.getUrl());
            connection.connect();

            if(!connection.authenticateWithPassword(scpConnectEntity.getUserName(),scpConnectEntity.getPassWord())){
                throw new RuntimeException("SSH连接服务器失败");
            }

            File file = new File("");
            String envpath = file.getAbsolutePath() + File.separator + "Datafile";

            SCPClient sc = connection.createSCPClient();
            // 获取远程机器上的文件流 (远程文件地址的绝对路径)
            SCPInputStream is = sc.get(targetPath+"/"+filename);
            // 创建本机文件写入流
            FileOutputStream os = new FileOutputStream(new File(envpath+File.separator+filename));
            // 将文件写入本地
            byte[] b = new byte[4096];
            int i;
            while ((i = is.read(b)) != -1) {
                os.write(b, 0, i);
            }
            os.flush();
            // copy完成，关闭相应资源
            is.close();
            os.close();
            System.out.println("copy ok");




//            StringBuilder sb = new StringBuilder();
//            byte[] arr = new byte[4096];
//            int read;
//            while (true) {
//                read = scpi.read(arr);
//                if (read < 0) {break;}
//                sb.append(new String(arr,"UTF-8"));
//                //sb.append(arr);
//            }
//            String[] s = sb.toString().split("\u0000\u0001");
//
//            for(String e:s){
//                System.out.println(e);
//            }
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            if(null != scpi){
                try {
                    scpi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != session){
                session.close();
            }
            if(null != connection){
                connection.close();
            }
        }
        return res;
    }

}