DIM num AS INTEGER
DIM reversed AS INTEGER
DIM remainder AS INTEGER

num = 1234
reversed = 0

WHILE num > 0
    remainder = num MOD 10
    reversed = reversed * 10 
    reversed = reversed + remainder
    num = num / 10
WEND

PRINT reversed
