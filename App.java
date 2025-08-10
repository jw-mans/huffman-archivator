import java.io.IOException;
import java.util.logging.*;

public class App {

    private static final Logger logger = Logger.getLogger(App.class.getName());

    private static void setupLogging() {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.ALL);
        rootLogger.setUseParentHandlers(true);

        try {
            Handler fileHandler = new FileHandler("logs/debug.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(fileHandler);
        } catch (IOException ex) {
            System.err.println("Error: bad logging setup! " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * args:
     * [0]: file to encode name
     * [1]: file to write code
     * [2]: file to write decoded text
     * [3]: some tag with '_' delimeters
     */
    public static void main(String[] args) {
        setupLogging();

        if (args.length != 4) {
            logger.severe("Incorrect arguments number. Excepted: 4.");
            System.exit(1);
        }
        logger.info("Remark. " + args[3]);
        logger.info("Launch with " + String.join(", ", args) + " parameters");

        Encoder.encode(args[0], args[1]);
        Decoder.decode(args[1], args[2]);

        logger.info("Program successfully completed.");
    }
}
