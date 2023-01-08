package ciotola.network.connection;

public interface InputMessageHandler<I> {
    void read(I message);
}
