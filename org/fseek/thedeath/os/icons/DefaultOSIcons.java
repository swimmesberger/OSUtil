/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fseek.thedeath.os.icons;

import java.io.IOException;
import java.util.HashMap;
import javax.swing.ImageIcon;
import org.fseek.thedeath.os.interfaces.IOSIcons;

/**
 *
 * @author Thedeath<www.fseek.org>
 */
public abstract class DefaultOSIcons implements IOSIcons
{
    private static volatile HashMap<String, ImageIcon> cache;
    
    public abstract ImageIcon getIconByName(String name) throws IOException;
    
    public ImageIcon getIconByNameQuiet(String name){
        try
        {
            return getIconByName(name);
        } catch (IOException ex)
        {
            return null;
        }
        
    }
    
    @Override
    public ImageIcon getCollapsedIcon()
    {
        return this.getIconByNameQuiet("collapsed.png");
    }

    @Override
    public ImageIcon getUncollapsedIcon()
    {
        return this.getIconByNameQuiet("uncollapsed.png");
    }

    @Override
    public ImageIcon getStarIcon()
    {
        return this.getIconByNameQuiet("star.png");
    }

    @Override
    public ImageIcon getLibaryIcon()
    {
        return this.getIconByNameQuiet("bibliothek.png");
    }

    @Override
    public ImageIcon getComputerIcon()
    {
        return this.getIconByNameQuiet("computer.png");
    }

    @Override
    public ImageIcon getPictureIcon()
    {
        return this.getIconByNameQuiet("pictureSmall.png");
    }

    @Override
    public ImageIcon getDocumentIcon()
    {
        return this.getIconByNameQuiet("documentsSmall.png");
    }

    @Override
    public ImageIcon getMusicIcon()
    {
        return this.getIconByNameQuiet("musicSmall.png");
    }

    @Override
    public ImageIcon getVideoIcon()
    {
        return this.getIconByNameQuiet("videosSmall.png");
    }

    @Override
    public ImageIcon getMainDriveIcon()
    {
        return this.getIconByNameQuiet("primaryHarddriveIcon.png");
    }

    @Override
    public ImageIcon getDesktopIcon()
    {
        return this.getIconByNameQuiet("desktop.png");
    }

    @Override
    public ImageIcon getMainDriveIconSmall()
    {
        return this.getIconByNameQuiet("primaryHarddriveIconSmall.png");
    }

    @Override
    public ImageIcon getRecentIcon()
    {
        return this.getIconByNameQuiet("recent.png");
    }

    @Override
    public ImageIcon getDirectoryIcon()
    {
        return this.getIconByNameQuiet("directory.png");
    }

    @Override
    public ImageIcon getFileIcon()
    {
        return this.getIconByNameQuiet("file.png");
    }
    
    protected static void addToCache(String name, ImageIcon i){
        if(cache == null){
            cache = new HashMap<>();
        }
        cache.put(name, i);
    }
    
    protected static ImageIcon getFromCache(String name){
        if(cache == null || cache.containsKey(name) == false){
            return null;
        }
        return cache.get(name);
    }
}
