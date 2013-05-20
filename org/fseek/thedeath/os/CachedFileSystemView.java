package org.fseek.thedeath.os;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;
import org.fseek.thedeath.os.util.Debug;
import org.fseek.thedeath.os.util.OSUtil;
import sun.awt.shell.ShellFolder;


/**
 *
 * @author Thedeath<www.fseek.org>
 * Own implementation with icon caching of the FileSystemView
 */


public abstract class CachedFileSystemView extends FileSystemView
{
    private static CachedFileSystemView windowsFileSystemView = null;
    private static CachedFileSystemView unixFileSystemView = null;
    private static CachedFileSystemView genericFileSystemView = null;
    
    private ImageIcon folderIcon;
    private ImageIcon fileIcon;
    private HashMap<Integer, ImageIcon> cache = new HashMap<>();
 
    public static CachedFileSystemView getFileSystemView() {
        if(File.separatorChar == '\\') {
            if(windowsFileSystemView == null) {
                windowsFileSystemView = new WindowsCachedFileSystemView();
            }
            return windowsFileSystemView;
        }

        if(File.separatorChar == '/') {
            if(unixFileSystemView == null) {
                unixFileSystemView = new UnixCachedFileSystemView();
            }
            return unixFileSystemView;
        }

        if(genericFileSystemView == null) {
            genericFileSystemView = new GenericCachedFileSystemView();
        }
        return genericFileSystemView;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Custom icon methods">
    public ImageIcon getSystemIcon(File f, boolean large){
        return getSystemIcon(f, large, false);
    }
    
    // gets the system icon of a file, I made a custom implementation of the FileSystemView.getSystemIcon(File f) method because I also need the large icons
    public ImageIcon getSystemIcon(File f, boolean large, boolean force)
    {
        if (f == null)
        {
            return null;
        }
        ShellFolder sf;

        try
        {
            sf = this.getShellFolder(f);
        } catch (FileNotFoundException e)
        {
            return OSUtil.getOsIcons().getDocumentIcon();
        }
        BufferedImage img = (BufferedImage)sf.getIcon(large);
        ImageIcon icon;
        if (img != null)
        {
            int[] data = ((DataBufferInt) img.getData().getDataBuffer()).getData();
            int hash = Arrays.hashCode(data);
            
            if(cache.containsKey(hash) && force == false){
                icon = cache.get(hash);
            }else{
                icon = new ImageIcon(img, sf.getFolderType());
                cache.put(hash, icon);
            } 
        }
        else
        {
            icon = getSimpleIcon(f);
        }
        if(icon == null){
            icon = OSUtil.getOsIcons().getDocumentIcon();
        }
        return icon;
    }

    /**
     * Name of a file, directory, or folder as it would be displayed in
     * a system file browser. Example from Windows: the "M:\" directory
     * displays as "CD-ROM (M:)"
     *
     * The default implementation gets information from the ShellFolder class.
     * 
     * If the display name length should be 0 this method returns the file name
     * or if the file name length is also 0 the absolute path is returned.
     * 
     * @param f a <code>File</code> object
     * @return the file name as it would be displayed by a native file chooser
     * @see JFileChooser#getName
     * @since 1.4
     */
    @Override
    public String getSystemDisplayName(File f)
    {
        String systemDisplayName = super.getSystemDisplayName(f);
        if(systemDisplayName.length() <= 0){
            systemDisplayName = f.getName();
            if(systemDisplayName.length() <= 0){
                systemDisplayName = f.getAbsolutePath();
            }
        }
        return systemDisplayName;
    }

    /*
     * Asks the UIManager for a directory/file if the UIManager
     * don't have an icon the default icon is returned.
     */
    public ImageIcon getSimpleIcon(File f){
        boolean isDir = f.isDirectory();
        if(isDir && folderIcon != null){
            return folderIcon;
        }else if(isDir == false && fileIcon != null){
            return fileIcon;
        }
        ImageIcon icon = (ImageIcon)UIManager.getIcon(isDir ? "FileView.directoryIcon" : "FileView.fileIcon");
        if(icon == null){
           if(isDir){
               icon = getFolderIcon();
           }else{
               icon = getFileIcon();
           }
        }
        if(isDir){
            folderIcon = icon;
        }else{
            fileIcon = icon;
        }
        return icon;
    }
    
    /*
     * Default File icon of the org.fseek.thedeath.os.icons.* package
     */
    public ImageIcon getFileIcon()
    {
        return OSUtil.getOsIcons().getFileIcon();
    }
    
    /*
     * Default Folder icon of the org.fseek.thedeath.os.icons.* package
     */
    public ImageIcon getFolderIcon()
    {
        return OSUtil.getOsIcons().getDirectoryIcon();
    }
    
    //checks if a file is on the same partition this is needed to check if the file should be copied and deleted or just just renamed
    public static boolean isSameDrive(File d, File f)
    {
        String dPath = d.getAbsolutePath();
        String fPath = f.getAbsolutePath();
        int index = dPath.indexOf(File.separator);
        int index1 = fPath.indexOf(File.separator);
        dPath = dPath.substring(0, index + File.separator.length());
        fPath = fPath.substring(0, index1 + File.separator.length());
        if(dPath.equals(fPath))
        {
            return true;
        }
        return false;
    }
    
    /**
     * Throws {@code FileNotFoundException} if file not found or current thread was interrupted
     */
    ShellFolder getShellFolder(File f) throws FileNotFoundException {
        if (!(f instanceof ShellFolder) && !(f instanceof FileSystemRoot) && isFileSystemRoot(f)) {
            f = createFileSystemRoot(f);
        }

        try {
            return ShellFolder.getShellFolder(f);
        } catch (InternalError e) {
            Debug.printError("CachedFileSystemView.getShellFolder: f="+f);
            Debug.printException(e);
            return null;
        }
    }
    
    /**
     * Creates a new <code>File</code> object for <code>f</code> with correct
     * behavior for a file system root directory.
     *
     * @param f a <code>File</code> object representing a file system root
     *          directory, for example "/" on Unix or "C:\" on Windows.
     * @return a new <code>File</code> object
     * @since 1.4
     */
    @Override
    protected File createFileSystemRoot(File f) {
        return new FileSystemRoot(f);
    }
        
    static class FileSystemRoot extends File {
        public FileSystemRoot(File f) {
            super(f,"");
        }

        public FileSystemRoot(String s) {
            super(s);
        }

        @Override
        public boolean isDirectory() {
            return true;
        }

        @Override
        public String getName() {
            return getPath();
        }
    }
    
    

}
/**
 * FileSystemView that handles some specific unix-isms.
 */
class UnixCachedFileSystemView extends CachedFileSystemView {

