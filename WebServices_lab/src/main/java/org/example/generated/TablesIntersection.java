
package org.example.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="tableIndex1" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="tableIndex2" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
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
    "tableIndex1",
    "tableIndex2"
})
@XmlRootElement(name = "TablesIntersection")
public class TablesIntersection {

    protected int tableIndex1;
    protected int tableIndex2;

    /**
     * Gets the value of the tableIndex1 property.
     * 
     */
    public int getTableIndex1() {
        return tableIndex1;
    }

    /**
     * Sets the value of the tableIndex1 property.
     * 
     */
    public void setTableIndex1(int value) {
        this.tableIndex1 = value;
    }

    /**
     * Gets the value of the tableIndex2 property.
     * 
     */
    public int getTableIndex2() {
        return tableIndex2;
    }

    /**
     * Sets the value of the tableIndex2 property.
     * 
     */
    public void setTableIndex2(int value) {
        this.tableIndex2 = value;
    }

}
