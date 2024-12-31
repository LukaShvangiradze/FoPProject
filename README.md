# Project: Building a Simple Interpreter for FreeBASIC language

In this capstone project, we will design and implement a simple interpreter 
for a variant of BASIC programming language, called FreeBASIC. The project will focus on creating and supporting 
a minimal subset of the language, with an emphasis on basic constructs like 
variables, arithmetic, conditionals, and iterative control flow. The interpreter 
will be capable of executing simple algorithms, serving as a proof of its functionality.

### The full code is written in Java and it is located in the file called InterpreterMain.java.

## Team members:

#### 1. Luka Shvangiradze : LukaShvangiradze ---> Role: Editor/Writer
#### 2. Archili Sanikidze : Archi-Lee-Sun ---> Role: Idea thinker
#### 3. Sandro Kapanadze : sandro140 ---> Role: Error corrector
#### 4. Saba Nemsitsveridze : Talgami ---> Role: Improver of functions

## Instructions on how to use the interpreter:

Run the main code in any Java compiler or IDE of your choice. After running, type the code you want to test in FreeBASIC language only, this is what the interpreter supports.
In order to stop receiving inputs, press CTRL+D or CTRL+Z, depending on the OS you are using, or if you run the code using a terminal, you can just accept the file directly.

Comment: We tested all FreeBASIC codes using onecompiler.com, so everything was double checked there as well.

Key things about this interpreter:
1) Out of the primitive and advanced data types, only Integers are supported, because of the required rules.
2) Because of the latter, for boolean outputs we used 1 for "true" and 0 for "false".
3) Division is supported only with the character '/', instead of '\' supported by onecompiler.
4) It is recommended that variable names shouldn't contain the names of the FreeBASIC keywords, like "DIM", "WHILE", "IF", and etc.
5) Expressions do not support more than two variables or literals. For example, 10+20-30 is not supported, to calculate the result first find 10+20 and then substitute 30 to 
the result. But things like i = j + k is definetely supported.
6) It is preferred to separate all the things with 1 space exactly, but when writing a negative number for example -10, don't write space between '-' and 10 for example
7) No nested loops. In ELSEIF keyword is supported and any number of them are executed by the code
8) Multiple statemens after if-else are supported, but after ELSE make sure to write the order to be executed below ELSE on the upcoming lines, for example:
ELSE PRINT 1 is not supported by our interpreter, instead write:
                                                    ELSE
                                                    PRINT 1
9) When using for loop, NEXT keyword should be followed by the name of the variable going through the loop, for example:
   FOR I = 1 TO 10
   PRINT I
   NEXT I
   
 ### 10) Everything else is perfect really. The interpreter correctly executes all of the 10 algorithms required for this project, examples of each of them
### are in .java files, all files are named according to the name 
### of the problem, the Algorithms file has all the info about the algorithms and they have been tested by us. Also, everything required per the rules, like variable assignment, arithmetic operations, and etc 
### is supported by this interpreter.
