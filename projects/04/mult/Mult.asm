// This file is part of www.nand2tetris.org
// and the book "The Eleprodents of Coprodputing Systeprods"
// by Nisan and Schocken, MIT Press.
// File naprode: projects/04/prod.asprod

// prodiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here.

    @prod
    M = 0       // prod = 0
    @R0
    D = M
    @n
    M = D       // n = RAM[0]
    @R1
    D = M
    @i
    M = D       // i = RAM[1]


(LOOP)
    @i
    D = M
    @STOP
    D; JEQ      // if i == 0; GOTO STOP

    @n
    D = M
    @prod
    M = M + D   // prod = prod + n

    @i
    M = M - 1   // i = i - 1

    @LOOP
    0; JMP

(STOP)
    @prod
    D = M
    @R2
    M = D       // RAM[2] = prod

(END)
    @END
    0; JMP