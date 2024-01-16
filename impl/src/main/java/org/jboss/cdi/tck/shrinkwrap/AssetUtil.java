/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.shrinkwrap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.impl.base.path.BasicPath;

/**
 *
 * @author <a href="mailto:aslak@conduct.no">Aslak Knutsen</a>
 * @author Martin Kouba
 */
public final class AssetUtil {

    /**
     * The delimiter used for classes.
     */
    public static final String DELIMITER_CLASS_NAME_PATH = "\\.";

    /**
     * The delimiter used for classes represented in resource form.
     */
    public static final String DELIMITER_RESOURCE_PATH = "/";

    /**
     * Extension applied to .class files
     */
    private static final String EXTENSION_CLASS = ".class";

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * No instance.
     */
    private AssetUtil() {
    }

    /**
     * Taken from the ShrinkWrap impl.
     *
     * Helper to extract a ClassResources full path. <br/>
     * <br/>
     * ie: package.MyClass = package/MyClass.class
     *
     * @param clazz
     * @return
     */
    public static ArchivePath getFullPathForClassResource(Class<?> clazz) {
        String classResourceDelimiter = clazz.getName().replaceAll(DELIMITER_CLASS_NAME_PATH, DELIMITER_RESOURCE_PATH);
        String classFullPath = classResourceDelimiter + EXTENSION_CLASS;
        return new BasicPath(classFullPath);
    }

    /**
     * Taken from the ShrinkWrap impl.
     *
     * Helper to extract a ClassResources full path. <br/>
     * <br/>
     *
     * ie: package.MyClass = package/MyClass.class
     *
     * @param className
     * @return
     */
    public static ArchivePath getFullPathForClassResource(String className) {
        String classResourceDelimiter = className.replaceAll(DELIMITER_CLASS_NAME_PATH, DELIMITER_RESOURCE_PATH);
        String classFullPath = classResourceDelimiter + EXTENSION_CLASS;
        return new BasicPath(classFullPath);
    }

    /**
     * Doesn't guarantee line terminators to be preserved.
     *
     * @param asset
     * @return the contents of the specified asset as a String in UTF-8 charset
     */
    public static String readAssetContent(Asset asset) {

        if (asset == null) {
            throw new NullPointerException();
        }

        InputStream in = asset.openStream();

        if (in == null) {
            return null;
        }

        try {

            StringBuilder content = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = reader.readLine();

            while (line != null) {
                content.append(line);
                content.append(LINE_SEPARATOR);
                line = reader.readLine();
            }
            return content.toString();

        } catch (IOException ioe) {
            throw new RuntimeException("Cannot read string from " + asset, ioe);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ioe) {
                throw new RuntimeException("Cannot not close input stream from " + asset, ioe);
            }
        }
    }

}
