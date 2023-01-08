package ciotola.actor;

final class BackgroundDelayActorHelper implements BackgroundActor{

    private BackgroundDelayActor actor;

    public BackgroundDelayActorHelper(BackgroundDelayActor actor) {
        this.actor = actor;
    }

    @Override
    public void process() {
        actor.process();
    }

    @Override
    public long getDelay() {
        return actor.getDelay();
    }

    @Override
    public boolean isReady() {
        return true;
    }
}
