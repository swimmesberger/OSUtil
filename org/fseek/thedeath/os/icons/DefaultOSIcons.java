/* 
 * The MIT License
 *
 * Copyright 2014 Simon Wimmesberger.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.fseek.thedeath.os.icons;

import java.io.IOException;
import java.util.HashMap;
import javax.swing.ImageIcon;
import org.fseek.thedeath.os.interfaces.IOSIcons;

/**
 *
 * @author Simon Wimmesberger
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
