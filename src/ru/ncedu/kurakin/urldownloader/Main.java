package ru.ncedu.kurakin.urldownloader;


// http://dcn.icc.spbstu.ru/~karpov/%D0%9A%D1%83%D1%80%D1%81%20%D0%9B%D0%9E%D0%93%D0%98%D0%9A%D0%90/KURSOVAJA/%D0%9A%D0%A3%D0%A0%D0%A1%D0%9E%D0%92%D0%90%D0%AF%20%D0%9C%D0%B5%D1%82%D0%BE%D0%B4%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B8%D0%B5%20%D0%A3%D0%BA%D0%B0%D0%B7%D0%B0%D0%BD%D0%B8%D1%8F.doc
// http://www.itmm.unn.ru/files/2020/09/4-kurs-IITMM-2020.xlsx
// https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_700KB.mp3

public class Main {
    public static void main(String[] args) {
        IUrlDownloader ud = new UrlDownloader();
        String filepath = "./files/"; //exist dir
//        String filepath = "./files/newFile.html"; //not exist filepath
//        String filepath = "./files/index.mp3"; // exist filepath
        String urlStr = "https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_700KB.mp3";
        ud.downloadFileFromUrl(urlStr, filepath,"o");
//        ud.downloadFileFromUrl(args);
    }

//    public static void main(String[] args) {
//        IUrlDownloader ud = new UrlDownloader();
//        if (args.length == 3) {
//            ud.downloadFileFromUrl(args[0], args[1]);
//            if (args[2] == "o") {
//            }
//        } else if (args.length == 2) {
//            ud.downloadFileFromUrl(args[0], args[1]);
//        } else {
//            ud.downloadFileFromUrl(args[0], "");
//        }
//    }
}
