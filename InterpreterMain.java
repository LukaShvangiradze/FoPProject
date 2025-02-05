import java.util.*;

public class InterpreterMain {

    private static Scanner scanner = new Scanner(System.in);

    private static Map<String, Integer> map = new HashMap<>(); // Variable storage

    private static boolean mulIfElse = true;

    private static int ind; // Stores index for While loop

    private static int ind2; // Stores index for For loop

    private static int x, y; // Temporary variables for calculations

    private static String gen;  // Variable name in FOR loop

    public void eval(String code) {
        String[] lines = code.split("\n"); // Split code into lines
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].trim(); // Remove leading and trailing spaces
            if (lines[i].isEmpty()) continue; // Skip empty lines

            else if (lines[i].startsWith("FOR ")) { // Handle FOR loops
                ind2 = i;
                String[] par = lines[i].split("=");
                String[] div = par[1].split("TO");
                String[] dev = par[0].split("FOR");
                gen = dev[1].trim();
                String lef = div[0].trim();
                String rig = div[1].trim();
                if (('0' <= lef.charAt(0) && lef.charAt(0) <= '9') || lef.charAt(0) == '-') { //if lef is number
                    x = Integer.parseInt(lef); // Initialize loop variable
                    map.put(gen, x);
                } else {  // if lef is variable
                    x = map.get(lef);
                    map.put(gen, x);
                }
                if (('0' <= rig.charAt(0) && rig.charAt(0) <= '9') || rig.charAt(0) == '-') {
                    y = Integer.parseInt(rig);  // Loop limit
                } else y = map.get(rig);

                if (x > y) {  // Skip loop if start > end
                    for (int j = i; j < lines.length; j++) {
                        if (lines[j].startsWith("NEXT ")) {
                            i = j;  // we move to the line where the for loop ends
                            break;
                        }
                    }
                }

            } else if (lines[i].startsWith("NEXT ")) {  // Handle NEXT in FOR loops
                map.put(gen, ++x); // Increment loop variable
                if (x <= y) {
                    i = ind2;   // Repeat loop , We return to the line where the FOR loop starts
                } else
                    continue; // If the loop variable exceeds the loop limit, we terminate the current FOR loop and proceed to the next line of code.
            } else if (lines[i].startsWith("WHILE ")) {
                ind = i;
                if (isHandleWhile(lines[ind])) { // Check whether the WHILE loop condition is still true.
                    continue;
                }
                for (int j = i; j < lines.length; j++) { //If the WHILE loop condition is false, we move to the line where the loop end
                    if (lines[j].startsWith("WEND")) {
                        i = j;
                        break;
                    }
                }
            }


                else if (lines[i].startsWith("NEXT ")) {  // Handle NEXT in FOR loops
                    map.put(gen, ++x); // Increment loop variable
                    if (x <= y) {
                        i = ind2;   // Repeat loop, return to FOR loop start
                    } else continue; // Exit loop if variable exceeds limit
                }

// Handle WHILE loops - store current line index and check condition
                else if (lines[i].startsWith("WHILE ")) {
                    ind = i;
                    if (isHandleWhile(lines[ind])) { // If condition is true, continue execution
                        continue;
                    }
                    // If condition is false, skip to end of loop
                    for (int j = i; j < lines.length; j++) {
                        if (lines[j].startsWith("WEND")) {
                            i = j;
                            break;
                        }
                    }

// Handle end of WHILE loop
                } else if (lines[i].startsWith("WEND")) {
                    if (isHandleWhile(lines[ind])) {
                        i = ind;
                    } else continue;

// Handle END statement in IF-ELSE blocks
                } else if (lines[i].startsWith("END ")) {
                    mulIfElse = true;
                    continue;

// Handle ELSE statement when previous IF was false
                } else if (
                    !lines[i].startsWith("ELSEIF ")
                     && (lines[i].startsWith("ELSE") && !mulIfElse)) {
                    mulIfElse = true;
                    continue;


                }

            else if(lines[i].startsWith("ELSEIF ") && !mulIfElse) {
                boolean ind = handleIfOrElseIf(lines[i], "ELSEIF");
                if(!ind) {
                    mulIfElse = false;
                }
                else mulIfElse = true;
            }
            // Skip execution if in false branch of IF-ELSE

                else if (!mulIfElse) continue;

// Handle variable declaration (DIM statement)
                else if (lines[i].startsWith("DIM ")) {
                    handleDim(lines[i]);

// Handle PRINT statement for output
                } else if (lines[i].startsWith("PRINT ")) {
                    handlePrint(lines[i]);

// Handle IF statement and its condition
                } else if (lines[i].startsWith("IF ")) {
                    boolean ind = handleIfOrElseIf(lines[i], "IF");
                    if (!ind) {
                        mulIfElse = false; // Mark IF condition as false
                    }


// Handle ELSE statement when previous IF was true
                }

