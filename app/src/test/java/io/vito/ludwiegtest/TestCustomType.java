package io.vito.ludwiegtest;

import io.vito.ludwieg.LudwiegField;
import io.vito.ludwieg.Serializable;
import io.vito.ludwieg.types.ProtocolType;
import io.vito.ludwieg.types.TypeString;

@Serializable
public final class TestCustomType {
    public TestCustomType() {
        fieldV = new TypeString();
    }

    @LudwiegField(index = 0, protocolType = ProtocolType.STRING)
    private TypeString fieldV;

    public String getFieldV() { return fieldV.getValue(); }
    public TestCustomType setFieldV(String v) { fieldV.setValue(v); return this; }
}
