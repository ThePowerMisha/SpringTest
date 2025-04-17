package org.example;



import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.example.annotation.Component;
import org.example.annotation.MikhailScanner;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.example.annotation.Scheduler;
import org.example.context.BeanContext;


public class MikhailBoot{
    public static void run(Class<?> clazz, String[] args){
        String[] packages =  Arrays.stream(clazz.getAnnotations())
                .filter((i)->i.annotationType() == MikhailScanner.class)
                .map((i)->((MikhailScanner)i).value())
                .toArray(String[]::new);
        new BeanContext(packages);
    }
}
