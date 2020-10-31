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
    private String urlStr = null;
    private String fName = null;
    private boolean isHtml = false;
    private String charset = "utf-8";
    private String fileExt = ".html";
    private String savedFileFpath = null;

    private void resetFields() {
        urlStr = null;
        fName = null;
        isHtml = false;
        charset = "utf-8";
        fileExt = ".html";
        savedFileFpath = null;
    }

    public void downloadFileFromUrl(String[] args) {
        resetFields();
        String open = "";
        String filePath = "";
        if (args.length >= 1) {
            urlStr = args[0];
        } else {
            throw new IllegalArgumentException();
        }
        if (args.length >= 2) {
            if(args[1].equals("o")) {
                open = args[1];
                filePath = "./";
            } else {
                filePath = args[1];
            }
        }
        if (args.length == 3) {
            open = args[2];
        }
        URLConnection conn = connect();
        if (filePath == null || filePath.isEmpty()) {
            filePath = getFileName();
            filePath = downloadAndSaveFile(filePath, connect());
        } else {
            Path path = Paths.get(filePath);
            String contentType = conn.getContentType();
            if (contentType != null) {
                getFileExt(contentType);
                getCharset(contentType);
            }
            if (path.toFile().isFile()) {
                filePath = existingFileDown(filePath, conn);
            } else if (path.toFile().isDirectory()) {
                filePath = path.toString() + "\\" + getFileName();
                if (Paths.get(filePath + fileExt).toFile().isFile()) {
                    filePath = existingFileDown(filePath, conn);
                } else {
                    filePath = downloadAndSaveFile(filePath, conn);
                }
            } else if (path.getParent().toFile().isDirectory()) {
                filePath = downloadAndSaveFile(filePath, conn);
            }
        }
        savedFileFpath = filePath;
        if (open.equals("o")) {
            openFile(filePath);
        }
    }

    /**
     * This method is handling the case when file path exists
     *
     * @param filePath   - path of file where we want to save file
     * @param connection UrlConnection object that is connected to the web-site
     * @return file path String
     */
    private String existingFileDown(String filePath, URLConnection connection) {
        System.out.println("File with the name already exists");
        System.out.println("To replace it with a new file enter r");
        System.out.println("To change the name of the saved file enter c");
        Scanner scanner = new Scanner(System.in);
        Path path = Paths.get(filePath);
        String input = scanner.nextLine();
        if (input.equals("r")) {
            filePath = downloadAndSaveFile(filePath, connection);
        } else if (input.equals("c")) {
            System.out.println("Enter file name");
            String filename = scanner.nextLine();
            filePath = path.toFile().getParent() + "\\" + filename;
            filePath = downloadAndSaveFile(filePath, connection);
        } else {
            throw new IllegalArgumentException();
        }
        return filePath;
    }

    private URLConnection connect() {
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
        return conn;
    }

    /**
     * This methods downloads file by url and saves it
     *
     * @param filePath   - path of file where we want to save file
     * @param connection UrlConnection object that is connected to the web-site
     * @return - file path of saved file
     */
    private String downloadAndSaveFile(String filePath, URLConnection connection) {
        filePath += fileExt;
        try {
            BufferedInputStream serverResponse = new BufferedInputStream(connection.getInputStream());
            FileOutputStream fileOS = new FileOutputStream(filePath);
            int bufSize = 1024;
            byte[] buf = new byte[bufSize];
            int byteContent;
            while ((byteContent = serverResponse.read(buf, 0, bufSize)) != -1) {
                fileOS.write(buf, 0, byteContent);
            }
            serverResponse.close();
        } catch (IOException e) {
            System.out.println("Can't download file");
        }
        return filePath;
    }

    private String getFileName() {
        if (fName == null) {
            String fileName = "index";
            Pattern pattern = Pattern.compile("[^/]*\\?");
            Matcher matcher = pattern.matcher(urlStr);
            if (matcher.find()) {
                fileName = matcher.group();
                fileName = fileName.substring(0, fileName.length() - 1);
            }
            fName = fileName;
        }
        return fName;
    }

    private void openFile(String filepath) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                File myFile = new File(filepath);
                desktop.open(myFile);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private String getFileExt(String contentType) {
        fileExt = contentType.split("/")[1];
        if (fileExt.contains(";")) {
            fileExt = "." + fileExt.split(";")[0];
        } else if (fileExt.contains("icon")) {
            fileExt = ".png";
        } else {
            fileExt = "." + fileExt;
        }
        if (fileExt.equals(".html") && !(urlStr.toLowerCase().startsWith("ftp"))) {
            isHtml = true;
        }
        return fileExt;

    }

    private String getCharset(String contentType) {
        if (this.charset == null) {
            Pattern pattern = Pattern.compile("(?<=charset=).*");
            Matcher matcher = pattern.matcher(contentType);
            if (matcher.find()) {
                charset = matcher.group();
            }
        }
        return charset;
    }

    public String getDownloadedHtmlPage() {
        if (!isHtml) {
            throw new IllegalStateException("downloaded file is not html page");
        }
        FileInputStream fis;
        InputStreamReader is;
        StringBuilder sb = new StringBuilder();
        try {
            fis = new FileInputStream(savedFileFpath);
            is = new InputStreamReader(fis, charset);
            char[] cbuf = new char[1024];
            while (is.read(cbuf, 0, 1024) != -1) {
                sb.append(cbuf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
