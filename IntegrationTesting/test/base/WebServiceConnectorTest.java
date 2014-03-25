package base;

import junit.framework.TestCase;
import org.ntnunotif.wsnu.base.internal.UnpackingConnector;
import org.ntnunotif.wsnu.services.implementations.notificationconsumer.NotificationConsumer;
import org.ntnunotif.wsnu.services.implementations.notificationproducer.SimpleNotificationProducer;

import javax.jws.WebService;

/**
 * Created by tormod on 25.03.14.
 */
public class WebServiceConnectorTest extends TestCase {
    private SimpleNotificationProducer producer;
    private NotificationConsumer consumer;
    private UnpackingConnector one, two, three;

    @WebService
    private class WebServiceWithoutEndPoint{

    }

    public void setUp() throws Exception {
        super.setUp();
        producer = new SimpleNotificationProducer();
        consumer = new NotificationConsumer();
    }

    public void testConstructor() throws Exception {
        one = new UnpackingConnector(producer);
        two = new UnpackingConnector(consumer);
        three = new UnpackingConnector(new WebServiceWithoutEndPoint());
    }
}
