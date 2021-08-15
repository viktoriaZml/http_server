import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Connection implements Runnable {
  private final Socket socket;
  private final Server server;

  public Connection(Socket socket, Server server) {
    this.socket = socket;
    this.server = server;
  }

  @Override
  public void run() {
    try (final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         final var out = new BufferedOutputStream(socket.getOutputStream())) {
      //System.out.println("поток с именем" + Thread.currentThread().getName() + " и идентификатором " + Thread.currentThread().getId());
      Request request = readRequest(in);
      //System.out.println(request.getQueryParam("last"));
      Map<String, Handler> handlers = server.getHandlers().get(request.getMethod());
      if (handlers != null) {
        Handler handler = handlers.get(request.getPath());
        if (handler != null) {
          handler.handle(request, out);
          return;
        }
      }

      out.write((
              "HTTP/1.1 404 Not Found\r\n" +
                      "Content-Length: 0\r\n" +
                      "Connection: close\r\n" +
                      "\r\n"
      ).getBytes());
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Map<String, String> getQueryParams(String str) {
    int pos = str.indexOf("?");
    if (pos > 0) {
      str = str.substring(pos + 1);
      List<NameValuePair> params = URLEncodedUtils.parse(str, Charset.defaultCharset(), '&');
      //System.out.println(params);
      Map<String,String> paramMap = params.stream().collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
      return paramMap;
    }
    return new HashMap<>();
  }

  public Request readRequest(BufferedReader reader) throws IOException {
    final var requestLine = reader.readLine();
    final var parts = requestLine.split(" ");

    if (parts.length != 3) {
      // just close socket
      return null;
    }
    final String method = parts[0];
    final var path = parts[1].split("\\?")[0];
    var params = getQueryParams(parts[1]);

    Map<String, String> headers = new HashMap<>();
    String line;
    while (!(line = reader.readLine()).equals("")) {
      int pos = line.indexOf(":");
      headers.put(line.substring(0, pos), line.substring(pos + 2));
    }
    return new Request(method, path, params, headers);
  }
}
