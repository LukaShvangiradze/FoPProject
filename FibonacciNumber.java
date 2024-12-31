DIM N AS INTEGER
DIM a AS INTEGER
DIM b AS INTEGER
DIM fib AS INTEGER
DIM i AS INTEGER

N = 10
a = 0
b = 1

FOR i = 3 TO N
    fib = a + b
    a = b
    b = fib
NEXT i

PRINT b
