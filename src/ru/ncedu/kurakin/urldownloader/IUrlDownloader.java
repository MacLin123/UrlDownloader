package ru.ncedu.kurakin.urldownloader;

/**
 * This interface provides the ability to download files by url
 * This interface can:
 * Set the file name if a file with this name exists
 * open the file when download is completed
 * detect the encoding of the html page and read it with the correct encoding
 *
 * @author Mikhail Kurakin
 */
public interface IUrlDownloader {
    /**
     * This method downloads the file from the url, saves it to the specified path,
     * and opens file if it needed
     * Input example: https://www.google.com/ ./files/ o
     * Input example2: https://www.google.com/ o
     * Input example3: https://www.google.com/
     *
     * @param args - arg[0] is url
     *             arg[1] is file path or equals o and it means, that file will be open at the end of downloading.
     *             if arg[2] equals o it means, that file will be open at the end of downloading.
     */
    void downloadFileFromUrl(String[] args);

    /**
     * This methods returns downloaded html in String
     *
     * @return html in String
     */
    String getDownloadedHtmlPage();

}
