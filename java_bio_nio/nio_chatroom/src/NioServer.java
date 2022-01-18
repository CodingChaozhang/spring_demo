import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @author : codingchao
 * @date : 2022-01-18 09:29
 * @Description: NIO服务器端
 **/
public class NioServer {
    /**
     * 启动函数
     */
    public void start() throws IOException {
        //1.创建selector
        Selector selector = Selector.open();
        //2.通过serversocketchannel创建channel通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //3.为channel绑定监听端口
        serverSocketChannel.bind(new InetSocketAddress(8000));
        //4.设置channel为非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //5.将channel注册到selector上监听连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动成功");
        //6.循环等待新接入的连接
        for (;;){
            
            //获取可用的channel数量
            int readChannels = selector.select();
            if (readChannels==0){
                continue;
            }
            //获取可用的channel地址集合
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeySet.iterator();
            while (iterator.hasNext()){
                //selectionKey实例
                SelectionKey  selectionKey= (SelectionKey) iterator.next();
                //移除set中当前selectionKey实例
                iterator.remove();
                //7.根据对应状态，调用对应方法处理业务逻辑
                if (selectionKey.isAcceptable()){
                    //如果是接入事件
                    acceptHandler(serverSocketChannel,selector);
                }
                if (selectionKey.isReadable()){
                    //如果是可读事件
                    readHandler(selectionKey,selector);
                }
            }
        }
        
    }

    /**
     * 接入事件处理器
     */
    private void acceptHandler(ServerSocketChannel serverSocketChannel,
                               Selector selector) throws IOException {
        //如果是接入事件，创建socketchannel
        SocketChannel socketChannel = serverSocketChannel.accept();
        //将socketchannel设置为非阻塞工作模式
        socketChannel.configureBlocking(false);
        //将channel注册到selector上，监听可读事件
        socketChannel.register(selector,SelectionKey.OP_READ);
        //回复客户端提示信息
        socketChannel.write(StandardCharsets.UTF_8.encode("你与聊天室李其他人都不是朋友关系，请注意隐私安全"));
    }

    /**
     * 可读事件处理器
     * @param selectionKey
     * @param selector
     */
    private void readHandler(SelectionKey selectionKey,Selector selector) throws IOException {
        //从selectionkey中获取到已经就绪的channel
        SocketChannel socketChannel  = (SocketChannel) selectionKey.channel();
        //创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //循环读取客户端请求信息
        StringBuilder request = new StringBuilder();
        while (socketChannel.read(byteBuffer)>0){
            //切换buffer为读模式
            byteBuffer.flip();
            //读取buffer中的内容
            request.append(StandardCharsets.UTF_8.decode(byteBuffer));
        }

        //将channel再次注册到selector上，监听其它的可读事件
        socketChannel.register(selector,SelectionKey.OP_READ);
        //将客户端发送的请求信息，广播到其它客户端
        if (request.length()>0){
            boardCast(selector,socketChannel,request.toString());
        }
    }

    /**
     * 将消息广播到其它客户端中
     * @param selector
     * @param sourceChannel
     * @param request
     */
    private void boardCast(Selector selector, SocketChannel sourceChannel, String request) {
        //获取到所有已接入的客户端channel
        Set<SelectionKey> selectionKeySet = selector.keys();
        //循环向所有channel广播信息
        selectionKeySet.forEach(selectionKey -> {
            Channel targetChannel= selectionKey.channel();
            //剔除发消息的客户端
            if (targetChannel instanceof SocketChannel && targetChannel != sourceChannel){
                try {
                    //将消息发送到targetChannel客户端
                    ((SocketChannel) targetChannel).write(StandardCharsets.UTF_8.encode(request));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) throws IOException {
        NioServer nioServer = new NioServer();
        nioServer.start();
    }
}
