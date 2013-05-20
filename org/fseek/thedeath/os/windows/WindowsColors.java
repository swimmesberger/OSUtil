package org.fseek.thedeath.os.windows;

import java.awt.Color;
import org.fseek.thedeath.os.interfaces.IOSColors;

/**
 *
 * @author Thedeath<www.fseek.org>
 */
public class WindowsColors implements IOSColors
{
    @Override
    public Color getTreePanelColor()
    {
        return new Color(255,255,255);
    }

    @Override
    public Color getTreeFontColor()
    {
        return new Color(0,0,0);
    }
    
    @Override
    public boolean isTreeFontToUpperCase(){
        return false;
    }
}
