package io.vito.ludwiegtest;

import io.vito.ludwieg.LudwiegField;
import io.vito.ludwieg.Serializable;
import io.vito.ludwieg.types.ProtocolType;
import io.vito.ludwieg.types.TypeString;
import io.vito.ludwieg.types.TypeStruct;

@Serializable
public final class TestSubOther {
    public TestSubOther() {
        fieldL = new TypeString();
    }

    @LudwiegField(index = 0, protocolType = ProtocolType.STRING)
    private TypeString fieldL;

    public String getFieldL() { return fieldL.getValue(); }
    public TestSubOther setFieldL(String v) { fieldL.setValue(v); return this; }
}
