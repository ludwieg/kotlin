package io.vito.ludwiegtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.vito.ludwieg.*;
import io.vito.ludwieg.types.*;

@LudwiegPackage(id = 0x01)
public class Test {
    public Test() {
        fieldA = new TypeUint8();
        fieldB = new TypeUint32();
        fieldC = new TypeUint64();
        fieldD = new TypeFloat64();
        fieldE = new TypeString();
        fieldF = new TypeBlob();
        fieldG = new TypeBool();
        fieldH = new TypeUUID();
        fieldZ = new TypeArray<>(TypeString.class);
        fieldY = new TypeAny();
        fieldI = new TypeStruct<>(TestSub.class);
        fieldZA = new TypeArray<>(TypeStruct.class);
    }

    @LudwiegField(index = 0, protocolType = ProtocolType.UINT8)
    private TypeUint8 fieldA;
    @LudwiegField(index = 1, protocolType = ProtocolType.UINT32)
    private TypeUint32 fieldB;
    @LudwiegField(index = 2, protocolType = ProtocolType.UINT64)
    private TypeUint64 fieldC;
    @LudwiegField(index = 3, protocolType = ProtocolType.FLOAT64)
    private TypeFloat64 fieldD;
    @LudwiegField(index = 4, protocolType = ProtocolType.STRING)
    private TypeString fieldE;
    @LudwiegField(index = 5, protocolType = ProtocolType.BLOB)
    private TypeBlob fieldF;
    @LudwiegField(index = 6, protocolType = ProtocolType.BOOL)
    private TypeBool fieldG;
    @LudwiegField(index = 7, protocolType = ProtocolType.UUID)
    private TypeUUID fieldH;
    @LudwiegField(index = 8, protocolType = ProtocolType.ANY)
    private TypeAny fieldY;
    @LudwiegField(index = 9, protocolType = ProtocolType.ARRAY, arrayType = ProtocolType.STRING)
    private TypeArray<TypeString> fieldZ;
    @LudwiegField(index = 10, protocolType = ProtocolType.ARRAY, arrayType = ProtocolType.STRUCT, structType = TestCustomType.class)
    private TypeArray<TypeStruct<TestCustomType>> fieldZA;
    @LudwiegField(index = 11, protocolType = ProtocolType.STRUCT, structType = TestSub.class)
    private TypeStruct<TestSub> fieldI;

    public Integer getFieldA() {
        return fieldA.getValue();
    }
    public Integer getFieldB() {
        return fieldB.getValue();
    }
    public Long getFieldC() {
        return fieldC.getValue();
    }
    public Double getFieldD() {
        return fieldD.getValue();
    }
    public String getFieldE() {
        return fieldE.getValue();
    }
    public byte[] getFieldF() {
        return fieldF.getValue();
    }
    public Boolean getFieldG() {
        return fieldG.getValue();
    }
    public UUID getFieldH() {
        return fieldH.getValue();
    }
    public Object getFieldY() {
        return fieldY.getValue() == null ? null : fieldY.getValue().getValue();
    }
    public List<String> getFieldZ() { return fieldZ.getNativeArray(String.class); }
    public TestSub getFieldI() { return fieldI.getNativeValue(); }

    public List<TestCustomType> getFieldZA() {
        ArrayList<TypeStruct<TestCustomType>> list = fieldZA.getValue();
        ArrayList<TestCustomType> arr = new ArrayList<>();
        if(list != null) {
            for (TypeStruct s : list) {
                arr.add((TestCustomType)s.getNativeValue());
            }
        }
        return arr;
    }

    public Test setFieldA(Integer v) {
        fieldA.setValue(v);
        return this;
    }
    public Test setFieldB(Integer v) {
        fieldB.setValue(v);
        return this;
    }
    public Test setFieldC(Long v) {
        fieldC.setValue(v);
        return this;
    }
    public Test setFieldD(Double v) {
        fieldD.setValue(v);
        return this;
    }
    public Test setFieldE(String v) {
        fieldE.setValue(v);
        return this;
    }
    public Test setFieldF(byte[] v) {
        fieldF.setValue(v);
        return this;
    }
    public Test setFieldG(Boolean v) {
        fieldG.setValue(v);
        return this;
    }
    public Test setFieldH(UUID v) {
        fieldH.setValue(v);
        return this;
    }
    public Test setFieldY(Object o) {
        fieldY.setValue(Type.Companion.coerce(o));
        return this;
    }
    public Test setFieldI(TestSub o) {
        fieldI.setNativeValue(o);
        return this;
    }

    public <T> Test setFieldY(T... a) {
        fieldY.setValue(Type.Companion.coerce(new ArrayList<>(Arrays.asList(a))));
        return this;
    }

    public Test setFieldZ(Collection<String> v) {
        fieldZ.setNativeArray(v);
        return this;
    }

    public Test setFieldZ(String... a) {
        return this.setFieldZ(Arrays.asList(a));
    }

    public Test setFieldZA(Collection<TestCustomType> a) {
        // At this point, we need to coerce TestCustomType to TypeStruct<TestCustomType>
        ArrayList<TypeStruct<TestCustomType>> arr = new ArrayList<>();
        for(TestCustomType i : a) {
            TypeStruct<TestCustomType> inst = new TypeStruct<>(TestCustomType.class);
            inst.setNativeValue(i);
            arr.add(inst);
        }
        this.fieldZA.setValue(arr); // IMPORTANT: do not use setNativeArray with
                                    // TypeArray<TypeStruct>.
        return this;
    }

    public Test setFieldZA(TestCustomType... a) {
        return this.setFieldZA(Arrays.asList(a));
    }
}
