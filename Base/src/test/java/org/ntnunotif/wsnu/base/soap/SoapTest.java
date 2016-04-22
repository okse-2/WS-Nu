package org.ntnunotif.wsnu.base.soap;

import org.junit.Test;

import javax.xml.bind.JAXBElement;

import static junit.framework.TestCase.assertEquals;

public class SoapTest {

    @Test
    public void testSoap() {
        final Soap soap11 = Soap.create(Soap.SoapVersion.SOAP_1_1);
        final Soap soap12 = Soap.create(Soap.SoapVersion.SOAP_1_2_2003);
        final Soap soap12_draft = Soap.create(Soap.SoapVersion.SOAP_1_2_2001);
        assertEquals(soap11.version(), Soap.SoapVersion.SOAP_1_1);
        assertEquals(soap12.version(), Soap.SoapVersion.SOAP_1_2_2003);
        assertEquals(soap12_draft.version(), Soap.SoapVersion.SOAP_1_2_2001);

        final String str = "Hello";
        JAXBElement element;

        element = soap11.createMessage(str);
        assertEquals(element.getDeclaredType(), org.xmlsoap.schemas.soap.envelope.Envelope.class);
        assertEquals(Soap.version(element), Soap.SoapVersion.SOAP_1_1);
        assertEquals(Soap.SoapVersion.SOAP_1_1, Soap.createSameAs(element).version());
        assertEquals(soap11.getBodyContent(element.getValue()).get(0), str);

        element = soap12.createMessage(str);
        assertEquals(element.getDeclaredType(), org.w3._2003._05.soap_envelope.Envelope.class);
        assertEquals(Soap.version(element), Soap.SoapVersion.SOAP_1_2_2003);
        assertEquals(Soap.SoapVersion.SOAP_1_2_2003, Soap.createSameAs(element).version());
        assertEquals(soap12.getBodyContent(element.getValue()).get(0), str);

        element = soap12_draft.createMessage(str);
        assertEquals(element.getDeclaredType(), org.w3._2001._12.soap_envelope.Envelope.class);
        assertEquals(Soap.version(element), Soap.SoapVersion.SOAP_1_2_2001);
        assertEquals(Soap.SoapVersion.SOAP_1_2_2001, Soap.createSameAs(element).version());
        assertEquals(soap12_draft.getBodyContent(element.getValue()).get(0), str);
    }
}
