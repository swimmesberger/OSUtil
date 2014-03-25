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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;
import org.fseek.thedeath.os.util.Debug;
import org.fseek.thedeath.os.windows.registry.INativeRegistryHandler;
import org.fseek.thedeath.os.windows.registry.RegHandler;
import org.fseek.thedeath.os.windows.registry.RegeditHandler;
import org.fseek.thedeath.os.windows.registry.RegistryErrorException;

@SuppressWarnings("all")
public class WinRegistry {

    public static final int HKEY_CLASSES_ROOT = 0x80000000;
    public static final int HKEY_CURRENT_USER = 0x80000001;
    public static final int HKEY_LOCAL_MACHINE = 0x80000002;
    
    private static final String _HKEY_CLASSES_ROOT = "HKEY_CLASSES_ROOT";
    private static final String _HKEY_CURRENT_USER = "HKEY_CURRENT_USER";
    private static final String _HKEY_LOCAL_MACHINE = "HKEY_LOCAL_MACHINE";

    public static final int REG_SUCCESS = 0;
    public static final int REG_NOTFOUND = 2;
    public static final int REG_ACCESSDENIED = 5;

    private static final int KEY_ALL_ACCESS = 0xf003f;
    private static final int KEY_READ = 0x20019;
    private static final Preferences userRoot = Preferences.userRoot();
    private static final Preferences systemRoot = Preferences.systemRoot();
    private static final Class<? extends Preferences> userClass = userRoot.getClass();
    private static Method regOpenKey = null;
    private static Method regCloseKey = null;
    private static Method regQueryValueEx = null;
    private static Method regEnumValue = null;
    private static Method regQueryInfoKey = null;
    private static Method regEnumKeyEx = null;
    private static Method regCreateKeyEx = null;
    private static Method regSetValueEx = null;
    private static Method regDeleteKey = null;
    private static Method regDeleteValue = null;

    /**
     * Every binary entry starts with this (when exported or for the import)
     */
    public static final String BINARY_KEY_IDENT = "hex:";

    /**
     * Every dword entry starts with this, also used for import
     */
    public static final String DWORD_KEY_IDENT = "dword:";

    /**
     * Every multi string entry starts with this, also used for import
     */
    public static final String MULTI_KEY_IDENT = "hex(7):";

    /**
     * Every expand string entry starts with this, also used for import
     */
    public static final String EXPAND_KEY_IDENT = "hex(2):";

    /**
     * Handler to reg.exe or regedit.exe to read, save and cache entries
     */
    private static INativeRegistryHandler nativeHandler = null;

    static {
        try {
            regOpenKey = userClass.getDeclaredMethod("WindowsRegOpenKey",
                    new Class[]{int.class, byte[].class, int.class});
            regOpenKey.setAccessible(true);
            regCloseKey = userClass.getDeclaredMethod("WindowsRegCloseKey",
                    new Class[]{int.class});
            regCloseKey.setAccessible(true);
            regQueryValueEx = userClass.getDeclaredMethod("WindowsRegQueryValueEx",
                    new Class[]{int.class, byte[].class});
            regQueryValueEx.setAccessible(true);
            regEnumValue = userClass.getDeclaredMethod("WindowsRegEnumValue",
                    new Class[]{int.class, int.class, int.class});
            regEnumValue.setAccessible(true);
            regQueryInfoKey = userClass.getDeclaredMethod("WindowsRegQueryInfoKey1",
                    new Class[]{int.class});
            regQueryInfoKey.setAccessible(true);
            regEnumKeyEx = userClass.getDeclaredMethod(
                    "WindowsRegEnumKeyEx", new Class[]{int.class, int.class,
                        int.class});
            regEnumKeyEx.setAccessible(true);
            regCreateKeyEx = userClass.getDeclaredMethod(
                    "WindowsRegCreateKeyEx", new Class[]{int.class,
                        byte[].class});
            regCreateKeyEx.setAccessible(true);
            regSetValueEx = userClass.getDeclaredMethod(
                    "WindowsRegSetValueEx", new Class[]{int.class,
                        byte[].class, byte[].class});
            regSetValueEx.setAccessible(true);
            regDeleteValue = userClass.getDeclaredMethod(
                    "WindowsRegDeleteValue", new Class[]{int.class,
                        byte[].class});
            regDeleteValue.setAccessible(true);
            regDeleteKey = userClass.getDeclaredMethod(
                    "WindowsRegDeleteKey", new Class[]{int.class,
                        byte[].class});
            regDeleteKey.setAccessible(true);
        } catch (NoSuchMethodException | SecurityException e) {
        }
        try {
            initNatvieRegistry();
        } catch (RegistryErrorException ex) {
            Debug.printException(ex);
        }
    }

