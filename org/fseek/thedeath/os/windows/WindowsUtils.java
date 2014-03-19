package org.fseek.thedeath.os.windows;

import java.lang.reflect.InvocationTargetException;

public class WindowsUtils
{
    private static final String DESKTOP_FOLDER_CMD = "Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders";

    public static String getCurrentUserPath(String key)
    {
        try {
          return WinRegistry.readString(WinRegistry.HKEY_CURRENT_USER, DESKTOP_FOLDER_CMD, key);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
          return null;
        }
    }
}