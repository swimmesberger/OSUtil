/*
 * Copyright (C) 2013 Thedeath<www.fseek.org>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.fseek.thedeath.os.icons;

import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Thedeath<www.fseek.org>
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
