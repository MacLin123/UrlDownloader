package ru.ncedu.kurakin.urldownloader;

/**
 * This interface provides the ability to download files by url
 * @author Mikhail Kurakin
 */
public interface IUrlDownloader {
    /**
     * This method downloads the file from the url, saves it to the specified path,
     * and opens file if it needed
     * @param args - arg[0] is url, arg[1] is file path, if arg[2] equals o it means,
     *             that file will be open at the end of downloading.
     */
    void downloadFileFromUrl(String ... args);

}
