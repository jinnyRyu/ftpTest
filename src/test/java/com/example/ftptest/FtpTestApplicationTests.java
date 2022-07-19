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
//            ftpData.setAttrDate(KSTtoSDF(attrs.substring(attrs.length()-28))); //문제
            ftpData.setFileName(entry.getFilename());
            ftpData.setFileDate(fileData);

            ftpListSort.add(ftpData);
        }

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        Collections.sort(ftpListSort, (o1, o2) -> {
//            try {
//                Date o1Time =  sdf.parse(o1.getFileDate());
//                Date o2Time =  sdf.parse(o2.getFileDate());
//
//                if (o1Time.before(o2Time)) {
//                    return -1;
//                } else if (o1Time.after(o2Time)) {
//                    return 1;
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            return 0;
//        });

//        for (int i =0; i<ftpListSort.size(); i++){
//
//            log.info(" listdata {} {}", ftpListSort.get(i).getFileName(),ftpListSort.get(i).getFileDate());
//        }

    }


//    public String KSTtoSDF(String kst) {
//
//        String strDate = null;
//        try {
//            SimpleDateFormat recvSimpleFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.KOREA);
//
//            SimpleDateFormat tranSimpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
//
//            Date data = recvSimpleFormat.parse(kst);
//            strDate = tranSimpleFormat.format(data);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return strDate;
//    }
}
