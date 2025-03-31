// push constant
@17
D=A

@SP
M=M+1
A=M-1
M=D
// push constant
@17
D=A

@SP
M=M+1
A=M-1
M=D
// eq
@SP // A=0
M=M-1 // Move stack pointer up by 1 RAM[0]=RAM[0]-1
A=M // Set address to value in SP
D=M // Pop value to D register
A=A-1 // Set address to 1 less than stack pointer
D=M-D

@EQ_TRUE_0
D;JEQ

@SP
A=M-1
M=0
@EQ_END_0
0;JMP

(EQ_TRUE_0)
@SP
A=M-1
M=-1
(EQ_END_0)
// push constant
@17
D=A

@SP
M=M+1
A=M-1
M=D
// push constant
@16
D=A

@SP
M=M+1
A=M-1
M=D
// eq
@SP // A=0
M=M-1 // Move stack pointer up by 1 RAM[0]=RAM[0]-1
A=M // Set address to value in SP
D=M // Pop value to D register
A=A-1 // Set address to 1 less than stack pointer
D=M-D

@EQ_TRUE_1
D;JEQ

@SP
A=M-1
M=0
@EQ_END_1
0;JMP

(EQ_TRUE_1)
@SP
A=M-1
M=-1
(EQ_END_1)
// push constant
@16
D=A

@SP
M=M+1
A=M-1
M=D
// push constant
@17
D=A

@SP
M=M+1
A=M-1
M=D
// eq
@SP // A=0
M=M-1 // Move stack pointer up by 1 RAM[0]=RAM[0]-1
A=M // Set address to value in SP
D=M // Pop value to D register
A=A-1 // Set address to 1 less than stack pointer
D=M-D

@EQ_TRUE_2
D;JEQ

@SP
A=M-1
M=0
@EQ_END_2
0;JMP

(EQ_TRUE_2)
@SP
A=M-1
M=-1
(EQ_END_2)
// push constant
@892
D=A

@SP
M=M+1
A=M-1
M=D
// push constant
@891
D=A

@SP
M=M+1
A=M-1
M=D
// lt
@SP
M=M-1
A=M
D=M
A=A-1
D=M-D

@LT_TRUE_0
D;JLT

@SP
A=M-1
M=0
@LT_END_0
0;JMP

(LT_TRUE_0)
@SP
A=M-1
M=-1
(LT_END_0)
// push constant
@891
D=A

@SP
M=M+1
A=M-1
M=D
// push constant
@892
D=A

@SP
M=M+1
A=M-1
M=D
// lt
@SP
M=M-1
A=M
D=M
A=A-1
D=M-D

@LT_TRUE_1
D;JLT

@SP
A=M-1
M=0
@LT_END_1
0;JMP

(LT_TRUE_1)
@SP
A=M-1
M=-1
(LT_END_1)
// push constant
@891
D=A

@SP
M=M+1
A=M-1
M=D
// push constant
@891
D=A

@SP
M=M+1
A=M-1
M=D
// lt
@SP
M=M-1
A=M
D=M
A=A-1
D=M-D

@LT_TRUE_2
D;JLT

@SP
A=M-1
M=0
@LT_END_2
0;JMP

(LT_TRUE_2)
@SP
A=M-1
M=-1
(LT_END_2)
// push constant
@32767
D=A

@SP
M=M+1
A=M-1
M=D
// push constant
@32766
D=A

@SP
M=M+1
A=M-1
M=D
// gt
@SP
M=M-1
A=M
D=M
A=A-1
D=M-D

@GT_TRUE_0
D;JGT

@SP
A=M-1
M=0
@GT_END_0
0;JMP

(GT_TRUE_0)
@SP
A=M-1
M=-1
(GT_END_0)
// push constant
@32766
D=A

@SP
M=M+1
A=M-1
M=D
// push constant
@32767
D=A

@SP
M=M+1
A=M-1
M=D
// gt
@SP
M=M-1
A=M
D=M
A=A-1
D=M-D

@GT_TRUE_1
D;JGT

@SP
A=M-1
M=0
@GT_END_1
0;JMP

(GT_TRUE_1)
@SP
A=M-1
M=-1
(GT_END_1)
// push constant
@32766
D=A

@SP
M=M+1
A=M-1
M=D
// push constant
@32766
D=A

@SP
M=M+1
A=M-1
M=D
// gt
@SP
M=M-1
A=M
D=M
A=A-1
D=M-D

@GT_TRUE_2
D;JGT

@SP
A=M-1
M=0
@GT_END_2
0;JMP

(GT_TRUE_2)
@SP
A=M-1
M=-1
(GT_END_2)
// push constant
@57
D=A

@SP
M=M+1
A=M-1
M=D
// push constant
@31
D=A

@SP
M=M+1
A=M-1
M=D
// push constant
@53
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
// push constant
@112
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
// neg
@SP
A=M-1
M=!M
M=M+1
// and
@SP
M=M-1
A=M
D=M
A=A-1
M=D&M
// push constant
@82
D=A

@SP
M=M+1
A=M-1
M=D
// or
@SP
M=M-1
A=M
D=M
A=A-1
M=D|M
// not
@SP
A=M-1
M=!M
