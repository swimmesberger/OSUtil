package org.fseek.thedeath.os.interfaces;

import java.io.File;

/**
 *
 * @author Thedeath<www.fseek.org>
 */
public interface IFileSystem
{
    //This method trys to find the desktop folder
    public File getDesktop();
    
    // gets the folder where the last used files are located, works only on linux yet
    public File getRecentFolder();

    // gets the downloads folder
    public File getDownloadsFolder();

    // gets the images folder
    public File getImageFolder();

    // gets the music folder
    public File getMusicFolder();

    // gets the documents folder
    public File getDocumentsFolder();

    // gets the videos folder
    public File getVideosFolder();

    // gets the home folder
    public File getHomeFolder();
    
    /*
     * Returns the working directory so if the application is started via command line the directory where the command was executed is returned.
     */
    public File getWorkingDirectory();
    /*
     * Returns the directory where the .class (.jar) files are located
     */
    public File getJarDirectory();
}
