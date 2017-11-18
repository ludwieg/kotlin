package io.vito.ludwiegtest;

import org.junit.Assert;
import org.junit.Test;

import io.vito.ludwieg.Deserializer;
import io.vito.ludwieg.Registry;
import io.vito.ludwieg.Serializer;
import io.vito.ludwieg.models.MessageMeta;

public class AnotherUnitTest {
    @Test
    public void testSomething() {
        Registry.Companion.getInstance().register(io.vito.ludwiegtest.Test.class);
        io.vito.ludwiegtest.Test t = new io.vito.ludwiegtest.Test()
                .setFieldA(0x1b)
                .setFieldB(28)
                .setFieldC(29L)
                .setFieldD(30.2)
                .setFieldE("String")
                .setFieldF(new byte[]{0x27, 0x24, 0x50 })
                .setFieldG(true)
                .setFieldH("3232ee42c2f24baf841318335b4d5640")
                .setFieldZ("Robin", "Tom")
                .setFieldY("Any field retaining a string")
                .setFieldI(new TestSub().setFieldJ("Structure").setFieldK(new TestSubOther().setFieldL("Other Structure")))
                .setFieldZA(new TestCustomType().setFieldV("hello"), new TestCustomType().setFieldV("friend"));
        Serializer s = new Serializer();
        byte[] result = s.serialize(t, (byte)0x01);

        Deserializer d = new Deserializer();
        for(byte b : result) {
                if (d.read(b)) {
                    MessageMeta meta = d.getMessageMeta();
                    Object pack = d.getResult();
                    assert meta != null;
                    assert pack != null;
                    Assert.assertEquals(0x01, meta.getMessageID());
                    Assert.assertEquals(0x01, meta.getPackageType());
                    Assert.assertEquals(0x01, meta.getProtocolVersion());
                    Assert.assertTrue(pack instanceof io.vito.ludwiegtest.Test);
                    io.vito.ludwiegtest.Test x = (io.vito.ludwiegtest.Test)pack;
                    Assert.assertEquals(27, (int)x.getFieldA());
                    Assert.assertEquals(28,(int)x.getFieldB());
                    Assert.assertEquals(29, (long)x.getFieldC());
                    Assert.assertEquals((Double)30.2, x.getFieldD());
                    Assert.assertEquals("String", x.getFieldE());
                    Assert.assertArrayEquals(new byte[]{0x27, 0x24, 0x50 }, x.getFieldF());
                    Assert.assertTrue(x.getFieldG());
                    Assert.assertEquals("3232ee42c2f24baf841318335b4d5640", x.getFieldH());
                    Assert.assertArrayEquals(new String[]{"Robin", "Tom"}, x.getFieldZ().toArray());
                    Assert.assertTrue(x.getFieldY() instanceof String);
                    Assert.assertEquals("Any field retaining a string", x.getFieldY());
                    Assert.assertNotNull(x.getFieldI());
                    Assert.assertEquals("Structure", x.getFieldI().getFieldJ());
                    Assert.assertNotNull(x.getFieldI().getFieldK());
                    Assert.assertEquals("Other Structure", x.getFieldI().getFieldK().getFieldL());
                    Assert.assertNotNull(x.getFieldZA());
                    Assert.assertNotNull(x.getFieldZA().get(0));
                    Assert.assertNotNull(x.getFieldZA().get(1));
                    Assert.assertNotNull(x.getFieldZA().get(0).getFieldV());
                    Assert.assertNotNull(x.getFieldZA().get(1).getFieldV());
                    Assert.assertEquals("hello", x.getFieldZA().get(0).getFieldV());
                    Assert.assertEquals("friend", x.getFieldZA().get(1).getFieldV());
                    return;
                }
        }
        assert false;
    }
}
