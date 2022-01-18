import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author : codingchao
 * @date : 2022-01-17 21:14
 * @Description: 服务器端
 **/
public class Server {
    /**
     * 启动服务器端
     */
    public void start() throws IOException {
        //1.服务器端监听建立连接请求
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("服务端即将启动，等待客户端的连接...");
        //2.客户端发起建立连接请求
        int count = 0;
        Socket socket  = null;
        while(true){//循环监听客户端的连接
            //调用accept()方法监听，等待客户端连接以获取socket实例
            socket = serverSocket.accept();
            //3.服务器端启动新线程
            //4.线程响应客户端
            //5.等待客户单再次请求
            //创建新线程
            Thread thread = new Thread(new ServerThread(socket));
            thread.start();
            count++;
            System.out.println("服务器端被连接过的次数："+count);
            InetAddress address = socket.getInetAddress();
            System.out.println("当前客户端IP为："+address.getHostAddress());
        }


    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }
}
