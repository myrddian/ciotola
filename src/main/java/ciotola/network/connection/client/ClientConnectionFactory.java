package ciotola.network.connection.client;

import ciotola.network.connection.Decoder;
import ciotola.network.connection.Encoder;
import ciotola.network.connection.PollingConnection;

public class ClientConnectionFactory<I,O> {

    public PollingConnection<I,O> createPollingConnection(Decoder<I> decoder, Encoder<O> encoder) {

        return new ClientConnectionContainer<I,O,PollingConnection>();
    }

}
