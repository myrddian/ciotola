package ciotola.network.connection;

public interface ChannelContext<O> {
    ChannelAttributes getAttributes();
    void write(O msg);

}
