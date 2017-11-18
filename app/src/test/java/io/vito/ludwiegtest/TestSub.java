package io.vito.ludwiegtest;

import io.vito.ludwieg.*;
import io.vito.ludwieg.types.*;

@Serializable
public class TestSub {
    public TestSub() {
        fieldJ = new TypeString();
        fieldK = new TypeStruct<>(TestSubOther.class);
    }

    @LudwiegField(index = 0, protocolType = ProtocolType.STRING)
    private TypeString fieldJ;
    @LudwiegField(index = 1, protocolType = ProtocolType.STRUCT, structType = TestSubOther.class)
    private TypeStruct<TestSubOther> fieldK;

    public String getFieldJ() { return fieldJ.getValue(); }
    public TestSub setFieldJ(String v) { fieldJ.setValue(v); return this; }

    public TestSubOther getFieldK() { return fieldK.getNativeValue(); }
    public TestSub setFieldK(TestSubOther v) { fieldK.setNativeValue(v); return this; }

}
