package se.liu.jonla400.project.main.game;

import se.liu.jonla400.project.main.leveldefinition.LineSegmentType;
import se.liu.jonla400.project.physics.collision.CollisionData;
import se.liu.jonla400.project.physics.collision.CollisionListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class LevelEventSender implements CollisionListener<LineSegmentType>
{
    private Collection<LevelListener> listeners;
    private Map<LineSegmentType, Consumer<LevelListener>> segmentTypeToListenerAction;

    private LevelEventSender(final Collection<LevelListener> listeners,
                            final Map<LineSegmentType, Consumer<LevelListener>> segmentTypeToListenerAction)
    {
        this.listeners = listeners;
        this.segmentTypeToListenerAction = segmentTypeToListenerAction;
    }

    public static LevelEventSender createWithoutListeners() {
        Map<LineSegmentType, Consumer<LevelListener>> segmentTypeToListenerAction = Map.of(
                LineSegmentType.LOOSE, LevelListener::levelFailed,
                LineSegmentType.WIN, LevelListener::levelCompleted
        );
        return new LevelEventSender(new ArrayList<>(), segmentTypeToListenerAction);
    }

    public void addListener(final LevelListener listener) {
        listeners.add(listener);
    }

    @Override public void collisionOccured(final CollisionData<LineSegmentType> collision) {
        getListenerActionOf(collision).ifPresent(listeners::forEach);
    }

    private Optional<Consumer<LevelListener>> getListenerActionOf(final CollisionData<LineSegmentType> collision) {
        final LineSegmentType segmentType = collision.getUserData();
        return Optional.ofNullable(segmentTypeToListenerAction.get(segmentType));
    }
}
