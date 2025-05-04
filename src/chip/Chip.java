package chip;

public class Chip {
    // Chip8 has 4 kb of memory (8 bit memory)
    // 4 kb = 4 * 1024 = 4096 bytes
    // all of chip8's memory is RAM and considered writable
    private byte[] memory = new byte[4096];

    // 64 * 32 resolution
    private byte[] display = new byte[2048];

    // program counter
    // points at current instruction in memory
    private byte pc;

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
        byte op1 = memory[Byte.toUnsignedInt(pc)];
        byte op2 = memory[Byte.toUnsignedInt(pc) + 1];

        pc += 2;

        // Shift the first op 8 left to put it first
        // OR them to put them together
        short opcode = (short) (Byte.toUnsignedInt(op1) << 8 | Byte.toUnsignedInt(op2));

        System.out.println("OPCODE: " + Integer.toHexString(Short.toUnsignedInt(opcode)));

        // return opcode
        return opcode;
    }

    public void run() {
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
            // second+third+fourth nibbles - 12-bit immediate mmeory address

            // execute opcode
                // in each case, do something
    }
}