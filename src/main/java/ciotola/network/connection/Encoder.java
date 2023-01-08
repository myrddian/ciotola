package ciotola.network.connection;

public interface Encoder<I> {
    /***
     * Encode a given message to bytes
     * @param message the message to encode
     * @return the byte array of the message
     */
    byte [] encode(I message);
}
