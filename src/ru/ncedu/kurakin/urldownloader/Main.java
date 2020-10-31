package ru.ncedu.kurakin.urldownloader;

public class Main {

    public static void main(String[] args) {
        IUrlDownloader ud = new UrlDownloader();
        ud.downloadFileFromUrl(args);

        System.out.println("Printing html page...");
        try {
        System.out.println(ud.getDownloadedHtmlPage());
        } catch (IllegalStateException ese) {
            System.out.println(ese.getMessage());
        }
    }
}
