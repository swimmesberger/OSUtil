package org.fseek.thedeath.os;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.swing.ImageIcon;
import org.fseek.thedeath.os.util.FileSystemUtil;

/**
 *
 * @author Thedeath<www.fseek.org>
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
