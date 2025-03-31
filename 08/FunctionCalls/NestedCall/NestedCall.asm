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
// function Sys.init 0
(Sys.init)

// push constant 4000
@4000
D=A
@SP
M=M+1
A=M-1
M=D

// pop pointer 0
@SP
M=M-1
A=M
D=M
@R13
M=D
@3
D=A
@0
D=D+A
@R14
M=D
@R13
D=M
@R14
A=M
M=D

// push constant 5000
@5000
D=A
@SP
M=M+1
A=M-1
M=D

// pop pointer 1
@SP
M=M-1
A=M
D=M
@R13
M=D
@3
D=A
@1
D=D+A
@R14
M=D
@R13
D=M
@R14
A=M
M=D

// call Sys.main
@Sys.main_RETURN1 // return address
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

// goto Sys.main
@Sys.main
0;JMP

(Sys.main_RETURN1)
// pop temp 1
@SP
M=M-1
A=M
D=M
@R13
M=D
@5
D=A
@1
D=D+A
@R14
M=D
@R13
D=M
@R14
A=M
M=D

// label Sys.init$LOOP
(Sys.init$LOOP)

// goto Sys.init$LOOP
@Sys.init$LOOP
0;JMP

// function Sys.main 5
(Sys.main)
// push constant 0
@0
D=A

@SP
M=M+1
A=M-1
M=D
// push constant 0
@0
D=A

@SP
M=M+1
A=M-1
M=D
// push constant 0
@0
D=A

@SP
M=M+1
A=M-1
M=D
// push constant 0
@0
D=A

@SP
M=M+1
A=M-1
M=D
// push constant 0
@0
D=A

@SP
M=M+1
A=M-1
M=D

// push constant 4001
@4001
D=A
@SP
M=M+1
A=M-1
M=D

// pop pointer 0
@SP
M=M-1
A=M
D=M
@R13
M=D
@3
D=A
@0
D=D+A
@R14
M=D
@R13
D=M
@R14
A=M
M=D

// push constant 5001
@5001
D=A
@SP
M=M+1
A=M-1
M=D

// pop pointer 1
@SP
M=M-1
A=M
D=M
@R13
M=D
@3
D=A
@1
D=D+A
@R14
M=D
@R13
D=M
@R14
A=M
M=D

// push constant 200
@200
D=A
@SP
M=M+1
A=M-1
M=D

// pop local 1
@SP
M=M-1
A=M
D=M
@R13
M=D
@LCL
D=M
@1
D=D+A
@R14
M=D
@R13
D=M
@R14
A=M
M=D

// push constant 40
@40
D=A
@SP
M=M+1
A=M-1
M=D

// pop local 2
@SP
M=M-1
A=M
D=M
@R13
M=D
@LCL
D=M
@2
D=D+A
@R14
M=D
@R13
D=M
@R14
A=M
M=D

// push constant 6
@6
D=A
@SP
M=M+1
A=M-1
M=D

// pop local 3
@SP
M=M-1
A=M
D=M
@R13
M=D
@LCL
D=M
@3
D=D+A
@R14
M=D
@R13
D=M
@R14
A=M
M=D

// push constant 123
@123
D=A
@SP
M=M+1
A=M-1
M=D

// call Sys.add12
@Sys.add12_RETURN2 // return address
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
@1
D=D-A
@ARG
M=D

// LCL = SP
@SP
D=M
@LCL
M=D

// goto Sys.add12
@Sys.add12
0;JMP

(Sys.add12_RETURN2)
// pop temp 0
@SP
M=M-1
A=M
D=M
@R13
M=D
@5
D=A
@0
D=D+A
@R14
M=D
@R13
D=M
@R14
A=M
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

// push local 1
@LCL
D=M // Save address of LCL to D register
@1
A=D+A // Store address+index to address register
D=M // Store value of LCL[index] to D register
@SP
M=M+1
A=M-1
M=D

// push local 2
@LCL
D=M // Save address of LCL to D register
@2
A=D+A // Store address+index to address register
D=M // Store value of LCL[index] to D register
@SP
M=M+1
A=M-1
M=D

// push local 3
@LCL
D=M // Save address of LCL to D register
@3
A=D+A // Store address+index to address register
D=M // Store value of LCL[index] to D register
@SP
M=M+1
A=M-1
M=D

// push local 4
@LCL
D=M // Save address of LCL to D register
@4
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

// add
@SP // A=0
M=M-1 // Move stack pointer up by 1 RAM[0]=RAM[0]-1
A=M // Set address to value in SP
D=M // Pop value to D register
A=A-1 // Set address to 1 less than stack pointer
M=D+M // Add values together

// add
@SP // A=0
M=M-1 // Move stack pointer up by 1 RAM[0]=RAM[0]-1
A=M // Set address to value in SP
D=M // Pop value to D register
A=A-1 // Set address to 1 less than stack pointer
M=D+M // Add values together

// add
@SP // A=0
M=M-1 // Move stack pointer up by 1 RAM[0]=RAM[0]-1
A=M // Set address to value in SP
D=M // Pop value to D register
A=A-1 // Set address to 1 less than stack pointer
M=D+M // Add values together

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

// function Sys.add12 0
(Sys.add12)

// push constant 4002
@4002
D=A
@SP
M=M+1
A=M-1
M=D

// pop pointer 0
@SP
M=M-1
A=M
D=M
@R13
M=D
@3
D=A
@0
D=D+A
@R14
M=D
@R13
D=M
@R14
A=M
M=D

// push constant 5002
@5002
D=A
@SP
M=M+1
A=M-1
M=D

// pop pointer 1
@SP
M=M-1
A=M
D=M
@R13
M=D
@3
D=A
@1
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

// push constant 12
@12
D=A
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

