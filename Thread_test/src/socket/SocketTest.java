package socket;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SocketTest {
    public static void main(String[] args) throws IOException{
        try (Socket s = new Socket("time-a.nist.gov", 13);
             Scanner in = new Scanner(s.getInputStream(), "UTF-8"))
                // UnknownHostException error
        {
            System.out.println(s.isConnected());
            while (in.hasNextLine())
            {
                String line = in.nextLine();
                System.out.println(line);
            }

            int k =s.getPort();
            System.out.println(k);
            System.out.println(s.isConnected());
        }
    }
}
