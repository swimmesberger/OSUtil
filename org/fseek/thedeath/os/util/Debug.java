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

import java.io.PrintStream;

/**
 *
 * @author Simon Wimmesberger
 */


public class Debug
{
    private static boolean DEBUG = false;
    private static boolean SHOW_ERRORS = false;
    
    private static PrintStream debugStream;
    private static PrintStream errorStream;
    
    static {
        debugStream = System.out;
        errorStream = System.err;
    }
    
    public static void println(String s){
        if(DEBUG){
            debugStream.println(s);
        }
    }
    
    public static void println(String[] s){
        if(DEBUG){
            for(String str : s){
                println(str);
            }
        }
    }
    
    public static void printError(String s){
        if(SHOW_ERRORS){
            errorStream.println(s);
        }
    }
    
    public static void printException(Exception ex){
        if(SHOW_ERRORS){
            ex.printStackTrace(errorStream);
        }
    }
    
    public static void printException(Error err){
        if(SHOW_ERRORS){
            err.printStackTrace(errorStream);
        }
    }

    public static void setDebugStream(PrintStream debugStream)
    {
        Debug.debugStream = debugStream;
    }

    public static void setErrorStream(PrintStream errorStream)
    {
        Debug.errorStream = errorStream;
    }
    
    public static boolean isOn(){
        return DEBUG;
    }

    public static boolean isErrors()
    {
        return SHOW_ERRORS;
    }

    public static void setDebug(boolean DEBUG)
    {
        Debug.DEBUG = DEBUG;
    }

    public static void setShowErrors(boolean SHOW_ERRORS)
    {
        Debug.SHOW_ERRORS = SHOW_ERRORS;
    }
}
