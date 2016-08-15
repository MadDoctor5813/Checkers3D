package com.kevin.util;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Created by 532259 on 12/7/2015.
 */

//Java treats .dll dependencies oddly, so they have to be loaded manually.
public class DLLLoader {

    static final String NATIVES_PATH = "natives/";

    public static void load() {
        //set java library path
        System.setProperty( "java.library.path", NATIVES_PATH);
        //I don't really know what this does, but I get .dll link errors if I remove it.
        Field fieldSysPath = null;
        try {
            fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
            fieldSysPath.setAccessible( true );
            fieldSysPath.set( null, null );
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
