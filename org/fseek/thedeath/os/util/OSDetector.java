package org.fseek.thedeath.os.util;

import java.util.Locale;
/**
 * Util class to detect the running operating system
 * @author Simon
 */
@SuppressWarnings("unused")
public class OSDetector {
  private static final int EMULATE_NONE = -1;

  private static final int EMULATE_LINUX = 1;
  private static final int EMULATE_WINDOWS = 2;
  private static final int EMULATE_MAC = 3;

  private static final int EMULATE = EMULATE_NONE;

  public static final byte OS_LINUX_OTHER = 6;
  public static final byte OS_MAC_OTHER = 5;
  public static final byte OS_WINDOWS_OTHER = 4;
  public static final byte OS_WINDOWS_NT = 3;
  public static final byte OS_WINDOWS_2000 = 2;
  public static final byte OS_WINDOWS_XP = 0;
  public static final byte OS_WINDOWS_2003 = 7;
  public static final byte OS_WINDOWS_VISTA = 1;
  public static final byte OS_WINDOWS_7 = 8;
  public static final byte OS_WINDOWS_8 = 9;
  private static final byte OS_ID;

  static {
    if (EMULATE > 0) {
      switch (EMULATE) {
        case EMULATE_WINDOWS:
          OS_ID = OS_WINDOWS_7;
          break;
        case EMULATE_MAC:
          OS_ID = OS_MAC_OTHER;
          break;
        default:
          OS_ID = OS_LINUX_OTHER;
          break;
      }
    } else {
      String OS = System.getProperty("os.name").toLowerCase(Locale.getDefault());
      if (OS.indexOf("windows 8") > -1) {
        OS_ID = OS_WINDOWS_8;
      } else if (OS.indexOf("windows 7") > -1) {
        OS_ID = OS_WINDOWS_7;
      } else if (OS.indexOf("windows xp") > -1) {
        OS_ID = OS_WINDOWS_XP;
      } else if (OS.indexOf("windows vista") > -1) {
        OS_ID = OS_WINDOWS_VISTA;
      } else if (OS.indexOf("windows 2000") > -1) {
        OS_ID = OS_WINDOWS_2000;
      } else if (OS.indexOf("windows 2003") > -1) {
        OS_ID = OS_WINDOWS_2003;
      } else if (OS.indexOf("nt") > -1) {
        OS_ID = OS_WINDOWS_NT;
      } else if (OS.indexOf("windows") > -1) {
        OS_ID = OS_WINDOWS_OTHER;
      } else if (OS.indexOf("mac") > -1) {
        OS_ID = OS_MAC_OTHER;
      } else {
        OS_ID = OS_LINUX_OTHER;
      }
    }
  }

  public static boolean isLinux() {
    return OS_ID == OS_LINUX_OTHER;
  }

  public static boolean isMac() {
    return OS_ID == OS_MAC_OTHER;
  }

  public static boolean isUnix() {
    return isLinux() || isMac();
  }

  public static boolean isWindows() {
    switch (OS_ID) {
    case OS_WINDOWS_XP:
    case OS_WINDOWS_VISTA:
    case OS_WINDOWS_2000:
    case OS_WINDOWS_2003:
    case OS_WINDOWS_NT:
    case OS_WINDOWS_8:
    case OS_WINDOWS_7:
    case OS_WINDOWS_OTHER:
      return true;
    }
    return false;
  }

}