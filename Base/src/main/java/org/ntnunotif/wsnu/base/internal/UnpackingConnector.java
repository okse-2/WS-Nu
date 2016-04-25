//-----------------------------------------------------------------------------
// Copyright (C) 2014 Tormod Haugland and Inge Edward Haulsaunet
//
// This file is part of WS-Nu.
//
// WS-Nu is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// WS-Nu is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with WS-Nu. If not, see <http://www.gnu.org/licenses/>.
//-----------------------------------------------------------------------------

package org.ntnunotif.wsnu.base.internal;

import org.ntnunotif.wsnu.base.soap.Soap;
import org.ntnunotif.wsnu.base.util.InternalMessage;
import org.ntnunotif.wsnu.base.util.Log;

import javax.jws.WebMethod;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import static org.ntnunotif.wsnu.base.util.InternalMessage.*;

/**
 * Connector that takes a soap-envelope, unpacks it's body, and sends it forward.
 * This connector <b>does not</b> bother with checking the soap-headers for any information.
 * This should ideally be used with a web service whose methods only take the parsed-objects as parameters,
 * and nothing more. I.e. a NotificationConsumer
 * @author Tormod Haugland
 *         Created by tormod on 3/11/14.
 */
public class UnpackingConnector extends WebServiceConnector {

    private final Object _webService;
    private final HashMap<String, Method> _allowedMethods;

    /**
     * Default and only constructor, takes a webService as parameter. Finds all allowed methods.
     */
    public UnpackingConnector(Object webService) {
        super(webService);
        this._webService = webService;
        this._allowedMethods = new HashMap<>();

        /* Get all methods of this class */
        Method[] methods = this._webService.getClass().getMethods();

        for(Method method : methods){
            Annotation[] annotations = method.getAnnotations();

            /* Check that the method is a @WebMethod, if not, continue*/
            for(Annotation annotation : annotations){
                if(annotation instanceof WebMethod){
                    WebMethod webMethod = (WebMethod)annotation;
                    /* If the method is to be excluded as a webmethod */
                    if(webMethod.exclude()){
                        continue;
                    }
                    Log.d("UnpackingConnector", "Allowedmethod: " + ((WebMethod) annotation).operationName());
                    this._allowedMethods.put(webMethod.operationName(), method);
                    break;
                }
            }
        }
    }

    /**
     * The accept-message of UnpackingConnector. The message of the passed in {@link org.ntnunotif.wsnu.base.util.InternalMessage}
     * is attempted unwrapped and forwarded. This is done by first unwrapping the soap-body, and fetching the object from the body.
     * If the object-name is the same as the operationName of one of the Web Service's {@link javax.jws.WebMethod} methods,
     * this method is called with the object.
     * @param internalMessage
     * @return Anything coming back. Might be an exception.
     */
    @Override
    //TODO: ContextHandling
    //TODO: Support multiple messages
    public final InternalMessage acceptMessage(InternalMessage internalMessage) {

        synchronized (this){

            super.acceptMessage(internalMessage);

            /* The message */
            Object potentialEnvelope = internalMessage.getMessage();

            if (!Soap.isSoapEnvelope(potentialEnvelope)) {
                Log.e("UnpackingConnector", "Someone try to send something else than a Soap-Envelope.");
                return new InternalMessage(STATUS_FAULT|STATUS_FAULT_INVALID_PAYLOAD, null);
            }

            Soap soap = Soap.createSameAs(potentialEnvelope);

            /* Unpack the body */
            List<Object> messages;
            try {
                messages = soap.getBodyContent(potentialEnvelope);
            } catch(RuntimeException e) {
                Log.w("UnpackingConnector", "Invalid payload in SOAP envelope");
                return new InternalMessage(STATUS_FAULT|STATUS_FAULT_INVALID_PAYLOAD, null);
            }

            Log.d("UnpackingConnector", "Sending message to Web Service at " + _webService.toString());

            for(Object message : messages){

                /* The class of this message */
                Class objectClass = message.getClass();
                Annotation[] messageAnnotations = objectClass.getAnnotations();

                for(Annotation annotation : messageAnnotations){

                    /* Look for the annotation @XmlRootElement */
                    if(annotation instanceof XmlRootElement){
                        XmlRootElement xmlRootElement = (XmlRootElement)annotation;
                        /* Check if this connector's web service has a matching method */
                        if(_allowedMethods.containsKey(xmlRootElement.name())){
                            Method method = _allowedMethods.get(xmlRootElement.name());
                            try {
                                /* Run method on the Web Service */
                                InternalMessage returnMessage;

                                Object method_returnedData;

                                if(xmlRootElement.name().equals("Subscribe")) {
                                    method_returnedData = method.invoke(_webService, message, soap.version());
                                } else {
                                /* Spit this error-message out, however try and send the message regardless*/
                                    method_returnedData = method.invoke(_webService, message);
                                }

                                /* If is the case, nothing is being returned */
                                if (method.getReturnType().equals(Void.TYPE)) {
                                    returnMessage = new InternalMessage(STATUS_OK, null, soap.version());
                                } else {
                                    returnMessage = new InternalMessage(STATUS_OK | STATUS_HAS_MESSAGE, method_returnedData, soap.version());
                                }
                                return returnMessage;

                            } catch (IllegalAccessException e) {
                                Log.e("UnpackingConnector","The method being accessed is not public. Something must be wrong with the" +
                                        "org.generated classes.\n A @WebMethod can not have private access");
                                e.printStackTrace();
                                return new InternalMessage(STATUS_FAULT|STATUS_FAULT_INTERNAL_ERROR, null);
                            } catch (InvocationTargetException e) {
                                Log.d("UnpackingConnector", "Caught exception at the web service " + e.getCause().getClass() + " | " + e.getMessage());
                                return new InternalMessage(STATUS_FAULT|STATUS_EXCEPTION_SHOULD_BE_HANDLED, e.getTargetException(), soap.version());
                            }
                        }else{
                            Log.d("UnpackingConnector", "Invalid destination");
                            return new InternalMessage(STATUS_FAULT|STATUS_FAULT_INVALID_DESTINATION, null, soap.version());
                        }
                    }
                }
            }
            Log.d("UnpackingConnector", "Unknonwn method");
            return new InternalMessage(STATUS_FAULT|STATUS_FAULT_UNKNOWN_METHOD, null, soap.version());
        }
    }
}
