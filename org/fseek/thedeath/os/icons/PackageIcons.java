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
package org.fseek.thedeath.os.icons;

import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Simon Wimmesberger
 */
public class PackageIcons extends DefaultOSIcons
{
    protected String iconPackage;
    protected String iconPackageDefault;
    
    //the class of this file cached
    private Class thisClass;
    private Class defaultClass;

    /*
     * Only use this constructor if you have extended this class AND have a package called "icons" as direct child (which should contain the icons)
     */
    protected PackageIcons()
    {
        thisClass = this.getClass();
        defaultClass = PackageIcons.class;
        iconPackage = getIconPackage();
        iconPackageDefault = iconPackage;
    }
    
    /*
     * Pass classes where we find the icons in the "icons" subpackage
     */
    public PackageIcons(Class mainIcons, Class defaultIcons)
    {
        thisClass = mainIcons.getClass();
        defaultClass = defaultIcons;
        iconPackage = getIconPackage();
        iconPackageDefault = iconPackage;
    }
    
    public PackageIcons(Class mainIcons, Class defaultIcons, String subfolder, String subfolderDefault)
    {
        thisClass = mainIcons.getClass();
        defaultClass = defaultIcons;
        iconPackage = subfolder; 
        iconPackageDefault = subfolderDefault;
    }
    
    public PackageIcons(Class c, String subfolder, String subfolderDefault)
    {
        thisClass = c;
        defaultClass = c;
        iconPackage = subfolder; 
        iconPackageDefault = subfolderDefault;
    }
    
    public ImageIcon getIconByNameImpl(String name) throws IOException{
        String path = iconPackage + "/" + name;
        try (InputStream in = defaultClass.getResourceAsStream(path))
        {
            ImageIcon iconByStream = getIconByStream(in);
            return iconByStream;
        } catch (IOException ex)
        {
            // try to get the default icon
            try{
                return getDefaultIconByName(name);
            }catch(IOException exn){
                throw new IOException("Icon not found !");
            }
        }
    }
    
    public ImageIcon getDefaultIconByName(String name) throws IOException{
        String path = iconPackageDefault + "/" + name;
        ImageIcon iconByStream;
        try (InputStream in = defaultClass.getResourceAsStream(path))
        {
            iconByStream = getIconByStream(in);
        }
        return iconByStream;
    }
    
    private ImageIcon getIconByStream(InputStream in) throws IOException{
        if(in == null){
            throw new IOException();
        }
        return new ImageIcon(ImageIO.read(in));
    }
    
    private String getIconPackage(){
        return "icons";
    }

    @Override
    public ImageIcon getIconByName(String name) throws IOException
    {
        ImageIcon cacheIcon = getFromCache(name);
        if(cacheIcon == null){
            cacheIcon = getIconByNameImpl(name);
            addToCache(name, cacheIcon);
        }
        return cacheIcon;
    }
}
