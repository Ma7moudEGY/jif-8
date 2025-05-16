package core;

public class Memory {

   protected byte[] RAM;
   protected byte[] fontSet;
   public static final int MEMORY_SIZE = 4096;
   private static final int ROM_START = 0x200;

   public Memory() {
      RAM = new byte[MEMORY_SIZE];
      fontSet = new byte[] {
            (byte) 0xF0, (byte) 0x90, (byte) 0x90, (byte) 0x90, (byte) 0xF0, // 0
            (byte) 0x20, (byte) 0x60, (byte) 0x20, (byte) 0x20, (byte) 0x70, // 1
            (byte) 0xF0, (byte) 0x10, (byte) 0xF0, (byte) 0x80, (byte) 0xF0, // 2
            (byte) 0xF0, (byte) 0x10, (byte) 0xF0, (byte) 0x10, (byte) 0xF0, // 3
            (byte) 0x90, (byte) 0x90, (byte) 0xF0, (byte) 0x10, (byte) 0x10, // 4
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x10, (byte) 0xF0, // 5
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x90, (byte) 0xF0, // 6
            (byte) 0xF0, (byte) 0x10, (byte) 0x20, (byte) 0x40, (byte) 0x40, // 7
            (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x90, (byte) 0xF0, // 8
            (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x10, (byte) 0xF0, // 9
            (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x90, (byte) 0x90, // A
            (byte) 0xE0, (byte) 0x90, (byte) 0xE0, (byte) 0x90, (byte) 0xE0, // B
            (byte) 0xF0, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0xF0, // C
            (byte) 0xE0, (byte) 0x90, (byte) 0x90, (byte) 0x90, (byte) 0xE0, // D
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x80, (byte) 0xF0, // E
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x80, (byte) 0x80 // F
      };
      System.out.println("Memory initialized");
      // Load font set during initialization
      loadFontSet();
   }

   public void reset() {
      for (int i = 0; i < RAM.length; i++) {
         RAM[i] = 0;
      }
      // Reload font set on reset as RAM is cleared
      loadFontSet();
   }

   public void loadFontSet() {
      if (0x050 + fontSet.length > MEMORY_SIZE) {
         throw new IllegalStateException("Font set too large or memory too small.");
      }
      System.arraycopy(fontSet, 0, RAM, 0x050, fontSet.length);
   }

   public byte get(int index) {
      return RAM[index];
   }

   public void loadROM(byte[] rom) {
      if (rom == null) {
         throw new IllegalArgumentException("ROM data cannot be null");
      }

      if (ROM_START + rom.length > MEMORY_SIZE) {
         throw new IllegalArgumentException("ROM is too large to fit in memory");
      }

      System.arraycopy(rom, 0, RAM, ROM_START, rom.length);
   }

   public byte read(int address) {
      if (address < 0 || address >= RAM.length) { // Added lower bound check for completeness
         throw new IllegalArgumentException("Memory address out of bounds: " + address);
      }
      if (address >= RAM.length) {
         throw new IllegalArgumentException("Memory address out of bounds");
      }
      return (byte) (RAM[address] & 0xFF);
   }

   public void write(int address, int value) {
      if (address < 0 || address >= RAM.length) { // Added lower bound check for completeness
         throw new IllegalArgumentException("Memory address out of bounds: " + address);
      }
      if (address >= RAM.length) {
         throw new IllegalArgumentException("Memory address out of bounds");
      }
      RAM[address] = (byte) value;
   }

   public byte[] dump(int start, int length) {
      byte[] segment = new byte[length];
      for (int i = 0; i < length; i++) {
         segment[i] = RAM[start + i];
      }
      return segment;
   }


   public static int getMemorySize() {
       return MEMORY_SIZE;
   }
}
