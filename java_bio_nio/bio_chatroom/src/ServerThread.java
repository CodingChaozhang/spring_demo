/**
 * @author : codingchao
 * @date : 2022-01-17 21:21
 * @Description:
 **/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable{

    Socket socket = null;
    public ServerThread(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        OutputStream os = null;
        PrintWriter pw = null;
        //与客户端建立通信，获取输入流，读取客户端提供的信息
        try {
            is = socket.getInputStream();
            isr = new InputStreamReader(is,"UTF-8");
            br = new BufferedReader(isr);
            String data = null;
            while((data = br.readLine())!=null){
                System.out.println("我是服务器，客户端提交的信息为："+data);
                socket.shutdownInput();//关闭输入流

                //获取输出流，响应客户端的请求
                os = socket.getOutputStream();
                pw = new PrintWriter(os);
                pw.write("服务器端响应成功");
                pw.flush();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            //关闭资源和相关socket
            try{
                if(pw!=null){
                    pw.close();
                }
                if(os!=null){
                    os.close();
                }
                if(is!=null){
                    is.close();
                }
                if(isr!=null){
                    isr.close();
                }
                if(br!=null){
                    br.close();
                }
                if(socket!=null){
                    socket.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
