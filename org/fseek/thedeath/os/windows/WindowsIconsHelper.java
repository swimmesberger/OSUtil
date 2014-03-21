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
import javax.swing.ImageIcon;
import org.fseek.thedeath.os.util.FileSystemUtil;
import org.fseek.thedeath.os.util.OSUtil;

/**
 *
 * @author Simon Wimmesberger
 */
public class WindowsIconsHelper {
    protected static final String IMAGE_FOLDER = "IMAGE";
    protected static final String MUSIC_FOLDER = "MUSIC";
    protected static final String VIDEO_FOLDER = "VIDEO";
    protected static final String DOCUMENT_FOLDER = "DOCUMENT";
    protected static final String RECENT_FOLDER = "RECENT";
    protected static final String PRIMARY_HARDDRIVE = "PRIMARY";
    protected static final String PRIMARY_HARDDRIVE_SMALL = "PRIMARY_SMALL";

    public static ImageIcon get(String name) {
        File f = null;
        boolean large = false;
        switch (name) {
            case IMAGE_FOLDER:
                f = OSUtil.getFileSystem().getImageFolder();
                break;
            case MUSIC_FOLDER:
                f = OSUtil.getFileSystem().getMusicFolder();
                break;
            case VIDEO_FOLDER:
                f = OSUtil.getFileSystem().getVideosFolder();
                break;
            case DOCUMENT_FOLDER:
                f = OSUtil.getFileSystem().getDocumentsFolder();
                break;
            case RECENT_FOLDER:
                f = OSUtil.getFileSystem().getRecentFolder();
                break;
            case PRIMARY_HARDDRIVE:
                f = FileSystemUtil.getMainDrive();
                large = true;
                break;
            case PRIMARY_HARDDRIVE_SMALL:
                f = FileSystemUtil.getMainDrive();
                break;
        }
        if(f == null)return null;
        return OSUtil.getFileSystemView().getSystemIcon(f, large);
    }
}
