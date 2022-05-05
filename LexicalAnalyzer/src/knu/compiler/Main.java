package knu.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.math.*;

import static java.lang.Double.parseDouble;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        String inputText = "";
        List<String> memory = new ArrayList<>();

        File file = new File("tests/samples/baddouble.frag");
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

                boolean doubleBool = false;
                boolean hex = false;
                boolean hasE=false;
                String ifHasE="";
                String hasEpost="";
                String hasEop="";

                if (string[i] == '0' && (string[i + 1] == 'x' || string[i + 1] == 'X')) {
                    if (string[i + 2] != '\n'&&((string[i+2] >= 'a' && string[i+2] <= 'f') || (string[i+2] >= 'A' && string[i+2] <= 'F') || (string[i+2] >= '0' && string[i+2] <= '9'))) {
                        hex = true;
                        temp += string[i];
                        temp += string[i + 1];
                        colNum+=2;
                        i += 2;


                        while (i < string.length) {
                            if (string[i] >= '0' && string[i] <= '9' ||
                                    (string[i] >= 'a' && string[i] <= 'f') || (string[i] >= 'A' && string[i] <= 'F') || string[i] == '.') {
                                if (string[i] == '.')
                                    doubleBool = true;
                                temp += string[i];
                                i++;
                                colNum++;
                            } else
                                break;
                        }
                    }
                    else
                    {
                        temp+=string[i];
                        i++;
                        colNum++;
                        hex=false;

                    }
                } else {
                    while (i < string.length) {
                        if (string[i] >= '0' && string[i] <= '9' ||
                                string[i] == '.'||string[i]=='E'||string[i]=='e') {
                            if (string[i] == '.')
                                doubleBool = true;
                            if(string[i]=='E'||string[i]=='e') {
                                if(string[i+1]=='+'||string[i+1]=='-'){
                                    if(string[i+2] >= '0' && string[i+2] <= '9'){
                                        hasE = true;
                                        hasEpost=temp;
                                        temp += string[i];
                                        i++;
                                        colNum++;
                                        temp += string[i];
                                        hasEop+=string[i];
                                        i++;
                                        colNum++;
                                        temp += string[i];
                                        ifHasE+=string[i];
                                        i++;
                                        colNum++;
                                        while(i<string.length){
                                            if (string[i] >= '0' && string[i] <= '9'){
                                                temp += string[i];
                                                ifHasE+=string[i];
                                                i++;
                                                colNum++;
                                            }
                                            else
                                                break;
                                        }
                                    }
                                    else
                                        break;
                                }
                                else
                                    break;
                            }
                            else{
                            temp += string[i];
                            i++;
                            colNum++;
                            }
                        } else
                            break;
                    }
                }

                String temp2 = "";
                if (hex == true) {
                    temp2 = temp.substring(2);
                    temp2 = String.valueOf(Integer.parseInt(temp2, 16));

                    if (doubleBool == true) {
                        System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is T_DoubleConstant (token value: %5$s)", temp, lineNum, colNum - temp.length(), colNum - 1, temp2));
                    } else if (doubleBool == false)
                        System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is T_IntConstant (token value: %5$s)", temp, lineNum, colNum - temp.length(), colNum - 1, temp2));

                } else {
                    if(hasE==true){
                        double a=0;
                        if(hasEop=="-"){
                            double b=-1*(Double.parseDouble(ifHasE));
                            a=Double.parseDouble(hasEpost)*Math.pow(10,b);
                        }
                        else{
                            a=Double.parseDouble(hasEpost)*Math.pow(10,(Double.parseDouble(ifHasE)));
                        }
                        temp2=String.valueOf(a);
                    }
                    else
                        temp2=temp;
                    if (doubleBool == true) {
                        System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is T_DoubleConstant (token value: %5$s)", temp, lineNum, colNum - temp.length(), colNum - 1, temp2));
                    } else if (doubleBool == false)
                        System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is T_IntConstant (token value: %5$s)", temp, lineNum, colNum - temp.length(), colNum - 1, temp2));

                }
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
                    if (!memory.contains(temp))
                        memory.add(temp);
                    System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is T_Identifier (token address: %5$s)", temp, lineNum, colNum - temp.length(), colNum - 1, memory.indexOf(temp) + 1));
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
            if (string[i] == '/') {
                if (string[i + 1] == '/') {
                    while (string[i + 1] != '\n')
                        i++;

                    i++;
                } else if (string[i + 1] == '*') {
                    while (string[i] != '*' || string[i + 1] != '/')
                        i++;

                    i += 2;
                } else {
                    String temp = "" + string[i];
                    System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is '%5$s'", temp, lineNum, colNum, colNum + temp.length() - 1, temp));
                    unknown_char = false;
                }
            }

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
