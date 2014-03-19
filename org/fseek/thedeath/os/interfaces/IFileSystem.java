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
package org.fseek.thedeath.os.interfaces;

import java.io.File;

/**
 *
 * @author Simon Wimmesberger
 */
public interface IFileSystem
{
    //This method trys to find the desktop folder
    public File getDesktop();
    
    // gets the folder where the last used files are located, works only on linux yet
    public File getRecentFolder();

    // gets the downloads folder
    public File getDownloadsFolder();

    // gets the images folder
    public File getImageFolder();

    // gets the music folder
    public File getMusicFolder();

    // gets the documents folder
    public File getDocumentsFolder();

    // gets the videos folder
    public File getVideosFolder();

    // gets the home folder
    public File getHomeFolder();
    
    /*
     * Returns the working directory so if the application is started via command line the directory where the command was executed is returned.
     */
    public File getWorkingDirectory();
    /*
     * Returns the directory where the .class (.jar) files are located
     */
    public File getJarDirectory();
}
