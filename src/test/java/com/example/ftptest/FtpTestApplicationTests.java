package com.example.ftptest;

import com.example.ftptest.ftpData.FtpData;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class FtpTestApplicationTests {

    String username = "";
    String host = "";
    int port = 22;
    String password = "";
    String ftpDir = "";

    @Test
    void contextLoads() throws JSchException, SftpException {

        JSch jsch = new JSch();
        Session session = jsch.getSession(username, host, port);
        session.setPassword(password);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        log.info("session {}", session);
        session.connect();

        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp channelSftp = (ChannelSftp) channel;

        Vector<ChannelSftp.LsEntry> entries = channelSftp.ls(ftpDir);

        ArrayList<FtpData> ftpListSort = new ArrayList<>();

        int index = 0;
        for (ChannelSftp.LsEntry entry : entries) {
            String attrs = String.valueOf(entry.getAttrs());
            String fileData = entry.getFilename().split("[_-]")[1];

            FtpData ftpData = new FtpData();
            ftpData.setAttrDate(KSTtoSDF(attrs.substring(attrs.length()-28)));
            ftpData.setFileName(entry.getFilename());
            ftpData.setFileDate(fileData);

            ftpListSort.add(ftpData);
            log.info("get filename {}",ftpListSort.get(0).getFileName());
        }

    }


    public String KSTtoSDF(String kst){
        String strDate = null;
        try {
            SimpleDateFormat recvSimpleFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.KOREA);

            SimpleDateFormat tranSimpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

            Date data = recvSimpleFormat.parse(kst);
            strDate = tranSimpleFormat.format(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }
}
