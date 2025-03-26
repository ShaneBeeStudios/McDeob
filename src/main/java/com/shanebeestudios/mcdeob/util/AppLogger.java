package com.shanebeestudios.mcdeob.util;

import com.shanebeestudios.mcdeob.app.App;
import org.jetbrains.java.decompiler.main.extern.IFernflowerLogger;

/**
 * Custom logger for FernFlower
 * <p>Copied/Modifier from {@link org.jetbrains.java.decompiler.main.decompiler.ThreadedPrintStreamLogger}</p>
 */

public class AppLogger extends IFernflowerLogger {

    private App app;
    private Thread statusThread;
    private int processedCount = 0;
    private int decompiledCount = 0;
    private int currentStep = 0; // New variable to track current step

    public AppLogger(App app) {
        this.app = app;
        if (this.app != null) {
            startTimer();
        }
    }

    @SuppressWarnings("BusyWait")
    private void startTimer() {
        this.statusThread = new Thread(() -> {
            while (this.app != null) {
                if (this.processedCount != 0) {
                    if (this.decompiledCount == 0) {
                        int progress = Math.min(100, (int)((this.processedCount / (double)this.processedCount) * 100));
                        this.app.updateProgressBar(Math.min(this.processedCount, 100), this.processedCount, "PreProcessing...");
                    } else {
                        int progress = Math.min(100, (int)((this.decompiledCount / (double)this.processedCount) * 100));
                        this.app.updateProgressBar(Math.min(progress, 100), 100, "Decompiling...");
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        this.statusThread.start();
    }

    public void stopLogging() {
        if (this.app == null) return;
        this.app.resetProgressBar();
        this.app = null;
        this.statusThread = null;
    }


    public void writeMessage(String message, Severity severity) {
        if (this.accepts(severity)) {
            switch (severity) {
                case TRACE -> Logger.trace(message);
                case INFO -> Logger.info(message);
                case WARN -> Logger.warn(message);
                case ERROR -> Logger.error(message);
            }
        }
    }

    public void writeMessage(String message, Severity severity, Throwable t) {
        if (this.accepts(severity)) {
            this.writeMessage(message, severity);
            t.printStackTrace(System.out);
        }

    }

    public void startProcessingClass(String className) {
        if (this.accepts(Severity.INFO)) {
            this.writeMessage("PreProcessing class " + className, Severity.INFO);
        }

    }

    public void endProcessingClass() {
        if (this.accepts(Severity.INFO)) {
            this.writeMessage("... done", Severity.INFO);
            this.processedCount++;
        }

    }

    public void startReadingClass(String className) {
        if (this.accepts(Severity.INFO)) {
            this.writeMessage("Decompiling class " + className, Severity.INFO);
        }

    }

    public void endReadingClass() {
        if (this.accepts(Severity.INFO)) {
            this.writeMessage("... done", Severity.INFO);
            this.decompiledCount++;
        }

    }

    public void startClass(String className) {
        if (this.accepts(Severity.INFO)) {
            this.writeMessage("Processing class " + className, Severity.TRACE);
        }

    }

    public void endClass() {
        if (this.accepts(Severity.INFO)) {
            this.writeMessage("... proceeded", Severity.TRACE);
        }

    }

    public void startMethod(String methodName) {
        if (this.accepts(Severity.INFO)) {
            this.writeMessage("Processing method " + methodName, Severity.TRACE);
        }

    }

    public void endMethod() {
        if (this.accepts(Severity.INFO)) {
            this.writeMessage("... proceeded", Severity.TRACE);
        }

    }

    public void startWriteClass(String className) {
        if (this.accepts(Severity.INFO)) {
            this.writeMessage("Writing class " + className, Severity.TRACE);
        }
    }

    public void endWriteClass() {
        if (this.accepts(Severity.INFO)) {
            this.writeMessage("... written", Severity.TRACE);
        }
    }

}
