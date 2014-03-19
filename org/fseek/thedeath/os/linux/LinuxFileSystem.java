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
import org.fseek.thedeath.os.DefaultFileSystem;

public class LinuxFileSystem extends DefaultFileSystem
{
    private static final LinuxUserDirectory userDirectoryHelper = new LinuxUserDirectoryHybrid();
    
    protected File getDownloadsFolderByTries(){
        return get(LinuxUserDirectory.DOWNLOAD_DIR_KEY);
    }
    
    protected File getRecentFolderByTries(){
        return get(LinuxUserDirectory.RECENT_DIR_KEY);
    }
    
    protected File getMusicFolderByTries(){
        return get(LinuxUserDirectory.MUSIC_DIR_KEY);
    }
    
    protected File getDocumentsFolderByTries(){
        return get(LinuxUserDirectory.DOCUMENTS_DIR_KEY);
    }
    
    protected File getVideosFolderByTries()
    {
        return get(LinuxUserDirectory.VIDEOS_DIR_KEY);
    }
    
    protected File getDesktopFolderByTries(){
        return get(LinuxUserDirectory.DESKTOP_DIR_KEY);
    }
    
    protected File getImageFolderByTries(){
        return get(LinuxUserDirectory.PICTURES_DIR_KEY);
    }

    @Override
    public File getDesktop()
    {
        return get(LinuxUserDirectory.DESKTOP_DIR_KEY);
    }
    
    /**
     * Workaround on Linux with @see org.fseek.thedeath.os.DefaultRecentFolder
     * @return Recent directory File
     */
    @Override
    public File getRecentFolder()
    {
        return get(LinuxUserDirectory.RECENT_DIR_KEY);
    }

    @Override
    public File getDownloadsFolder()
    {
        return get(LinuxUserDirectory.DOWNLOAD_DIR_KEY);
    }

    @Override
    public File getImageFolder()
    {
        return get(LinuxUserDirectory.PICTURES_DIR_KEY);
    }

    @Override
    public File getMusicFolder()
    {
        return get(LinuxUserDirectory.MUSIC_DIR_KEY);
    }

    @Override
    public File getDocumentsFolder()
    {
        return get(LinuxUserDirectory.DOCUMENTS_DIR_KEY);
    }

    @Override
    public File getVideosFolder()
    {
        return get(LinuxUserDirectory.VIDEOS_DIR_KEY);
    }
    
    private File get(String key){
        File cache = checkCache(key);
        if(cache != null){
            return cache;
        }
        File file = userDirectoryHelper.getUserDirectory(key);
        addCache(key, file);
        return file;
    }
}
