DIM N AS INTEGER
DIM isPrime AS INTEGER
DIM i AS INTEGER

N = 13
isPrime = 1 

DIM j AS INTEGER

j = N - 1

FOR i = 2 TO j
    DIM x AS INTEGER
    x = N MOD i
    IF x = 0 THEN
        isPrime = 0
    END IF
NEXT i

DIM ans AS INTEGER

IF isPrime = 1 THEN
    ans = 1
END IF

PRINT ans
