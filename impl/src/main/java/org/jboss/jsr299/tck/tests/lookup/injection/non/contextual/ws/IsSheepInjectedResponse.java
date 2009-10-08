
package org.jboss.jsr299.tck.tests.lookup.injection.non.contextual.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "isSheepInjectedResponse", propOrder = {
    "_return"
})
public class IsSheepInjectedResponse {

    @XmlElement(name = "return")
    protected boolean _return;

    public boolean isReturn() {
        return _return;
    }

    public void setReturn(boolean value) {
        this._return = value;
    }
}
