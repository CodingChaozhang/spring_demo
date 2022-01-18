/**
 * @author : codingchao
 * @date : 2022-01-17 21:23
 * @Description:
 **/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
    public static void main(String[] args) {
        try{
            Socket socket = new Socket("localhost",8888);
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.write("用户名:jinxueling;密码：123");
            pw.flush();
            socket.shutdownOutput();

            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is,"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String data = null;
            while((data = br.readLine())!=null){
                System.out.println("我是客户端，服务器端响应的数据为："+data);
            }
            socket.close();
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}