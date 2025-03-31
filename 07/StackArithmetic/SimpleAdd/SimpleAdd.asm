// push constant
@7
D=A

@SP
M=M+1
A=M-1
M=D
// push constant
@8
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
