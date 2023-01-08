package ciotola.network.connection.client;

import ciotola.network.connection.Connection;

public interface ClientConnection<T>  {
    T openConnection(String hostname, int port);

}
