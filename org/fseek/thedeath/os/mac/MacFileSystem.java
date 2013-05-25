package org.fseek.thedeath.os.mac;

import java.io.File;
import org.fseek.thedeath.os.DefaultFileSystem;
import org.fseek.thedeath.os.DefaultRecentFolder;

public class MacFileSystem extends DefaultFileSystem
{
    @Override
    public File getDesktop()
    {
        return get("Desktop");
    }

    @Override
    public File getRecentFolder()
    {
        //does not exist on linux (?)
        File cache = checkCache("RECENT");
        if(cache != null){
            return cache;
        }
        File addCache = addCache("RECENT", new DefaultRecentFolder());
        return addCache;
    }

    @Override
    public File getDownloadsFolder()
    {
        return get("Downloads");
    }

    @Override
    public File getImageFolder()
    {
        return get("Pictures");
    }

    @Override
    public File getMusicFolder()
    {
        return get("Music");
    }

    @Override
    public File getDocumentsFolder()
    {
        return get("Documents");
    }

    @Override
    public File getVideosFolder()
    {
        return get("Movies");
    }
    
    private File get(String folderName){
        //does not exist on linux (?)
        File cache = checkCache(folderName);
        if(cache != null){
            return cache;
        }
        File addCache = addCache(folderName, new File(getHomeFolder(), folderName));
        return addCache;
    }
    
}
