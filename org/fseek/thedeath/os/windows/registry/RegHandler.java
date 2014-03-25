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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.fseek.thedeath.os.util.Debug;

public class RegHandler implements INativeRegistryHandler {
    private static final int TIMEOUT = 500;
    public RegHandler() {
    }

    /**
     * ******************************************************************************************************************************
     * Method saves any variable to the registry via the regedit.exe and runtime
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
            if (type.equals(WinRegistry.BINARY_KEY_IDENT)) {
                type = "REG_BINARY";
            } else if (type.equals(WinRegistry.DWORD_KEY_IDENT)) {
                type = "REG_DWORD";
            } else if (type.equals(WinRegistry.MULTI_KEY_IDENT)) {
                type = "REG_MULTI_SZ";
            } else if (type.equals(WinRegistry.EXPAND_KEY_IDENT)) {
                type = "REG_EXPAND_SZ";
            }
            //Runtime.getRuntime().exec("reg add \"" + path + "\" /v \"" + valueName + "\" /t " + type + " /d \"" + data + "\" /f");
            ProcessBuilder pb = new ProcessBuilder("reg", "add", path, "/v", valueName, "/t", type, "/d", data, "/f");
            Process process = pb.start();
            return process.waitFor() == 0;
        } catch (Exception ex) {
            //        ex.printStackTrace(System.out);
            Debug.printException(ex);
            throw RegistryErrorException.getException(ex);
        }
    }

    /**
     * ********************************************************************************************************************************
     * Method extracts any variable from the registry via the regedit.exe
     *
     * @param path String the registry path to the parent key
     * @param valueName String the valuename which should be read from the
     * registry key
     * @param appendType boolean add the type to the return string
     * @return String null if the valuename is not found or the path could not
     * be exported - otherwhise the data from the registry
     * @throws RegistryErrorException
     * *******************************************************************************************************************************
     */
    @Override
    public String extractAnyValue(String path, String valueName, boolean appendType) throws RegistryErrorException {
        try {
            StringBuilder strRet = new StringBuilder(); //stringbuffer for appending, if an entry has multiplie lines
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "reg", "query", path, "/v", valueName);
            Process process = pb.start();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))){
                Callable<String> readTask = new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return br.readLine();
                    }
                };
                Future<String> future = new FutureTask(readTask);
                boolean lineFound = false;
                while (true) {
                    String line = future.get(TIMEOUT, TimeUnit.MILLISECONDS);
                    if(line == null)break;
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
            }
            return strRet.toString();
        } catch (IOException ex) {
            Debug.printException(ex);
            throw RegistryErrorException.getException(ex);
        }
    }
}
