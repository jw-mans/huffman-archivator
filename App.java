public class App {
    /* 
    args:
    [0]: file to encode name
    [1]: file to write code
    */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Error: uncorrect amount of arguments, please try again . . .");
            System.exit(1);
        }
        System.out.println("\nCorrect launch!");
        Encoder.encode(args[0], args[1]);
        Decoder.decode(args[1], args[2]);
    }
}
