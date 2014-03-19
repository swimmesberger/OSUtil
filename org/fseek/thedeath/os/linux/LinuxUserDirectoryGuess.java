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
import org.fseek.thedeath.os.DefaultRecentFolder;

/**
 *
 * @author Simon Wimmesberger
 */
public class LinuxUserDirectoryGuess implements LinuxUserDirectory{
    protected static final String[] DOWNLOAD_TRIES =   {"Downloads"};
    protected static final String[] DESKTOP_TRIES =   {"Desktop"};
    protected static final String[] PICTURES_TRIES =   {"Pictures", "Bilder"};
    protected static final String[] DOCUMENTS_TRIES =   {"Documents", "Dokumente"};
    protected static final String[] VIDEOS_TRIES =   {"Videos"};
    protected static final String[] MUSIC_TRIES =   {"Music", "Musik"};
 
    @Override
    public File getUserDirectory(String key) {
        return getUserDirectoryImpl(key);
    }
    
    protected File getUserDirectoryImpl(String key){
        switch(key){
            case DESKTOP_DIR_KEY:
                return getDesktopFolder();
            case PICTURES_DIR_KEY:
                return getImageFolder();
            case DOCUMENTS_DIR_KEY:
                break;
            case DOWNLOAD_DIR_KEY:
                return getDownloadsFolder();
            case VIDEOS_DIR_KEY:
                return getVideosFolder();
            case MUSIC_DIR_KEY:
                return getMusicFolder();
            case TEMPLATES_DIR_KEY:
                break;
            case PUBLICSHARE_DIR_KEY:
                break;
        }
        return null;
    }
    
    protected File getDownloadsFolder(){
        return get(DOWNLOAD_TRIES);
    }
    
    protected File getMusicFolder(){
        return get(MUSIC_TRIES);
    }
    
    protected File getDocumentsFolder(){
        return get(DOCUMENTS_TRIES);
    }
    
    protected File getVideosFolder()
    {
        return get(VIDEOS_TRIES);
    }
    
    protected File getDesktopFolder(){
        return get(DESKTOP_TRIES);
    }
    
    protected File getImageFolder(){
        return get(PICTURES_TRIES);
    }
    
    protected File getHomeFolder()
    {
        return new File(System.getProperty("user.home"));
    }
    
    private File get(String[] trys){
        File file = null;
        for (String s : trys)
        {
            file = new File(getHomeFolder(), s);
            if (file.exists())
            {
                return file;
            }
        }
        return file;
    }
}
