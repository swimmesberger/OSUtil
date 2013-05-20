package org.fseek.thedeath.os.icons;

import org.fseek.thedeath.os.interfaces.IOSIcons;
import org.fseek.thedeath.os.interfaces.IOSIconsAdapter;

/**
 *
 * @author Thedeath<www.fseek.org>
 */
public class DefaultPackageIconsAdapter implements IOSIconsAdapter
{
    @Override
    public IOSIcons getWindows()
    {
        return new PackageIcons(this.getClass(), "windows", "def");
    }

    @Override
    public IOSIcons getMac()
    {
        return new PackageIcons(this.getClass(), "mac", "def");
    }

    @Override
    public IOSIcons getLinux()
    {
        return new PackageIcons(this.getClass(), "linux", "def");
    }
}
