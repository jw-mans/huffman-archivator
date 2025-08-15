package common.encoder;

public class ConsoleEncoder implements EncoderInterface {
    @Override
    public void encode (String initialFilename, String codeFilename) {
        Encoder.encode(initialFilename, codeFilename);
    }
}
