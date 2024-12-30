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
                if (('0' <= lef.charAt(0) && lef.charAt(0) <= '9') || lef.charAt(0) == '-') { //if it is number
                    x = Integer.parseInt(lef);
                    map.put(gen, x);
                } else {  // if it is variable
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
                if (isHandleWhile(lines[ind])) {
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
                } else if (lines[i].startsWith("ELSE") && !mulIfElse) {
                    mulIfElse = true;
                    continue;

// Skip execution if in false branch of IF-ELSE
                } else if (!mulIfElse) continue;

// Handle variable declaration (DIM statement)
                else if (lines[i].startsWith("DIM ")) {
                    handleDim(lines[i]);

// Handle PRINT statement for output
                } else if (lines[i].startsWith("PRINT ")) {
                    handlePrint(lines[i]);

// Handle IF statement and its condition
                } else if (lines[i].startsWith("IF ")) {
                    boolean ind = handleIf(lines[i]);
                    if (!ind) {
                        mulIfElse = false; // Mark IF condition as false
                    }

// Handle ELSE statement when previous IF was true
                } else if (lines[i].startsWith("ELSE") && mulIfElse) {
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

        private boolean isHandleWhile(String line){ // this is the main code that handles the WHILE LOOP
            String[] par = line.split("WHILE"); // par[0] contains nothing useful
            if (line.contains(">=")) { // case for ">="
                String[] div = par[1].split(">="); // we split up the code into 2 parts
                int x, y;
                div[0] = div[0].trim();
                div[1] = div[1].trim();
                if (('0' <= div[0].charAt(0) && div[0].charAt(0) <= '9') || div[0].charAt(0) == '-') { // with this, we verify if our input is a number or a variable for the left side
                    x = Integer.parseInt(div[0]);
                } else x = map.get(div[0]); // case for a variable
                if (('0' <= div[1].charAt(0) && div[1].charAt(0) <= '9') || div[1].charAt(0) == '-') { // the same for the right side
                    y = Integer.parseInt(div[1]);
                } else y = map.get(div[1]);
                return x >= y;

            } else if (line.contains("<=")) { // case for <=, contains the same operations as the other conditions
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
            } else if (line.contains("=")) { // case for =, contains the same operations as the other conditions
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
            } else if (line.contains("<>")) { // case for <>, contains the same operations as the other conditions, this checks inequality
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
            } else if (line.contains("<")) { // case for <, contains the same operations as the other conditions
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
            } else if (line.contains(">")) { // case for <=, contains the same operations as the other conditions
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


    private void handleDim(String line) { // used for handling DIM operation
        String sub = line;
        sub = line.substring(3);
        String[] par = sub.split(" AS "); // since " AS " can never have a duplicate, and must be in the DIM line, we used that to split
        map.put(par[0].trim(), 0); // par[0] stores the name of the variable, since Integers are always 0 from start
    }

    private void handlePrint(String line) { // used for handling the PRINT operation
        String sub = line;
        sub = line.substring(5).trim(); // we are trying to single out the "PRINT" and remove it

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

        } else {
            if (('0' <= sub.charAt(0) && sub.charAt(0) <= '9') || sub.charAt(0) == '-') {
                System.out.println(Integer.parseInt(sub));
            } else {
                System.out.println(map.get(sub));
            }
        }
        // these are cases for the different expressions PRINT might include
    }

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


    private void handleA(String v, String par1, String op) {
        String[] div = par1.split(op);
        String lef = div[0].trim();
        String rig = div[1].trim();

        int x, y;

        if (('0' <= lef.charAt(0) && lef.charAt(0) <= '9') || lef.charAt(0) == '-') {
            x = Integer.parseInt(lef);
        } else x = map.get(lef);

        if (('0' <= rig.charAt(0) && rig.charAt(0) <= '9') || rig.charAt(0) == '-') {
            y = Integer.parseInt(rig);
        } else y = map.get(rig);

        if (op == "\\+") map.put(v, x + y);
        if (op == "\\-") map.put(v, x - y);
        if (op == "\\*") map.put(v, x * y);
        if (op == "\\/") map.put(v, x / y); // 0
        if (op == "MOD") map.put(v, x % y); //0

    }


    private int handlePrintBetter(String sub, String op) { // only computes expression for 1 equasion
        String[] div = sub.split(op); // splits up the incomming expression by the operation itself
        String lef = div[0].trim();
        String rig = div[1].trim();

        int x, y;

        if (('0' <= lef.charAt(0) && lef.charAt(0) <= '9') || lef.charAt(0) == '-') { // if the left side is a variable
            x = Integer.parseInt(lef);
        } else x = map.get(lef);

        if (('0' <= rig.charAt(0) && rig.charAt(0) <= '9') || rig.charAt(0) == '-') { // if the right side is a variable
            y = Integer.parseInt(rig);
        } else y = map.get(rig);

        if (op == "\\/" || op == "MOD") { // checks if we devide by 0
            if (y == 0) {
                System.out.println("ERROR! DIVISION OR MOD BY 0 NOT ALLOWED");
                System.exit(0);
            }
        }


        int ans = 0;

        if (op == "\\+") ans = x + y;
        if (op == "\\-") ans = x - y;
        if (op == "\\/") ans = x / y;
        if (op == "MOD") ans = x % y;
        if (op == "\\*") ans = x * y; // different cases for different outputs (what we are looking for)

        return ans;
    }


        private boolean handleIf(String line) {
            if(line.contains(">=")) {
                String[] par = line.split(">=");
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

                return x >= y;
            }
            else if(line.contains("<=")) {
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
            else if(line.contains("=")) {
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
            else if(line.contains("<>")) {
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
            else if(line.contains(">")) {
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
            else if(line.contains("<")) {
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
