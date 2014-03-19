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

import org.fseek.thedeath.os.util.OSDetector;
import org.fseek.thedeath.os.icons.DefaultPackageIconsAdapter;
import org.fseek.thedeath.os.interfaces.IOSIcons;
import org.fseek.thedeath.os.interfaces.IOSIconsAdapter;

/**
 *
 * @author Simon Wimmesberger
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
