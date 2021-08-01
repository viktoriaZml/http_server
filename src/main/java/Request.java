import org.apache.http.NameValuePair;

import java.util.List;
import java.util.Map;

public class Request {
  private String method;
  private String path;
  private List<NameValuePair> params;
  private Map<String, String> headers;
  private String body;

  public Request(String method, String path, List<NameValuePair> params, Map<String, String> headers) {
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
    for (var i = 0; i < params.size(); i++) {
      if (params.get(i).getName().equals(name))
        return params.get(i).getValue();
    }
    return null;
  }
}
