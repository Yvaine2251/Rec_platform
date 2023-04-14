package com.platform.course.utils;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ClassRandomCodeUtil {

    public String randomCode(){
        String charList = "ABCDEFGHIJKLMNPQRSTUVWXYZ";
        String numList = "0123456789";
        StringBuilder rev = new StringBuilder();
        int maxNumCount = 4;
        int length = 8;
        Random f = new Random();
        for (int i = 0; i < length; i++) {
            if (f.nextBoolean() && maxNumCount > 0) {
                maxNumCount--;
                rev.append(numList.charAt(f.nextInt(numList.length())));
            } else {
                rev.append(charList.charAt(f.nextInt(charList.length())));
            }
        }
        return rev.toString();
    }
}
