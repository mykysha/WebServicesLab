
package org.example.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ColumnType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ColumnType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="INT"/&gt;
 *     &lt;enumeration value="CHAR"/&gt;
 *     &lt;enumeration value="REAL"/&gt;
 *     &lt;enumeration value="STRING"/&gt;
 *     &lt;enumeration value="COMPLEXINT"/&gt;
 *     &lt;enumeration value="COMPLEXREAL"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ColumnType")
@XmlEnum
public enum ColumnType {

    INT,
    CHAR,
    REAL,
    STRING,
    COMPLEXINT,
    COMPLEXREAL;

    public String value() {
        return name();
    }

    public static ColumnType fromValue(String v) {
        return valueOf(v);
    }

}
