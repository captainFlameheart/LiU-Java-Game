package se.liu.jonla400.restructure.filehandling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import se.liu.jonla400.restructure.main.leveldefinition.LevelDefinition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LevelIO
{
    private LevelIO() {}

    public static LevelDefinition loadLevel(final Path path) throws IOException, JsonSyntaxException {
	final String levelAsJson = Files.readString(path);
	final Gson gson = new Gson();
	final LevelDefinition levelDef = gson.fromJson(levelAsJson, LevelDefinition.class);
	return levelDef != null ? levelDef : LevelDefinition.createEmpty();
    }

    public static void saveLevel(final LevelDefinition levelDef, final Path path) throws IOException {
	final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	final String levelAsJson = gson.toJson(levelDef);
	Files.writeString(path, levelAsJson);
    }
}
