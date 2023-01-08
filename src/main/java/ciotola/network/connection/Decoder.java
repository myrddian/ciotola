package ciotola.network.connection;

public interface Decoder<I> {
    /**
     * Decode the data held in the buffer
     * @param data array of bytes to decode
     * @return
     */
    I decode(byte [] data);

    /**
     * Return the minimum number of bytes needed to peek
     * @return
     */
    int minSize();

    /**
     * Return how many bytes to fetch before decoding
     * @param data - peeking data
     * @return the number of bytes required to be held in RAM before decoding
     */
    int peek(byte [] data);
}
