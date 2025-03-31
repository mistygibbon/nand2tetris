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

(BEGIN)
@SCREEN
D = A // Load screen address to D register
@i
M = D // Set i to screen address

@KBD
D = M // Set D register to keyboard data
@BEGIN
D;JEQ // If D is equal to zero, jump to BEGIN
@BLACK
D;JGT // If D is greater tha  zero, which means something is read from the keyboard, jump to BLACK

(WHITE)
@i
A = M // Load screen address to A
M = 0 // Set screen at that memory loc to 0
@i
M = M + 1 // Increment screen address
D = M // Load incremented address to D register
@KBD
D = A - D // Set D to address of keyboard minus incremented address
@WHITE
D;JGT // Jump to WHITE if result greater than zero
@BEGIN
D;JEQ // Jump to begin if result equal to zero

(BLACK)
@i
A = M // Set A register to i variable, which should be address of screen
M = -1 // Set memory at A to -1, which is all 1 in 2's complement
@i
M = M + 1 // Increment 1 on address of the screen
D = M // Set D register to the incremented address
@KBD
D = A - D // Set D to address of keyboard minus incremented address
@BLACK
D;JGT // Jump back to BLACK if the result is greater than zero
@END
D;JEQ // Jump to END if result is equal to zero

(END)
@SCREEN
D = A // Load address of screen to D register
@i
M = D // Set i to address of screen 

@KBD
D = M // Read keyboard data
@WHITE
D;JEQ // Jump to white if no input
@END
D;JGT // Jump to end if keyboard has input