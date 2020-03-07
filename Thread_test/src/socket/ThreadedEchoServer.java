package socket;

import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ThreadedEchoServer {

    public static void main(String[] args) throws InterruptedException
    {
        final int clientCount = 3;
        Semaphore semaphore = new Semaphore(clientCount);
        ExecutorService executorService = Executors.newFixedThreadPool(clientCount);

        try(ServerSocket s = new ServerSocket(8189))
        {
            int i = 1;

            while(true)
            {
                Socket incoming = s.accept();
                System.out.println("Spawning " + i);
                Runnable r = new ThreadedEchoHandler(incoming);
                semaphore.acquire();
                executorService.execute(r);
                semaphore.release();
                i++;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

/**
 This class handles the client input for one server socket connection
 */

class ThreadedEchoHandler implements Runnable
{
    private Socket incoming;

    public ThreadedEchoHandler(Socket incomingSocket)
    {
        incoming = incomingSocket;
    }

    public void run()
    {
        try (InputStream inStream = incoming.getInputStream();
             OutputStream outStream = incoming.getOutputStream();
             ) {
            Scanner in = new Scanner(inStream, "UTF-8");
            PrintWriter out = new PrintWriter(new OutputStreamWriter(outStream, "UTF-8"), true);

            out.println("Hello! Enter exit to exit");

            boolean done = false;
            InetAddress localHostAddress = InetAddress.getLocalHost();
            while (!done && in.hasNextLine()) {
                String line = in.nextLine();
                out.println(localHostAddress + "Echo: " + line);
                if (line.trim().equals("exit") || line.trim().equals("EXIT"))
                    done = true;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}