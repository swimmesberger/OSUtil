package org.fseek.thedeath.os.windows;

import java.io.File;
import org.fseek.thedeath.os.DefaultFileSystem;

public class WindowsFileSystem extends DefaultFileSystem
{
    @Override
    public File getDesktop()
    {
        return get("DESKTOP");
    }
        
    @Override
    public File getRecentFolder()
    {
        return get("RECENT");
    }

    @Override
    public File getDownloadsFolder()
    {
        //special reg code for downloads directory
        return get("DOWNLOADS", "{374DE290-123F-4565-9164-39C4925E467B}");
    }

    @Override
    public File getImageFolder()
    {
        return get("My Pictures");
    }

    @Override
    public File getMusicFolder()
    {
        return get("My Music");
    }

    @Override
    public File getDocumentsFolder()
    {
        return get("Personal");
    }

    @Override
    public File getVideosFolder()
    {
        return get("My Video");
    }
    
    private File get(String method, String regKey){
        File cache = checkCache(method);
        if(cache != null){
            return cache;
        }
        String path = WindowsUtils.getCurrentUserPath(regKey);
        if(path == null)return null;
        return addCache(method, new File(path));
    }
    
    private File get(String regKey){
        return get(regKey, regKey);
    }
}
