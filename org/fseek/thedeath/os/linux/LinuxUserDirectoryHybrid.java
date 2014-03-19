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
 * Combines the features of LinuxUserDirectoryConfig and LinuxUserDirectoryGuess
 * @author Simon Wimmesberger
 */
public class LinuxUserDirectoryHybrid implements LinuxUserDirectory{
    private static final LinuxUserDirectoryGuess guessHelper = new LinuxUserDirectoryGuess();
    private static final LinuxUserDirectoryConfig configHelper = new LinuxUserDirectoryConfig();
    
    @Override
    public File getUserDirectory(String key) {
        if(key.equals(RECENT_DIR_KEY) || key.equals(HOME_DIR_KEY)){
            switch(key){
                case RECENT_DIR_KEY:
                    return getRecentFolder();
                case HOME_DIR_KEY:
                    return getHomeFolder();
            }
        }
        File f = configHelper.getUserDirectory(key);
        if(f != null && f.exists())return f;
        //configHelper can't determine the user directories -> fallback to "guess" the directories
        return guessHelper.getUserDirectory(key);
    }
    
     /**
     * Not available on linux workaround via custom finder
     * @return Recent directory file
     */
    protected File getRecentFolder(){
        return new DefaultRecentFolder();
    }

    protected File getHomeFolder()
    {
        return new File(System.getProperty("user.home"));
    }
}
