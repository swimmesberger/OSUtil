package org.fseek.thedeath.os.util;

import java.util.Locale;

public class OSDetector
{
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
        private static final byte OS_ID;

        static
        {
            if(EMULATE > 0){
                switch(EMULATE){
                    case EMULATE_WINDOWS:
                        OS_ID = 8;
                        break;
                    case EMULATE_MAC:
                        OS_ID = 5;
                        break;
                    default:
                        OS_ID = 6;
                        break;
                }
            }else{
                String OS = System.getProperty("os.name").toLowerCase(Locale.getDefault());
                if (OS.indexOf("windows 7") > -1)
                {
                    OS_ID = 8;
                }
                else if (OS.indexOf("windows xp") > -1)
                {
                    OS_ID = 0;
                }
                else if (OS.indexOf("windows vista") > -1)
                {
                    OS_ID = 1;
                }
                else if (OS.indexOf("windows 2000") > -1)
                {
                    OS_ID = 2;
                }
                else if (OS.indexOf("windows 2003") > -1)
                {
                    OS_ID = 7;
                }
                else if (OS.indexOf("nt") > -1)
                {
                    OS_ID = 3;
                }
                else if (OS.indexOf("windows") > -1)
                {
                    OS_ID = 4;
                }
                else if (OS.indexOf("mac") > -1)
                {
                    OS_ID = 5;
                }
                else
                {
                    OS_ID = 6;
                }
            }
        }

        public static boolean isLinux()
        {
            return OS_ID == 6;
        }

        public static boolean isMac()
        {
            return OS_ID == 5;
        }

        public static boolean isWindows()
        {
            switch (OS_ID)
            {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 7:
                case 8:
                    return true;
                case 5:
                case 6:
            }
            return false;
        }

}
