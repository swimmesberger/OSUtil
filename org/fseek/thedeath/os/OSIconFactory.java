/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fseek.thedeath.os;

import org.fseek.thedeath.os.util.OSDetector;
import org.fseek.thedeath.os.icons.DefaultPackageIconsAdapter;
import org.fseek.thedeath.os.interfaces.IOSIcons;
import org.fseek.thedeath.os.interfaces.IOSIconsAdapter;

/**
 *
 * @author Thedeath<www.fseek.org>
 */
public class OSIconFactory
{
    private static final DefaultPackageIconsAdapter DEFAULT = new DefaultPackageIconsAdapter();
    
    public static IOSIcons createOSIcons(){
        return createOSIcons(DEFAULT);
    }
    
    public static IOSIcons createOSIcons(IOSIconsAdapter adapter)
    {
        IOSIcons osIcons;
        if(OSDetector.isWindows()){
            osIcons = adapter.getWindows();
        }else if(OSDetector.isMac()){
            osIcons = adapter.getMac();
        }
        else{
            osIcons = adapter.getLinux();
        }
        return osIcons;
    }

    public static DefaultPackageIconsAdapter getDefaultAdapter()
    {
        return DEFAULT;
    }
}
