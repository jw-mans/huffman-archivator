# Huffman Archivator

**Console application** for encoding and decoding files using the **Huffman algorithm**.

---

## Program Launch

1. Open the project in **VS Code** with the **Java Extension Pack** installed.
2. Compile the main Java file by running:
   ```bash
   javac App.java
3) Run the program with the following command, providing three arguments:
    ```bash 
    java App [source_filename.txt] [compressed_filename] [decompressed_filename] [tag]
`[source_filename.txt]` — the text file to encode.  
`[compressed_filename.bin]` — the file where the compressed data will be saved.  
`[decompressed_filename.txt]` — the file where the decompressed data will be saved.
`[tag]` — logger tag (with '_' delimiters).

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