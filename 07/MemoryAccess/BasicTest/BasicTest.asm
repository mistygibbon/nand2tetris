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
@21
D=A

@SP
M=M+1
A=M-1
M=D
// push constant
@22
D=A

@SP
M=M+1
A=M-1
M=D
// pop argument
@SP
M=M-1
A=M
D=M

@R13
M=D

@ARG
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
// pop argument
@SP
M=M-1
A=M
D=M

@R13
M=D

@ARG
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
// push constant
@36
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
@6
D=D+A
@R14
M=D

@R13
D=M

@R14
A=M

M=D
// push constant
@42
D=A

@SP
M=M+1
A=M-1
M=D
// push constant
@45
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
@5
D=D+A
@R14
M=D

@R13
D=M

@R14
A=M

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
@510
D=A

@SP
M=M+1
A=M-1
M=D
// pop temp
@SP
M=M-1
A=M
D=M

@R13
M=D

@5
D=A
@6
D=D+A
@R14
M=D

@R13
D=M

@R14
A=M

M=D
// push local
@LCL
D=M // Save address of LCL to D register
@0
A=D+A // Store address+index to address register
D=M // Store value of LCL[index] to D register

@SP
M=M+1
A=M-1
M=D
// push that
@THAT
D=M // Save address of THAT to D register
@5
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
// push argument
@ARG
D=M // Save address of ARG to D register
@1
A=D+A // Store address+index to address register
D=M // Store value of ARG[index] to D register

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
// push this
@THIS
D=M // Save address of THIS to D register
@6
A=D+A // Store address+index to address register
D=M // Store value of THIS[index] to D register

@SP
M=M+1
A=M-1
M=D
// push this
@THIS
D=M // Save address of THIS to D register
@6
A=D+A // Store address+index to address register
D=M // Store value of THIS[index] to D register

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
// sub
@SP // A=0
M=M-1 // Move stack pointer up by 1 RAM[0]=RAM[0]-1
A=M // Set address to value in SP
D=M // Pop value to D register
A=A-1 // Set address to 1 less than stack pointer
M=M-D // Subtract values together
// push temp
@5
D=A // D=5
@6
A=D+A // Store address+index to address register A=5+i
D=M // Store value of temp[index] to D register

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

