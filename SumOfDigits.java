DIM num AS INTEGER
DIM sum AS INTEGER
DIM remainder AS INTEGER

num = 1234
sum = 0

WHILE num > 0
    remainder = num MOD 10
    sum = sum + remainder
    num = num / 10
WEND

PRINT sum
