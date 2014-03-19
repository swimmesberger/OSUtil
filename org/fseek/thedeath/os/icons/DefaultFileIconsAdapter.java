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

package org.fseek.thedeath.os.icons;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.ImageIcon;
import org.fseek.thedeath.os.interfaces.IOSIcons;
import org.fseek.thedeath.os.interfaces.IOSIconsAdapter;

/**
 *
 * @author Simon Wimmesberger
 */
public class DefaultFileIconsAdapter implements IOSIconsAdapter{
    private File file;
    private String defaultDir;
    public DefaultFileIconsAdapter(File iconsDirectory) throws IOException{
        this.file = iconsDirectory;
    }
    
    private IOSIcons get(String dir){
        return new FileIcons(file, dir);
    }
    
    @Override
    public IOSIcons getWindows() {
        return get("windows");
    }

    @Override
    public IOSIcons getMac() {
        return get("mac");
    }

    @Override
    public IOSIcons getLinux() {
        return get("linux");
    }
}
