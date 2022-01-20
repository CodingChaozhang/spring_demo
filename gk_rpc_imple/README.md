#  手把手实现一个RPC框架

## 一、RPC前置知识介绍

### 1.什么是RPC？

RPC是远程过程调用（Remote Procedure Call）的缩写形式。

假设我们有两台服务器A与B，一个在A服务器上部署的应用想要调用B服务器上部署的应用的函数、方法，由于不在同一个内存空间，不能直接调用，因此需要通过网络来表达调用的语义和传达调用的数据。  **在Java中，即将被调用的类、方法、参数序列化后通过网络传到目标应用，通过反射的方式调用执行**。

![image-20220119194430211](rpc_imple\1.png)

### 2.数据交换方式

利用中间件进行数据交换。

![image-20220119194638487](rpc_imple\2.png)

直接进行数据交换。 

![image-20220119194657949](rpc_imple\3.png)

### 3.现有RPC框架对比

目前主流的RPC框架如下表所示：

![image-20220119194849722](rpc_imple\4.png)

### 4.核心原理

#### 4.1 调用流程

![image-20220119195347236](rpc_imple\5.png)

- Server: Provider，服务提供者
- Client: Consumer，服务消费者
- Stub: 存根，服务描述

一次函数调用的流程如下：

- **首先客户端需要告诉服务端，需要调用的函数**，这里函数和进程存在一个映射，客户端远程调用时，需要查一下函数，找到对应的标识，然后执行函数的代码。
- **客户端需要把本地参数传给远程函数，本地调用的过程中，直接压栈即可**，但是在远程调用过程中不在同一个内存里，无法直接传递函数的参数，因此需要**客户端把参数转换成字节流，传给服务端**，然后**服务端将字节流转换成自身能读取**的格式，是一个序列化和反序列化的过程。
- 数据准备好了之后，如何进行传输？**网络传输层需要把调用的ID和序列化后的参数**传给服务端，然后把**计算好的结果序列化**传给客户端，因此TCP层即可完成上述过程。

#### 4.2 架构

```
my-rpc
├── my-rpc-client    -- 客户端
├── my-rpc-codec     -- 序列化与反序列化
├── my-rpc-common    -- 提供一些反射工具
├── my-rpc-protocol  -- 规定数据传输协议
├── my-rpc-server    -- 服务端
├── my-rpc-transport -- 用于client与server的http通信处理
└── my-rpc-example   -- 调用样例
```

模块依赖关系如下图所示，my-rpc-server和my-rpc-client依赖关系相同。

![image-20220119195903553](rpc_imple\6.png)

#### 4.3 各功能模块功能详细介绍

**（1）protocal模块**

用于规定数据传输协议和规则；

**（2）transport模块**

- 该模块主要用于client与server的http通信处理问题，其client请求内容以request类形式封装传输，server响应内容以response类封装返回；
- 使用jetty容器完成init，start和stop功能；
- 最重要的是RequestHandler实例的初始化，该抽象类定义于Transport模块，主要用于server处理来自client的请求。其抽象方法实现将在RpcServer类中详细讲解。

**（3）common模块**

- common模块主要为一些反射工具，其具体实现如下:
- getPublicMethods()方法一个用途是Server注册时存储所有的method的ServcieSescriptor。
- invoke()方法用于执行指定实例对象的method。

**（4）codec模块**

- Encoder 编码器
- Decoder 解码器

**（5）server模块**

- 本项目最核心两个模块之一，主要作用是定义了处理client请求的方法。
- register()方法主要用于注册该class的所有共有方法，并且获取之前讲述的ServiceDescriptor实例与ServiceInstance作为键值对的形式存储。
- 其内部主要定义了连个变量，一个是需要执行某个method的目标对象，另一个是需要执行的method。
- 其onRequest()方法通过Servlet的inputStream与OutputStream参数获取来自Client的数据，并且通过获取到的Request实例参数从ServiceManager中get实例对象与method。
- 因为Request对象中包含有Client获取到的实际参数，因此将上述参数一起传递到ServiceInvoker对象进行执行。

**（6）client模块**

- 该模块主要功能有连个一个时动态代理获取实参，一个是请求Server进行过程调用。
- 其RpcClient类主要是用于处理Client对Server的连接问题，相当于连接池，由有需求时随机返回连接。
- RpcClient类的getProxy()方法为动态代理，需要重点关注RemoteInvoker类。
- invoke()方法中对代理方法的参数进行存储封装到Request对象并且最终序列化传递到Server。

**（7）example样例**

- 一个加减法的使用样例

#### 4.4 涉及的技术栈

- 基础：Java、Maven、反射、JDK动态代理
- 序列化：FastJson
- 网络：Jetty、URLConnection





## 二、实现

### 1.类依赖图

![image-20220119201002640](rpc_imple\7.png)

### 2.实现过程

**附加问题：dependencies与dependencyManagement区别是什么**

（1）dependencies即使在子项目中不写该依赖项，那么子项目仍然会从父项目中继承该依赖项（全部继承）；

