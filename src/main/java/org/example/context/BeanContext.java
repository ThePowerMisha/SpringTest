package org.example.context;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.example.annotation.Autowired;
import org.example.annotation.Component;
import org.example.annotation.Scheduler;
import org.example.bean.ComponentExample;
import org.example.processors.SchedulerProcessor;

import java.util.*;
import java.util.stream.Collectors;

public class BeanContext {
    private final ClassGraph classGraph = new ClassGraph();
    private final Map<String, Object> allBeans = new HashMap<>();
    private final SchedulerProcessor schedulerProcessor = new SchedulerProcessor();


    public BeanContext(String... packages) {
        if (packages.length == 0){
            throw new IllegalArgumentException("0 packages");
        }
        for (String packg : packages) {
            classGraph.acceptPackages(packg);
        }
        classGraph.enableAllInfo();

        runMikhailScanner();

    }

    private void runMikhailScanner() {
        try (ScanResult scanResult =  classGraph.scan()) {
             var stringList = scanResult.getClassesWithAnnotation(Component.class).stream()
                    .filter((i) -> !i.isAnnotation())
                    .collect(Collectors.toMap(k-> k.getName(), v->v.loadClass()));

             for (var entry: stringList.entrySet()){
                 allBeans.put(entry.getKey() ,entry.getValue().getDeclaredConstructor().newInstance());
             }


            for (var entry: allBeans.entrySet()){
                Arrays.stream(entry.getValue().getClass().getDeclaredFields())
                        .filter((i) -> i.isAnnotationPresent(Autowired.class))
                        .forEach((i) -> {
                            var name = i.getType().getName();
                            if (!allBeans.containsKey(name)) {
                                throw new RuntimeException("Key contain in bean");
                            }
                            i.setAccessible(true);
                            try {
                                i.set(entry.getValue(),allBeans.get(name));
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }

//            var bean = (ComponentExample) allBeans.get("org.example.bean.ComponentExample");
//            bean.isSchedulerAlive();
            startSchedulers();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void startSchedulers(){

        var scheduler = allBeans.values().stream()
                .filter(e-> e.getClass().isAnnotationPresent(Scheduler.class))
                .toList();

        schedulerProcessor.init(scheduler);
    }

}
