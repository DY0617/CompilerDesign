package Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("./samples/badparen.decaf");
        Scanner scanner = new Scanner(file);
        String inputText = scanner.nextLine();

        ArrayList<Pair<String, String>> symbolList = LexicalAnalyzer.lex(inputText);

        LLParser.parse(symbolList);
    }
}