    private WinRegistry() {
    }

    /**
     * Read a value from key and value name
     *
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key
     * @param valueName
     * @return the value
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static String readString(int hkey, String key, String valueName)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        return readString(getUnderlyingObject(hkey), hkey, key, valueName);
    }

    /**
     * Read value(s) and value name(s) form given key
     *
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key
     * @return the value name(s) plus the value(s)
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Map<String, String> readStringValues(int hkey, String key)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        return readStringValues(getUnderlyingObject(hkey), hkey, key);
    }

    /**
     * Read the value name(s) from a given key
     *
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key
     * @return the value name(s)
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static List<String> readStringSubKeys(int hkey, String key)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        return readStringSubKeys(getUnderlyingObject(hkey), hkey, key);
    }

    /**
     * Create a key
     *
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void createKey(int hkey, String key)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        int[] ret = createKey(getUnderlyingObject(hkey), hkey, key);
        regCloseKey.invoke(userRoot, new Object[]{new Integer(ret[0])});
        if (ret[1] != REG_SUCCESS) {
            throw new IllegalArgumentException("rc=" + ret[1] + "  key=" + key);
        }
    }

    /**
     * Write a value in a given key/value name
     *
     * @param hkey
     * @param key
     * @param valueName
     * @param value
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void writeStringValue(int hkey, String key, String valueName, String value)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        writeStringValue(getUnderlyingObject(hkey), hkey, key, valueName, value);
    }

    /**
     * Delete a given key
     *
     * @param hkey
     * @param key
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void deleteKey(int hkey, String key)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        int rc = deleteKey(getUnderlyingObject(hkey), hkey, key);
        if (rc != REG_SUCCESS) {
            throw new IllegalArgumentException("rc=" + rc + "  key=" + key);
        }
    }

    /**
     * delete a value from a given key/value name
     *
     * @param hkey
     * @param key
     * @param value
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void deleteValue(int hkey, String key, String value)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        int rc = deleteValue(getUnderlyingObject(hkey), hkey, key, value);
        if (rc != REG_SUCCESS) {
            throw new IllegalArgumentException("rc=" + rc + "  key=" + key + "  value=" + value);
        }
    }

    private static Preferences getUnderlyingObject(int hkey) {
        switch (hkey) {
            case HKEY_CLASSES_ROOT:
                return null;
            case HKEY_CURRENT_USER:
                return userRoot;
            case HKEY_LOCAL_MACHINE:
                return systemRoot;
            default:
                throw new IllegalArgumentException("hkey=" + hkey);
        }
    }
    
    private static String getAccessString(int hkey){
        switch (hkey) {
            case HKEY_CLASSES_ROOT:
                return _HKEY_CLASSES_ROOT;
            case HKEY_CURRENT_USER:
                return _HKEY_CURRENT_USER;
            case HKEY_LOCAL_MACHINE:
                return _HKEY_LOCAL_MACHINE;
            default:
                throw new IllegalArgumentException("hkey=" + hkey);
        }
    }
    
    private static String addHkey(int hkey, String key){
        return key + "\\" + getAccessString(hkey);
    }

    /**
     * ********************************************************************************************************************************
     * Method saves a binary entry for the given key, valuename and data
     *
     * @param hkey
     * @see <code>extractAnyValue</code> - method could have a deadlock
     * @param key
     * @param valueName String the binary value name in the registry
     * @param plainData String like you would see in the registry (without any
     * spaces, etc..)
     * @throws RegistryErrorException
     * *******************************************************************************************************************************
     */
    public static void writeBinary(int hkey, String key, String valueName, String plainData) throws RegistryErrorException {
        if (nativeHandler == null) {
            throw new RegistryErrorException("NativeHandler is not initalized!");
        }
        if (nativeHandler instanceof RegeditHandler) {
            plainData = convertStringToHexComma(plainData, false);
        } else {
            plainData = convertStringToHex(plainData);
        }
        key = addHkey(hkey, key);
        nativeHandler.saveAnyValue(key, valueName, BINARY_KEY_IDENT, plainData);
    }

