package com.shanebeestudios.mcdeop;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public static void info(String info) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String th = Thread.currentThread().getName();
        System.out.println("[" + dtf.format(now) + " INFO - " + th + "]: " + info);
    }

}
