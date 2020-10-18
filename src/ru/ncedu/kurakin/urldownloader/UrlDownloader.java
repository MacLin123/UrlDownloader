package ru.ncedu.kurakin.urldownloader;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlDownloader implements IUrlDownloader {

    public void downloadFileFromUrl(String... args) {
        String open = "";
        String urlStr = "";
        String filePath = "";
        String readyFilePath = filePath;
        if (args.length >= 1) {
            urlStr = args[0];
        } else {
            throw new IllegalArgumentException();
        }
        if (args.length >= 2) {
            filePath = args[1];
        }
        if(args.length == 3) {
            open = args[2];
        }
        if (filePath == null || filePath.isEmpty()) {
            downloadAndSaveFile(urlStr, getFileName(urlStr));
        } else {
            Path path = Paths.get(filePath);
            if (path.toFile().isFile()) {
                System.out.println("File with the name already exists");
                System.out.println("To replace it with a new file enter r");
                System.out.println("To change the name of the saved file enter c");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                if (input.equals("r")) {
                    downloadAndSaveFile(urlStr, filePath);
                } else if (input.equals("c")) {
                    System.out.println("Enter file name");
                    String filename = scanner.nextLine();
                    path.toFile().getParent();
                    readyFilePath = path.toFile().getParent() + "\\" + filename;
                    downloadAndSaveFile(urlStr, readyFilePath);
                } else {
                    throw new IllegalArgumentException();
                }
            } else if (path.toFile().isDirectory()) {
                readyFilePath = path.toString() + "\\" + getFileName(urlStr);
                downloadAndSaveFile(urlStr, readyFilePath);
            } else if (path.getParent().toFile().isDirectory()) {
                downloadAndSaveFile(urlStr, filePath);
            }
        }
        if(open.equals("o")) {
            openFile(readyFilePath);
        }
    }

    private void downloadAndSaveFile(String urlStr, String filePath) {
        URL url;
        URLConnection conn = null;
        try {
            url = new URL(urlStr);
            conn = url.openConnection();
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
            conn.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedInputStream serverResponse = new BufferedInputStream(conn.getInputStream());
            FileOutputStream fileOS = new FileOutputStream(filePath);
            int bufSize = 1024;
            byte[] buf = new byte[bufSize];
            int byteContent;
            while ((byteContent = serverResponse.read(buf, 0, bufSize)) != -1) {
                fileOS.write(buf, 0, byteContent);
            }
            serverResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileName(String urlStr) {
        String fileName = "index";
        String fileExt;
        Pattern pattern = Pattern.compile("[^/]*\\?");
        Matcher matcher = pattern.matcher(urlStr);
        if (matcher.find()) {
            fileName = matcher.group();
            fileName = fileName.substring(0, fileName.length() - 1);
        }
        fileExt = getExtension(urlStr);
        return fileName + fileExt;
    }

    private String getExtension(String urlStr) {
        String ext = ".html";
        int idx = urlStr.lastIndexOf('.');
        if (idx != -1) {
            String tmp = urlStr.substring(idx);
            if (tmp.matches("\\.[a-zA-Z0-9]+")) {
                ext = tmp;
            }
        }
        return ext;
    }
    private void openFile(String filepath) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                File myFile = new File(filepath);
                desktop.open(myFile);
            } catch (IOException ex) {}
        }
    }
}
