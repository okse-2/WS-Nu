package org.ntnunotif.wsnu.base.net;

import junit.framework.TestCase;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.InputStreamContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.junit.Test;
import org.ntnunotif.wsnu.base.internal.SoapForwardingHub;
import org.ntnunotif.wsnu.base.net.ApplicationServer;
import org.ntnunotif.wsnu.base.net.XMLParser;
import org.ntnunotif.wsnu.base.util.InternalMessage;
import org.w3._2001._12.soap_envelope.Body;
import org.w3._2001._12.soap_envelope.Envelope;
import org.w3._2001._12.soap_envelope.Header;

import javax.xml.bind.JAXBElement;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by tormod on 3/6/14.
 */
public class ApplicationServerTest extends TestCase {

    private ApplicationServer _server;

    public void setUp() throws Exception {
        _server = ApplicationServer.getInstance();
        _server.start(new SoapForwardingHub());
    }

    @Test
    public void testSimpleServer() throws Exception {

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
        request.content(new InputStreamContentProvider(getClass().getResourceAsStream("/server_test_html_content.html")),
                                                                           "text/html;charset/utf-8");
        ContentResponse response = request.send();
        assertEquals(500, response.getStatus());
    }

    @Test
    public void testSendingXML() throws Exception {

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
        request.content(new InputStreamContentProvider(getClass().getResourceAsStream("/server_test_xml.xml")),
                "application/soap+xml;charset/utf-8");

        ContentResponse response = request.send();
        //TODO: This should be changed to some error status, as the server should not be able to process plain xml
        assertEquals(500, response.getStatus());
    }

    @Test
    public void testSendingSoap() throws Exception {
        SoapForwardingHub soapForwardingHub = new SoapForwardingHub();

        // Start the client
        SslContextFactory sslFactory = new SslContextFactory();

        HttpClient client = new HttpClient(sslFactory);
        client.setFollowRedirects(true);
        client.start();

        Object object = XMLParser.parse(getClass().getResourceAsStream("/server_test_soap.xml"));
        Envelope env = (Envelope)((JAXBElement)((InternalMessage) object).getMessage()).getValue();
        Header head = env.getHeader();
        Body body = env.getBody();

        // Send response
        Request request = client.newRequest("http://localhost:8080/");
        request.method(HttpMethod.POST);
        request.content(new InputStreamContentProvider(getClass().getResourceAsStream("/server_test_soap.xml")),
                "application/soap+xml;charset/utf-8");

        ContentResponse response = request.send();
        assertEquals(404, response.getStatus());

        System.out.println(response.getContentAsString());

    }

    @Test
    public void testSubscribe() throws Exception {

        // Start the client
        SslContextFactory sslFactory = new SslContextFactory();

        HttpClient client = new HttpClient(sslFactory);
        client.setFollowRedirects(true);
        client.start();

        // Send response
        InputStream file = getClass().getResourceAsStream("/server_test_subscribe.xml");

        Request request = client.newRequest("http://localhost:8080/");
        request.method(HttpMethod.POST);
        request.header(HttpHeader.CONTENT_TYPE, "application");
        request.header(HttpHeader.CONTENT_LENGTH, "200");
        request.content(new InputStreamContentProvider(file),
                "application/soap+xml;charset/utf-8");

        ContentResponse response = request.send();
        assertEquals(404, response.getStatus());

        System.out.println(response.getContentAsString());
    }

    public void tearDown() throws Exception {
        super.tearDown();
        _server.stop();
    }
}