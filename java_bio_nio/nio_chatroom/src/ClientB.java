import java.io.IOException;

/**
 * @author : codingchao
 * @date : 2022-01-18 10:13
 * @Description:
 **/
public class ClientB {
    public static void main(String[] args) throws IOException {
        new NioClient().start("ClientB");
    }
}
