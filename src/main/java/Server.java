import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private final int PORT = 9999;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(64);
    final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");

    public Server() {
        startServer();
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            while (true)
                threadPool.execute(new Connection(serverSocket.accept(), validPaths));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }

    }
}
