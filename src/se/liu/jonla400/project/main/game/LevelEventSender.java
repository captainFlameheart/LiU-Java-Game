package se.liu.jonla400.project.main.game;

import se.liu.jonla400.project.main.leveldefinition.LineSegmentType;
import se.liu.jonla400.project.physics.collision.CollisionData;
import se.liu.jonla400.project.physics.collision.CollisionListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Messages a set of {@link LevelListener} when a level is failed or completed based on line segments
 * collided with
 */
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

    /**
     * Creates a new LevelEventSender initially without listeners.
     *
     * @return The createt LevelEventSender
     */
    public static LevelEventSender createWithoutListeners() {
        Map<LineSegmentType, Consumer<LevelListener>> segmentTypeToListenerAction = Map.of(
                LineSegmentType.LOOSE, LevelListener::onLevelFailed,
                LineSegmentType.WIN, LevelListener::onLevelCompleted
        );
        return new LevelEventSender(new ArrayList<>(), segmentTypeToListenerAction);
    }

    /**
     * Adds a listener
     *
     * @param listener The listener to add
     */
    public void addListener(final LevelListener listener) {
        listeners.add(listener);
    }

    /**
     * Potentially notifies listeners about a level event, depending on the type of line segment
     * collided with
     *
     * @param collision The collision that occured
     */
    @Override public void collisionOccurred(final CollisionData<LineSegmentType> collision) {
        getListenerActionOf(collision).ifPresent(listeners::forEach);
    }

    private Optional<Consumer<LevelListener>> getListenerActionOf(final CollisionData<LineSegmentType> collision) {
        final LineSegmentType segmentType = collision.getUserData();
        return Optional.ofNullable(segmentTypeToListenerAction.get(segmentType));
    }
}
