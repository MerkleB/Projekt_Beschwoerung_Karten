package main.util;

import java.io.InputStream;

import main.jsonObjects.CardDefinitionLibrary;

public class FileLoader {
	public InputStream getFileAsStream(String filename) {
		ClassLoader classLoader = CardDefinitionLibrary.class.getClassLoader();
		return classLoader.getResourceAsStream(filename);
	}
}
