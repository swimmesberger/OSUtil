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
package org.fseek.thedeath.os.util;

import java.io.File;
import java.io.IOException;
import org.fseek.thedeath.os.interfaces.IFileSystem;
import org.fseek.thedeath.os.OSFileSystemFactory;

/**
 *
 * @author Thedeath<www.fseek.org>
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
