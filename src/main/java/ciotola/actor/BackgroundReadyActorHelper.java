package ciotola.actor;

final class BackgroundReadyActorHelper implements BackgroundActor{
    private BackgroundReadyActor actor;

    public BackgroundReadyActorHelper(BackgroundReadyActor actor) {
        this.actor = actor;
    }

    @Override
    public void process() {
        actor.process();
    }

    @Override
    public long getDelay() {
        return 0L;
    }

    @Override
    public boolean isReady() {
        return actor.isReady();
    }
}
