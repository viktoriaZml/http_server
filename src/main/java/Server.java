import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
  private final ExecutorService threadPool = Executors.newFixedThreadPool(64);
  private final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
  private Map<String, Map<String, Handler>> handlers;

  public Server() {
    handlers = new HashMap<>();
  }

  public void listen(int port) {
    try {
      ServerSocket serverSocket = new ServerSocket(port);
      while (true)
        threadPool.execute(new Connection(serverSocket.accept(), this));
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      threadPool.shutdown();
    }

  }

  public void addHandler(String method, String path, Handler handler) {
    Map<String, Handler> methodHandlers = new HashMap<>();
    if (handlers.containsKey(method)) {
      methodHandlers = handlers.get(method);
    }
    methodHandlers.put(path, handler);
    handlers.put(method, methodHandlers);
  }

  public List<String> getValidPaths() {
    return validPaths;
  }

  public Map<String, Map<String, Handler>> getHandlers() {
    return handlers;
  }
}
