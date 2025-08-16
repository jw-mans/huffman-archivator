# Huffman Archivator

**Console application** for encoding and decoding files using the **Huffman algorithm**.

---

## Program Launch

1. Open the project in **VS Code** with the **Java Extension Pack** installed.

2. Compile the main Java file by running:
   ```bash
   javac Main.java  
3) Start the program with the following command:
    ```bash
    java Main start  
4) Select the operating mode by entering the keywords:  
    - `compress` : file compression mode;
    - `decompress` : file decompression mode;  
    - another string to exit program.

5) Run the process by typing following command:
    ```text 
    [source file (without spaces)] [target file (without spaces)] [logging tag]  
---
## Code File Structure
The compressed file consists of the following parts:

1) **Number of unique symbols**
An integer representing how many unique characters are encoded.

2) **Symbols and their codes**
For each unique symbol, the following information is stored:

    - The symbol itself

    - The length of its Huffman code

    - The Huffman code (binary sequence)

3) **Padding information**  
The number of padding bits added at the end to complete the last byte.

4) **Encoded data bytes**  
The actual compressed data stream, byte by byte (with possible padding in the last byte).