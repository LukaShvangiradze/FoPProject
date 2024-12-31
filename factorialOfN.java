DIM N AS INTEGER
DIM factorial AS INTEGER
DIM i AS INTEGER

N = 5
factorial = 1
i = 1

WHILE i <= N
    factorial = factorial * i
    i = i + 1
WEND

PRINT factorial
