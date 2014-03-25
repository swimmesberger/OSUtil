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
package org.fseek.thedeath.os.windows.registry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import org.fseek.thedeath.os.util.Debug;
import org.fseek.thedeath.os.util.UtilBox;

public class RegHandler implements INativeRegistryHandler {

    public RegHandler() {
    }

    /**
     * ******************************************************************************************************************************
     * Method saves any variable to the registry via the regedit.exe and runtime
     * - because its java 1.4 compatbile (otherwhise it would be the
     * processbuilder)
     *
     * @param path String the registy path (without [])
     * @param valueName String the valuename to set
     * @param type String the type (BINARY, DWORD, MULTI, EXPAND)
     * @param data String the data which should be stored in the registry (it
     * must be converted into the right format for the given type)
     * @return boolean returns always true (otherwhise exceptio is thrown) -
     * maybe better use in future
     * @throws RegistryErrorException
     ******************************************************************************************************************************
     */
    @Override
    public boolean saveAnyValue(String path, String valueName, String type, String data) throws RegistryErrorException {
        try {
            if (type.equals(WinRegistry.BINARY_KEY_IDENT)) {
                type = "REG_BINARY";
            } else if (type.equals(WinRegistry.DWORD_KEY_IDENT)) {
                type = "REG_DWORD";
            } else if (type.equals(WinRegistry.MULTI_KEY_IDENT)) {
                type = "REG_MULTI_SZ";
            } else if (type.equals(WinRegistry.EXPAND_KEY_IDENT)) {
                type = "REG_EXPAND_SZ";
            }
            Runtime.getRuntime().exec("reg add \"" + path + "\" /v \"" + valueName + "\" /t " + type + " /d \"" + data + "\" /f");
        } catch (Exception ex) {
            //        ex.printStackTrace(System.out);
            Debug.printException(ex);
            throw RegistryErrorException.getException(ex);
        }
        return true;
    }

    /**
     * ********************************************************************************************************************************
     * Method extracts any variable from the registry via the regedit.exe and
     * runtime - because its java 1.4 compatbile (otherwhise it would be the
     * processbuilder)
     *
     * @param path String the registry path to the parent key
     * @param valueName String the valuename which should be read from the
     * registry key
     * @param appendType boolean add the type to the return string
     * @return String null if the valuename is not found or the path could not
     * be exported - otherwhise the data from the registry
     * @throws RegistryErrorException
     ********************************************************************************************************************************
     */
    @Override
    public String extractAnyValue(String path, String valueName, boolean appendType) throws RegistryErrorException {
        StringBuilder strRet = new StringBuilder(); //stringbuffer for appending, if an entry has multiplie lines
        BufferedReader br = null;
        File f = null;
        try {
            f = File.createTempFile("regorexp", ".jta"); //creates tmp File for storing the registry key
            //ATTENTION!! THESE COULD BE A DEADLOCK BECAUSE I WAITFOR THE END OF PROCESS HERE
            Runtime.getRuntime().exec("cmd /c \"reg query \"" + path + "\" /v \"" + valueName + "\" > " + f.getAbsolutePath() + " 2>&1\"").waitFor(); //<-- WAITING FOR END OF PROCESS
            UtilBox._waitForFile(f); //wait until the file size is not increasing anymore
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String line = "";
            boolean lineFound = false;
            while ((line = br.readLine()) != null) {
                if (line.equals(path)) {
                    lineFound = true;
                } else if (lineFound && line.trim().length() > 0) {
                    StringTokenizer st = new StringTokenizer(line, " \t");
                    String[] items = {"", "", ""};
                    int i = 0;
                    while (st.hasMoreTokens()) {
                        items[i] += st.nextToken() + " ";
                        if (i < 2) {
                            i++;
                        }
                    }
                    for (int j = 0; j < items.length; j++) {
                        items[j] = items[j].trim();
                    }
                    if (items[0].equals(valueName)) {
                        if (appendType) {
                            strRet.append(items[1]);
                            strRet.append(" ");
                        }
                        //[0] = type
                        //[1] = entry
                        /*              if (items[0].equals("REG_MULTI_SZ"))
                         strRet.append(MULTI_KEY); //add this for older version
                         else if (items[0].equals("REG_EXPAND_SZ"))
                         strRet.append(EXPAND_KEY);
                         else if (items[0].equals("REG_DWORD"))
                         strRet.append(DWORD_KEY);
                         else if (items[0].equals("REG_BINARY"))
                         strRet.append(BINARY_KEY);*/
                        strRet.append(items[2]);
                        if (items[1].equals("REG_MULTI_SZ") && strRet.toString().endsWith("\\0\\0")) {
                            strRet.setLength(strRet.length() - 4);
                        }
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            //        ex.printStackTrace(System.out);
            Debug.printException(ex);
            throw RegistryErrorException.getException(ex);
        } finally {
            try {
                if (br != null) {
                    br.close(); //close reader, so that you can delete the file
                }
            } catch (Exception ex) {
            }
            if (f != null) {
                if (!f.delete()) {
                    f.deleteOnExit(); //mark it, for delete on exit
                }
            }
        }
        return strRet.toString();
    }
}