    /**
     * ********************************************************************************************************************************
     * Method reads from the registry a BINARY value - this is made via
     * Runtime.getRuntime().exec(regedit) and is not one of the best methods,
     * but at least it doesnt need a dll
     *
     * @param hkey
     * @see <code>extractAnyValue</code> - method could have a deadlock
     * @param key
     * @param valueName String the valueName of the binary entry which you want
     * to read
     * @return String null or the binary data separated by comma
     * @throws RegistryErrorException
     * *******************************************************************************************************************************
     */
    public static String readBinary(int hkey, String key, String valueName) throws RegistryErrorException {
        if (key == null) {
            throw new NullPointerException("Registry key cannot be null");
        }
        if (valueName == null) {
            throw new NullPointerException("Valuename cannot be null, because the default value is always a STRING! If you want to read a String use readValue");
        }
        if (nativeHandler == null) {
            throw new RegistryErrorException("NativeHandler is not initalized!");
        }
        key = addHkey(hkey, key);
        String ret = nativeHandler.extractAnyValue(key, valueName, false);
        //if it is not null and it starts with hex: it is hopefully a binary entry
        if (ret != null && ret.startsWith(BINARY_KEY_IDENT)) {
            return ret.substring(4);
        } //if the reghandler or caching is active, the plain key will be returned
        else if (ret != null && (nativeHandler instanceof RegHandler)) {
            return ret;
        }
        return null;
    }

    /**
     * ********************************************************************************************************************************
     * Method saves a dword entry in the registry
     *
     * @param hkey
     * @see <code>saveAnyValue</code> - method could have a deadlock
     * @param key
     * @param valueName String the valuename of the dword entry
     * @param hexData String a hexadecimal String withouth comma or spaces (use
     * <code>Long.toHexString(long)</code> to get a hex string)
     * @throws RegistryErrorException
     * *******************************************************************************************************************************
     */
    public static void writeDword(int hkey, String key, String valueName, String hexData) throws RegistryErrorException {
        if (nativeHandler == null) {
            throw new RegistryErrorException("NativeHandler is not initalized!");
        }
        try {
            if (Long.parseLong(hexData, 16) > 4294967295L) {
                throw new RegistryErrorException("Dword entry to high for registry! FFFF FFFF is the highest value!");
            }
        } catch (Exception ex) {
            throw RegistryErrorException.getException(ex);
        }
        key = addHkey(hkey, key);
        nativeHandler.saveAnyValue(key, valueName, DWORD_KEY_IDENT, hexData);
    }

