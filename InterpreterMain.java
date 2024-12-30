import java.util.*;

public class BASICinterpreter {
    private static Scanner codeINPUT = new Scanner(System.in); // the main code we will have
    private static Map<String, Integer> varibaleSTORAGE = new HashMap<>(); // this stores our variables with DIM (We only use Integer values)
    private static boolean IFresult = true;

    public void lineOperation(String code){
        String[] line = code.split("\n");

        for (int PC = 0; PC < line.length; PC++){
            line[PC] = line[PC].trim();
            if(IFresult) {
                if (line[PC].isEmpty()) continue;

                if (line[PC].startsWith("DIM ")) {
                    String current = line[PC].substring(3);
                    if(current.endsWith(" INTEGER")){
                        String[] par = current.split(" AS ");
                        varibaleSTORAGE.put(par[0].trim(), 0);
                    }
                } else if (line[PC].startsWith("IF ")) {
//                    line[PC] = line[PC].substring(2, line[PC].length() - 4).trim();
//                    IFresult = booleanOperation(line[PC]);
                } else if (line[PC].startsWith("WHILE ")) {

                } else if (line[PC].startsWith("FOR ")) {

                }
            }
        }
    }

    private static void IFhandler(String input){
    }

    private static boolean booleanOperation(String input){
        boolean result = false;
        String boolOperations = ("(==|>=|<=|<>|>|<)");
        String[] sides = input.split(boolOperations);
        String leftSide = sides[0].trim();
        String rightSide = sides[1].trim(); //couldn't figure out a better option to split them
        String operation = input.substring(leftSide.length(), input.length() - rightSide.length()).trim();
        int leftValue, rightValue;
        leftValue = formula(leftSide);
        rightValue = formula(rightSide);

        switch(operation){
            case ">=": result = leftValue >= rightValue; break;
            case "<=": result = leftValue <= rightValue; break;
            case "==": result = leftValue == rightValue; break;
            case "<>": result = leftValue > rightValue || leftValue < rightValue; break;
            case ">": result = leftValue > rightValue; break;
            case "<": result = leftValue < rightValue; break;
            default: throw new IllegalArgumentException("Invalid boolean operation: " + operation);
        }
        return result;
    }

    private static int formula (String input) { // input will only contain MOD, operations, and integers, variables
        input = input.replace(" MOD ", " % "); // in a formula, in freeBASIC we use MOD instead of %
        Stack<Integer> value = new Stack<>(); // value holder
        Stack<Character> operation = new Stack<>(); // operation holder
        input = input.replaceAll(" +", ""); // tested and only replaces ' ' and its sequences, this was done to make the next process easier
        String[] inputSplit = input.split("(?<=[-+*/%])|(?=[-+*/%])"); // this thing splits our input into a string array that is split up based on the operations, the operations themselves also get stored

         for(int i = 0; i < inputSplit.length; i++){
            if (inputSplit[i].equals("-") && (i == 0 || isOperator(inputSplit[i - 1].charAt(0)))) { // basically adds the "-" to the next integer, (good for negatives)
                inputSplit[i + 1] = "-" + inputSplit[i + 1];
                continue;
            }
            if(isNumber(inputSplit[i])){ // case for standard integers
                value.push(Integer.parseInt(inputSplit[i]));
            } else if (varibaleSTORAGE.containsKey(inputSplit[i])) {
                value.push(varibaleSTORAGE.get(inputSplit[i])); // Use variable value
                 }
            else if(isOperator(inputSplit[i].charAt(0)) && inputSplit[i].length() == 1) { // case for opeartions
                while (!operation.isEmpty() && moreValue(inputSplit[i].charAt(0), operation.peek())) {
                    value.push(applyOperation(operation.pop(), value.pop(), value.pop()));
                }
                operation.push(inputSplit[i].charAt(0));
            }
        }
        while (!operation.empty()) {
            value.push(applyOperation(operation.pop(), value.pop(), value.pop()));
        }
        return value.pop();
    }
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%'; // if any of these matches, it returns true
    }
    private static boolean isNumber (String potentialNumber){
        return potentialNumber.matches("-?\\d+"); // this can check negatives too
    }
    private static boolean moreValue(char first, char second){ // if the first input has more "value" than second, then it results in true
        if ((first == '*' || first == '/' || first == '%') && (second == '+' || second == '-')) {
            return false;
        } else { return true;}
    }
    public static int applyOperation(char op, int b, int a) { // does the operations
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) {
                    throw new UnsupportedOperationException("Cannot divide by zero");
                }
                return a / b;
            case '%': return a % b;
        }
        return 0;
    }

    public static void main(String[] args) {
    }
}
