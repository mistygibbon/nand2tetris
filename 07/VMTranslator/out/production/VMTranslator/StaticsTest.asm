// function Class1.set 0
(Class1.set)

// push argument 0
@ARG
D=M // Save address of ARG to D register
@0
A=D+A // Store address+index to address register
D=M // Store value of ARG[index] to D register
@SP
M=M+1
A=M-1
M=D

// pop static 0
@SP
M=M-1
A=M
D=M
@../../../../../08/FunctionCalls/StaticsTest/Class1.0
M=D

// push argument 1
@ARG
D=M // Save address of ARG to D register
@1
A=D+A // Store address+index to address register
D=M // Store value of ARG[index] to D register
@SP
M=M+1
A=M-1
M=D

// pop static 1
@SP
M=M-1
A=M
D=M
@../../../../../08/FunctionCalls/StaticsTest/Class1.1
M=D

// push constant 0
@0
D=A
@SP
M=M+1
A=M-1
M=D

// return
// FRAME=LCL
@LCL
D=M

@R13
M=D

// RET = *(FRAME-5)
@5
D=D-A
A=D
D=M
@R14
M=D

// *ARG = pop()
@SP
M=M-1
A=M
D=M

@ARG
A=M
M=D

// SP = (ARG+1)
@ARG
D=M
D=D+1
@SP
M=D

// THAT = *(FRAME-1)
@R13
D=M
A=D-1
D=M
@THAT
M=D

// THIS = *(FRAME-2)
@R13
D=M
@2
A=D-A
D=M
@THIS
M=D

// ARG = *(FRAME-3)
@R13
D=M
@3
A=D-A
D=M
@ARG
M=D

// LCL = *(FRAME-4)
@R13
D=M
@4
A=D-A
D=M
@LCL
M=D

// goto RET
@R14
A=M
0;JMP

// function Class1.get 0
(Class1.get)

// push static 0
@../../../../../08/FunctionCalls/StaticsTest/Class1.0
D=M
@SP
M=M+1
A=M-1
M=D

// push static 1
@../../../../../08/FunctionCalls/StaticsTest/Class1.1
D=M
@SP
M=M+1
A=M-1
M=D

// sub
@SP // A=0
M=M-1 // Move stack pointer up by 1 RAM[0]=RAM[0]-1
A=M // Set address to value in SP
D=M // Pop value to D register
A=A-1 // Set address to 1 less than stack pointer
M=M-D // Subtract values together

// return
// FRAME=LCL
@LCL
D=M

@R13
M=D

// RET = *(FRAME-5)
@5
D=D-A
A=D
D=M
@R14
M=D

// *ARG = pop()
@SP
M=M-1
A=M
D=M

@ARG
A=M
M=D

// SP = (ARG+1)
@ARG
D=M
D=D+1
@SP
M=D

// THAT = *(FRAME-1)
@R13
D=M
A=D-1
D=M
@THAT
M=D

// THIS = *(FRAME-2)
@R13
D=M
@2
A=D-A
D=M
@THIS
M=D

// ARG = *(FRAME-3)
@R13
D=M
@3
A=D-A
D=M
@ARG
M=D

// LCL = *(FRAME-4)
@R13
D=M
@4
A=D-A
D=M
@LCL
M=D

// goto RET
@R14
A=M
0;JMP

