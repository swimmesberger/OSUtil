package org.fseek.thedeath.os;

import org.fseek.thedeath.os.util.OSDetector;
import org.fseek.thedeath.os.interfaces.IFileSystem;
import org.fseek.thedeath.os.windows.WindowsFileSystem;
import org.fseek.thedeath.os.linux.LinuxFileSystem;
import org.fseek.thedeath.os.mac.MacFileSystem;

public class OSFileSystemFactory
{
    public static IFileSystem createExtendFileSystem()
    {
        if (OSDetector.isWindows())
        {
            return new WindowsFileSystem();
        }
        else if (OSDetector.isMac())
        {
            return new MacFileSystem();
        }
        //default fileSystem
        else
        {
            return new LinuxFileSystem();
        }
    }
}
