package ciotola.network.connection.client;

import ciotola.actor.AgentPort;
import ciotola.actor.NotifySourceActor;
import ciotola.network.ByteHandler;
import ciotola.network.connection.Decoder;

import java.io.IOException;
import java.io.InputStream;

public class InputDecoderWrapper<I> implements NotifySourceActor<I> {

    private Decoder<I> decoder;

    private InputStream networkInputStream;
    private byte[] minHeaderBuffer;
    private byte[] message;
    private boolean isOpen;

    @Override
    public void execute(AgentPort<I> target) {
        try {
            for (int byteCount = 0; byteCount < decoder.minSize(); ++byteCount) {
                int val = networkInputStream.read();
                if (val != -1) {
                    minHeaderBuffer[byteCount] = (byte) (val & 0xFF);
                }
            }
            int nextBytes = decoder.peek(minHeaderBuffer);
            byte[] content = new byte[nextBytes];
            int rsize = networkInputStream.read(content);
            message = new byte[decoder.minSize() + nextBytes];
            ByteHandler.copyBytesTo(minHeaderBuffer,message,0,decoder.minSize());
            ByteHandler.copyBytesTo(content,message,decoder.minSize(),rsize);
            target.write(decoder.decode(message));
        } catch (IOException e) {
            isOpen = false;
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isReady() {
        return isOpen;
    }

    public void startInputDecode(Decoder<I> decoder, InputStream inputStream) {
        this.decoder = decoder;
        this.networkInputStream = inputStream;
        isOpen = true;
        minHeaderBuffer = new byte[decoder.minSize()];
    }

    public void close() {
        isOpen = false;
    }
}
