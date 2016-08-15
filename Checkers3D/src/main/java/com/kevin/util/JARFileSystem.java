package com.kevin.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.HashMap;

/**
 * Created by Kevin on 2015-12-17.
 */

//Because the resources dir is packed inside the .jar, we need to do some work so we can treat
//it like a normal directory. This class does that work.
public class JARFileSystem {

    private static FileSystem system;
    static {
        //create a filesystem for the jar file
        String jarLoc = null;
        try {
            //grab the filesystem location of the .jar file we're running from
            jarLoc = JARFileSystem.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString();
            //Create a filesystem class from it.
            system = FileSystems.newFileSystem(URI.create("jar:" + jarLoc), new HashMap<String, String>());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DirectoryStream<Path> getAllPathsIn(String dir) {
        //use the system to get directory stream holding all paths in the given dir
        Path path = system.getPath(dir);
        try {
            return Files.newDirectoryStream(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Path getPath(String dir) {
        return system.getPath(dir);
    }

}
