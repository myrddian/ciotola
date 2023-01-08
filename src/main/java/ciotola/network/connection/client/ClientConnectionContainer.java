package ciotola.network.connection.client;

import ciotola.actor.AgentPort;
import ciotola.actor.SinkActor;
import ciotola.actor.SourceRecord;
import ciotola.network.connection.Connection;
import ciotola.network.connection.InputMessageHandler;
import ciotola.network.connection.PollingConnection;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

final class ClientConnectionContainer<I,O,T> implements ClientConnection<T>, Connection, SinkActor<I>, PollingConnection<I, O> {

    private AgentPort<O> outPort;
    private AgentPort<I> inPort;
    private InputMessageHandler<I> handler;
    private ClientConnectionFactory connectionFactory;
    private BlockingQueue<I> incomingMessages = new LinkedBlockingQueue<>();

    @Override
    public T openConnection(String hostname, int port) {
        return (T) this;
    }


    @Override
    public int size() {
        return incomingMessages.size();
    }

    @Override
    public I read() {
        try {
            return incomingMessages.poll(250, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return null;
        }
    }

    @Override
    public void write(O value) {
        outPort.write(value);
    }


    public boolean setReader(InputMessageHandler<I> handler) {
        if(handler!=null) {
            if(this.handler == null) {
                this.handler = handler;
            }
        } else {
            return  false;
        }
        return true;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public boolean close() {
        return false;
    }

    @Override
    public void onRecord(SourceRecord<I> record) {
        incomingMessages.add(record.getValue());
    }
}
