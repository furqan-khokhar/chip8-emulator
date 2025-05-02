# Notes

## Bitwise Operations

<p>
**0x** - Notation for hexadecimal (e.g., 0xFF)
A single hex digit can be represented by a nibble in binary.

In Java, numbers are all treated as signed.
However, for the purpose of the chip8 emulator, unsigned numbers are required.
Also, chip8 emulators traditionally use various data types to store data.

There are a number of potential solutions:

- The method *.toUnsignedInt* can be used to preserve their original unsigned values whilst converting.

- Another solution would be to exclusively use ints, which would simplify conversions. However, explicitly converting between types seems helpful in code readability.

- A less clean, though simple, solution would be to use more bits than otherwise necessary, and mask out the sign bit.

The third solution - using a larger type and masking down - is what I will be using.

</p>

### Type ranges:

<p>
- Byte (0x00 - 0xFF)
- Short (0x0000, 0xFFFF)
- Integer (0x00000000, 0xFFFFFFFF)
- Long (0x0000000000000000 - 0xFFFFFFFFFFFFFFFF)
</p>

### Logic Operations

<p>
Unary operations use a single operand.
Binary operations use two operands.

NOT is unary.
AND, OR, and XOR are binary.

</p>

#### AND (&&)

<p>
A | B | Q
0 | 0 | 0
0 | 1 | 0
1 | 0 | 0
1 | 1 | 1
</p>

#### OR (||)

<p>
A | B | Q
0 | 0 | 0
0 | 1 | 1
1 | 0 | 1
1 | 1 | 1
</p>

#### XOR (^)

<p>
A | B | Q
0 | 0 | 0
0 | 1 | 1
1 | 0 | 1
1 | 1 | 0
</p>

#### NOT (~)

<p>
A | Q
0 | 1
1 | 0
</p>