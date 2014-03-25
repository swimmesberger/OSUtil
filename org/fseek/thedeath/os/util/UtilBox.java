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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.AbstractButton;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class UtilBox {

    protected static volatile HashMap<ImageIcon, ImageIcon> scaleCache;
    private static volatile File mainPath;

    /**
     * Convenience method that returns a scaled instance of the provided
     * {@code BufferedImage}.
     *
     * @param img the original image to be scaled
     * @param targetWidth the desired width of the scaled instance, in pixels
     * @param targetHeight the desired height of the scaled instance, in pixels
     * @param hint one of the rendering hints that corresponds to
     * {@code RenderingHints.KEY_INTERPOLATION} (e.g.
     * {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
     * {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
     * {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @param higherQuality if true, this method will use a multi-step scaling
     * technique that provides higher quality than the usual one-step technique
     * (only useful in downscaling cases, where {@code targetWidth} or
     * {@code targetHeight} is smaller than the original dimensions, and
     * generally only when the {@code BILINEAR} hint is specified)
     * @return a scaled version of the original {@code BufferedImage}
     */
    public static BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint, boolean higherQuality) {
        int type = (img.getTransparency() == Transparency.OPAQUE)
                ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = (BufferedImage) img;
        int w, h;
        if (higherQuality) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            if (hint == null) {
                hint = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
            }
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }

    public static BufferedImage imageToBufferedImage(Image img) {
        int width = img.getWidth(null); // es muss keinen ImageObserver geben
        int height = img.getHeight(null);
        BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufImg.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return bufImg;
    }

    // return true if both image files are equal else return false//**
    public static boolean compareImage(BufferedImage biA, BufferedImage biB) {
        // take buffer data from botm image files //
        DataBuffer dbA = biA.getData().getDataBuffer();
        int sizeA = dbA.getSize();
        DataBuffer dbB = biB.getData().getDataBuffer();
        int sizeB = dbB.getSize();
        // compare data-buffer objects //
        if (sizeA == sizeB) {
            for (int i = 0; i < sizeA; i++) {
                if (dbA.getElem(i) != dbB.getElem(i)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static ImageIcon rescaleIconIfNeeded(ImageIcon systemIcon, int height, int width) {
        if (scaleCache == null) {
            scaleCache = new HashMap<>();
        }
        if (scaleCache.containsKey(systemIcon)) {
            ImageIcon get = scaleCache.get(systemIcon);
            if (get.getIconHeight() == height && get.getIconWidth() == width) {
                return get;
            }
        }
        BufferedImage scaledInstance = null;
        if (systemIcon.getIconHeight() != height || systemIcon.getIconWidth() != width) {
            BufferedImage img = UtilBox.imageToBufferedImage(systemIcon.getImage());
            scaledInstance = UtilBox.getScaledInstance(img, width, height, null, false);
        }
        if (scaledInstance == null) {
            return systemIcon;
        }
        ImageIcon scaledIcon = new ImageIcon(scaledInstance);
        scaleCache.put(systemIcon, scaledIcon);
        return scaledIcon;
    }

    public static String fileSizeToString(long size) {
        String symbol = "B";
        double length = (Long) size;
        int round = 0;
        while (length > 1024) {
            switch (round) {
                case 0:
                    symbol = "KB";
                    break;
                case 1:
                    symbol = "MB";
                    break;
                case 2:
                    symbol = "GB";
                    break;
                case 3:
                    symbol = "TB";
                    break;
                default:
                    symbol = "B";
                    break;
            }
            length = length / 1024;
            round++;
        }
        return (int) length + " " + symbol;
    }

    public static String fileDateToFormatString(long d) {
        SimpleDateFormat form = new SimpleDateFormat();
        form.applyPattern("dd.MM.yyy HH:mm");
        String format = form.format(new Date(d));
        return format;
    }

    public static File getMainPath() {
        if (mainPath != null) {
            return mainPath;
        }
        String path = UtilBox.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        while (path.contains("%20")) {
            path = path.replace("%20", " ");
        }
        File mainFileT = new File(path);
        try {
            String absolutePath = mainFileT.getCanonicalPath();
            if (absolutePath.contains(".jar")) {
                int index = absolutePath.lastIndexOf(File.separator);
                absolutePath = absolutePath.substring(0, index);
            }
            mainPath = new File(absolutePath);
        } catch (IOException ex) {
            //something really strange happened
        }
        return mainPath;
    }

    public static ImageIcon iconToImageIcon(Icon ui) {
        if (ui instanceof ImageIcon) {
            return (ImageIcon) ui;
        }
        BufferedImage img = new BufferedImage(ui.getIconWidth(), ui.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D createGraphics = img.createGraphics();
        try {
            ui.paintIcon(new JComponent() {
            }, createGraphics, 0, 0);
        } catch (ClassCastException e) {
            ui.paintIcon(createStandIn(e), createGraphics, 0, 0);
        }

        return new ImageIcon(img);
    }

    /**
     * @param clazz
     * @throws IllegalAccessException
     */
    private static JComponent getSubstitute(Class<?> clazz) throws IllegalAccessException {
        JComponent standInComponent;
        try {
            standInComponent = (JComponent) clazz.newInstance();
        } catch (InstantiationException e) {
            standInComponent = new AbstractButton() {

            };
            ((AbstractButton) standInComponent).setModel(new DefaultButtonModel());
        }
        return standInComponent;
    }

    /**
     * @param e
     */
    private static JComponent createStandIn(ClassCastException e) {
        try {
            Class<?> clazz = getClass(e);
            JComponent standInComponent = getSubstitute(clazz);
            return standInComponent;
        } catch (ClassNotFoundException | IllegalAccessException e1) {
            // something went wrong - fallback to this paintin
            Debug.printException(e1);
        }
        return null;
    }

    private static Class<?> getClass(ClassCastException e) throws ClassNotFoundException {
        String className = e.getMessage();
        className = className.substring(className.lastIndexOf(" ") + 1);
        return Class.forName(className);
    }

    /**
     * Time (milliseconds) for waiting for a file to grow (needed for caching
     * and reading dword, binary, multi and expand values)
     */
    public static int WAIT_FOR_FILE = 250;

    /**
     * ********************************************************************************************************************************
     * Method looks if the filesize becomes bigger after waiting some ms (which
     * can be defined at WAIT_FOR_FILE)
     *
     * @param file File
     * *******************************************************************************************************************************
     */
    public static void _waitForFile(File file) {
        try {
            long size = file.length();
            Thread.sleep(WAIT_FOR_FILE);
            if (size != file.length()) {
                _waitForFile(file);
            }
        } catch (Exception ex) {
            // something went wrong - fallback to this paintin
            Debug.printException(ex);
        }
    }
}
