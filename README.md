# huffman-archivator
The encoding-decoding files application based on Huffman algorithm.

# Program launch

1) Open in VS Code with Java Extension Pack
2) In terminal enter "javac App.java"
3) In terminal enter "javac App input.txt encoded.bin"

# Code file structure:
1) [0 byte] : padding info (number of padding bits) 
2) [1st byte] : code bits
3) [2nd byte] : code bits
...
The last byte may have padding bits. 