                else if (lines[i].startsWith("ELSE") && mulIfElse) {
                    // Skip to END statement since IF was true
                    for (int j = i; j < lines.length; j++) {
                        if (lines[j].startsWith("END ")) {
                            i = j;
                            break;
                        }
                    }

// Handle variable assignment (contains '=')
                } else if (lines[i].contains("=")) {
                    handleAssignment(lines[i]);
                }
        }
    }

        private boolean isHandleWhile(String line){ 
            String[] par = line.split("WHILE");
            //If a line contains '>=': first, split the line by '>='. Then, using regex, extract the left and right variables(number). If the first character of a part is a digit or '-', it indicates that the part is a number, not a variable.
            //it is the same logic for other operations 
            if (line.contains(">=")) {
                String[] div = par[1].split(">=");
                int x, y;  // x becomes the value of the left variable (number), and y becomes the value of the right variable (number).
                div[0] = div[0].trim();
                div[1] = div[1].trim();
                if (('0' <= div[0].charAt(0) && div[0].charAt(0) <= '9') || div[0].charAt(0) == '-') {
                    x = Integer.parseInt(div[0]);
                } else x = map.get(div[0]);
                if (('0' <= div[1].charAt(0) && div[1].charAt(0) <= '9') || div[1].charAt(0) == '-') {
                    y = Integer.parseInt(div[1]);
                } else y = map.get(div[1]);
                return x >= y;

            } else if (line.contains("<=")) { 
                String[] div = par[1].split("<=");
                int x, y;
                div[0] = div[0].trim();
                div[1] = div[1].trim();
                if (('0' <= div[0].charAt(0) && div[0].charAt(0) <= '9') || div[0].charAt(0) == '-') {
                    x = Integer.parseInt(div[0]);
                } else x = map.get(div[0]);
                if (('0' <= div[1].charAt(0) && div[1].charAt(0) <= '9') || div[1].charAt(0) == '-') {
                    y = Integer.parseInt(div[1]);
                } else y = map.get(div[1]);
                return x <= y;
            } else if (line.contains("=")) {
                String[] div = par[1].split("=");
                int x, y;
                div[0] = div[0].trim();
                div[1] = div[1].trim();
                if (('0' <= div[0].charAt(0) && div[0].charAt(0) <= '9') || div[0].charAt(0) == '-') {
                    x = Integer.parseInt(div[0]);
                } else x = map.get(div[0]);
                if (('0' <= div[1].charAt(0) && div[1].charAt(0) <= '9') || div[1].charAt(0) == '-') {
                    y = Integer.parseInt(div[1]);
                } else y = map.get(div[1]);
                return x == y;
            } else if (line.contains("<>")) {
                String[] div = par[1].split("<>");
                int x, y;
                div[0] = div[0].trim();
                div[1] = div[1].trim();
                if (('0' <= div[0].charAt(0) && div[0].charAt(0) <= '9') || div[0].charAt(0) == '-') {
                    x = Integer.parseInt(div[0]);
                } else x = map.get(div[0]);
                if (('0' <= div[1].charAt(0) && div[1].charAt(0) <= '9') || div[1].charAt(0) == '-') {
                    y = Integer.parseInt(div[1]);
                } else y = map.get(div[1]);
                return x != y;
            } else if (line.contains("<")) {
                String[] div = par[1].split("<");
                int x, y;
                div[0] = div[0].trim();
                div[1] = div[1].trim();
                if (('0' <= div[0].charAt(0) && div[0].charAt(0) <= '9') || div[0].charAt(0) == '-') {
                    x = Integer.parseInt(div[0]);
                } else x = map.get(div[0]);
                if (('0' <= div[1].charAt(0) && div[1].charAt(0) <= '9') || div[1].charAt(0) == '-') {
                    y = Integer.parseInt(div[1]);
                } else y = map.get(div[1]);
                return x < y;
            } else if (line.contains(">")) {
                String[] div = par[1].split(">");
                int x, y;
                div[0] = div[0].trim();
                div[1] = div[1].trim();
                if (('0' <= div[0].charAt(0) && div[0].charAt(0) <= '9') || div[0].charAt(0) == '-') {
                    x = Integer.parseInt(div[0]);
                } else x = map.get(div[0]);
                if (('0' <= div[1].charAt(0) && div[1].charAt(0) <= '9') || div[1].charAt(0) == '-') {
                    y = Integer.parseInt(div[1]);
                } else y = map.get(div[1]);
                return x > y;
            }


            return false;
            
        }


    private void handleDim(String line) {
        String sub = line;
        sub = line.substring(3); // In this sub, we have a line that starts with our variable.
        String[] par = sub.split(" AS "); 
        map.put(par[0].trim(), 0); // We put this variable in the map as a key, and its value is set to 0.
    }

    private void handlePrint(String line) {
        String sub = line;
        sub = line.substring(5).trim(); // In this sub, we have line which does not starts with PRINT(It may start with a variable, a number, or an operation.).

        //If we need to print an operation, we check whether the line contains '=', '-', '', '/', or 'mod'. If it contains any of these, it means we need to print the operation, and we proceed as we do for these methods ('=', '-', '', '/', 'mod').
        
        if (sub.contains("+")) { 
            System.out.println(handlePrintBetter(sub, "\\+"));

        } else if (sub.contains("-") && sub.charAt(0) != '-') {
            System.out.println(handlePrintBetter(sub, "\\-"));

        } else if (sub.contains("*")) {
            System.out.println(handlePrintBetter(sub, "\\*"));

        } else if (sub.contains("/")) {
            System.out.println(handlePrintBetter(sub, "\\/"));

        } else if (sub.contains("MOD")) {
            System.out.println(handlePrintBetter(sub, "MOD"));

        } else {  // If there is no operation, it means we need to print either a number or a variable. We check which one it is and then print it
            if (('0' <= sub.charAt(0) && sub.charAt(0) <= '9') || sub.charAt(0) == '-') {
                System.out.println(Integer.parseInt(sub));
            } else {
                System.out.println(map.get(sub));
            }
        }
    }
    // For an assignment, we first check how the left-side variable gets its value. 
    //If the right side contains an operation, we check whether the line includes '/', '*', '-', or 'mod'. 
    //If the line does not contain any of these, it means the right side contains either a number or a variable, 
    //and we will assign this value to the right-side variable.
    private void handleAssignment(String line) {
        String[] par = line.split("=");
        String val = par[0].trim();
        par[1] = par[1].trim();
        if (par[1].contains("+")) {
            handleA(val, par[1], "\\+");
        } else if (par[1].contains("-")) {
            handleA(val, par[1], "\\-");
        } else if (par[1].contains("/")) {
            handleA(val, par[1], "\\/");
        } else if (par[1].contains("*")) {
            handleA(val, par[1], "\\*");
        } else if (par[1].contains("MOD")) {
            handleA(val, par[1], "MOD");
        } else {

            if (('0' <= par[1].charAt(0) && par[1].charAt(0) <= '9') || par[1].charAt(0) == '-') {
                map.put(val, Integer.parseInt(par[1]));
            } else {
                map.put(val, map.get(par[1]));
            }
        }

    }

