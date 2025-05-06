package chip;

import java.util.Arrays;

public class Chip {
    // Chip8 has 4 kb of memory (8-bit memory)
    // 4 kb = 4 * 1024 = 4096 bytes
    // all of chip8's memory is RAM and considered writable
    public byte[] memory = new byte[4096];

    // 64 * 32 resolution
    public boolean[][] display = new boolean[32][64];

    /*
    {
      {0000}
      {0000}
      {0000}
    }
     */

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
        //System.out.println("PC: " + pc);
        //System.out.println("Op1: " + op1);
        //System.out.println("Op2: " + op2);

        pc += 2;

        // Shift the first op 8 left to put it first
        // OR them to put them together
        short opcode = (short) (Byte.toUnsignedInt(op1) << 8 | Byte.toUnsignedInt(op2));

        //System.out.printf("Opcode: 0x%04X\n", opcode);

        // return opcode
        return opcode;
    }

    private void decode_and_execute( short opcode ) {

        // nibbles
        int n1 = (opcode & 0xF000) >> 12;
        int n2 = (opcode & 0x0F00) >> 8;
        int n3 = (opcode & 0x00F0) >> 4;
        int n4 = (opcode & 0x000F);

        int x = n2;
        int y = n3;
        int n = n4;
        int nn = (n3 << 4) + n4;
        int nnn = (n2 << 8) + (n3 << 4) + n4;

        System.out.format("OP: 0x%04X\t\t\tN1: 0x%01X\tN2: 0x%01X\tN3: 0x%01X\tN4: 0x%01X\n", opcode, n1, n2, n3, n4);
        System.out.format("\t\t\t\t\tX: 0x%01X\tY: 0x%01X\tN: 0x%01X\tNN: 0x%02X\tNNN: 0x%03X\n", x, y, n, nn, nnn);
        System.out.format("Before executing.\tPC: 0x%03X\tVX: 0x%01X\tVY: 0x%01X\tI: 0x%03X\n", pc, V[x], V[y], i);


        switch ((opcode & 0xF000) >> 12) {

            // 0...
            case (0x0):
                System.out.println("0...");
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
            case (0x1):
                // 1NNN
                // jump to nnn (12-bit immediate memory address)
                System.out.println("1NNN");
                pc = (short) nnn;
                break;

            // 2...
            case (0x2):
                break;

            // 3...
            case (0x3):
                break;

            // 4...
            case (0x4):
                break;

            // 5...
            case (0x5):
                break;

            // 6...
            case (0x6):
                // 6XNN
                System.out.println("6XNN");
                V[x] = (byte) (nn);
                break;

            // 7...
            case (0x7):
                // 7XNN
                System.out.println("7XNN");
                V[x] += (byte) (nn);
                break;

            // 8...
            case (0x8):
                break;

            // 9...
            case (0x9):
                break;

            // A...
            case(0xA):
                // ANNN
                System.out.println("ANNN");
                i = (short) nnn;
                break;

            // B...
            case(0xB):
                break;

            // C...
            case (0xC):
                break;

            // D...
            case (0xD):
                // DXYN
                System.out.println("DXYN");

                V[0xF] = 0;

                int regX = Byte.toUnsignedInt(V[x]);
                int regY = Byte.toUnsignedInt(V[y]);

                System.out.printf("Xpos: 0x%02X\tYpos: 0x%02X\n", regX, regY);

                byte height = (byte) n;
                byte xPos = (byte)(regX % 64);
                byte yPos = (byte)(regY % 32);

                for (int row = 0; row < height; row++) {
                    byte spriteByte = memory[i+row];
                    System.out.printf("0x%02X\t",spriteByte);
                    for (int col = 0; col < 8; col++) {
                        boolean spritePixel = (spriteByte & (0x80 >> col)) != 0;
                        boolean displayPixel = display[yPos+row][xPos+col];

                        if (spritePixel) {
                            if (displayPixel)
                                V[0xF] = 1;
                            display[yPos+row][xPos+col] = !displayPixel;
                        }
                    }
                }

                System.out.println();

                printDisplay();
                break;

            // E...
            case (0xE):
                break;

            // F...
            case (0xF):
                break;
        }

        System.out.format("After executing.\tPC: 0x%04X\tVX: 0x%01X\tVY: 0x%01X\tI: 0x%03X\n", pc, V[x], V[y], i);
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
        for (boolean[] row : display) {
            for (boolean col : row) {
                System.out.print(col ? "# " : ". ");
            }
            System.out.println();
        }
    }
}