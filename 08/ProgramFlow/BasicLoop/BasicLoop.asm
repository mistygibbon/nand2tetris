// init
@256
D=A
@SP
M=D
// call Sys.init
@Sys.init_RETURN0 // return address
D=A

@SP
M=M+1
A=M-1
M=D

// push LCL
@LCL
D=M

@SP
M=M+1
A=M-1
M=D

// push ARG
@ARG
D=M

@SP
M=M+1
A=M-1
M=D

// push THIS
@THIS
D=M

@SP
M=M+1
A=M-1
M=D

// push THAT
@THAT
D=M

@SP
M=M+1
A=M-1
M=D

// ARG = SP-n-5
@SP
D=M
@5
D=D-A
@0
D=D-A
@ARG
M=D

// LCL = SP
@SP
D=M
@LCL
M=D

// goto Sys.init
@Sys.init
0;JMP

(Sys.init_RETURN0)
// push constant 0
@0
D=A
@SP
M=M+1
A=M-1
M=D

// pop local 0
@SP
M=M-1
A=M
D=M
@R13
M=D
@LCL
D=M
@0
D=D+A
@R14
M=D
@R13
D=M
@R14
A=M
M=D

// label LOOP_START
(LOOP_START)

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

// push local 0
@LCL
D=M // Save address of LCL to D register
@0
A=D+A // Store address+index to address register
D=M // Store value of LCL[index] to D register
@SP
M=M+1
A=M-1
M=D

// add
@SP // A=0
M=M-1 // Move stack pointer up by 1 RAM[0]=RAM[0]-1
A=M // Set address to value in SP
D=M // Pop value to D register
A=A-1 // Set address to 1 less than stack pointer
M=D+M // Add values together

// pop local 0
@SP
M=M-1
A=M
D=M
@R13
M=D
@LCL
D=M
@0
D=D+A
@R14
M=D
@R13
D=M
@R14
A=M
M=D

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

// push constant 1
@1
D=A
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

// pop argument 0
@SP
M=M-1
A=M
D=M
@R13
M=D
@ARG
D=M
@0
D=D+A
@R14
M=D
@R13
D=M
@R14
A=M
M=D

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

// if-goto LOOP_START
@SP
M=M-1
A=M
D=M
@LOOP_START
D;JNE

// push local 0
@LCL
D=M // Save address of LCL to D register
@0
A=D+A // Store address+index to address register
D=M // Store value of LCL[index] to D register
@SP
M=M+1
A=M-1
M=D

// function Sys.init 0
(Sys.init)

// label Sys.init.LOOP
(Sys.init.LOOP)

// goto Sys.init.LOOP
@Sys.init.LOOP
0;JMP

