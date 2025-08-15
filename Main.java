import java.util.Scanner;
import common.decoder.*;
import common.encoder.*;
import logs.AppLogger;

public class Main {
    public static void main(String[] args) {
        AppLogger logger = AppLogger.getLogger(Main.class);
        if (args.length != 1 || !args[0].equals("start")) {
            logger.severe("Incorrect start message. Excepted: \'start\'.");
            System.exit(1);
        }

        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            System.out.print("Select operation: \'compress\', \'decompress\', or \'exit\'. ");
            input = scanner.nextLine();

            switch (input) {

                case "exit": {
                    logger.info("Program successfully completed.");
                    scanner.close();
                    System.exit(0);
                }
                case "compress": {
                    logger.info("Encode mode.");
                    System.out.print("Enter: [source file (without spaces)], [target file (without spaces)], [tag].");
                    String line = scanner.nextLine().trim();
                    String[] parts = line.split("\\s+");

                    if (parts.length != 3) {
                        logger.info("Incorrect number of arguments. Expected: 3.");
                    } else {
                        logger.info("Remark." + parts[2]);
                        EncoderInterface eif = new ConsoleEncoder();
                        eif.encode(parts[0], parts[1]);
                    }
                    break;
                }
                case "decompress": {
                    logger.info("Decode mode");
                    System.out.print("Enter: [source file (without spaces)], [target file (without spaces)], [tag]. ");
                    String line = scanner.nextLine().trim();
                    String[] parts = line.split("\\s+");

                    if (parts.length != 3) {
                        logger.info("Incorrect number of arguments. Expected: 3.");
                    } else {
                        logger.info("Remark." + parts[2]);
                        DecoderInterface dif = new ConsoleDecoder();
                        dif.decode(parts[0], parts[1]);
                    }
                    break;
                }
                default: {
                    System.out.println("Bad Input. Try again . . .");
                    logger.info("Bad input.");
                    break;
                }
            }
        }
    }
}
