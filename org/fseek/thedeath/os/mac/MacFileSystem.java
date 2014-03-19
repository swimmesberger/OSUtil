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
package org.fseek.thedeath.os.mac;

import java.io.File;
import org.fseek.thedeath.os.DefaultFileSystem;
import org.fseek.thedeath.os.DefaultRecentFolder;

public class MacFileSystem extends DefaultFileSystem
{
    @Override
    public File getDesktop()
    {
        return get("Desktop");
    }

    @Override
    public File getRecentFolder()
    {
        //does not exist on linux (?)
        File cache = checkCache("RECENT");
        if(cache != null){
            return cache;
        }
        File addCache = addCache("RECENT", new DefaultRecentFolder());
        return addCache;
    }

    @Override
    public File getDownloadsFolder()
    {
        return get("Downloads");
    }

    @Override
    public File getImageFolder()
    {
        return get("Pictures");
    }

    @Override
    public File getMusicFolder()
    {
        return get("Music");
    }

    @Override
    public File getDocumentsFolder()
    {
        return get("Documents");
    }

    @Override
    public File getVideosFolder()
    {
        return get("Movies");
    }
    
    private File get(String folderName){
        //does not exist on linux (?)
        File cache = checkCache(folderName);
        if(cache != null){
            return cache;
        }
        File addCache = addCache(folderName, new File(getHomeFolder(), folderName));
        return addCache;
    }
    
}
