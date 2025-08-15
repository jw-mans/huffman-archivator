package common.decoder;

public class ConsoleDecoder implements DecoderInterface {
    @Override
    public void decode (String codeFilename, String outputFilename) {
        Decoder.decode(codeFilename, outputFilename);
    }
}
