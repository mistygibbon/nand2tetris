// push constant
@111
D=A

@SP
M=M+1
A=M-1
M=D
// push constant
@333
D=A

@SP
M=M+1
A=M-1
M=D
// push constant
@888
D=A

@SP
M=M+1
A=M-1
M=D
// pop static
@SP
M=M-1
A=M
D=M

@test.8
M=D
// pop static
@SP
M=M-1
A=M
D=M

@test.3
M=D
// pop static
@SP
M=M-1
A=M
D=M

@test.1
M=D
// push static
@test.3
D=M

@SP
M=M+1
A=M-1
M=D
// push static
@test.1
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
// push static
@test.8
D=M

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
