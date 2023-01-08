package ciotola.network.connection;

public interface ChannelPipeline {
    void addLast(ChannelHandler handler);
}
