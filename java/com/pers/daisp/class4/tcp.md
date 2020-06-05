## TCP

### TCP与UDP
+ 面向连接
+ 可靠的，基于字节流的传输层通信协议
+ 与UDP一样完成第四层传输层指定的功能和职责

### TCP机制
+ 三次握手、四次挥手
+ 具有校验机制、可靠、数据传输稳定


### TCP能做什么
+ 聊天消息传输、推送
+ 单人语音、视频聊天等
+ 几乎UDP能做的TCP都能做，但需要考虑复杂性、性能问题
+ 限制：无法广播、多播等操作
+ 进程间的数据传输


### 核心API
+ socket() 创建一个Socket客户端
+ bind() 绑定一个socket到一个本地地址和端口上
+ connect() 发起链接
+ accept() 接受一个新的连接，服务端特有
+ write() 数据写入到Socket输出流
+ read() 从Socket输入流中读取数据


### 连接可靠性-三次握手
+ 客户端发起连接，SYN x=rand()
+ 服务端响应，SYN ACK x+1,y=rand()
+ 客户端响应 ACK x+1,y+1


### 连接可靠性-四次挥手
+ 发送端发送 FIN=1,seq=u
+ 接收方接受后发送 ACK=1，seq=v,ack=u+1
+ 接收方继续发送 FIN=1，ACK=1,seq=w,ack=u+1
+ 发送方发送 ACK=1,seq=u+1,ack=w+1


### 传输可靠性
+ 排序、顺序发送、顺序组装
+ 丢弃、超时
+ 重发机制-定时器