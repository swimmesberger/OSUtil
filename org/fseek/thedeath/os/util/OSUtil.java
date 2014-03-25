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
package org.fseek.thedeath.os.util;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.fseek.thedeath.os.CachedFileSystemView;
import org.fseek.thedeath.os.OSAppearanceFactory;
import org.fseek.thedeath.os.OSIconFactory;
import org.fseek.thedeath.os.interfaces.IFileSystem;
import org.fseek.thedeath.os.interfaces.IOSAppearance;
import org.fseek.thedeath.os.interfaces.IOSIcons;
import org.fseek.thedeath.os.interfaces.IOSIconsAdapter;

/**
 *
 * @author Simon Wimmesberger
 */
public class OSUtil {

    private static IOSAppearance osColors;
    private static IOSIcons osIcons;

    static {
        osColors = OSAppearanceFactory.createOSColors();
        setOsIcons(OSIconFactory.getDefaultAdapter());
    }

    public static IOSAppearance getOSAppearance() {
        return osColors;
    }

    public static void setOsIcons(IOSIconsAdapter adapter) {
        osIcons = OSIconFactory.createOSIcons(adapter);
    }

    public static void setOsIcons(IOSIcons icons) {
        osIcons = icons;
    }

    public static void setIOSAppearance(IOSAppearance colors) {
        osColors = colors;
    }

    public static IOSIcons getOsIcons() {
        return osIcons;
    }
    
    public static CachedFileSystemView getFileSystemView(){
        return CachedFileSystemView.getFileSystemView();
    }
    
    public static IFileSystem getFileSystem(){
        return FileSystemUtil.getFileSystem();
    }

    public static void openURL(URL url) {
        if (!Desktop.isDesktopSupported()) {
            throw new UnsupportedOperationException("Desktop is not supported (fatal)");
        }

        Desktop desktop = Desktop.getDesktop();

        if (!desktop.isSupported(Desktop.Action.BROWSE)) {
            throw new UnsupportedOperationException("Desktop doesn't support the browse action (fatal)");
        }
        try {
            desktop.browse(url.toURI());
        } catch (URISyntaxException | IOException e) {
            Debug.printException(e);
        }
    }

    public static boolean canOpenFile() {
        if (!Desktop.isDesktopSupported()) {
            return false;
        }

        Desktop desktop = Desktop.getDesktop();

        return desktop.isSupported(Desktop.Action.OPEN);
    }

    public static void openFile(File file) {
        Debug.println("Opening file: " + file.getAbsolutePath());
        if ((OSDetector.isWindows()) && (file.isFile())) {
            int exitCode = runWindows(file);
            if (exitCode == 0) {
                //success
                return;
            } else {
                Debug.println("Opening file: " + file.getAbsolutePath() + " failed with windows method. (fall back to default action)");
            }
        }

        if (!Desktop.isDesktopSupported()) {
            throw new UnsupportedOperationException("Desktop is not supported (fatal)");
        }
        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.OPEN)) {
            throw new UnsupportedOperationException("Desktop doesn't support the OPEN action (fatal)");
        }
        try {
            URI uri = file.getCanonicalFile().toURI();
            desktop.open(new File(uri));
        } catch (IOException e) {
            Debug.printException(e);
        }
    }

    private static int runWindows(File file) {
        int exitCode = -1;
        try {
            ProcessBuilder ps = new ProcessBuilder("cmd.exe", "/c", file.getAbsolutePath());
            ps.redirectErrorStream(true);
            Process pr = ps.start();
            
            Worker worker = new Worker(pr);
            worker.start();
            try {
                worker.join(100);
                //if the program is still running after 100 milliseconds the start was successful
                if (worker.exit == null) {
                    exitCode = 0;
                } else {
                    exitCode = worker.exit;
                }
            } catch (InterruptedException ex) {
                worker.interrupt();
                Thread.currentThread().interrupt();
                throw ex;
            } finally {
                pr.destroy();
            }
        } catch (IOException | InterruptedException ex) {
            Debug.printException(ex);
        }
        return exitCode;
    }

    private static class Worker extends Thread {

        private final Process process;
        private Integer exit;

        private Worker(Process process) {
            this.process = process;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    Debug.println("Process cmd.exe: " + line);
                }
                exit = process.waitFor();
                Debug.println("Process cmd.exe exited with " + exit);
            } catch (InterruptedException | IOException ignore) {
                return;
            }
        }
    }
}
