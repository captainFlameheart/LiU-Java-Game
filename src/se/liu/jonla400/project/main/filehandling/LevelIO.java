package se.liu.jonla400.project.main.filehandling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import se.liu.jonla400.project.main.leveldefinition.LevelDefinition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LevelIO
{
    private LevelIO() {}

    public static LevelDefinition loadLevel(final Path path) throws IOException, JsonSyntaxException {
	final String levelAsJson = Files.readString(path);
	final Gson gson = new Gson();
	final LevelDefinition level = gson.fromJson(levelAsJson, LevelDefinition.class);
	return level != null ? level : LevelDefinition.createEmpty();
    }

    public static void saveLevel(final LevelDefinition level, final Path path) throws IOException {
	final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	final String levelAsJson = gson.toJson(level);
	Files.writeString(path, levelAsJson);
    }
}
