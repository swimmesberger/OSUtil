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

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Simon Wimmesberger
 */
public class FileIcons extends DefaultOSIcons
{
    private File dir;
    private String subdir;
    private String defaultDir;
    public FileIcons(File directory){
        this(directory, "");
    }
    
    public FileIcons(File directory, String subdir){
         this(directory, subdir, "def");
    }
    
    public FileIcons(File directory, String subdir, String defaultDir){
        if(directory.isDirectory() == false){
            throw new IllegalArgumentException("The passed file isn't a directory!");
        }
        if(subdir.endsWith(File.separator) == false){
            subdir = subdir + File.separator;
        }
        if(defaultDir.endsWith(File.separator) == false){
            defaultDir = defaultDir + File.separator;
        }
        this.subdir = subdir;
        this.dir = directory;
        this.defaultDir = defaultDir;
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
        ImageIcon ic = getIconByNameImpl(name, subdir);
        if(ic == null){
            ic = getIconByNameImpl(name, defaultDir);
        }
        return ic;
    }
    
    private ImageIcon getIconByNameImpl(String name, String dir) throws IOException{
        File f = new File(this.dir, dir + name);
        if(f.canRead() == false)return null;
        return new ImageIcon(ImageIO.read(f));
    }
}
