import java.util.Map;

public class Request {
  private String method;
  private String path;
  private Map<String,String> params;
  private Map<String, String> headers;
  private String body;

  public Request(String method, String path, Map<String,String> params, Map<String, String> headers) {
    this.method = method;
    this.path = path;
    this.params = params;
    this.headers = headers;
  }

  public String getMethod() {
    return method;
  }

  public String getPath() {
    return path;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public String getBody() {
    return body;
  }

  public String getQueryParam(String name) {
    return params.get(name);
  }
}
