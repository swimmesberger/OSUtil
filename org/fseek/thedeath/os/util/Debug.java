package org.fseek.thedeath.os.util;

import java.io.PrintStream;

/**
 *
 * @author Thedeath<www.fseek.org>
 */


public class Debug
{
    private static boolean DEBUG = true;
    private static boolean SHOW_ERRORS = true;
    
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
