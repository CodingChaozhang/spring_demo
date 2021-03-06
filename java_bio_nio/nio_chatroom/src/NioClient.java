import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author : codingchao
 * @date : 2022-01-18 09:50
 * @Description: Nio客户端
 **/
public class NioClient {
    /**
     * 启动客户端
     */
    public void start(String nickname) throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8000));
        System.out.println("客户端启动成功");
        /**
         * 接收服务器响应
         * 新开线程，专门负责来接收服务器端的响应数据
         * selector socketchannel注册
         */
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        new Thread(new NioClientHandler(selector)).start();

        /**
         * 向服务器端发送数据
         */
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String request = scanner.nextLine();
            if (request!=null&&request.length()>0){
                socketChannel.write(StandardCharsets.UTF_8.encode(nickname+" : " + request));
            }
        }
    }
}
