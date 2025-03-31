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

// call SimpleFunction.test
@SimpleFunction.test_RETURN1 // return address
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

// goto SimpleFunction.test
@SimpleFunction.test
0;JMP

(SimpleFunction.test_RETURN1)
// call SimpleFunction.test
@SimpleFunction.test_RETURN2 // return address
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

// goto SimpleFunction.test
@SimpleFunction.test
0;JMP

(SimpleFunction.test_RETURN2)
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

// function SimpleFunction.test 0
(SimpleFunction.test)

// push constant 1
@1
D=A
@SP
M=M+1
A=M-1
M=D

// push constant 2
@2
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

