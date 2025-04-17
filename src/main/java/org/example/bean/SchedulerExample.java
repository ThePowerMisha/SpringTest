package org.example.bean;

import org.example.annotation.Autowired;
import org.example.annotation.Schedule;
import org.example.annotation.Scheduler;

@Scheduler
public class SchedulerExample {
    @Autowired
    private WiredExample wiredExample;

    public void imAlive(){
        wiredExample.imAlive();
        System.out.println("SCHEDULER IS LIVE");
    }

    @Schedule(milliseconds = 1000)
    public void start(){
        System.out.println("SchedulerStarted");
    }

    @Schedule(milliseconds = 500)
    public void secondStart(){
        System.out.println("Second is Started");
    }
}
