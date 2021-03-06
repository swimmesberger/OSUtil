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

package org.fseek.thedeath.os.windows;

import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import org.fseek.thedeath.os.icons.ZipIcons;

/**
 *
 * @author Simon Wimmesberger
 */
public class WindowsZipIcons extends ZipIcons{
    public WindowsZipIcons(File zipFile) throws IOException{
        super(zipFile, "windows");
    }
    
    @Override
    public ImageIcon getPictureIcon() {
        return get(WindowsIconsHelper.IMAGE_FOLDER);
    }

    @Override
    public ImageIcon getMusicIcon() {
        return get(WindowsIconsHelper.MUSIC_FOLDER);
    }

    @Override
    public ImageIcon getDocumentIcon() {
        return get(WindowsIconsHelper.DOCUMENT_FOLDER);
    }

    @Override
    public ImageIcon getVideoIcon() {
        return get(WindowsIconsHelper.VIDEO_FOLDER);
    }
    
    @Override
    public ImageIcon getRecentIcon() {
        return get(WindowsIconsHelper.RECENT_FOLDER);
    }
    
    @Override
    public ImageIcon getMainDriveIcon() {
        return get(WindowsIconsHelper.PRIMARY_HARDDRIVE);
    }

    @Override
    public ImageIcon getMainDriveIconSmall() {
        return get(WindowsIconsHelper.PRIMARY_HARDDRIVE_SMALL);
    }

    private ImageIcon get(String name){
        ImageIcon cacheIcon = getFromCache(name);
        if(cacheIcon == null){
            cacheIcon = WindowsIconsHelper.get(name);
            addToCache(name, cacheIcon);
        }
        return cacheIcon;
    }
}
