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
import javax.swing.ImageIcon;
import org.fseek.thedeath.os.util.OSUtil;

/**
 *
 * @author Simon Wimmesberger
 */
public class FileIconWrapper implements Comparable<FileIconWrapper>{
    private File file;
    private ImageIcon imgIcon;

    public FileIconWrapper(File file) {
        this.file = file;
        this.imgIcon = getSystemIcon();
    }

    public FileIconWrapper(File file, ImageIcon imgIcon) {
        this.file = file;
        this.imgIcon = imgIcon;
    }
    
    public void setFile(File file) {
        if(this.file != file){
            this.file = file;
        }
    }

    public File getFile() {
        return file;
    }
    
    public ImageIcon getIcon(){
        return this.imgIcon;
    }
    
    public void setIcon(ImageIcon icon){
        this.imgIcon = icon;
    }
    
    public void updateIcon(){
        this.setIcon(getSystemIcon());
    }
    
    public ImageIcon getSystemIcon(boolean large){
        return OSUtil.getFileSystemView().getSystemIcon(this.getFile(), large);
    }
    
    public ImageIcon getSystemIcon(){
        return getSystemIcon(false);
    }

    @Override
    public String toString() {
        return this.getFile().toString();
    }
 
    @Override
    public int compareTo(FileIconWrapper o)
    {
        File f = o.getFile();
        File thisF = this.getFile();
        if(f.isDirectory() && thisF.isDirectory() == false)
        {
            return 1;
        }
        else if(f.isDirectory() == false && thisF.isDirectory())
        {
            return -1;
        }
        else
        {
            int compareTo = thisF.getName().compareToIgnoreCase(f.getName());
            return compareTo;
        }
    }

}
