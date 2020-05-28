# socket
PrintWrite、PrintStream、BufferedWriter
+ PrintStream只需要println即可
+ PrintWriter在println之后需要flush
+ BufferedWriter在write后需要newLine，最后flush

## 总结：在socket编程中尽量使用PrintStream或者PrintWriter
