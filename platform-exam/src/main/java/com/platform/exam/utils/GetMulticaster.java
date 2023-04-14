package com.platform.exam.utils;

import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

public class GetMulticaster {

    private GetMulticaster(){}

    private static final ApplicationEventMulticaster applicationEventMulticaster = new SimpleApplicationEventMulticaster();

    public static ApplicationEventMulticaster getMulticaster(){
        return applicationEventMulticaster;
    }
}
