# miniRPC

本项目基于[he2121/MyRPCFromZero: 从零开始，手写一个RPC，任何人都能看懂](https://github.com/he2121/MyRPCFromZero)的基础上进行学习和编写，各版本体现为github的分支。新增了流程图，部分功能为参考各博客后改进。

参考如下：

+ [zookeeper之注册中心_Lucifer Zhao的博客-CSDN博客_zookeeper注册中心](https://blog.csdn.net/luciferlongxu/article/details/126529104?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0-126529104-blog-124920703.pc_relevant_multi_platform_whitelistv3&spm=1001.2101.3001.4242.1&utm_relevant_index=3)
+ [Netty 全网最详细的教程! 没有之一!_Beis的博客-CSDN博客_netty教程](https://blog.csdn.net/weixin_45722941/article/details/120219166)

+ [什么是负载均衡，看完文章秒懂_爱铭网络的博客-CSDN博客_负载均衡](https://blog.csdn.net/wanghangzhen/article/details/118554304)

## 基本概念

1. RPC（remote procedure call）即远程过程调用，一般是使用在分布式的微服务中，不同的微服务只对外暴露接口，而rpc则是负责传输文件，返回调用得到的数据/对象。
2. rpc可以理解为一种网络传输的针对服务端之间调用的具体实现，可以基于HTTP也可以基于TCP协议来实现，http是以head加body为报文，在head中会有非常多的参数来声明当前连接的一些状态，而rpc为了尽量提高服务端之间调用的性能，可以用tcp来实现，而舍弃掉繁复的数据头。
3. rpc调用时为了返回数据，需要实现序列化，简易流程如下

![image-20221104111040857](C:\Users\Liberal Wu\Documents\MarkDown\Work\image-20221104111040857.png)

## Version_0

最简易版本，实现服务端监听客户端，调用方法返回数据

实现思路：客户端用socket发给服务端userId，服务端用一个线程一直监听端口，拿到id调用方法得到根据id创建的对象，序列化后传输给客户端。

## Version_1

对发送的requset和response进行封装，封装底层通信，让客户端使用动态代理在调用远程方法的时候同步调用通信方法。

服务端使用反射，通过发送的request解析接口、方法名。调用后返回数据。

## Version_2

新增服务提供类，用hashmap来存储全称类名，更改服务端，使用线程池来实现监听客户端的信息，抽象出rpc服务端的代码。

![image-20221212142234643](C:\Users\Liberal Wu\Documents\MarkDown\Work\image-20221212142234643.png)

注意点： 

+ 注入serviceProvider，需要在所有使用了该类的类中创建相应的属性。
+ 涉及到代理类（method invoke）需传递全称类名。

更改后相较上一版本优势：

1. 代码解耦，抽象了server和client，模块之间专注于同一功能。
2. serviceProvier实现了注册多个接口服务
3. 使用线程池比收到任务时直接开辟线程更便于资源的管理与提高使用率。

仍存在的问题：

+ BIO（阻塞IO）监听，会有队头阻塞的风险
+ 网络传输性能低（使用netty底层传输）
+ serviceProvider仍需优化，实现自动服务注册

## Version_3

在ClientProxy新增netty的实现方式，使用netty进行底层网络传输，并设置netty为NIO的监听方式。

![image-20230215105325396](C:\Users\Liberal Wu\Documents\MarkDown\Work\image-20230215105325396.png)

注意点： 

+ netty的客户端与服务端使用相同的参数配置，自定义类需要根据执行的操作重写方法。

更改后相较上一版本优势：

1. 解决了socket底层传输性能低的问题
2. 使用netty自带的NIO解决了BIO存在的队头阻塞风险

仍存在的问题：

+ netty的NIO是用同步阻塞来获取request对应的response，可用回调函数来使同步改为异步
+ serviceProvider仍需优化，实现自动服务注册
+ java原生序列化方式不够高效

## version_4

新增ServiceRegister类，重构serviceProvider类，使用zookeeper实现服务注册和服务发现的功能，并重构客户端和服务端的服务类。

![image-20230216102043048](C:\Users\Liberal Wu\Documents\MarkDown\Work\image-20230216102043048.png)

注意点： 

+ 涉及到ip和端口的类，需要根据重写方法。服务端在添加服务的时候自动注册到zookeeper，客户端在发送请求之前访问zookeeper拿到ip和端口

更改后相较上一版本优势：

1. 服务自动注册，解决了服务变动造成的扩展性隐患

仍存在的问题：

+ 服务没有负载均衡，容易多个请求访问同一个服务地址造成宕机
+ netty的NIO是用同步阻塞来获取request对应的response，可用回调函数来使同步改为异步
+ java原生序列化方式不够高效

## version_5

重构服务发现类，使其实现负载均衡，新增服务状态监听功能，当访问地址数超过限定值后自动切换负载均衡方案为随机，默认为轮询。

![image-20230216150259192](C:\Users\Liberal Wu\Documents\MarkDown\Work\image-20230216150259192.png)

注意点： 

+ 可以将switch控制语句抽象为接口

更改后相较上一版本优势：

1. 实现了简单的自动监听服务地址数，根据策略选择负载均衡方案。

仍存在的问题：

+ netty的NIO是用同步阻塞来获取request对应的response，可用回调函数来使同步改为异步

+ java原生序列化方式不够高效