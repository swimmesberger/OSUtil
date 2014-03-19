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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.fseek.thedeath.os.OSColorFactory;
import org.fseek.thedeath.os.OSIconFactory;
import org.fseek.thedeath.os.interfaces.IOSColors;
import org.fseek.thedeath.os.interfaces.IOSIcons;
import org.fseek.thedeath.os.interfaces.IOSIconsAdapter;

/**
 *
 * @author Simon Wimmesberger
 */
public class OSUtil
{
    private static IOSColors osColors;
    private static IOSIcons osIcons;
    static{
        osColors = OSColorFactory.createOSColors();
        setOsIcons(OSIconFactory.getDefaultAdapter());
    }

    public static IOSColors getOsColors()
    {
        return osColors;
    }
    
    public static void setOsIcons(IOSIconsAdapter adapter){
        osIcons = OSIconFactory.createOSIcons(adapter);
    }
    
    public static void setOsIcons(IOSIcons icons)
    {
        osIcons = icons;
    }
    
    public static void setOsColors(IOSColors colors){
        osColors = colors;
    }

    public static IOSIcons getOsIcons()
    {
        return osIcons;
    }
    
    public static void openURL(URL url)
    {
        if (!Desktop.isDesktopSupported())
        {
            throw new UnsupportedOperationException("Desktop is not supported (fatal)");
        }

        Desktop desktop = Desktop.getDesktop();

        if (!desktop.isSupported(Desktop.Action.BROWSE))
        {
            throw new UnsupportedOperationException("Desktop doesn't support the browse action (fatal)");
        }
        try
        {
            desktop.browse(url.toURI());
        } catch (URISyntaxException | IOException e)
        {
            Debug.printException(e);
        }
    }

    public static void openFile(File file)
    {
        if ((OSDetector.isWindows()) && (file.isFile()))
        {
            try
            {
                Runtime.getRuntime().exec("cmd /c \"" + file.getAbsolutePath() + "\"");
            } catch (IOException e)
            {
                Debug.printException(e);
            }
            return;
        }

        if (!Desktop.isDesktopSupported())
        {
            throw new UnsupportedOperationException("Desktop is not supported (fatal)");
        }

        Desktop desktop = Desktop.getDesktop();

        if (!desktop.isSupported(Desktop.Action.OPEN))
        {
            throw new UnsupportedOperationException("Desktop doesn't support the OPEN action (fatal)");
        }
        try
        {
            URI uri = file.getCanonicalFile().toURI();
            desktop.open(new File(uri));
        } catch (IOException e)
        {
            Debug.printException(e);
        }
    }
}
