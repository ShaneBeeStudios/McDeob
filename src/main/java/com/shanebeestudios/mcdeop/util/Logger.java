package com.shanebeestudios.mcdeop.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("unused")
public class Logger {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("HH:mm:ss");
    // Colors for messages
    private static final String RESET = "\u001B[0m";
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";

    public static void info(String info) {
        LocalDateTime now = LocalDateTime.now();
        String th = Thread.currentThread().getName();
        System.out.println("[" + DTF.format(now) + " " + CYAN + th + RESET + " INFO" + "]: " + info);
    }

    public static void warn(String warning) {
        LocalDateTime now = LocalDateTime.now();
        String th = Thread.currentThread().getName();
        System.out.println(YELLOW + "[" + DTF.format(now) + " " + CYAN + th + YELLOW + " WARN" + "]: " + warning + RESET);
    }

    public static void error(String error) {
        LocalDateTime now = LocalDateTime.now();
        String th = Thread.currentThread().getName();
        System.out.println(RED + "[" + DTF.format(now) + " " + CYAN + th + RED + " ERROR" + "]: " + error + RESET);
    }

}
