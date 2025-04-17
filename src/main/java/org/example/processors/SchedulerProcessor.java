package org.example.processors;

import org.example.annotation.Schedule;
import org.example.annotation.Scheduler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SchedulerProcessor {
    private ExecutorService executorService;

    public void init(List<Object> schedulers){
        var schedulerWithAnnotations = schedulers.stream()
                .filter((e) -> e.getClass().isAnnotationPresent(Scheduler.class))
                .toList();
        if (schedulerWithAnnotations.size() != schedulers.size()){
            throw new IllegalArgumentException("Scheduler size is difference");
        }

        getMethodWithAnnotation(schedulerWithAnnotations);
    }

    private void getMethodWithAnnotation(List<Object> schedulers){
        schedulers.stream()
                .forEach((e)-> runMethod(e)
                );
    }

    private void runMethod(Object clazz){
        List<Method> methodList = Arrays.stream(clazz.getClass().getDeclaredMethods())
                .filter(e -> e.isAnnotationPresent(Schedule.class))
                .toList();

        executorService = Executors.newFixedThreadPool(methodList.size());
        for (Method method: methodList){
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    runner(clazz, method);
                }
            });
        }
//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void runner(Object clazz, Method method){
        for (;;) {
            try {
                method.invoke(clazz);
                Thread.sleep(method.getAnnotation(Schedule.class).milliseconds());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
