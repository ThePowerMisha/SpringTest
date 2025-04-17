package org.example.bean;

import org.example.annotation.Autowired;
import org.example.annotation.Component;

@Component
public class ComponentExample {
    @Autowired
    private SchedulerExample schedulerExample;
    @Autowired
    private WiredExample wiredExample;

    public void isSchedulerAlive(){
        schedulerExample.imAlive();
        wiredExample.imAlive();
    }
}