    /**
     * ********************************************************************************************************************************
     * Method reads the dword entry from the registry
     *
     * @param hkey
     * @see <code>extractAnyValue</code> - method could have a deadlock
     * @param key
     * @param valueName String the dword value
     * @return String the dword entry in a hex string
     * @throws RegistryErrorException
     * *******************************************************************************************************************************
     */
    public static String readDword(int hkey, String key, String valueName) throws RegistryErrorException {
        if (key == null) {
            throw new NullPointerException("Registry key cannot be null");
        }
        if (valueName == null) {
            throw new NullPointerException("Valuename cannot be null, because the default value is always a STRING! If you want to read a String use readValue");
        }
        if (nativeHandler == null) {
            throw new RegistryErrorException("NativeHandler is not initalized!");
        }
        key = addHkey(hkey, key);
        String ret = nativeHandler.extractAnyValue(key, valueName, false);
        //if it is not null and it starts with hex: it is hopefully a binary entry
        if (ret != null && ret.startsWith(DWORD_KEY_IDENT)) {
            return ret.substring(6);
        } //if the reghandler or caching is active, the plain key will be returned
        else if (ret != null && (nativeHandler instanceof RegHandler)) {
            return ret;
        }
        return null;
    }

    /**
     * ********************************************************************************************************************************
     * Method saves a multi string entry in the registry
     *
     * @param hkey
     * @see <code>saveAnyValue</code> - method could have a deadlock
     * @param key
     * @param valueName String the multi value name
     * @param plainData String
     * @throws RegistryErrorException
     * *******************************************************************************************************************************
     */
    public static void saveMulti(int hkey, String key, String valueName, String plainData) throws RegistryErrorException {
        if (nativeHandler == null) {
            throw new RegistryErrorException("NativeHandler is not initalized!");
        }
        if (nativeHandler instanceof RegeditHandler) {
            plainData = convertStringToHexComma(plainData, true);
        }
        key = addHkey(hkey, key);
        nativeHandler.saveAnyValue(key, valueName, MULTI_KEY_IDENT, plainData);
    }

    /**
     * ********************************************************************************************************************************
     * Method reads a multi string entry from the registry
     *
     * @param hkey
     * @see <code>extractAnyValue</code> - method could have a deadlock
     * @param key
     * @param valueName String the multi value name
     * @return String the HEXADECIMAL values separated by comma (use
     * <code>String parseHexString(String)</code> to convert it the line
     * seperator is also a hex null! You have to parse it out
     * @throws RegistryErrorException
     * *******************************************************************************************************************************
     */
    public static String readMulti(int hkey, String key, String valueName) throws RegistryErrorException {
        if (key == null) {
            throw new NullPointerException("Registry key cannot be null");
        }
        if (valueName == null) {
            throw new NullPointerException("Valuename cannot be null, because the default value is always a STRING! If you want to read a String use readValue");
        }
        if (nativeHandler == null) {
            throw new RegistryErrorException("NativeHandler is not initalized!");
        }
        key = addHkey(hkey, key);
        String ret = nativeHandler.extractAnyValue(key, valueName, false);
        //if it is not null and it starts with hex: it is hopefully a binary entry
        if (ret != null && ret.startsWith(MULTI_KEY_IDENT)) {
            return ret.substring(7);
        } //if the reghandler or caching is active, the plain key will be returned
        else if (ret != null && (nativeHandler instanceof RegHandler)) {
            return ret;
        }
        return null;
    }

    /**
     * ********************************************************************************************************************************
     * Method saves an expand string entry
     *
     * @param hkey
     * @see <code>saveAnyValue</code> - method could have a deadlock
     * @param key
     * @param valueName String the expand value name
     * @param plainData String
     * @throws RegistryErrorException
     * ********************************************************************************************************************************
     */
    public static void saveExpand(int hkey, String key, String valueName, String plainData) throws RegistryErrorException {
        if (nativeHandler == null) {
            throw new RegistryErrorException("NativeHandler is not initalized!");
        }
        if (nativeHandler instanceof RegeditHandler) {
            plainData = convertStringToHexComma(plainData, true);
        }
        key = addHkey(hkey, key);
        nativeHandler.saveAnyValue(key, valueName, EXPAND_KEY_IDENT, plainData);
    }

