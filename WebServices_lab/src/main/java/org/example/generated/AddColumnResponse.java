
package org.example.generated;

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
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AddColumnResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "addColumnResult"
})
@XmlRootElement(name = "AddColumnResponse")
public class AddColumnResponse {

    @XmlElement(name = "AddColumnResult")
    protected boolean addColumnResult;

    /**
     * Gets the value of the addColumnResult property.
     * 
     */
    public boolean isAddColumnResult() {
        return addColumnResult;
    }

    /**
     * Sets the value of the addColumnResult property.
     * 
     */
    public void setAddColumnResult(boolean value) {
        this.addColumnResult = value;
    }

}
