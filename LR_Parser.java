import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class LR_Parser {
    ArrayList<String> lexemes;
    ArrayList<String> stack;

    LR_Parser() {
        lexemes = new ArrayList<>();
        stack = new ArrayList<>();
        stack.add("0");
    }

    void splitIntoLexemes(String input) {
        for(int i = 0; i < input.length(); i++) {
            if(input.charAt(i) == 'i') {
                lexemes.add("" + input.charAt(i) + input.charAt(i + 1));
            } else if(  input.charAt(i) == '+' || input.charAt(i) == '*' ||
                        input.charAt(i) == '(' || input.charAt(i) == ')' ||
                        input.charAt(i) == '$') {

                lexemes.add("" + input.charAt(i));
            }
        }
    }

    private boolean isParsable(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    void S (int k) {
        stack.add(lexemes.get(0));
        stack.add(k+"");
        lexemes.remove(0);
    }

    void R1() { // E --> E + T
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).equals("+")) {
                stack.remove(i);
                while(  i < stack.size() && stack.get(i) != null && (stack.get(i).equals("E") ||
                        stack.get(i).equals("T") || stack.get(i).equals("F") ||
                        stack.get(i).equals("id") || isParsable(stack.get(i))) ) {
                    stack.remove(i);
                }
            }
        }
    }

    void R2() { // E --> T
        stack.remove(stack.size() - 1);
        for(int i = 0; i < stack.size() ; i++) {
            if(stack.get(i).equals("T")) {
                stack.set(i, "E");
            }
        }
        if(isParsable(stack.get(stack.size() - 1))) {
            stack.remove(stack.size() - 1);
        }
        int newStackTerm = parsingTableGoto(Integer.parseInt(stack.get(stack.size() - 2)),
                stack.get(stack.size() - 1));
        stack.add(newStackTerm + "");
    }

    void R3() { // T --> T * F
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).equals("*")) {
                stack.remove(i);
                while(  i < stack.size() && stack.get(i) != null && (stack.get(i).equals("E") ||
                        stack.get(i).equals("T") || stack.get(i).equals("F") ||
                        stack.get(i).equals("id") || isParsable(stack.get(i))) ) {
                    stack.remove(i);
                }
            }
        }
    }

    void R4() { // T --> F
        stack.remove(stack.size() - 1);
        for(int i = 0; i < stack.size() ; i++) {
            if(stack.get(i).equals("F")) {
                stack.set(i, "T");
            }
        }
        if(isParsable(stack.get(stack.size() - 1))) {
            stack.remove(stack.size() - 1);
        }
        int newStackTerm = parsingTableGoto(Integer.parseInt(stack.get(stack.size() - 2)),
                stack.get(stack.size() - 1));
        stack.add(newStackTerm + "");
    }

    void R5() { // F --> (E)
        int i = 0;
        int j1 = 0;
        int j2 = 0;
        for (i = 0; i < stack.size(); i++) {
            if(stack.get(i).equals("E")) {
                j1 = i;
                j2 = i;
                while(!stack.get(--j1).equals("("));
                while(!stack.get(++j2).equals(")"));
                break;
            }
        }
        stack.set(j1, "F");
        for(int k = j1; k <= stack.size(); k++) {
            stack.remove(j1 + 1);
        }
        if(isParsable(stack.get(stack.size() - 1))) {
            stack.remove(stack.size() - 1);
        }
        int newStackTerm = parsingTableGoto(Integer.parseInt(stack.get(stack.size() - 2)),
                stack.get(stack.size() - 1));
        stack.add(newStackTerm + "");
    }

    void R6() { // F --> id
        stack.remove(stack.size() - 1);
        for(int i = 0; i < stack.size() ; i++) {
            if(stack.get(i).equals("id")) {
                stack.set(i, "F");
            }
        }
        if(isParsable(stack.get(stack.size() - 1))) {
            stack.remove(stack.size() - 1);
        }
        int newStackTerm = parsingTableGoto(Integer.parseInt(stack.get(stack.size() - 2)),
                stack.get(stack.size() - 1));
        stack.add(newStackTerm + "");
    }


    String parsingTableActionStr(int state, String action) {
        if (state == 0) {
            if(action.equals("id")) {
                return "Shift 5";
            } else if(action.equals("(")) {
                return "Shift 4";
            } else {
                return "ERROR";
            }
        } else if (state == 1) {
            if(action.equals("+")) {
                return "Shift 6";
            } else if(action.equals("$")) {
                return "ACCEPT";
            } else {
                return "ERROR";
            }
        } else if (state == 2) {
            if(action.equals("+")) {
                return "Reduce 2";
            } else if(action.equals("*")) {
                return "Shift 7";
            } else if(action.equals(")")) {
                return "Reduce 2";
            } else if(action.equals("$")) {
                return "Reduce 2";
            } else {
                return "ERROR";
            }
        } else if (state == 3) {
            if(action.equals("+")) {
                return "Reduce 4";
            } else if(action.equals("*")) {
                return "Reduce 4";
            } else if(action.equals(")")) {
                return "Reduce 4";
            } else if(action.equals("$")) {
                return "Reduce 4";
            } else {
                return "ERROR";
            }
        } else if (state == 4 || state == 6 || state == 7) {
            if(action.equals("id")) {
                return "Shift 5";
            } else if(action.equals("(")) {
                return "Shift 4";
            } else {
                return "ERROR";
            }
        } else if (state == 5) {
            if(action.equals("+")) {
                return "Reduce 6";
            } else if(action.equals("*")) {
                return "Reduce 6";
            } else if(action.equals(")")) {
                return "Reduce 6";
            } else if(action.equals("$")) {
                return "Reduce 6";
            } else {
                return "ERROR";
            }
        } else if (state == 8) {
            if(action.equals("+")) {
                return "Shift 6";
            } else if(action.equals(")")) {
                return "Shift 11";
            } else {
                return "ERROR";
            }
        } else if (state == 9) {
            if(action.equals("+")) {
                return "Reduce 1";
            } else if(action.equals("*")) {
                return "Shift 7";
            } else if(action.equals(")")) {
                return "Reduce 1";
            } else if(action.equals("$")) {
                return "Reduce 1";
            } else {
                return "ERROR";
            }
        } else if (state == 10) {
            if(action.equals("+")) {
                return "Reduce 3";
            } else if(action.equals("*")) {
                return "Reduce 3";
            } else if(action.equals(")")) {
                return "Reduce 3";
            } else if(action.equals("$")) {
                return "Reduce 3";
            } else {
                return "ERROR";
            }
        } else if (state == 11) {
            if(action.equals("+")) {
                return "Reduce 5";
            } else if(action.equals("*")) {
                return "Reduce 5";
            } else if(action.equals(")")) {
                return "Reduce 5";
            } else if(action.equals("$")) {
                return "Reduce 5";
            } else {
                return "ERROR";
            }
        }
        return " ";
    }

    void parsingTableActions(String action) {
        if(action.equals("Shift 4")) {
            S(4);
        } else if(action.equals("Shift 5")) {
            S(5);
        } else if(action.equals("Shift 6")) {
            S(6);
        } else if(action.equals("Shift 7")) {
            S(7);
        } else if(action.equals("Shift 11")) {
            S(11);
        } else if(action.equals("Reduce 1")) {
            R1();
        } else if(action.equals("Reduce 2")) {
            R2();
        } else if(action.equals("Reduce 3")) {
            R3();
        } else if(action.equals("Reduce 4")) {
            R4();
        } else if(action.equals("Reduce 5")) {
            R5();
        } else if(action.equals("Reduce 6")) {
            R6();
        }

    }

    int parsingTableGoto(int state, String goTo) {
        if(state == 0) {
            if(goTo.equals("E")) {
                return 1;
            } else if(goTo.equals("T")) {
                return 2;
            } else if(goTo.equals("F")) {
                return 3;
            }
        } else if(state == 4) {
            if(goTo.equals("E")) {
                return 8;
            } else if(goTo.equals("T")) {
                return 2;
            } else if(goTo.equals("F")) {
                return 3;
            }
        } else if(state == 6) {
            if(goTo.equals("T")) {
                return 9;
            } else if(goTo.equals("F")) {
                return 3;
            }
        } else if(state == 7 && goTo.equals("F")) {
            return 10;
        }
        return -1;
    }

    void iteratedParse(String filename) throws IOException {  //Main Loop
        FileWriter fileWriter = new FileWriter(filename);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.printf("%-40s %-40s %-40s\n", "Stack", "Input", "Action");
        String action = parsingTableActionStr(Integer.parseInt(stack.get(stack.size() - 1)),
                lexemes.get(0));
        printWriter.printf("%-40s %-40s %-40s\n", toStr(stack), toStr(lexemes), action);
        parsingTableActions(action);
        while(!action.equals("ERROR") && !action.equals("ACCEPT")) {
            action = parsingTableActionStr(Integer.parseInt(stack.get(stack.size() - 1)),
                    lexemes.get(0));
            printWriter.printf("%-40s %-40s %-40s\n", toStr(stack), toStr(lexemes), action);
            parsingTableActions(action);
        }
        if(action.equals("ACCEPT")) {
            System.out.println("The input has been parsed successfully.");
        } else if (action.equals("ERROR")) {
            System.out.println("Error occurred.");
        } else if(action.equals(" ")) {
            System.out.println("There is something wrong!");
        }
        printWriter.close();
    }
    void printLexemes() {
        for(String s : lexemes) System.out.println(s);
    }

    String toStr(ArrayList<String> list) {
        StringBuilder builder = new StringBuilder();
        for (String s : list) {
            if(list == lexemes)
                builder.append(s + " ");
            else
                builder.append(s);
        }
        return builder.toString();
    }
}
