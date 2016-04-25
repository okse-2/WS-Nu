package org.ntnunotif.wsnu.base.soap;

import org.xmlsoap.schemas.soap.envelope.Body;
import org.xmlsoap.schemas.soap.envelope.Envelope;
import org.xmlsoap.schemas.soap.envelope.Fault;
import org.xmlsoap.schemas.soap.envelope.ObjectFactory;

import javax.annotation.Nullable;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.List;

class Soap11 extends Soap{
    private ObjectFactory factory;
    private final String namespace = "http://schemas.xmlsoap.org/soap/envelope/";

    Soap11() {
        factory = new ObjectFactory();
    }

    @Override
    public SoapVersion version() {
        return SoapVersion.SOAP_1_1;
    }

    @Override
    public JAXBElement createMessage(Object o) {
        if(o instanceof Envelope) {
            return factory.createEnvelope((Envelope)o);
        } else {
            Envelope env = factory.createEnvelope();
            Body body = factory.createBody();
            body.getAny().add(o);
            env.setBody(body);
            return factory.createEnvelope(env);
        }
    }

    private final static String faultCodes[] = {
            "VersionMismatch", // SOAP_VERSION_MISMATCH
            "MustUnderstand",  // SOAP_MUST_UNDERSTAND
            "Client",          // SOAP_CLIENT
            "Server"           // SOAP_SERVER
    };

    @Override
    public JAXBElement createFault(SoapFaultType type, String description, @Nullable JAXBElement detail) {
        ObjectFactory soapObjectFactory = new ObjectFactory();

        Envelope envelope = soapObjectFactory.createEnvelope();
        Body body = soapObjectFactory.createBody();
        Fault fault = soapObjectFactory.createFault();

        fault.setFaultcode(new QName(namespace, faultCodes[type.ordinal()]));
        fault.setFaultstring(description);
        envelope.setBody(body);

        body.getAny().add(soapObjectFactory.createFault(fault));

        return soapObjectFactory.createEnvelope(envelope);
    }

    @Override
    public List<Object> getBodyContent(Object envelope) {
        try {
            Envelope env = (Envelope) envelope;
            return env.getBody().getAny();
        } catch(NullPointerException e) {
            throw new RuntimeException("Unable to get body contents from envelope");
        }
    }

    @Override
    public String namespace() {
        return namespace;
    }
}
