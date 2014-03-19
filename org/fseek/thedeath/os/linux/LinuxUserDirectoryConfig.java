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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Simon Wimmesberger
 */
public class LinuxUserDirectoryConfig implements LinuxUserDirectory{
    private static final HashMap<String, String> XDG_USER_MAPPINGS;
    private static HashMap<String, File> userDirs;
    
    static{
        XDG_USER_MAPPINGS = new HashMap<>();
        XDG_USER_MAPPINGS.put("XDG_DESKTOP_DIR", DESKTOP_DIR_KEY);
        XDG_USER_MAPPINGS.put("XDG_DOWNLOAD_DIR", DOWNLOAD_DIR_KEY);
        XDG_USER_MAPPINGS.put("XDG_TEMPLATES_DIR", TEMPLATES_DIR_KEY);
        XDG_USER_MAPPINGS.put("XDG_PUBLICSHARE_DIR", PUBLICSHARE_DIR_KEY);
        XDG_USER_MAPPINGS.put("XDG_DOCUMENTS_DIR", DOCUMENTS_DIR_KEY);
        XDG_USER_MAPPINGS.put("XDG_MUSIC_DIR", MUSIC_DIR_KEY);
        XDG_USER_MAPPINGS.put("XDG_PICTURES_DIR", PICTURES_DIR_KEY);
        XDG_USER_MAPPINGS.put("XDG_VIDEOS_DIR", VIDEOS_DIR_KEY);
    }
    
    @Override
    public File getUserDirectory(String key) {
        return getUserDirectoryImpl(key);
    }
    
        
    protected File getUserDirectoryImpl(String key)
    {
        HashMap<String, File> dirStore = getUserDirectoryStore();
        if(dirStore == null){
            return null;
        }
        if(dirStore.containsKey(key) == false){
            return null;
        }
        return dirStore.get(key);
    }
    
    protected static HashMap<String, File> getUserDirectoryStore(){
        try{
            if(userDirs == null){
                userDirs = getUserDirectoriesImpl();
            }
            if(userDirs == null){
                return null;
            }
            return userDirs;
        }catch(UnsupportedOperationException ex){
            return null;
        }
    }
    
    public File[] getUserDirectories(){
        HashMap<String, File> dirStore = getUserDirectoryStore();
        if(dirStore == null){
            return null;
        }
        return dirStore.values().toArray(new File[0]);
    }

    /**
     * Checks ~/.config/user-dirs.dirs for the paths to the user directory
     * @return 
     */
    protected static HashMap<String, File> getUserDirectoriesImpl(){
        File userDir = new File(System.getProperty("user.home"));
        String userPath = userDir.toString();
        File f = new File(userDir, ".config/user-dirs.dirs");
        if(f.exists() == false){
            throw new UnsupportedOperationException("User-dirs not supported");
        }
        try(BufferedReader br = new BufferedReader(new FileReader(f))){
            HashMap<String, File> linuxStore = new HashMap<>();
            while(true){
                String line = br.readLine();
                if(line == null)break;
                if(line.startsWith("#"))continue;
                Pattern pattern = Pattern.compile("(?<KEY>[a-zA-Z_]+)=\"(?<VALUE>.+)\"");
                Matcher matcher = pattern.matcher(line);
                boolean find = matcher.find();
                if(find == false)continue;
                String key = matcher.group("KEY");
                String value = matcher.group("VALUE");
                value = value.replace("$HOME", userPath);
                String keyStoreKey = XDG_USER_MAPPINGS.get(key);
                if(keyStoreKey != null){
                    linuxStore.put(keyStoreKey, new File(value));
                }
            }
            return linuxStore;
        } catch (IOException ex) {
            return null;
        }
    }
}
