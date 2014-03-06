package ntnunotif.wsnu.base.net;

import junit.framework.TestCase;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.InputStreamContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.junit.Test;
import org.ntnunotif.wsnu.base.internal.Bus;
import org.ntnunotif.wsnu.base.net.ApplicationServer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by tormod on 3/6/14.
 */
public class ApplicationServerTest extends TestCase {

    private ApplicationServer _server;

    @Test
    public void testInstantiation() throws IOException {
        try{
            _server = ApplicationServer.getInstance();
            _server.start(null);
        } catch (Exception e) {
            System.err.println("Applicationserver failed to instantiate");
        }
    }

    @Test
    public void testSimpleServer() throws Exception {
        Bus bus = new Bus();

        // Start the server
        _server = null;
        try{
            _server = ApplicationServer.getInstance();
        } catch (Exception e) {
            System.err.println("Applicationserver failed to instantiate");
        }

        // This should not do anything, as the server is started through the bus (and we are working with a singleton)
        _server.start(null);

        // Start the client
        SslContextFactory sslFactory = new SslContextFactory();

        HttpClient client = new HttpClient(sslFactory);
        client.setFollowRedirects(true);
        client.start();

        // Send response
        Request request = client.newRequest("http://localhost:8080/");
        request.method(HttpMethod.POST);
        request.header(HttpHeader.CONTENT_TYPE, "application");
        request.header(HttpHeader.CONTENT_LENGTH, "200");
        request.content(new InputStreamContentProvider(new FileInputStream("Base/test/ntnunotif/wsnu/base/net/server_test_html_content.html")),
                                                                           "text/html;charset/utf-8");

        ContentResponse response = request.send();
        //TODO: This should be changed to some error status, as the server should not be able to process html
        assertEquals(200, response.getStatus());
        _server.stop();
    }

    @Test
    public void testSendingXML() throws Exception {

        Bus bus = new Bus();

        // Start the server
        _server = null;
        try{
            _server = ApplicationServer.getInstance();
        } catch (Exception e) {
            System.err.println("Applicationserver failed to instantiate");
        }

        // This should not do anything, as the server is started through the bus (and we are working with a singleton)
        _server.start(null);

        // Start the client
        SslContextFactory sslFactory = new SslContextFactory();

        HttpClient client = new HttpClient(sslFactory);
        client.setFollowRedirects(true);
        client.start();

        // Send response
        Request request = client.newRequest("http://localhost:8080/");
        request.method(HttpMethod.POST);
        request.header(HttpHeader.CONTENT_TYPE, "application");
        request.header(HttpHeader.CONTENT_LENGTH, "200");
        request.content(new InputStreamContentProvider(new FileInputStream("Base/test/ntnunotif/wsnu/base/net/server_test_xml.xml")),
                "application/soap+xml;charset/utf-8");

        ContentResponse response = request.send();
        //TODO: This should be changed to some error status, as the server should not be able to process plain xml
        assertEquals(200, response.getStatus());
        _server.stop();
    }

    @Test
    public void testSendingSoap() throws Exception {
        Bus bus = new Bus();

        // Start the server
        _server = null;
        try{
            _server = ApplicationServer.getInstance();
        } catch (Exception e) {
            System.err.println("Applicationserver failed to instantiate");
        }

        // This should not do anything, as the server is started through the bus (and we are working with a singleton)
        _server.start(null);

        // Start the client
        SslContextFactory sslFactory = new SslContextFactory();

        HttpClient client = new HttpClient(sslFactory);
        client.setFollowRedirects(true);
        client.start();

        // Send response
        Request request = client.newRequest("http://localhost:8080/");
        request.method(HttpMethod.POST);
        request.header(HttpHeader.CONTENT_TYPE, "application");
        request.header(HttpHeader.CONTENT_LENGTH, "200");
        request.content(new InputStreamContentProvider(new FileInputStream("Base/test/ntnunotif/wsnu/base/net/server_test_soap.xml")),
                "application/soap+xml;charset/utf-8");

        ContentResponse response = request.send();
        assertEquals(200, response.getStatus());

        //TODO: This should contain some WS error
        response.getContentAsString();

        _server.stop();
    }

    @Test
    public void testSubscribe() throws Exception {
        Bus bus = new Bus();

        // Start the server
        _server = null;
        try{
            _server = ApplicationServer.getInstance();
        } catch (Exception e) {
            System.err.println("Applicationserver failed to instantiate");
        }

        // This should not do anything, as the server is started through the bus (and we are working with a singleton)
        _server.start(null);

        // Start the client
        SslContextFactory sslFactory = new SslContextFactory();

        HttpClient client = new HttpClient(sslFactory);
        client.setFollowRedirects(true);
        client.start();

        // Send response
        Request request = client.newRequest("http://localhost:8080/");
        request.method(HttpMethod.POST);
        request.header(HttpHeader.CONTENT_TYPE, "application");
        request.header(HttpHeader.CONTENT_LENGTH, "200");
        request.content(new InputStreamContentProvider(new FileInputStream("Base/test/ntnunotif/wsnu/base/net/server_test_subscribe.xml")),
                "application/soap+xml;charset/utf-8");

        ContentResponse response = request.send();
        assertEquals(200, response.getStatus());

        //TODO: This should contain some WS error, unless we can process the ws:notify in server_test_soap.
        response.getContentAsString();

        _server.stop();
    }
}
