package io.vito.ludwieg;

import java.util.Arrays;

import kotlin.text.Regex;

/**
 * UUID represents an UUID4 value, comprehending a 122-bit random number.
 */
@SuppressWarnings("WeakerAccess")
public final class UUID {

    /**
     * Creates a new UUID instance from the given UUID value.
     * @param value String containing a UUID4 value. Throws an {@link InvalidUUIDValueException} if the input
     *              is out of the format <code>/^[0-9a-f]{32}$/i</code>. Hyphens are automatically
     *              stripped. Must not be null.
     * @return an UUID instance representing the input value
     */
    public static UUID from(final String value) {
        UUID val = new UUID();
        val.set(value);
        return val;
    }

    /**
     * Creates a new UUID instance from the given UUID value.
     * @param value Byte array containing 16 bytes. Must not be null
     * @return an UUID instance representing the input value
     */
    public static UUID from(final byte[] value) {
        UUID val = new UUID();
        val.set(value);
        return val;
    }

    /**
     * Creates a new UUID instance from the native {@link java.util.UUID} class.
     * @param value A non-null {@link java.util.UUID} instance
     * @return an UUID instance representing the input value
     */
    public static UUID from(final java.util.UUID value) {
        UUID val = new UUID();
        val.set(value);
        return val;
    }

    /**
     * Creates a new UUID instance holding a random value generated by {@link java.util.UUID}
     * @return A new UUID instance holding a random value
     */
    public static UUID random() {
        return from(java.util.UUID.randomUUID());
    }

    private UUID() {}
    private byte[] value;
    private final static Regex validUUID = new Regex("^[0-9a-f]{32}$");

    /**
     * Sets the content of this instance to the provided value.
     * @param value non-null UUID instance holding a native UUID value
     */
    public final void set(final java.util.UUID value) {
        if(value == null) throw new IllegalArgumentException("value cannot be null");
        this.set(value.toString());
    }

    /**
     * Sets the content of this instance to the provided value.
     * @throws InvalidUUIDValueException if the input value does not conform to the method expectations
     * @param rawValue non-null string containing a 32-byte UUID value conforming to the format
     *                 <code>/^[0-9a-f]{32}$/i</code>. Hyphens are ignored.
     */
    public final void set(final String rawValue) {
        if(rawValue == null) throw new IllegalArgumentException("value cannot be null");
        String val = rawValue.replace("-", "").toLowerCase();
        if(!validUUID.containsMatchIn(val)) throw new InvalidUUIDValueException();
        byte[] buffer = new byte[16];
        int i = 0;
        int pos = 0;
        while(i < 32) {
            buffer[pos++] = (byte)((Character.digit(val.charAt(i), 16) << 4) + Character.digit(val.charAt(i+1), 16));
            i += 2;
        }
        this.set(buffer);
    }

    /**
     * Sets the content of this instance to the provided value
     * @throws InvalidUUIDValueException if the input value does not conform to the method expectations
     * @param value non-null 16-byte array.
     */
    public final void set(final byte[] value) {
        if(value == null) throw new IllegalArgumentException("value cannot be null");
        if(value.length != 16) throw new InvalidUUIDValueException();
        this.value = value;
    }

    /**
     * Returns the value of this instance as an UUID string, using the format specified by the
     * RFC 4122.
     * @return The value of this instance, using format specified by RFC 4122
     */
    public final String toString() {
        StringBuilder builder = new StringBuilder(36);
        for (byte b : value) {
            builder.append(String.format("%02x", b));
        }
        return builder
                .insert(8, "-")
                .insert(13, "-")
                .insert(18, "-")
                .insert(23, "-")
                .toString();
    }

    /**
     * Returns a byte array representing this instance value
     * @return a byte array representing this instance value
     */
    public final byte[] toByteArray() {
        return value;
    }

    /**
     * Returns a native UUID representation of the value of this instance
     * @return a native UUID representation of the value of this instance
     */
    public java.util.UUID toNative() {
        return java.util.UUID.fromString(this.toString());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UUID && Arrays.equals(value, ((UUID) obj).value);
    }
}
