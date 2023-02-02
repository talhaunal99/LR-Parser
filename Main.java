import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        LR_Parser parser = new LR_Parser();
        parser.splitIntoLexemes(args[0] + "");
        parser.iteratedParse(args[1]);
    }
}
