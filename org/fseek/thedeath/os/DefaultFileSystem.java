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
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import org.fseek.thedeath.os.interfaces.IFileSystem;
import org.fseek.thedeath.os.util.Debug;

/**
 *
 * @author Simon Wimmesberger
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
        File checkCache = checkCache("user.home");
        if(checkCache != null){
            return checkCache;
        }
        File file = new File(System.getProperty("user.home"));
        return addCache("user.home", file);
    }

    @Override
    public File getWorkingDirectory()
    {
        File checkCache = checkCache("user.dir");
        if(checkCache != null){
            return checkCache;
        }
        return addCache("user.dir", new File(System.getProperty("user.dir")));
    }

    @Override
    public File getJarDirectory()
    {
        File checkCache = checkCache("jar.dir");
        if(checkCache != null){
            return checkCache;
        }
        return addCache("jar.dir", getMainFile());
    }
    
    public static Class<?> getCallerClassName() { 
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(DefaultFileSystem.class.getName()) && ste.getClassName().indexOf("java.lang.Thread")!=0) {
                try {
                    return Class.forName(ste.getClassName());
                } catch (ClassNotFoundException ex) {
                    Debug.printException(ex);
                }
            }
        }
        return null;
     }

    public static File getMainFile()
    {
        try
        {
            Class<?> caller = getCallerClassName();
            String path = caller.getProtectionDomain().getCodeSource().getLocation().getPath();
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
                return new File(absolutePath);
            }
            catch (IOException ex)
            {
                Debug.printException(ex);
            }
        }
        catch(Exception ex )
        {
            Debug.printException(ex);
        }
        return Paths.get("").toFile();
    }
}
