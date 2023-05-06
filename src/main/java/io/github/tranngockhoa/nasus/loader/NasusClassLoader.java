package io.github.tranngockhoa.nasus.loader;

import io.github.tranngockhoa.nasus.exception.IoCClassLoaderException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NasusClassLoader {
    private static final String CLASS_EXTENSION = ".class";
    private static NasusClassLoader INSTANCE;

    public static NasusClassLoader getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }

        synchronized (NasusClassLoader.class) {
            if (INSTANCE != null) {
                return INSTANCE;
            }
            INSTANCE = new NasusClassLoader();

            return INSTANCE;
        }
    }

    private NasusClassLoader() {
    }

    public List<Class<?>> getClassesInPackage(String packageName) {
        InputStream classStream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", File.separator));

        if (classStream == null) {
            throw new IoCClassLoaderException("Can't retrieve class InputStream from package " + packageName);
        }

        BufferedReader classReader = new BufferedReader(new InputStreamReader(classStream));
        List<Class<?>> classesInPackage = new ArrayList<>();

        classReader.lines()
                .forEach(fileName -> this.processSubFile(packageName, classesInPackage, fileName));

        return classesInPackage;
    }

    private void processSubFile(String packageName, List<Class<?>> classesInPackage, String fileName) {
        if (fileName.endsWith(CLASS_EXTENSION)) {
            classesInPackage.add(getClass(fileName, packageName));
        } else {
            List<Class<?>> classesInSubPackage = this.getClassesInPackage(packageName + "." + fileName);

            if (!classesInSubPackage.isEmpty()) {
                classesInPackage.addAll(classesInSubPackage);
            }
        }
    }

    private Class<?> getClass(String className, String packageName) {
        String classFullName = packageName + "." + className.substring(0, className.lastIndexOf('.'));
        try {
            return Class.forName(classFullName);
        } catch (ClassNotFoundException e) {
            throw new IoCClassLoaderException("Can't retrieve class for name " + classFullName);
        }
    }
}
