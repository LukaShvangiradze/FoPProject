DIM num AS INTEGER
DIM largestDigit AS INTEGER
DIM remainder AS INTEGER

num = 3947
largestDigit = 0

WHILE num > 0
    remainder = num MOD 10
    IF remainder > largestDigit THEN
        largestDigit = remainder
    END IF
    num = num / 10
WEND

PRINT largestDigit
