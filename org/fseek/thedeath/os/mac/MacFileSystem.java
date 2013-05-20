package org.fseek.thedeath.os.mac;

import java.io.File;
import org.fseek.thedeath.os.DefaultFileSystem;

public class MacFileSystem extends DefaultFileSystem
{
    @Override
    public File getDesktop()
    {
        File file = new File(getHomeFolder(), "Desktop");
        return file;
    }

    @Override
    public File getRecentFolder()
    {
        //does not exist on mac (?)
        return null;
    }

    @Override
    public File getDownloadsFolder()
    {
        File file = new File(getHomeFolder(), "Downloads");
        return file;
    }

    @Override
    public File getImageFolder()
    {
        File file = new File(getHomeFolder(), "Pictures");
        return file;
    }

    @Override
    public File getMusicFolder()
    {
        File file = new File(getHomeFolder(), "Music");
        return file;
    }

    @Override
    public File getDocumentsFolder()
    {
        File file = new File(getHomeFolder(), "Documents");
        return file;
    }

    @Override
    public File getVideosFolder()
    {
        File file = new File(getHomeFolder(), "Movies");
        return file;
    }
    
}
