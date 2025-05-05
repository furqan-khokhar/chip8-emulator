package chip;

import java.util.Arrays;

public class Chip {
    // Chip8 has 4 kb of memory (8-bit memory)
    // 4 kb = 4 * 1024 = 4096 bytes
    // all of chip8's memory is RAM and considered writable
    public byte[] memory = new byte[4096];

    // 64 * 32 resolution
    private boolean[][] display = new boolean[64][32];

    // program counter
    // points at current instruction in memory
    private short pc = (short) Integer.parseInt("512");

    // index register
    // 16 bits
    // can only address 12 bits
    // points at locations in memory
    private short i = 0x0;

    // calls subroutines and returns them
    // 16-bit addressing
    private short[] stack = new short[16];
    // points to next free slot in the stack
    private int stackpointer = 0;

    // delays events
    private int delay_timer = 0;
    // makes sound until decremented to 0
    private int sound_timer = 0;

    // 16 general purpose 8-bit variable registers
    // VF is also used as a flag register
    private byte[] V = new byte [16];

    // 16 keys
    // 1 2 3 4
    // Q W E R
    // A S D F
    // Z X C V
    private byte[] keys = new byte[16];

    // fonts
    // each character is 4x5 (4 wide, 5 tall)
    // stored in memory at 050 - 09F
    public static int[] fontdata =
            {
                    0xF0, 0x90, 0x90, 0x90, 0xF0, // 0
                    0x20, 0x60, 0x20, 0x20, 0x70, // 1
                    0xF0, 0x10, 0xF0, 0x80, 0xF0, // 2
                    0xF0, 0x10, 0xF0, 0x10, 0xF0, // 3
                    0x90, 0x90, 0xF0, 0x10, 0x10, // 4
                    0xF0, 0x80, 0xF0, 0x10, 0xF0, // 5
                    0xF0, 0x80, 0xF0, 0x90, 0xF0, // 6
                    0xF0, 0x10, 0x20, 0x40, 0x40, // 7
                    0xF0, 0x90, 0xF0, 0x90, 0xF0, // 8
                    0xF0, 0x90, 0xF0, 0x10, 0xF0, // 9
                    0xF0, 0x90, 0xF0, 0x90, 0x90, // A
                    0xE0, 0x90, 0xE0, 0x90, 0xE0, // B
                    0xF0, 0x80, 0x80, 0x80, 0xF0, // C
                    0xE0, 0x90, 0x90, 0x90, 0xE0, // D
                    0xF0, 0x80, 0xF0, 0x80, 0xF0, // E
                    0xF0, 0x80, 0xF0, 0x80, 0x80  // F
            };

    // true when the screen needs to be redrawn (because it's been modified)
    // otherwise, false
    // as such, it is technically "display modified (since the previous redraw)"
    private boolean displayModified;

    private short fetch() {
        // fetch opcode
        byte op1 = memory[Short.toUnsignedInt(pc)];
        byte op2 = memory[Short.toUnsignedInt(pc) + 1];

        //System.out.printf("0x%02X\n", pc);
        System.out.println("Program counter: " + pc);
        System.out.println("Op1: " + op1);
        System.out.println("Op2: " + op2);

        pc += 2;

        // Shift the first op 8 left to put it first
        // OR them to put them together
        short opcode = (short) (Byte.toUnsignedInt(op1) << 8 | Byte.toUnsignedInt(op2));

        System.out.printf("Opcode: 0x%04X\n", opcode);

        // return opcode
        return opcode;
    }

    private void decode_and_execute( short opcode ) {

        // nibbles
        int n1 = opcode & 0xF000;
        int n2 = opcode & 0x0F00;
        int n3 = opcode & 0x00F0;
        int n4 = opcode & 0x000F;

        /*
        int x = n2;
        int y = n3;
        int n = n4;
        int nn = (n3 << 4) + n4;
        int nnn = (n2 << 8) + (n3 << 4) + n4;
         */

        switch (opcode & 0xF000) {

            // 0...
            case (0x0000):
                // 00E0
                if (opcode == 0x00E0) {
                    // clear screen
                    System.out.println("00E0");
                    for (boolean[] row : display) {
                        Arrays.fill(row, false);
                    }
                }
                break;

            // 1...
            case (0x1000):
                // 1NNN
                // jump to nnn (12-bit immediate memory address)
                System.out.println("1NNN");
                pc = (byte) (opcode & 0x0FFF);
                break;

            // 2...
            case (0x2000):
                break;

            // 3...
            case (0x3000):
                break;

            // 4...
            case (0x4000):
                break;

            // 5...
            case (0x5000):
                break;

            // 6...
            case (0x6000):
                // 6XNN
                System.out.println("6XNN");
                V[(opcode & 0x0F00) >> 8] = (byte) (opcode & 0x00FF);
                break;

            // 7...
            case (0x7000):
                // 7XNN
                System.out.println("7XNN");
                V[opcode & 0x0F00] += (byte) (opcode & 0X00FF);
                break;

            // 8...
            case (0x8000):
                break;

            // 9...
            case (0x9000):
                break;

            // A...
            case(0xA000):
                // ANNN
                System.out.println("ANNN");
                i = (short) (opcode & 0x0FFF);
                break;

            // B...
            case(0xB000):
                break;

            // C...
            case (0xC000):
                break;

            // D...
            case (0xD000):
                // DXYN
                System.out.println("DXYN");
                int x = V[(opcode & 0x0F00) >> 8] & 0x3F;
                int y = V[(opcode & 0x00F0) >> 4] & 0x1F;

                V[0xF] = 0;

                for (int row = 0; row >= (opcode & 0x000F); row++) {
                    byte spriteByte = memory[i + row];
                    for (int col = 0; col < 8; col++) {
                        boolean spriteBit = (spriteByte & 0x80 >> col) != 0;
                        boolean displayPixel = display[x][y];

                        if (spriteBit) {
                            if (displayPixel) {
                                V[0xF] = 1;
                            }
                            display[y + row][x + col] = !displayPixel;
                        }
                        x++;
                    }
                    y++;
                }

                printDisplay();
                break;

            // E...
            case (0xE000):
                break;

            // F...
            case (0xF000):
                break;
        }

    }

    public void tick() {
        // ~700 instructions per second is usually good
        // should be able to configure this though

        // fetch opcode
            // read instruction
                // read next 2 bytes being pointed at in memory
                // combine these 2 bytes into a short
                // increment pc by 2
        short opcode = fetch();

        // decode opcode
            // mask with &0F
            // one case per number
            // some cases will need several additional switch statements to fully decode

            // first nibble - type of instruction
            // second nibble - looks up a register in V (V0-VF)
            // third nibble - looks up another register in V (V0-VF)
            // fourth nibble - 4-bit number
            // third+fourth nibbles - 8-bit immediate number
            // second+third+fourth nibbles - 12-bit immediate memory address

            // execute opcode
                // in each case, do something
        decode_and_execute(opcode);

        if (delay_timer > 0) {
            delay_timer--;
        }
        if (sound_timer > 0) {
            sound_timer--;
        }
    }

    private void printDisplay() {
        System.out.println("Display: ");
        for (boolean[] row : display) {
            for (boolean col : row) {
                System.out.print(col ? "#" : ".");
            }
            System.out.println();
        }
    }
}