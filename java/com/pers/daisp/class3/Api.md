### DatagramSocket
+ DatagramSocket()
+ DatagramSocket(port)  端口为接收数据的端口
+ DatagramSocket(port, InetAddress) 

+ receive(DatagramPacket d)  接收
+ send(DatagramPacket d) 发送
+ setSoTimeout()
+ close()


### DatagramPacket
+ 用于处理报文
+ 将byte数组、目标地址、目标端口等数据包装成报文或者将报文拆卸成byte数组
+ 是UDP的发送实体，也是接收实体
+ 构造函数byte[] offset length address port
+ setData(byte[] buf, int offset, int length)
+ setAddress setPort setData setOffset set Length
+ get....


### 单播 广播 多播(组播)
