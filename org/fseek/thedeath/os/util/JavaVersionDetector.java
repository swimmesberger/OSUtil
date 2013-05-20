package org.fseek.thedeath.os.util;

import java.util.Locale;

/**
 *
 * @author Thedeath<www.fseek.org>
 */
public class JavaVersionDetector
{

    public static final byte JAVA_OTHER = -1;
    public static final byte JAVA_7 = 7;
    public static final byte JAVA_6 = 6;
    public static final byte JAVA_5 = 5;
    private static final byte JAVA_ID;

    static
    {
        String version = System.getProperty("java.version").toLowerCase(Locale.getDefault());
        if (version.startsWith("1.7"))
        {
            JAVA_ID = JAVA_7;
        } else if (version.startsWith("1.6"))
        {
            JAVA_ID = JAVA_6;
        } else if (version.startsWith("1.5"))
        {
            JAVA_ID = JAVA_5;
        } else
        {
            JAVA_ID = JAVA_OTHER;
        }
    }

    public static boolean isJava7()
    {
        return JAVA_ID == JAVA_7;
    }

    public static boolean isJava6()
    {
        return JAVA_ID == JAVA_6;
    }

    public static boolean isJava5()
    {
        return JAVA_ID == JAVA_5;
    }

    public static boolean isOther()
    {
        return JAVA_ID == JAVA_OTHER;
    }
}
