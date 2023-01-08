package ciotola.network.connection;

public interface PollingConnection<R,W> extends Connection{
    int size();
    R read();
    void write(W msg);
}
