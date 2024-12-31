DIM a AS INTEGER
DIM b AS INTEGER

a = 48
b = 18

WHILE b <> 0
    DIM temp AS INTEGER
    temp = b
    b = a MOD b
    a = temp
WEND

PRINT a
