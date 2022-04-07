package knu.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        String inputText = "";
        List<String> memory = new ArrayList<>();

        File file = new File("tests/samples/number.frag");
        Scanner scanner = new Scanner(file);


        inputText = scanner.nextLine();
        while (scanner.hasNextLine()) {
            inputText = inputText + "\n" + scanner.nextLine();
        }

        int lineNum = 1;
        int colNum = 0;

        char[] string = inputText.toCharArray();

        for (int i = 0; i < string.length; i++) {
            boolean unknown_char = true;
            colNum++;

            if (string[i] >= '0' && string[i] <= '9') {
                String temp = "";

                while (i < string.length && string[i] >= '0' && string[i] <= '9') {
                    temp += string[i];
                    i++;
                    colNum++;
                }

                // How to deal with floating-point numbers?
                //16진수 처리 시작


                while (i < string.length) {
                    if (string[i] >= '0' && string[i] <= '9' || string[i] == 'x' || string[i] == 'X' || (string[i] >= 'a' && string[i] <= 'f') || (string[i] >= 'A' && string[i] <= 'F')) {
                        temp += string[i];
                        i++;
                        colNum++;
                    } else
                        break;

                }




                //16진수 처리 끝

                System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is T_IntConstant (token value: %5$s)", temp, lineNum, colNum - temp.length(), colNum - 1, temp));

                unknown_char = false;
            }

            if ((string[i] >= 'A' && string[i] <= 'Z') || string[i] == '_' || (string[i] >= 'a' && string[i] <= 'z')) {
                String temp = "";
                temp += string[i];

                i++;
                colNum++;
                while (i < string.length && ((string[i] >= 'A' && string[i] <= 'Z') || string[i] == '_'
                        || (string[i] >= 'a' && string[i] <= 'z')
                        || (string[i] >= '0' && string[i] <= '9'))) {
                    temp += string[i];
                    i++;
                    colNum++;
                }

                // 식별자, 키워드 구분
                if (temp.equals("if") || temp.equals("else") || temp.equals("for") || temp.equals("while") || temp.equals("void") ||
                        temp.equals("class") || temp.equals("extends") || temp.equals("implements") || temp.equals("interface") ||
                        temp.equals("Print") || temp.equals("break") || temp.equals("return") || temp.equals("this") ||
                        temp.equals("new") || temp.equals("ReadInteger") || temp.equals("ReadLine") || temp.equals("NewArray") ||
                        temp.equals("int") || temp.equals("double") || temp.equals("bool") || temp.equals("string") ||
                        temp.equals("id") || temp.equals("null")) {
                    System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is T_%5$s", temp, lineNum, colNum - temp.length(), colNum - 1, temp.substring(0, 1).toUpperCase() + temp.substring(1)));
                } else if (temp.equals("true") || temp.equals("false")) {
                    System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is T_BoolConstant (token value: %5$s)", temp, lineNum, colNum - temp.length(), colNum - 1, temp));
                } else {  // 키워드(타입), 식별자
                    // How to deal with identifiers?
                }

                if (i >= string.length) {
                    break;
                } else {
                    i--;
                    colNum--;
                }
                unknown_char = false;
            }

            if (string[i] == '"') {
                String temp = "\"";
                i++;
                colNum++;
                boolean is_terminated = true;

                while (i < string.length && string[i] != '"') {
                    if (string[i] == '\n') {
                        System.out.println(String.format("\n*** Error line %d.", lineNum));
                        System.out.println(String.format("*** Unterminated string constant: %s\n", temp));
                        is_terminated = false;
                        break;
                    }
                    temp += string[i];
                    i++;
                    colNum++;
                }

                if (i < string.length && string[i] == '"') {
                    temp += string[i];
                }

                if (is_terminated) {
                    System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is T_StringConstant (token value: %5$s)", temp, lineNum, colNum - temp.length() + 1, colNum, temp));
                }

                if (i >= string.length) {
                    break;
                }

                unknown_char = false;
            }

            if (string[i] == '+' || string[i] == '-' || string[i] == '*' || string[i] == '%' || string[i] == ';' || string[i] == '('
                    || string[i] == ')' || string[i] == '{' || string[i] == '}' || string[i] == ',' || string[i] == '.' || string[i] == '[' || string[i] == ']') {
                String temp = "" + string[i];
                System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is '%5$s'", temp, lineNum, colNum, colNum + temp.length() - 1, temp));
                unknown_char = false;
            }

            if (string[i] == '=' || string[i] == '!' || string[i] == '<' || string[i] == '>') {
                String temp = "" + string[i];
                if (string[i + 1] == '=') {
                    temp += string[i + 1];
                    i++;
                    colNum++;
                }
                System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is '%5$s'", temp, lineNum, colNum - temp.length() + 1, colNum, temp));
                unknown_char = false;
            }

            if (string[i] == '&') {
                String temp = "" + string[i];
                if (string[i + 1] == '&') {
                    temp += string[i + 1];
                    i++;
                    colNum++;
                    System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is '%5$s'", temp, lineNum, colNum - temp.length() + 1, colNum, temp));
                    unknown_char = false;
                }
            }

            if (string[i] == '|') {
                String temp = "" + string[i];
                if (string[i + 1] == '|') {
                    temp += string[i + 1];
                    i++;
                    colNum++;
                    System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is '%5$s'", temp, lineNum, colNum, colNum + temp.length() - 1, temp));
                    unknown_char = false;
                }
            }

            // How to deal with single-line and multi-line comments?

            if (string[i] == '\n') {
                colNum = 0;
                lineNum++;
            } else {
                if (unknown_char && string[i] != ' ') {
                    System.out.println(String.format("\n*** Error line %d.", lineNum));
                    System.out.println(String.format("*** Unrecognized char: '%c'\n", string[i]));
                }
            }
        }
    }
}
