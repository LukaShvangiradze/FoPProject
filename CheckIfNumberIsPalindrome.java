DIM num AS INTEGER
DIM reversed AS INTEGER
DIM original AS INTEGER
DIM remainder AS INTEGER

num = 121
original = num
reversed = 0

WHILE num > 0
    remainder = num MOD 10
    reversed = reversed * 10 
    reversed = reversed + remainder
    num = num / 10
WEND

DIM ans AS INTEGER

IF original = reversed THEN
    ans = 1
END IF

PRINT ans
