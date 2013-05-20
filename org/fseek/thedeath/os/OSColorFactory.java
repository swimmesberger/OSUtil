package org.fseek.thedeath.os;

import org.fseek.thedeath.os.util.OSDetector;
import org.fseek.thedeath.os.interfaces.IOSColors;
import org.fseek.thedeath.os.linux.LinuxColors;
import org.fseek.thedeath.os.mac.MacColors;
import org.fseek.thedeath.os.windows.WindowsColors;

/**
 *
 * @author Thedeath<www.fseek.org>
 */
public class OSColorFactory
{
    public static IOSColors createOSColors()
    {
        if (OSDetector.isWindows())
        {
            return new WindowsColors();
        }
        else if (OSDetector.isMac())
        {
            return new MacColors();
        }
        //default fileSystem
        else
        {
            return new LinuxColors();
        }
    }
}
