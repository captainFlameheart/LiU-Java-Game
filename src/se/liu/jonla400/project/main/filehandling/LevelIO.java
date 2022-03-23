package se.liu.jonla400.project.main.filehandling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import se.liu.jonla400.project.main.leveldefinition.LevelDefinition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class LevelIO
{
    private LevelIO() {}

    public static LevelDefinition loadLevelFromFile(final Path path) throws IOException, JsonSyntaxException {
	final String levelAsJson = Files.readString(path);
	return convertJsonToLevel(levelAsJson);
    }

    public static LevelDefinition loadLevelFromResource(final String resourceName) throws IOException, JsonSyntaxException {
	final String levelAsJson = readStringFromResource(resourceName);
	return convertJsonToLevel(levelAsJson);
    }

    public static void saveLevelToFile(final LevelDefinition level, final Path path) throws IOException {
	final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	final String levelAsJson = gson.toJson(level);
	Files.writeString(path, levelAsJson);
    }

    private static LevelDefinition convertJsonToLevel(final String json) throws JsonSyntaxException {
	if (json.isEmpty()) {
	    return LevelDefinition.createEmpty();
	}
	final Gson gson = new Gson();
	final LevelDefinition level = gson.fromJson(json, LevelDefinition.class);
	if (level.isInvalid()) {
	    throw new JsonSyntaxException("The json specified a level with invalid fields");
	}
	return level;
    }

    private static String readStringFromResource(final String resourceName) throws IOException {
	final StringBuilder result = new StringBuilder();

	final URL resource = ClassLoader.getSystemResource(resourceName);
	if (resource == null) {
	    throw new IOException("Unable to access the resource " + resourceName);
	}
	try (final BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream()))) {
	    String line = reader.readLine();
	    while (line != null) {
		result.append(line);
		line = reader.readLine();
	    }
	}
	return result.toString();
    }
}
