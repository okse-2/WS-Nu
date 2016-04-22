package org.ntnunotif.wsnu.base.soap;

import org.w3._2001._12.soap_envelope.*;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.List;

class Soap12_2001 extends Soap{
    private ObjectFactory factory;

    Soap12_2001() {
        factory = new ObjectFactory();
    }

    @Override
    public SoapVersion version() {
        return SoapVersion.SOAP_1_2_2001;
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
    public JAXBElement createFault(SoapFaultType type, String description) {
        ObjectFactory soapObjectFactory = new ObjectFactory();

        Envelope envelope = soapObjectFactory.createEnvelope();
        Body body = soapObjectFactory.createBody();
        Fault fault = soapObjectFactory.createFault();

        fault.setFaultcode(new QName("http://www.w3.org/2001/12/soap-envelope", faultCodes[type.ordinal()]));
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
}
