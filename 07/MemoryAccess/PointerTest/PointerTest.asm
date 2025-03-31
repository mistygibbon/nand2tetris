// push constant
@3030
D=A

@SP
M=M+1
A=M-1
M=D
// pop pointer
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
// push constant
@3040
D=A

@SP
M=M+1
A=M-1
M=D
// pop pointer
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
// push constant
@32
D=A

@SP
M=M+1
A=M-1
M=D
// pop this
@SP
M=M-1
A=M
D=M

@R13
M=D

@THIS
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
// push constant
@46
D=A

@SP
M=M+1
A=M-1
M=D
// pop that
@SP
M=M-1
A=M
D=M

@R13
M=D

@THAT
D=M
@6
D=D+A
@R14
M=D

@R13
D=M

@R14
A=M

M=D
//push pointer
@3
D=A // D=3
@0
A=D+A // Store address+index to address register A=3+i
D=M // Store value of pointer[index] to D register

@SP
M=M+1
A=M-1
M=D
//push pointer
@3
D=A // D=3
@1
A=D+A // Store address+index to address register A=3+i
D=M // Store value of pointer[index] to D register

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
// push this
@THIS
D=M // Save address of THIS to D register
@2
A=D+A // Store address+index to address register
D=M // Store value of THIS[index] to D register

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
// push that
@THAT
D=M // Save address of THAT to D register
@6
A=D+A // Store address+index to address register
D=M // Store value of THAT[index] to D register

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
