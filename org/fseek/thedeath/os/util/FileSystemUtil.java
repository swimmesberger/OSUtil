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
package org.fseek.thedeath.os.util;

import java.io.File;
import java.io.IOException;
import org.fseek.thedeath.os.interfaces.IFileSystem;
import org.fseek.thedeath.os.OSFileSystemFactory;

/**
 *
 * @author Simon Wimmesberger
 */
public class FileSystemUtil
{
    private static IFileSystem fileSystem;
    private static volatile File mainDrive;
    
    static
    {
        fileSystem = OSFileSystemFactory.createExtendFileSystem();
    }

    public static IFileSystem getFileSystem()
    {
        return fileSystem;
    }
    
    public static boolean isMainDrive(File f){
        if(mainDrive == null)
        {
            mainDrive = new File(getDriveBegin(getFileSystem().getHomeFolder()));
        }
        if(f.getAbsolutePath().equals(mainDrive.getAbsolutePath()))
        {
            return true;
        }
        return false;
    }
    
    public static String getDriveBegin(File f)
    {
        try
        {
            String canonicalPath = f.getCanonicalPath();
            int index = canonicalPath.indexOf(File.separator);
            String s = canonicalPath.substring(0, index);
            return s;
        } catch (IOException ex)
        {
            System.out.println(ex);
        }
        return null;
    }
}
