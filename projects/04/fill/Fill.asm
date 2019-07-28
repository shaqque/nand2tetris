// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

(INF)
    @KBD
    D = M
    @FILL
    D; JNE          // if KBD != 0; GOTO: FILL
    @CLEAR
    0; JMP          // else GOTO: CLEAR

    @INF
    0; JMP          // GOTO: INF

(FILL)
    @8192
    D = A
    @i
    M = D           // i = 8191

    (LOOP1)
        @i
        D = M
        @INF
        D; JLT      // if i < 0; GOTO: CLEAR

        @i
        D = M
        @SCREEN
        A = A + D   // addr = screen + i
        M = -1      // RAM[addr] = -1
        @i
        M = M - 1   // i = i - 1

        @LOOP1
        0; JMP      // GOTO: LOOP1

(CLEAR)
    @8192
    D = A
    @i
    M = D           // i = 8191

    (LOOP2)
        @i
        D = M
        @INF
        D; JLT      // if i < 0; GOTO: INF

        @i
        D = M
        @SCREEN
        A = A + D   // addr = screen + i
        M = 0       // RAM[addr] = 0
        @i
        M = M - 1   // i = i - 1

        @LOOP2
        0; JMP      // GOTO: LOOP2
