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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.fseek.thedeath.os.util.Debug;
import org.fseek.thedeath.os.util.UtilBox;

public class RegeditHandler implements INativeRegistryHandler {

    /**
     * Needed to replace the entries in exported tmp registry file
     */
    private static final String NULL_STRING = new String(new char[]{0});
    /**
     * Standard text for inserting in the registry - used for import dword,
     * binary, multi and expand
     */
    private static final String INIT_WINDOWS_STRING = "Windows Registry Editor Version 5.00";

    public RegeditHandler() {
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
     * *****************************************************************************************************************************
     */
    @Override
    public boolean saveAnyValue(String path, String valueName, String type, String data) throws RegistryErrorException {
        try {
            File f = File.createTempFile("regorexp", ".jta"); //creates tmp File for storing the registry key
            //now writing the file for registry import
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
            bw.write(INIT_WINDOWS_STRING);
            bw.newLine();
            bw.newLine();
            bw.write("[");
            bw.write(path);
            bw.write("]");
            bw.newLine();
            bw.write("\"");
            bw.write(valueName);
            bw.write("\"=");
            bw.write(type);
            bw.write(data);
            bw.newLine();
            bw.close();
            //ATTENTION!! THESE COULD BE A DEADLOCK BECAUSE I WAITFOR THE END OF PROCESS HERE
            Runtime.getRuntime().exec("regedit /s /i " + f.getAbsolutePath()).waitFor(); //<-- Waiting for END of Process
            if (!f.delete()) {
                f.deleteOnExit(); //mark it, for delete on exit
            }
        } catch (Exception ex) {
            Debug.printException(ex);
            //      ex.printStackTrace(System.out);
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
     * @param appendType boolean if the method should append the type - not
     * needed with regedit.exe method, because the type is always added
     * @return String null if the valuename is not found or the path could not
     * be exported - otherwhise the data from the registry
     * @throws RegistryErrorException
     * *******************************************************************************************************************************
     */
    @Override
    public String extractAnyValue(String path, String valueName, boolean appendType) throws RegistryErrorException {
        StringBuilder strRet = new StringBuilder(); //stringbuffer for appending, if an entry has multiplie lines
        File f = null;
        BufferedReader br = null;
        try {
            f = File.createTempFile("regorexp", ".jta"); //creates tmp File for storing the registry key
            //ATTENTION!! THESE COULD BE A DEADLOCK BECAUSE I WAITFOR THE END OF PROCESS HERE
            Runtime.getRuntime().exec("regedit /e " + f.getAbsolutePath() + " \"" + path + "\"").waitFor(); //<-- WAITING FOR END OF PROCESS
            UtilBox.waitForFile(f); //wait until the file size is not increasing anymore
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String line = "";
            boolean lineFound = false;
            boolean keyFound = false;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll(NULL_STRING, "");
                if (line.length() > 0) {
                    if (keyFound || (line.startsWith("[") && line.endsWith("]"))) {
                        if (line.startsWith("[") && line.endsWith("]")) {
                            if (keyFound) {
                                break;
                            } else {
                                if (line.equals("[" + path + "]")) {
                                    keyFound = true;
                                }
                            }
                        } else if (keyFound && (lineFound || line.startsWith("\"" + valueName) && line.indexOf("=") != -1)) {
                            if (lineFound) {
                                if (line.length() > 0) {
                                    if (line.indexOf("=") != -1) {
                                        break;
                                    }
                                    //and append the line, if its for the same item
                                    strRet.append(line.trim().replaceAll("\\\\", "")); //eliminate every \
                                    if (!line.endsWith("\\")) {
                                        break;
                                    }
                                }
                            } else {
                                line = line.substring(line.indexOf("=") + 1);
                                strRet.append(line.replaceAll("\\\\", "")); //eliminate every \ also if there is none
                                lineFound = true;
                                if (line.indexOf("\\") == -1) {
                                    break; //abort if no \ is found
                                }
                            }
                        }
                    }
                }
            }
            br.close(); //close reader, so that you can delete the file
            if (!f.delete()) {
                f.deleteOnExit(); //mark it, for delete on exit
            }
        } catch (Exception ex) {
            Debug.printException(ex);
            //      ex.printStackTrace(System.out);
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
        //if the buffer length is zero, return null
        return strRet.length() == 0 ? null : strRet.toString();
    }
}
