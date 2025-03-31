// push constant
@10
D=A

@SP
M=M+1
A=M-1
M=D
// pop local
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