    private static final String newFolderString =
            UIManager.getString("FileChooser.other.newFolder");
    private static final String newFolderNextString  =
            UIManager.getString("FileChooser.other.newFolder.subsequent");

    /**
     * Creates a new folder with a default folder name.
     */
    @Override
    public File createNewFolder(File containingDir) throws IOException {
        if(containingDir == null) {
            throw new IOException("Containing directory is null:");
        }
        File newFolder;
        // Unix - using OpenWindows' default folder name. Can't find one for Motif/CDE.
        newFolder = createFileObject(containingDir, newFolderString);
        int i = 1;
        while (newFolder.exists() && i < 100) {
            newFolder = createFileObject(containingDir, MessageFormat.format(
                    newFolderNextString, new Integer(i)));
            i++;
        }

        if(newFolder.exists()) {
            throw new IOException("Directory already exists:" + newFolder.getAbsolutePath());
        } else {
            newFolder.mkdirs();
        }

        return newFolder;
    }

    @Override
    public boolean isFileSystemRoot(File dir) {
        return dir != null && dir.getAbsolutePath().equals("/");
    }

    @Override
    public boolean isDrive(File dir) {
        return isFloppyDrive(dir);
    }

    @Override
    public boolean isFloppyDrive(File dir) {
        // Could be looking at the path for Solaris, but wouldn't be reliable.
        // For example:
        // return (dir != null && dir.getAbsolutePath().toLowerCase().startsWith("/floppy"));
        return false;
    }

    @Override
    public boolean isComputerNode(File dir) {
        if (dir != null) {
            String parent = dir.getParent();
            if (parent != null && parent.equals("/net")) {
                return true;
            }
        }
        return false;
    }
}


/**
 * FileSystemView that handles some specific windows concepts.
 */
class WindowsCachedFileSystemView extends CachedFileSystemView {

