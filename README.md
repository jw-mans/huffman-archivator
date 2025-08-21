# Huffman Archivator

**GUI application** for encoding and decoding files using the **Huffman algorithm**.  
Now distributed as a ready-to-use **Windows executable (.exe)**.

---

## Program Launch

1. Double-click `HuffmanArchivator.exe` to start the program.  

2. Use the GUI window to:  
   - Select **Compress** to encode a file (`input.txt` → `input.txt.huf`).  
   - Select **Decompress** to decode a file (`input.txt.huf` → `input.txt`).  

**File restrictions:**
- Files ending with `.huf` **cannot be compressed**.  
- Only files ending with `.huf` **can be decompressed**.  
- Other files will be rejected with an error message.  

---

## File Format

The `.huf` file consists of:

1) **Number of unique symbols** – integer count of unique characters.  
2) **Symbols and their codes** – for each symbol: symbol, length of Huffman code, and code itself.  
3) **Padding information** – number of padding bits at the end.  
4) **Encoded data bytes** – compressed data stream (with possible padding).  

---

## License

Distributed under the MIT License. See LICENSE.txt for details.

---