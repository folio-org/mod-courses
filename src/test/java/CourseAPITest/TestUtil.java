package CourseAPITest;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static CourseAPITest.CourseAPITest.MODULE_FROM;
import static CourseAPITest.CourseAPITest.MODULE_TO;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class TestUtil {
  private static HttpClient httpClient = null;
  private static final Logger logger = LogManager.getLogger(TestUtil.class);

  static class WrappedResponse {
    private String explanation;
    private int code;
    private String body;
    private JsonObject json;
    private HttpResponse<Buffer> response;

    public WrappedResponse(String explanation, int code, String body,
        HttpResponse<Buffer> response) {
      this.explanation = explanation;
      this.code = code;
      this.body = body;
      this.response = response;
      try {
        json = new JsonObject(body);
      } catch(Exception e) {
        json = null;
      }
    }

    public String getExplanation() {
      return explanation;
    }

    public int getCode() {
      return code;
    }

    public String getBody() {
      return body;
    }

    public HttpResponse<Buffer> getResponse() {
      return response;
    }

    public JsonObject getJson() {
      return json;
    }

  }

  private static HttpClient getHttpClient(Vertx vertx) {
    if(httpClient == null) {
      httpClient = vertx.createHttpClient();
    }
    return httpClient;
  }

  public static Future<WrappedResponse> doOkapiRequest(Vertx vertx,
      String requestPath, HttpMethod method, Map<String, String> okapiHeaders,
      Map<String, String> extraHeaders, String payload,
      Integer expectedCode, String explanation) {
    WebClient client = WebClient.create(vertx);
    MultiMap headers = MultiMap.caseInsensitiveMultiMap();
    MultiMap originalHeaders = MultiMap.caseInsensitiveMultiMap();
    originalHeaders.setAll(okapiHeaders);
    String okapiUrl = originalHeaders.get("x-okapi-url");
    if(okapiUrl == null) {
      return Future.failedFuture("No okapi URL found in headers");
    }
    String requestUrl = okapiUrl + requestPath;
    if(originalHeaders.contains("x-okapi-token")) {
      headers.add("x-okapi-token", originalHeaders.get("x-okapi-token"));
    }
    if(originalHeaders.contains("x-okapi-tenant")) {
      headers.add("x-okapi-tenant", originalHeaders.get("x-okapi-tenant"));
    }
    headers.add("content-type", "application/json");
    headers.add("accept", "application/json");
    if(extraHeaders != null) {
      for(Map.Entry<String, String> entry : extraHeaders.entrySet()) {
        headers.add(entry.getKey(), entry.getValue());
      }
    }
    HttpRequest<Buffer> request = client.requestAbs(method, requestUrl);
    for(Map.Entry entry : headers.entries()) {
      String key = (String)entry.getKey();
      String value = (String)entry.getValue();
      if( key != null && value != null) {
        request.putHeader(key, value);
      }
    }
    return wrapRequestResponse(request, payload, requestUrl, method, expectedCode, explanation);
  }

  public static Future<WrappedResponse> doRequest(Vertx vertx, String url,
          HttpMethod method, MultiMap headers, String payload,
          Integer expectedCode, String explanation) {

    boolean addPayLoad = false;
    //HttpClient client = vertx.createHttpClient();
    WebClient client = WebClient.create(vertx);
    HttpRequest<Buffer> request = client.requestAbs(method, url);
    //Add standard headers
    request.putHeader("X-Okapi-Tenant", "diku")
            .putHeader("content-type", "application/json")
            .putHeader("accept", "application/json");
    if(headers != null) {
      for(Map.Entry entry : headers.entries()) {
        request.putHeader((String)entry.getKey(), (String)entry.getValue());
        System.out.println(String.format("Adding header '%s' with value '%s'",
            (String)entry.getKey(), (String)entry.getValue()));
      }
    }
    return wrapRequestResponse(request, payload, url, method, expectedCode, explanation);
  }

  /* HttpClientRequest should have headers set before calling */
  private static Future<WrappedResponse> wrapRequestResponse(HttpRequest<Buffer> request,
      String payload, String uri, HttpMethod method, Integer expectedCode, String explanation) {
    Promise<WrappedResponse> promise = Promise.promise();
    Future<HttpResponse<Buffer>> sentRequestFuture;

    if( method == HttpMethod.PUT || method == HttpMethod.POST ) {
      sentRequestFuture = request.sendBuffer(Buffer.buffer(payload));
    } else {
      sentRequestFuture = request.send();
    }
    sentRequestFuture.onComplete(res -> {
      if(res.failed()) {
        promise.fail(res.cause());
      } else {
        try {
          HttpResponse<Buffer> result = res.result();
          String responseString = result.bodyAsString();
          String explainString = (explanation != null) ? explanation : "(no explanation)";
          if(expectedCode != null && expectedCode != result.statusCode()) {
            String message = method.toString() + " to " + uri
                  + " failed. Expected status code "
                  + expectedCode + ", got status code " + result.statusCode() + ": "
                  + responseString + " | " + explainString;
            throw new RuntimeException(message);
          } else {
            logger.info("Got status code {} with payload of: {} | {}",
              result.statusCode(), responseString, explainString);
            WrappedResponse wr = new WrappedResponse(explanation, result.statusCode(),
                responseString, result);
            promise.complete(wr);
          }
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
          promise.tryFail(e);
        }
      }
    });

    logger.info("Sending {} request to url {} with payload {}\n",
        method.toString(), uri, payload);
    return promise.future();

  }

  private static Future<Void> initTenantWait(WebClient client, String tenantId, String url) {
    HttpRequest<Buffer> request = client.getAbs(url).addQueryParam("wait", "10000");
    request.putHeader("X-Okapi-Tenant", tenantId);
    return request.send().compose(result -> {
      assertThat(result.statusCode(), is(200));
      return Future.succeededFuture();
    });
  }

  public static Future<Void> initTenant(WebClient client, String tenantId, int port,
      String okapiUrl, JsonArray parameters) {
    String url = "http://localhost:" + port + "/_/tenant";
    JsonObject payload = new JsonObject()
        .put("module_to", MODULE_TO)
        .put("module_from", MODULE_FROM)
        .put("parameters", parameters);
    HttpRequest<Buffer> request = client.postAbs(url);
    request.putHeader("X-Okapi-Tenant", tenantId);
    request.putHeader("X-Okapi-Url", okapiUrl);
    request.putHeader("X-Okapi-Url-To", "http://localhost:" + port);
    return request.sendJsonObject(payload)
        .compose(result -> {
          assertThat(result.statusCode(), is(201));
          return initTenantWait(client, tenantId,
              "http://localhost:" + port + result.getHeader("Location"));
        });
  }

}

