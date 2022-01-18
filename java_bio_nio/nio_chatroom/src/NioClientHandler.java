import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @author : codingchao
 * @date : 2022-01-18 10:03
 * @Description:客户端线程类，专门接收服务器端响应消息
 **/
public class NioClientHandler implements Runnable{
    private final Selector selector;

    public NioClientHandler(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            for (;;){
                int readChannels = selector.select();
                if (readChannels==0){
                    continue;
                }
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeySet.iterator();
                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if (selectionKey.isReadable()){
                        readHander(selectionKey,selector);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void readHander(SelectionKey selectionKey,Selector selector) throws IOException {
       SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        StringBuilder response = new StringBuilder();
        while(socketChannel.read(byteBuffer)>0){
            byteBuffer.flip();
            response.append(StandardCharsets.UTF_8.decode(byteBuffer));
        }
        socketChannel.register(selector,SelectionKey.OP_READ);
        if (response.length()>0){
            System.out.println(response);
        }
    }
}
