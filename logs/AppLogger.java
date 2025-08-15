package logs;

import java.io.IOException;
import java.util.logging.*;

public final class AppLogger {

    private static boolean isConfigured = false; //singleton for logging configuration
    private final Logger logger; //inner logger for specific class
    
    private AppLogger(Class<?> clazz) {
        logger = Logger.getLogger(clazz.getName());
    }

    private static void setupLogging() { //one-time-calling
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.ALL);

        try {
            Handler fileHandler = new FileHandler("logs/debug.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Logging setup error: " + e.getMessage());
            e.printStackTrace();
        }

        isConfigured = true;
    }

    public static AppLogger getLogger(Class<?> clazz) { //for custom class
        if (!isConfigured) {
            setupLogging();
        }
        return new AppLogger(clazz);
    }

    //for-using methods
    public void info(String msg) { logger.info(msg); }
    public void warning(String msg) { logger.warning(msg); }
    public void severe(String msg) { logger.severe(msg); }
    public void debug(String msg) { logger.fine(msg);}
}
