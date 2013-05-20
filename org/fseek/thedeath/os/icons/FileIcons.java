package org.fseek.thedeath.os.icons;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Thedeath<www.fseek.org>
 */
public class FileIcons extends DefaultOSIcons
{

    private File dir;
    public FileIcons(File directory){
        if(directory.isDirectory() == false){
            throw new IllegalArgumentException("The passed file isn't a directory!");
        }
        this.dir = directory;
    }
    
    @Override
    public ImageIcon getIconByName(String name) throws IOException
    {
        ImageIcon cacheIcon = getFromCache(name);
        if(cacheIcon == null){
            cacheIcon = getIconByNameImpl(name);
            addToCache(name, cacheIcon);
        }
        return cacheIcon;
    }
    
    private ImageIcon getIconByNameImpl(String name) throws IOException{
        File f = new File(dir, name);
        return new ImageIcon(ImageIO.read(f));
    }
}
