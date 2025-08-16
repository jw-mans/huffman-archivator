import java.util.Scanner;

import com.src.encoder.Encoder;
import com.src.Coder;
import com.src.decoder.Decoder;

import logs.AppLogger;

public class Main {
    private static final AppLogger logger = AppLogger.getLogger(Main.class);
    private static Scanner scanner = new Scanner(System.in);

    private static void exitProgram() {
        logger.info("Program successfully completed.");
        scanner.close();
        System.exit(0);
    }

    private static void process(String mode) {
        System.out.print("Enter: [source file (without spaces)], [target file (without spaces)], [tag]. ");
        String line = scanner.nextLine().trim();
        String[] inputData = line.split("\\s+");
        if(inputData.length != 3) {
            logger.info("Incorrect number of arguments. Expected: 3.");
            return;
        }
        logger.info("REMARK." + inputData[2]);

        logger.info(mode + " mode.");
        Coder coder = new Encoder();
        try {
            if(mode.equals("Decompress"))
                coder = new Decoder();
            coder.code(inputData[0], inputData[1]);
        } catch (Exception ex) {
            logger.severe("Failed process!");
        }
    }

    public static void main(String[] args) {
        if (args.length != 0) {
            logger.severe("Error: unexpected arguments.");
            scanner.close();
            System.exit(1);
        }
        String input;

        while (true) {
            System.out.print("Select operation: \'compress\', \'decompress\', or another to exit program. ");
            input = scanner.nextLine();
            switch(input) {
                case "compress" -> process("Compress");
                case "decompress" -> process("Decompress");
                default -> exitProgram();
            }
        }
    }
}
