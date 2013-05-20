/*
 * Copyright (C) 2013 Thedeath<www.fseek.org>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.fseek.thedeath.os;

import org.fseek.thedeath.os.interfaces.IFileSystem;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import org.fseek.thedeath.os.util.Debug;
import org.fseek.thedeath.os.util.JavaVersionDetector;

/**
 *
 * @author Thedeath<www.fseek.org>
 */
public abstract class DefaultFileSystem implements IFileSystem
{
    private HashMap<String, File> cache;
    
    protected File checkCache(String method){
        method = method.toUpperCase(Locale.getDefault());
        if(cache == null){
            return null;
        }
        if(cache.containsKey(method)){
            return cache.get(method);
        }
        return null;
    }
    
    protected File addCache(String method, File f){
        method = method.toUpperCase(Locale.getDefault());
        if(cache == null){
            cache = new HashMap<>();
        }
        cache.put(method, f);
        return f;
    }

    // gets the home folder
    @Override
    public File getHomeFolder()
    {
        return new File(System.getProperty("user.home"));
    }

    @Override
    public File getWorkingDirectory()
    {
        return new File(System.getProperty("user.dir"));
    }

    @Override
    public File getJarDirectory()
    {
        if(JavaVersionDetector.isJava7()){
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            return new File(s);
        }else{
            return getMainFile();
        }
    }
    
    private File mainFile;
    private File getMainFile()
    {
        if(mainFile != null)return mainFile;
        try
        {
            String path = DefaultFileSystem.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String decodedPath = URLDecoder.decode(path, "UTF-8");
            File mainFileT = new File(decodedPath);
            String absolutePath;
            try
            {
                absolutePath = mainFileT.getCanonicalPath();
                if (absolutePath.contains(".jar"))
                {
                    int index = absolutePath.lastIndexOf(File.separator);
                    absolutePath = absolutePath.substring(0, index);
                }
                mainFile = new File(absolutePath);
                return mainFile;
            }
            catch (IOException ex)
            {
                Debug.printException(ex);
            }
        } 
        catch (UnsupportedEncodingException ex)
        {
            Debug.printException(ex);
        }
        catch(Exception ex)
        {
            Debug.printException(ex);
        }
        return new File(".");
    }
}
