package org.ntnunotif.wsnu.base.soap;

import javax.xml.bind.JAXBElement;
import java.util.List;

public abstract class Soap {
    public enum SoapVersion{
        SOAP_1_1,
        SOAP_1_2_2001,
        SOAP_1_2_2003,
        SOAP_NOT_ENVELOPE
    }

    public enum SoapFaultType {
        SOAP_VERSION_MISMATCH,
        SOAP_MUST_UNDERSTAND,
        SOAP_CLIENT,
        SOAP_SERVER
    }

    public static Soap createSameAs(JAXBElement elem) {
        return create(version(elem));
    }

    public static Soap createSameAs(Object obj) {
        return create(version(obj.getClass()));
    }

    public static Soap create(SoapVersion version) {
        switch(version) {
            case SOAP_1_1:
                return new Soap11();
            case SOAP_1_2_2001:
                return new Soap12_2001();
            case SOAP_1_2_2003:
                return new Soap12();
            default:
                throw new RuntimeException("Invalid soap version");
        }
    }

    public static Boolean isSoapEnvelope(Object o) {
        return (o instanceof org.xmlsoap.schemas.soap.envelope.Envelope) ||
                (o instanceof org.w3._2001._12.soap_envelope.Envelope) ||
                (o instanceof org.w3._2003._05.soap_envelope.Envelope);
    }

    public static SoapVersion version(Class cls) {
        if(cls.equals(org.xmlsoap.schemas.soap.envelope.Envelope.class))
            return SoapVersion.SOAP_1_1;
        else if(cls.equals(org.w3._2001._12.soap_envelope.Envelope.class))
            return SoapVersion.SOAP_1_2_2001;
        else if(cls.equals(org.w3._2003._05.soap_envelope.Envelope.class))
            return SoapVersion.SOAP_1_2_2003;
        else
            return SoapVersion.SOAP_NOT_ENVELOPE;
    }

    public static SoapVersion version(JAXBElement elem) {
        return version(elem.getDeclaredType());
    }

    public abstract SoapVersion version();
    public abstract JAXBElement createMessage(Object o);
    public abstract JAXBElement createFault(SoapFaultType type, String description);
    public abstract List<Object> getBodyContent(Object envelope);
}
