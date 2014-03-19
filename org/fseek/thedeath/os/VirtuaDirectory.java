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
package org.fseek.thedeath.os;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.swing.ImageIcon;
import org.fseek.thedeath.os.util.FileSystemUtil;

/**
 *
 * @author Simon Wimmesberger
 */
public abstract class VirtuaDirectory extends File
{
    public static final int TYPE_NONE = -1;
    public static final int TYPE_RECENT = 1;
    
    private int type = TYPE_NONE;
    public VirtuaDirectory(String pathname)
    {
        super(pathname);
    }

    @Override
    public abstract File[] listFiles();
    
    @Override
    public abstract File[] listFiles(FileFilter filter);
            
    @Override
    public abstract File[] listFiles(FilenameFilter filter);
    
    @Override
    public abstract String getName();
    
    public ImageIcon getIcon(){
        return null;
    }

    @Override
    public String getAbsolutePath()
    {
        return "__"+getName()+"__";
    }

    @Override
    public String getCanonicalPath() throws IOException
    {
        return getAbsolutePath();
    }

    @Override
    public boolean isDirectory()
    {
        return true;
    }

    @Override
    public boolean exists()
    {
        return true;
    }

    @Override
    public boolean canExecute()
    {
        return false;
    }

    @Override
    public boolean canRead()
    {
        return true;
    }

    @Override
    public boolean canWrite()
    {
        return false;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getType()
    {
        return type;
    }
    
    public static File getFileByType(int type){
        switch(type){
            case TYPE_RECENT:
                return FileSystemUtil.getFileSystem().getRecentFolder();
        }
        return null;
    }
}
