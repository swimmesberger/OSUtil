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
import org.fseek.thedeath.os.DefaultFileSystem;

public class WindowsFileSystem extends DefaultFileSystem
{
    @Override
    public File getDesktop()
    {
        return get("DESKTOP");
    }
        
    @Override
    public File getRecentFolder()
    {
        return get("RECENT");
    }

    @Override
    public File getDownloadsFolder()
    {
        //special reg code for downloads directory
        return get("DOWNLOADS", "{374DE290-123F-4565-9164-39C4925E467B}");
    }

    @Override
    public File getImageFolder()
    {
        return get("My Pictures");
    }

    @Override
    public File getMusicFolder()
    {
        return get("My Music");
    }

    @Override
    public File getDocumentsFolder()
    {
        return get("Personal");
    }

    @Override
    public File getVideosFolder()
    {
        return get("My Video");
    }
    
    private File get(String method, String regKey){
        File cache = checkCache(method);
        if(cache != null){
            return cache;
        }
        String path = WindowsUtils.getCurrentUserPath(regKey);
        if(path == null)return null;
        return addCache(method, new File(path));
    }
    
    private File get(String regKey){
        return get(regKey, regKey);
    }
}
