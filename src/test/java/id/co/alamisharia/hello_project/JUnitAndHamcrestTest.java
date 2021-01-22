package id.co.alamisharia.hello_project;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(VertxUnitRunner.class)
public class JUnitAndHamcrestTest {

  Vertx vertx;
  HttpServer server;

  @Before
  public void before(TestContext context) {
    vertx = Vertx.vertx();

    // Register the context exception handler
    vertx.exceptionHandler(context.exceptionHandler());

    server = vertx.createHttpServer().requestHandler(req -> req.response().end("foo")).
        listen(8888, context.asyncAssertSuccess());
  }

  @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void testHTTPCall(TestContext context) {
    // Send a request and get a response
    HttpClient client = vertx.createHttpClient();
    client.request(HttpMethod.GET, 8888, "localhost", "/")
      .compose(req -> req
        .send()
        .compose(HttpClientResponse::body))
      .onComplete(context.asyncAssertSuccess(body -> {
        assertThat(body.toString(), is("foo"));
        client.close();
      }));
  }
}
