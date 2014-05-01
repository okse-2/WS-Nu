package org.ntnunotif.wsnu.base.net;

import javax.xml.bind.ValidationEvent;
import javax.xml.namespace.QName;
import java.util.Stack;

/**
 * Created by Inge on 28.04.2014.
 */
public class NuParseValidationEventInfo {
    /**
     * {@link javax.xml.bind.ValidationEvent#WARNING}
     */
    public static final int WARNING = ValidationEvent.WARNING;
    /**
     * {@link javax.xml.bind.ValidationEvent#ERROR}
     */
    public static final int ERROR = ValidationEvent.ERROR;
    /**
     * {@link javax.xml.bind.ValidationEvent#FATAL_ERROR}
     */
    public static final int FATAL_ERROR = ValidationEvent.FATAL_ERROR;

    private final ValidationEvent validationEvent;
    private final int severity;

    private final QName elementName;
    private final Stack<QName> elementPath;
    private final int parseEventType;
    private final boolean isStartElement;
    private final boolean isEndElement;

    /**
     * Creates a new <code>NuParseValidationEventInfo</code> with the given information.
     *
     * @param validationEvent The event that caused the info object to be created
     * @param severity        how serious the fault is
     * @param elementName     the name of the element (if start or end element, <code>null</code> otherwise)
     * @param elementPath     the path to the element
     * @param parseEventType  the event type in the parser
     * @param isStartElement  if this represents a start element
     * @param isEndElement    if this represents an end element
     */
    public NuParseValidationEventInfo(ValidationEvent validationEvent, int severity, QName elementName,
                                      Stack<QName> elementPath, int parseEventType, boolean isStartElement,
                                      boolean isEndElement) {

        this.validationEvent = validationEvent;
        this.severity = severity;
        this.elementName = elementName;
        this.elementPath = elementPath;
        this.parseEventType = parseEventType;
        this.isStartElement = isStartElement;
        this.isEndElement = isEndElement;
    }

    /**
     * Gets the validation event that caused this info to be generated
     *
     * @return the corresponding {@link javax.xml.bind.ValidationEvent}
     */
    public ValidationEvent getValidationEvent() {
        return validationEvent;
    }

    /**
     * Gets the name of the element that caused this info to be generated if parsing was at a start or end element.
     *
     * @return the name of this element if parser was at a start or end element. <code>null</code> otherwise
     */
    public QName getElementName() {
        return elementName;
    }

    /**
     * Gets the path of the element that caused this info to be created. If the element was a start element, the element
     * name is the top element of the stack. Bottom of the element is the root.
     *
     * @return the path of the element that caused a validation event
     */
    public Stack<QName> getElementPath() {
        return elementPath;
    }

    /**
     * Gets the severity of the validation event that caused this info to be created.
     * {@link javax.xml.bind.ValidationEvent#getSeverity()}
     *
     * @return the severity of the event
     */
    public int getSeverity() {
        return severity;
    }

    /**
     * Returns the event type of the parsing event that caused the validation event to fail.
     *
     * @return Event type {@link javax.xml.stream.XMLStreamReader#getEventType()}
     */
    public int getParseEventType() {
        return parseEventType;
    }

    /**
     * Tells if the element that caused the validation was a start element
     *
     * @return if the element was a start element
     */
    public boolean isStartElement() {
        return isStartElement;
    }

    /**
     * Tells if the element that caused the validation was an end element
     *
     * @return if the element was an end element
     */
    public boolean isEndElement() {
        return isEndElement;
    }
}