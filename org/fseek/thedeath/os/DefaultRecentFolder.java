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
package org.fseek.thedeath.os;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.ImageIcon;
import org.fseek.thedeath.os.util.Debug;
import org.fseek.thedeath.os.util.FileSystemUtil;
import org.fseek.thedeath.os.util.OSUtil;

/**
 *
 * @author Simon Wimmesberger
 */
public class DefaultRecentFolder extends VirtuaDirectory
{
    private static final File DIR = FileSystemUtil.getFileSystem().getHomeFolder();
    //one week (milliseconds)
    private static final long timespan = 604800000;
    
    private File[] fileCache;
    public DefaultRecentFolder(){
        this("__VIRTUAL_FOLDER__");
    }
    
    public DefaultRecentFolder(String pathname)
    {
        super(pathname);
        setType(TYPE_RECENT);
    }
    
    @Override
    public File[] listFiles()
    {
        return listFilesImpl(null, null);
    }

    @Override
    public File[] listFiles(FileFilter filter)
    {
        return listFilesImpl(filter, null);
    }

    @Override
    public File[] listFiles(FilenameFilter filter)
    {
        return listFilesImpl(null, filter);
    }
    
    private File[] listFilesImpl(FileFilter filter, FilenameFilter filterName){
        try
        {
            if(fileCache != null){
                return fileCache;
            }
            Path startingDir = Paths.get(DIR.getAbsolutePath());
            Finder finder = new Finder(filter, filterName);
            Files.walkFileTree(startingDir, finder);
            fileCache = finder.getFiles();
            return fileCache;
        } catch (IOException ex)
        {
            Debug.printException(ex);
        }
        return null;
    }

    @Override
    public String getName()
    {
        return "Recent";
    }

    @Override
    public ImageIcon getIcon() {
        return OSUtil.getOsIcons().getRecentIcon();
    }

    public static class Finder extends SimpleFileVisitor<Path> {
        private HashMap<File, FakeDirectory> directoryList = new HashMap<>();
        private FileFilter filter;
        private FilenameFilter filterName;
        public Finder(){
            this(null, null);
        }
        
        public Finder(FileFilter filter, FilenameFilter filterName) {
            this.filter = filter;
            this.filterName = filterName;
        }

        // Invoke the pattern matching
        // method on each directory.
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            File directoryFile = dir.toFile();
            if(checkFilter(directoryFile) == false){
                directoryList.put(directoryFile, new FakeDirectory(directoryFile));
                return FileVisitResult.CONTINUE;
            }else{
                return FileVisitResult.SKIP_SUBTREE;
            }
        }
        
        private boolean checkFilter(File f){
            if((filter     != null && filter.accept(f) == false) || 
               (filterName != null && filterName.accept(f.getParentFile(), f.getName()) == false)){
                //the file should be filtered
                Debug.println("Filter: " + f.getAbsolutePath());
                return true;
            }
            return false;
        }
        
        private boolean isLastModified(Path dir){
            try
            {
                FileTime lastModifiedTime = Files.getLastModifiedTime(dir);
                long currentTimeMillis = System.currentTimeMillis();
                long lastWeek = currentTimeMillis - timespan;
                if(lastModifiedTime.toMillis() >= lastWeek){
                    return true;
                }
            } catch (IOException ex)
            {
                Debug.printException(ex);
            }
            return false;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
        {
            if(isLastModified(file) == true && checkFilter(file.toFile()) == false){
                File parent = file.getParent().toFile();
                if(directoryList.containsKey(parent)){
                    FakeDirectory get = directoryList.get(parent);
                    // only handle files here
                    if(Files.isDirectory(file) == false){
                        get.addChild(file.toFile());
                    }else{
                        Debug.println("Skip:" + file.toString() + " (because its a directory)");
                    }
                }
            }
            return FileVisitResult.CONTINUE;
        }
        
        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            Debug.printException(exc);
            return FileVisitResult.CONTINUE;
        }
        
        public File[] getFiles(){
            HashSet<File> filtered = new HashSet<>();
            for(FakeDirectory f : directoryList.values()){
                if(shouldAdd(f)){
                    filtered.add(getHighest(f));
                }else{
                    Debug.println("Skip:" + f.toString());
                }
            }
            return filtered.toArray(new File[filtered.size()]);
        }
        
        private File getHighest(FakeDirectory f){
            File parentFile = f.getParentFile();
            if(directoryList.containsKey(parentFile)){
                FakeDirectory parentFakeDir = directoryList.get(parentFile);
                if(shouldAdd(f)){
                    parentFakeDir.addChild(f);
                }else{
                    Debug.println("Skip:" + f.toString());
                }
                return getHighest(parentFakeDir);
            }
            return f; 
        }
        
        private boolean shouldAdd(FakeDirectory f){
            return (f.isDirectory() == false || f.getChildCount() > 0);
        }
    }
    
    
    public static class FakeDirectory extends File{
        private HashSet<File> childs;
        private File origFile;

        public FakeDirectory(File realFile) {
            super(realFile.getAbsolutePath());
            childs = new HashSet<>();
            this.origFile = realFile;
        }

        @Override
        public File[] listFiles() {
            return childs.toArray(new File[childs.size()]);
        }

        @Override
        public File[] listFiles(FileFilter filter) {
            ArrayList<File> filtered = new ArrayList<>();
            for(File f : childs){
                if(filter == null || filter.accept(f)){
                    filtered.add(f);
                }
            }
            return filtered.toArray(new File[filtered.size()]);
        }

        @Override
        public File[] listFiles(FilenameFilter filter) {
            ArrayList<File> filtered = new ArrayList<>();
            for(File f : childs){
                if(filter == null || filter.accept(f.getParentFile(), f.getName())){
                    filtered.add(f);
                }
            }
            return filtered.toArray(new File[filtered.size()]);
        }
        
        public boolean addChild(File f){
            return this.childs.add(f);
        }
        
        public boolean removeChild(File f){
            return this.childs.remove(f);
        }
        
        public int getChildCount(){
            return this.childs.size();
        }

        public File getRealFile() {
            return origFile;
        }
    }


}
