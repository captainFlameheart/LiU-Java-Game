package se.liu.jonla400.project.main;

import se.liu.jonla400.project.math.Vector2D;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class MouseGameController extends MouseInputAdapter
{
    private final static PressReleaseCommandPair TRANSLATION_COMMANDS = new PressReleaseCommandPair(
	    Game::startTranslation, Game::endTranslation
    );
    private final static PressReleaseCommandPair LEFT_ROTATION_COMMANDS = new PressReleaseCommandPair(
	    game -> game.startRotation(RotationDirection.LEFT), game -> game.endRotation(RotationDirection.LEFT)
    );
    private final static PressReleaseCommandPair RIGHT_ROTATION_COMMANDS = new PressReleaseCommandPair(
	    game -> game.startRotation(RotationDirection.RIGHT), game -> game.endRotation(RotationDirection.RIGHT)
    );

    private Map<Integer, PressReleaseCommandPair> mouseButtonBinding;
    private Game game;
    private Function<Vector2D, Vector2D> mouseToGamePointConverter;

    private MouseGameController(final Map<Integer, PressReleaseCommandPair> mouseButtonBinding, final Game game,
				final Function<Vector2D, Vector2D> mouseToGamePointConverter)
    {
	this.mouseButtonBinding = mouseButtonBinding;
	this.game = game;
	this.mouseToGamePointConverter = mouseToGamePointConverter;
    }

    public static MouseGameController create(
	    final int translationMouseButton, final int leftRotationMouseButton, final int rightRotationMouseButton,
	    final Game game, final Function<Vector2D, Vector2D> mouseToGamePointConverter)
    {
	final Map<Integer, PressReleaseCommandPair> mouseButtonBinding = Map.of(
		translationMouseButton, TRANSLATION_COMMANDS,
		leftRotationMouseButton, LEFT_ROTATION_COMMANDS,
		rightRotationMouseButton, RIGHT_ROTATION_COMMANDS
	);
	return new MouseGameController(mouseButtonBinding, game, mouseToGamePointConverter);
    }

    public static MouseGameController createWithDefaultControls(
	    final Game game, final Function<Vector2D, Vector2D> mouseToGamePointConverter)
    {
	return create(MouseEvent.BUTTON2, MouseEvent.BUTTON1, MouseEvent.BUTTON3, game, mouseToGamePointConverter);
    }

    @Override public void mouseDragged(final MouseEvent e) {
	setGameCursorPos(e);
    }

    @Override public void mouseMoved(final MouseEvent e) {
	setGameCursorPos(e);
    }

    @Override public void mousePressed(final MouseEvent e) {
	performMouseButtonCommand(true, e);
    }

    @Override public void mouseReleased(final MouseEvent e) {
	performMouseButtonCommand(false, e);
    }

    private void setGameCursorPos(final MouseEvent e) {
	final Vector2D mousePos = Vector2D.createCartesianVector(e.getX(), e.getY());
	final Vector2D mousePosInGameSpace = mouseToGamePointConverter.apply(mousePos);
	game.setCursorPos(mousePosInGameSpace);
    }

    private void performMouseButtonCommand(final boolean mouseWasPressed, final MouseEvent e) {
	final PressReleaseCommandPair commandPair = mouseButtonBinding.get(e.getButton());
	if (commandPair != null) {
	    commandPair.performCommand(mouseWasPressed, game);
	}
    }

    private static class PressReleaseCommandPair {
	private Map<Boolean, Consumer<Game>> commands;

	private PressReleaseCommandPair(final Consumer<Game> pressCommand, final Consumer<Game> releaseCommand) {
	    commands = Map.of(true, pressCommand, false, releaseCommand);
	}

	private void performCommand(final boolean mouseWasPressed, final Game game) {
	    commands.get(mouseWasPressed).accept(game);
	}
    }
}
