package chip;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        String ch8Program = "roms/logo.ch8";
        //InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(ch8Program);
        InputStream inputStream = Main.class.getResourceAsStream(ch8Program);
        byte[] program = new byte[4096];
        int bytes_read = inputStream.read(program);

        //FileInputStream fileInputStream= new FileInputStream(ch8Program);
        //byte[] program = fileInputStream.readAllBytes();



        String hex = "";

        // Iterating through each byte in the array
        for (byte i : program) {
            hex += String.format("%02X", i);

        }

        //System.out.print(hex);

        //byte[] program = new byte[4096];
        //assert inputStream != null;
        //int bytes_read = inputStream.read(program);
        //File file = new File(".");
        //for(String fileNames : Objects.requireNonNull(file.list())) System.out.println(fileNames);
        //FileInputStream fileInputStream = new FileInputStream(ch8Program);

        Chip chip8 = new Chip();

        // Load program

        for (int i = 0; i < bytes_read; i++) {
            System.out.println(program[i]);
            chip8.memory[512 + i] = program[i];
        }

        System.out.println("Bytes read: " + bytes_read);

        for (int j = 0x200; j < bytes_read+0x200; j++) {
            chip8.tick();
        }
    }
}
