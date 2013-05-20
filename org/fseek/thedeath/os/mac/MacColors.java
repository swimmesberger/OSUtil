/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fseek.thedeath.os.mac;

import java.awt.Color;
import org.fseek.thedeath.os.interfaces.IOSColors;

/**
 *
 * @author Thedeath<www.fseek.org>
 */
public class MacColors implements IOSColors
{
    @Override
    public Color getTreePanelColor()
    {
        return new Color(222,228,234);
    }

    @Override
    public Color getTreeFontColor()
    {
        return new Color(115,130,146);
    }

    @Override
    public boolean isTreeFontToUpperCase()
    {
        return true;
    }
    
}