    /**
     * ********************************************************************************************************************************
     * Method reads an expand string entry
     *
     * @param hkey
     * @see <code>extractAnyValue</code> - method could have a deadlock
     * @param key
     * @param valueName String the expand value name
     * @return String the HEXADECIMAL values separated by comma (use
     * <code>String parseHexString(String)</code> to convert it
     * @throws RegistryErrorException
     * *******************************************************************************************************************************
     */
    public static String readExpand(int hkey, String key, String valueName) throws RegistryErrorException {
        if (key == null) {
            throw new NullPointerException("Registry key cannot be null");
        }
        if (valueName == null) {
            throw new NullPointerException("Valuename cannot be null, because the default value is always a STRING! If you want to read a String use readValue");
        }
        if (nativeHandler == null) {
            throw new RegistryErrorException("NativeHandler is not initalized!");
        }
        key = addHkey(hkey, key);
        String ret = nativeHandler.extractAnyValue(key, valueName, false);
        //if it is not null and it starts with hex: it is hopefully a binary entry
        if (ret != null && ret.startsWith(EXPAND_KEY_IDENT)) {
            return ret.substring(7);
        } //if the reghandler or caching is active, the plain key will be returned
        else if (ret != null && (nativeHandler instanceof RegHandler)) {
            return ret;
        }
        return null;
    }
    // <editor-fold desc="Reflection Magic" defaultstate="collapsed">
    // =====================
    private static int deleteValue(Preferences root, int hkey, String key, String value)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        int[] handles = (int[]) regOpenKey.invoke(root, new Object[]{
            new Integer(hkey), toCstr(key), new Integer(KEY_ALL_ACCESS)});
        if (handles[1] != REG_SUCCESS) {
            return handles[1];  // can be REG_NOTFOUND, REG_ACCESSDENIED
        }
        int rc = ((Integer) regDeleteValue.invoke(root,
                new Object[]{
                    new Integer(handles[0]), toCstr(value)
                })).intValue();
        regCloseKey.invoke(root, new Object[]{new Integer(handles[0])});
        return rc;
    }

    private static int deleteKey(Preferences root, int hkey, String key)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        int rc = ((Integer) regDeleteKey.invoke(root,
                new Object[]{new Integer(hkey), toCstr(key)})).intValue();
        return rc;  // can REG_NOTFOUND, REG_ACCESSDENIED, REG_SUCCESS
    }

    private static String readString(Preferences root, int hkey, String key, String value)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        int[] handles = (int[]) regOpenKey.invoke(root, new Object[]{
            new Integer(hkey), toCstr(key), new Integer(KEY_READ)});
        if (handles[1] != REG_SUCCESS) {
            return null;
        }
        byte[] valb = (byte[]) regQueryValueEx.invoke(root, new Object[]{
            new Integer(handles[0]), toCstr(value)});
        regCloseKey.invoke(root, new Object[]{new Integer(handles[0])});
        return (valb != null ? new String(valb).trim() : null);
    }

    private static Map<String, String> readStringValues(Preferences root, int hkey, String key)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        HashMap<String, String> results = new HashMap<String, String>();
        int[] handles = (int[]) regOpenKey.invoke(root, new Object[]{
            new Integer(hkey), toCstr(key), new Integer(KEY_READ)});
        if (handles[1] != REG_SUCCESS) {
            return null;
        }
        int[] info = (int[]) regQueryInfoKey.invoke(root,
                new Object[]{new Integer(handles[0])});

        int count = info[0]; // count  
        int maxlen = info[3]; // value length max
        for (int index = 0; index < count; index++) {
            byte[] name = (byte[]) regEnumValue.invoke(root, new Object[]{
                new Integer(handles[0]), new Integer(index), new Integer(maxlen + 1)});
            String value = readString(hkey, key, new String(name));
            results.put(new String(name).trim(), value);
        }
        regCloseKey.invoke(root, new Object[]{new Integer(handles[0])});
        return results;
    }

    private static List<String> readStringSubKeys(Preferences root, int hkey, String key)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        List<String> results = new ArrayList<String>();
        int[] handles = (int[]) regOpenKey.invoke(root, new Object[]{
            new Integer(hkey), toCstr(key), new Integer(KEY_READ)
        });
        if (handles[1] != REG_SUCCESS) {
            return null;
        }
        int[] info = (int[]) regQueryInfoKey.invoke(root,
                new Object[]{new Integer(handles[0])});

        int count = info[0]; // Fix: info[2] was being used here with wrong results. Suggested by davenpcj, confirmed by Petrucio
        int maxlen = info[3]; // value length max
        for (int index = 0; index < count; index++) {
            byte[] name = (byte[]) regEnumKeyEx.invoke(root, new Object[]{
                new Integer(handles[0]), new Integer(index), new Integer(maxlen + 1)
            });
            results.add(new String(name).trim());
        }
        regCloseKey.invoke(root, new Object[]{new Integer(handles[0])});
        return results;
    }

    private static int[] createKey(Preferences root, int hkey, String key)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        return (int[]) regCreateKeyEx.invoke(root,
                new Object[]{new Integer(hkey), toCstr(key)});
    }

    private static void writeStringValue(Preferences root, int hkey, String key, String valueName, String value)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        int[] handles = (int[]) regOpenKey.invoke(root, new Object[]{
            new Integer(hkey), toCstr(key), new Integer(KEY_ALL_ACCESS)});

        regSetValueEx.invoke(root,
                new Object[]{
                    new Integer(handles[0]), toCstr(valueName), toCstr(value)
                });
        regCloseKey.invoke(root, new Object[]{new Integer(handles[0])});
    }
    // </editor-fold>

    // utility
    private static byte[] toCstr(String str) {
        byte[] result = new byte[str.length() + 1];

        for (int i = 0; i < str.length(); i++) {
            result[i] = (byte) str.charAt(i);
        }
        result[str.length()] = 0;
        return result;
    }

    /**
     * *********************************************************************************************************************************
     * Method converts a plain String into a hex comma separated String with 0�s
     * between
     *
     * @param plain String
     * @param appendNullSigns boolean if you want to add null signs (needed for
     * multi and expand entries, but not for binary entry)
     * @return String the converted string
     * ********************************************************************************************************************************
     */
    private static String convertStringToHexComma(String plain, boolean appendNullSigns) {
        if (plain == null || plain.trim().length() == 0) {
            return plain;
        }
        StringBuilder strBuf = new StringBuilder();
        for (int x = 0; x != plain.length(); x++) {
            if (x > 0) {
                strBuf.append(",");
            }
            strBuf.append(Integer.toHexString(plain.charAt(x)));
            if (appendNullSigns) {
                strBuf.append(",00"); //this is needed, dunno why by the multi and expand string entries, but not for the binary
            }
        }
        return strBuf.toString();
    }

    /**
     *
     * @param plain String
     * @return String
     */
    private static String convertStringToHex(String plain) {
        if (plain == null || plain.trim().length() == 0) {
            return plain;
        }
        StringBuilder strBuf = new StringBuilder();
        for (int x = 0; x != plain.length(); x++) {
            strBuf.append(Integer.toHexString(plain.charAt(x)));
        }
        return strBuf.toString();
    }

    /**
     * ********************************************************************************************************************************
     * Method is looking for reg.exe or regedit.exe (first reg.exe if not found,
     * take regedit.exe)
     *
     * @throws RegistryErrorException throws this exception when no reg.exe or
     * regedit.exe is found
     * @todo test vista UAC
     * *******************************************************************************************************************************
     */
    private static void initNatvieRegistry() throws RegistryErrorException {
        try {
            Runtime.getRuntime().exec("reg.exe"); //if no exception is thrown, then reg.exe was successfull
            nativeHandler = new RegHandler(); //reg.exe handler
        } catch (Exception ex) {
            //no check for regedit.exe because of vista uac control
            nativeHandler = new RegeditHandler();
        }
    }
}
