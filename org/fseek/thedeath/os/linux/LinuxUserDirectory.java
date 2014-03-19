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
package org.fseek.thedeath.os.linux;

import java.io.File;

/**
 *
 * @author Simon Wimmesberger
 */
public interface LinuxUserDirectory {
    public  static final String DOWNLOAD_DIR_KEY = "DOWNLOAD";
    public static final String DESKTOP_DIR_KEY = "DESKTOP";
    public static final String TEMPLATES_DIR_KEY = "TEMPLATES";
    public static final String PUBLICSHARE_DIR_KEY = "PUBLICSHARE";
    public static final String DOCUMENTS_DIR_KEY = "DOCUMENTS";
    public static final String MUSIC_DIR_KEY = "MUSIC";
    public static final String PICTURES_DIR_KEY = "PICTURES";
    public static final String VIDEOS_DIR_KEY = "VIDEOS";
    public static final String RECENT_DIR_KEY = "RECENT";
    public static final String HOME_DIR_KEY = "HOME";
    
    public File getUserDirectory(String key);
}
