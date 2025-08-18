import java.util.Scanner;

import com.src.Coder;
import com.src.decoder.Decoder;
import com.src.encoder.Encoder;
import com.utils.files.SizeComparator;

import logs.AppLogger;

public class Main {
    private static final AppLogger logger = AppLogger.getLogger(Main.class);
    private static Scanner scanner = new Scanner(System.in);

    private static void inform(String msg) {
        System.out.println(msg);
        logger.info(msg);
    }

    private static void exitProgram() {
        scanner.close();
        inform("Program successfully completed. Exiting . . .");
        System.exit(0);
    }

    private static void process(String mode) {
        System.out.println("Enter the following string: [source file] [target file]");
        String line = scanner.nextLine().trim();
        /*
         * inputData structure:
         * 1) source filename
         * 2) target filename
         * 3) tag
         */
        String[] inputData = line.split("\\s+");
        if(inputData.length != 2) {
            inform("To run program enter TWO arguments due to instruction. Coming back . . .");
            return;
        }

        logger.info(mode + " mode.");
        boolean isCompressing = true;
        Coder coder = new Encoder();
        try {
            if(mode.equals("Decompress")) {
                coder = new Decoder();
                isCompressing = false;
            }
            coder.code(inputData[0], inputData[1]);
            System.out.println(SizeComparator.getRatio(isCompressing, inputData[0], inputData[1]));
        } catch (Exception ex) {
            logger.severe("Error, failed process!");
        }
    }

    public static void main(String[] args) {
        if (args.length != 0) {
            logger.severe("Error: unexpected arguments. Exit . . .");
            scanner.close();
            System.exit(1);
        }
        while (true) {
            System.out.print("Select operation: \'compress\', \'decompress\', or another to exit program.\n");
            try {
                String input = scanner.nextLine();
                switch(input) {
                    case "compress" -> process("Compress");
                    case "decompress" -> process("Decompress");
                    default -> exitProgram();
                }  
            } catch(Exception ex) { exitProgram(); }
        }
    }
}
