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

import java.util.Locale;

/**
 *
 * @author Simon Wimmesberger
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