// used for handling assignments in the above function
    private void handleA(String v, String par1, String op) {
        String[] div = par1.split(op); 
        String lef = div[0].trim(); // case for the left side
        String rig = div[1].trim(); // case for the right side

        int x, y;

        if (('0' <= lef.charAt(0) && lef.charAt(0) <= '9') || lef.charAt(0) == '-') { // if the left side is an integer
            x = Integer.parseInt(lef); 
        } else x = map.get(lef); // if it's a variable

        if (('0' <= rig.charAt(0) && rig.charAt(0) <= '9') || rig.charAt(0) == '-') { // if the right side is an integer
            y = Integer.parseInt(rig);
        } else y = map.get(rig); // if it's a variable

        if (op == "\\+") map.put(v, x + y);
        if (op == "\\-") map.put(v, x - y);
        if (op == "\\*") map.put(v, x * y);
        if (op == "\\/") map.put(v, x / y); // 0
        if (op == "MOD") map.put(v, x % y); //0
        // the above ifs do the operations
    }


    private int handlePrintBetter(String sub, String op) { // used in handleprint
        String[] div = sub.split(op); // splits the code by the operation
        String lef = div[0].trim(); // case for the left side
        String rig = div[1].trim(); // case for the right side

        int x, y;

        if (('0' <= lef.charAt(0) && lef.charAt(0) <= '9') || lef.charAt(0) == '-') {
            x = Integer.parseInt(lef);
        } else x = map.get(lef);

        if (('0' <= rig.charAt(0) && rig.charAt(0) <= '9') || rig.charAt(0) == '-') {
            y = Integer.parseInt(rig);
        } else y = map.get(rig);

        if (op == "\\/" || op == "MOD") {
            if (y == 0) {
                System.out.println("ERROR! DIVISION OR MOD BY 0 NOT ALLOWED");
                System.exit(0);
            }
        }
        // the above is used for fetching data and parsing
        
        int ans = 0;

        if (op == "\\+") ans = x + y;
        if (op == "\\-") ans = x - y;
        if (op == "\\/") ans = x / y;
        if (op == "MOD") ans = x % y;
        if (op == "\\*") ans = x * y;
        // the above is used for calculating
        return ans;
    }

    // used for handling if or elseif, returns a value for the boolean operations
        private boolean handleIfOrElseIf(String line, String d) {
    // below we have the following methods: split by operation, fetch left side value, fetch right side value, calculate boolean operation
            if(line.contains(">=")) { // case for >=
                String[] par = line.split(">=");
                String[] lef = par[0].split(d);
                String op = lef[1].trim();
                String[] rig = par[1].split("THEN");
                String opana = rig[0].trim();

                int x, y;

                if(('0' <= op.charAt(0) && op.charAt(0) <= '9') || op.charAt(0) == '-') {
                    x = Integer.parseInt(op);
                }
                else x = map.get(op);

                if(('0' <= opana.charAt(0) && opana.charAt(0) <= '9') || opana.charAt(0) == '-') {
                    y = Integer.parseInt(opana);
                }
                else y = map.get(opana);

                return x >= y;
            }
            else if(line.contains("<=")) { // case for <=
                String[] par = line.split("<=");
                String[] lef = par[0].split("IF");
                String op = lef[1].trim();
                String[] rig = par[1].split("THEN");
                String opana = rig[0].trim();

                int x, y;

                if(('0' <= op.charAt(0) && op.charAt(0) <= '9') || op.charAt(0) == '-') {
                    x = Integer.parseInt(op);
                }
                else x = map.get(op);

                if(('0' <= opana.charAt(0) && opana.charAt(0) <= '9') || opana.charAt(0) == '-') {
                    y = Integer.parseInt(opana);
                }
                else y = map.get(opana);

                return x < y;
            }
            else if(line.contains("=")) { // case for =
                String[] par = line.split("=");
                String[] lef = par[0].split("IF");
                String op = lef[1].trim();
                String[] rig = par[1].split("THEN");
                String opana = rig[0].trim();

                int x, y;

                if(('0' <= op.charAt(0) && op.charAt(0) <= '9') || op.charAt(0) == '-') {
                    x = Integer.parseInt(op);
                }
                else x = map.get(op);

                if(('0' <= opana.charAt(0) && opana.charAt(0) <= '9') || opana.charAt(0) == '-') {
                    y = Integer.parseInt(opana);
                }
                else y = map.get(opana);

                return x == y;
            }
            else if(line.contains("<>")) { // case for <>
                String[] par = line.split("<>");
                String[] lef = par[0].split("IF");
                String op = lef[1].trim();
                String[] rig = par[1].split("THEN");
                String opana = rig[0].trim();

                int x, y;

                if(('0' <= op.charAt(0) && op.charAt(0) <= '9') || op.charAt(0) == '-') {
                    x = Integer.parseInt(op);
                }
                else x = map.get(op);

                if(('0' <= opana.charAt(0) && opana.charAt(0) <= '9') || opana.charAt(0) == '-') {
                    y = Integer.parseInt(opana);
                }
                else y = map.get(opana);

                return x != y;
            }
            else if(line.contains(">")) { // case for >
                String[] par = line.split(">");
                String[] lef = par[0].split("IF");
                String op = lef[1].trim();
                String[] rig = par[1].split("THEN");
                String opana = rig[0].trim();

                int x, y;

                if(('0' <= op.charAt(0) && op.charAt(0) <= '9') || op.charAt(0) == '-') {
                    x = Integer.parseInt(op);
                }
                else x = map.get(op);

                if(('0' <= opana.charAt(0) && opana.charAt(0) <= '9') || opana.charAt(0) == '-') {
                    y = Integer.parseInt(opana);
                }
                else y = map.get(opana);

                return x > y;
            }
            else if(line.contains("<")) { // case for <
                String[] par = line.split("<");
                String[] lef = par[0].split("IF");
                String op = lef[1].trim();
                String[] rig = par[1].split("THEN");
                String opana = rig[0].trim();

                int x, y;

                if(('0' <= op.charAt(0) && op.charAt(0) <= '9') || op.charAt(0) == '-') {
                    x = Integer.parseInt(op);
                }
                else x = map.get(op);

                if(('0' <= opana.charAt(0) && opana.charAt(0) <= '9') || opana.charAt(0) == '-') {
                    y = Integer.parseInt(opana);
                }
                else y = map.get(opana);

                return x < y;
            }

            return false;
        }

    // main method to start running the code vvvvvvvvvvv
        public static void main(String[] args) {

            InterpreterMain interpreter = new InterpreterMain();

            String program = "";

            while(scanner.hasNextLine()) {
                program += scanner.nextLine();
                program += '\n';
                //press CTRL+D to stop receiving input, or CTRL+Z depending on the computer
            }

            interpreter.eval(program);

        }
    }