（2）dependencyManagement里只是声明依赖，并不实现引入，因此子项目需要显示的声明需要用的依赖。如果不在子项目中声明依赖，是不会从父项目中继承下来的；只有在子项目中写了该依赖项，并且没有指定具体版本，才会从父项目中继承该项，并且version和scope都读取自父pom;另外如果子项目中指定了版本号，那么会使用子项目中指定的jar版本。


**附加问题：Java泛型中Class<T> 、T与Class<?>**

单独的T 代表一个类型 ，而 Class<T>代表这个类型所对应的类， Class<？>表示类型不确定的类/

> **如何创建一个Class<T>类型的实例？** 
>
> 就像使用非泛型代码一样，有两种方式：调用方法 Class.forName() 或者使用类常量X.class。  Class.forName() 被定义为返 回 Class<?>。另一方面，类常量 X.class 被定义为具有类型 Class<X>，所 以 String.class 是Class<String> 类型的。
>
> **方法中为什么需要<T> T修饰呢**
>
> 泛型的声明，必须在方法的修饰符（public,static,final,abstract等）之后，返回值声明之前。
>
> ```
> public static <T> T request2Bean(HttpServletRequest request,Class<T> clazz){}
> ```
>
> 其中第一个<T>是与传入的参数Class<T>相对应的，相当于返回值的一个泛型，后面的T是返回值类型，代表方法必须返回T类型的（由传入的Class<T>决定）

**实现过程：**

```
1.建工程
2.实现通用模块
3.实现序列化模块
4.实现网络模块
5.实现server
6.实现client
7.gk-rpc使用案例
```



```
rpc-proto ： 基础协议封装
    Peer：主机+端口
    ServiceDescriptor：服务描述，将会是注册中心中对应的【服务的key值】包含有【类，方法，返回值类型，参数类型数组】，唯一确定一个方法
    Request：请求体,持有【服务描述+请求参数数组】
    Response：默认的响应体封装
rpc_transport: 网络服务封装
    TransportClient：客户端封装【接口+实现】
        1.创建连接
        2.发送数据等待响应：发送inputstream，等待outputstream
        3.关闭连接
    TransportServer:服务端封装【接口+实现】
        1.启动监听: servlet管理
        2.接收请求：接收请求，反序列化获取对象，处理调用，返回数据
        3.关闭监听
rpc-common: 工具类封装
    ReflectionUtils：
        根据class创建对象
        根据class获取该类所有公共方法
        invoke方法调用 ： public static Object invoke(Object object, Method method, Object... args)
rpc-codec: 序列化封装【接口+实现】
    Encoder 转二进制
    Decoder 转对象
rpc-server: 服务端封装
    RpcServerConfig：服务配置类
        HTTPTransportServer：默认服务实例类
        JSONEncoder：序列化实例类
        JSONDecoder：反序列化实例类
        port：监听端口
    ServiceInstance：服务的实例 --> 哪个对象暴露出哪个方法
        target：对象
        method：方法
    ServiceManager：管理rpc的所有服务
        Map<ServiceDescriptor, ServiceInstance> services：要将服务描述，服务实例作为key-value存储，便于客户端传来时，能够找到准确地实例，调用正确的方法
        register：服务注册【register(Class<T> interfaceClass, T bean) 】
            接口类 + 对象bean：将对象中的每一个方法都当做一个ServiceInstance注册进map中
        lookup：服务查找【lookUp(Request request)】
            获取请求中的ServiceDescriptor，去map中取出
    ServiceInvoke：【服务的调用】
        invoke(ServiceInstance serviceInstance, Request request):
            通过request的ServiceDescriptor找到服务的实例
            通过反射调用方法，传入参数
            ReflectionUtils.invoke(serviceInstance.getTarget(), serviceInstance.getMethod(),request.getParameters())
    RPCServer：【服务的封装】
        1.设置RpcServerConfig config
        2.反射获取网络实例     ReflectionUtils.newInstance(config.getTransportClass());
        3.反射获取序列化实例   ReflectionUtils.newInstance(config.getEncoderClass());
        4.反射获取反序列化实例 ReflectionUtils.newInstance(config.getDecoderClass());
        5.创建服务调用对象： this.serviceInvoke = new ServiceInvoke();
        6.创建服务管理对象：this.serviceManager = new ServiceManager();
        7.初始化网络实例：this.net.init(config.getPort(), this.handler); 此时只是准备好服务信息，并未开启监听
        
        8.register：服务注册 register(Class<T> interfaceClass, T bean) {serviceManager.register(interfaceClass, bean); }
        9.start：this.net.start 开启
        10.stop：this.net.stop  关闭
        11.handler请求处理：
            1.接收inputStream
            2.反序列化获得Request
            3.根据ServiceManager.lookup(request)找到实例ServiceInstance
            4.通过Object invoke = serviceInvoke.invoke(sis, request);得到响应结果并封装到 response.setData(invoke);
            5.序列化Response
            6.write
rpc-client: 客户端封装
```

**结果展示图：**

![image-20220120135846650](rpc_imple\8.png)

![image-20220120135904346](rpc_imple\9.png)
