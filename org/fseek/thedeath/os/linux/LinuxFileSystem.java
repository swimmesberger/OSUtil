package org.fseek.thedeath.os.linux;

import java.io.File;
import org.fseek.thedeath.os.DefaultFileSystem;

public class LinuxFileSystem extends DefaultFileSystem
{
    @Override
    public File getDesktop()
    {
        String trys[] =
        {
            "Desktop"
        };
        return get(trys, "DESKTOP");
    }
    
    // gets the folder where the last used files are located, works only on windows yet
    @Override
    public File getRecentFolder()
    {
        //does not exist on linux (?)
        return null;
    }

    @Override
    public File getDownloadsFolder()
    {
        // trys to find the downloads folder on linux, also for the different languages
        String trys[] =
        {
            "Downloads"
        };
        return get(trys, "DOWNLOADS");
    }

    //TODO: mac support
    @Override
    public File getImageFolder()
    {
        // trys to find the pictures folder on linux, also for the different languages
        String trys[] =
        {
            "Pictures", "Bilder"
        };
        return get(trys, "PICTURES");
    }

    //TODO: mac support
    @Override
    public File getMusicFolder()
    {
        String trys[] =
        {
            "Music", "Musik"
        };
        return get(trys, "MUSIC");
    }

    @Override
    public File getDocumentsFolder()
    {
        String trys[] =
        {
            "Documents", "Dokumente"
        };
        return get(trys, "DOCUMENTS");
    }

    @Override
    public File getVideosFolder()
    {
        String trys[] =
        {
            "Videos"
        };
        return get(trys, "VIDEOS");
    }
    
    private File get(String[] trys, String method){
        File cache = checkCache(method);
        if(cache != null){
            return cache;
        }
        File file = null;
        for (String s : trys)
        {
            file = new File(getHomeFolder(), s);
            if (file.exists())
            {
                return file;
            }
        }
        addCache(method, file);
        return file;
    }
}
