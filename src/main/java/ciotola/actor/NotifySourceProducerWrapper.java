package ciotola.actor;

final class NotifySourceProducerWrapper<T> implements NotifySourceProducer<T> {

    private SourceProducer<T> producer;

    public NotifySourceProducerWrapper(SourceProducer<T> sourceProducer){
        producer = sourceProducer;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void execute(AgentPort<T> target) {
        producer.execute(target);
    }
}
