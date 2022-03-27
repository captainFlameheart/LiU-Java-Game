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

/**
 * Contains methods for loading {@link LevelDefinition} objects from files on the local file system
 * as well as from resource files. This class is also used to save levels to the local file system.
 */
public class LevelIO
{
    private LevelIO() {}

    /**
     * Loads a level from the local file system at the given {@link Path}. If the file is empty
     * a default {@link LevelDefinition} is returned.
     *
     * @param path The path of the file
     * @return The level definition expressed in json by the file
     * @throws IOException If trouble reading the file
     * @throws JsonSyntaxException If the contents of the file is unable to express a valid level definition
     */
    public static LevelDefinition loadLevelFromFile(final Path path) throws IOException, JsonSyntaxException {
	final String levelAsJson = Files.readString(path);
	return convertJsonToLevel(levelAsJson);
    }

    /**
     * Loads a level from a resource file with the given name. If the resource is empty
     * a default {@link LevelDefinition} is returned.
     *
     * @param resourceName The name of the resource
     * @return The level definition expressed in json by the resource
     * @throws IOException If trouble reading the resource
     * @throws JsonSyntaxException If the contents of the resource is unable to express a valid level definition
     */
    public static LevelDefinition loadLevelFromResource(final String resourceName) throws IOException, JsonSyntaxException {
	final String levelAsJson = readStringFromResource(resourceName);
	return convertJsonToLevel(levelAsJson);
    }

    /**
     * Saves the {@link LevelDefinition} to the file specified by the given {@link Path}.
     * If the file does not exist, it will be created.
     *
     * @param level The level definition to save
     * @param path The path to save to
     * @throws IOException If trouble writing to the file
     */
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
	    // The level definition could be loaded but it contains invalid fields.
	    // Since the json string is not a valid representation for an object of type
	    // LevelDefinition, we manually report a JsonSyntaxException
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
