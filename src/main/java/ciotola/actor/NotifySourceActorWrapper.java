package ciotola.actor;

final class NotifySourceActorWrapper<T> implements NotifySourceActor<T> {

    private SourceActor<T> producer;

    public NotifySourceActorWrapper(SourceActor<T> sourceActor){
        producer = sourceActor;
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
