import org.apache.http.NameValuePair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    /*for (NameValuePair param : params){
      if (param.getName().equals(name))
        return param.getValue();
    }*/
    Map<String, String> paramMap = params.stream().collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
    return paramMap.get(name);
  }
}
