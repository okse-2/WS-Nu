//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.04.29 at 10:40:46 AM CEST 
//


package org.ntnunotif.wsnu.examples.StockBroker.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Symbol" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LastChange" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="ChangeAbsolute" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="ChangeRelative" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "symbol",
    "name",
    "lastChange",
    "value",
    "changeAbsolute",
    "changeRelative"
})
@XmlRootElement(name = "StockChanged")
public class StockChanged {

    @XmlElement(name = "Symbol", required = true)
    protected String symbol;
    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "LastChange", required = true)
    protected String lastChange;
    @XmlElement(name = "Value")
    protected float value;
    @XmlElement(name = "ChangeAbsolute")
    protected float changeAbsolute;
    @XmlElement(name = "ChangeRelative")
    protected float changeRelative;

    /**
     * Gets the value of the symbol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Sets the value of the symbol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSymbol(String value) {
        this.symbol = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the lastChange property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastChange() {
        return lastChange;
    }

    /**
     * Sets the value of the lastChange property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastChange(String value) {
        this.lastChange = value;
    }

    /**
     * Gets the value of the value property.
     * 
     */
    public float getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     */
    public void setValue(float value) {
        this.value = value;
    }

    /**
     * Gets the value of the changeAbsolute property.
     * 
     */
    public float getChangeAbsolute() {
        return changeAbsolute;
    }

    /**
     * Sets the value of the changeAbsolute property.
     * 
     */
    public void setChangeAbsolute(float value) {
        this.changeAbsolute = value;
    }

    /**
     * Gets the value of the changeRelative property.
     * 
     */
    public float getChangeRelative() {
        return changeRelative;
    }

    /**
     * Sets the value of the changeRelative property.
     * 
     */
    public void setChangeRelative(float value) {
        this.changeRelative = value;
    }


    @Override
    public String toString() {
        return "StockChanged{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", lastChange='" + lastChange + '\'' +
                ", value=" + value +
                ", changeAbsolute=" + changeAbsolute +
                ", changeRelative=" + changeRelative +
                '}';
    }
}
