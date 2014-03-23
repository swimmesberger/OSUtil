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
import java.net.URI;
import javax.swing.ImageIcon;
import org.fseek.thedeath.os.util.OSUtil;

/**
 *
 * @author Simon Wimmesberger
 */
public class FileIcon extends File{
    private ImageIcon fileIcon;
    private ImageIcon systemIcon;
    
    public FileIcon(String pathname) {
        super(pathname);
    }

    public FileIcon(URI uri) {
        super(uri);
    }

    public FileIcon(File parent, String child) {
        super(parent, child);
    }

    public FileIcon(String parent, String child) {
        super(parent, child);
    }
    
    public ImageIcon getIcon(){
        return fileIcon;
    }
    
    public void setIcon(ImageIcon icon){
        this.fileIcon = icon;
    }
    
    public ImageIcon getSystemIcon(boolean large){
        if(this.systemIcon == null){
            this.systemIcon = OSUtil.getFileSystemView().getSystemIcon(this, large);
        }
        return this.systemIcon;
    }
    
    public ImageIcon getSystemIcon(){
        return getSystemIcon(false);
    }
}
