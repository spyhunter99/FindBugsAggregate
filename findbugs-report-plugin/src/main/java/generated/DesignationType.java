//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.09.30 at 11:45:12 AM EDT 
//


package generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for designationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="designationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="UNCLASSIFIED"/>
 *     &lt;enumeration value="BAD_ANALYSIS"/>
 *     &lt;enumeration value="NOT_A_BUG"/>
 *     &lt;enumeration value="MOSTLY_HARMLESS"/>
 *     &lt;enumeration value="SHOULD_FIX"/>
 *     &lt;enumeration value="MUST_FIX"/>
 *     &lt;enumeration value="I_WILL_FIX"/>
 *     &lt;enumeration value="OBSOLETE_CODE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "designationType")
@XmlEnum
public enum DesignationType {

    UNCLASSIFIED,
    BAD_ANALYSIS,
    NOT_A_BUG,
    MOSTLY_HARMLESS,
    SHOULD_FIX,
    MUST_FIX,
    I_WILL_FIX,
    OBSOLETE_CODE;

    public String value() {
        return name();
    }

    public static DesignationType fromValue(String v) {
        return valueOf(v);
    }

}