    private static final String newFolderString =
            UIManager.getString("FileChooser.win32.newFolder");
    private static final String newFolderNextString  =
            UIManager.getString("FileChooser.win32.newFolder.subsequent");

    @Override
    public Boolean isTraversable(File f) {
        return Boolean.valueOf(isFileSystemRoot(f) || isComputerNode(f) || f.isDirectory());
    }

    @Override
    public File getChild(File parent, String fileName) {
        if (fileName.startsWith("\\")
            && !fileName.startsWith("\\\\")
            && isFileSystem(parent)) {

            //Path is relative to the root of parent's drive
            String path = parent.getAbsolutePath();
            if (path.length() >= 2
                && path.charAt(1) == ':'
                && Character.isLetter(path.charAt(0))) {

                return createFileObject(path.substring(0, 2) + fileName);
            }
        }
        return super.getChild(parent, fileName);
    }

    /**
     * Type description for a file, directory, or folder as it would be displayed in
     * a system file browser. Example from Windows: the "Desktop" folder
     * is desribed as "Desktop".
     *
     * The Windows implementation gets information from the ShellFolder class.
     */
    @Override
    public String getSystemTypeDescription(File f) {
        if (f == null) {
            return null;
        }

        try {
            return getShellFolder(f).getFolderType();
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * @return the Desktop folder.
     */
    @Override
    public File getHomeDirectory() {
        return getRoots()[0];
    }

    /**
     * Creates a new folder with a default folder name.
     */
    @Override
    public File createNewFolder(File containingDir) throws IOException {
        if(containingDir == null) {
            throw new IOException("Containing directory is null:");
        }
        // Using NT's default folder name
        File newFolder = createFileObject(containingDir, newFolderString);
        int i = 2;
        while (newFolder.exists() && i < 100) {
            newFolder = createFileObject(containingDir, MessageFormat.format(
                newFolderNextString, new Integer(i)));
            i++;
        }

        if(newFolder.exists()) {
            throw new IOException("Directory already exists:" + newFolder.getAbsolutePath());
        } else {
            newFolder.mkdirs();
        }

        return newFolder;
    }

    @Override
    public boolean isDrive(File dir) {
        return isFileSystemRoot(dir);
    }

    @Override
    public boolean isFloppyDrive(final File dir) {
        String path = AccessController.doPrivileged(new PrivilegedAction<String>() {
            @Override
            public String run() {
                return dir.getAbsolutePath();
            }
        });

        return path != null && (path.equals("A:\\") || path.equals("B:\\"));
    }

    /**
     * Returns a File object constructed from the given path string.
     */
    @Override
    public File createFileObject(String path) {
        // Check for missing backslash after drive letter such as "C:" or "C:filename"
        if (path.length() >= 2 && path.charAt(1) == ':' && Character.isLetter(path.charAt(0))) {
            if (path.length() == 2) {
                path += "\\";
            } else if (path.charAt(2) != '\\') {
                path = path.substring(0, 2) + "\\" + path.substring(2);
            }
        }
        return super.createFileObject(path);
    }

    @Override
    protected File createFileSystemRoot(File f) {
        // Problem: Removable drives on Windows return false on f.exists()
        // Workaround: Override exists() to always return true.
        return new CachedFileSystemView.FileSystemRoot(f) {
            @Override
            public boolean exists() {
                return true;
            }
        };
    }

}

/**
 * Fallthrough FileSystemView in case we can't determine the OS.
 */
class GenericCachedFileSystemView extends CachedFileSystemView {

    private static final String newFolderString =
            UIManager.getString("FileChooser.other.newFolder");

    /**
     * Creates a new folder with a default folder name.
     */
    @Override
    public File createNewFolder(File containingDir) throws IOException {
        if(containingDir == null) {
            throw new IOException("Containing directory is null:");
        }
        // Using NT's default folder name
        File newFolder = createFileObject(containingDir, newFolderString);

        if(newFolder.exists()) {
            throw new IOException("Directory already exists:" + newFolder.getAbsolutePath());
        } else {
            newFolder.mkdirs();
        }

        return newFolder;
    }
}