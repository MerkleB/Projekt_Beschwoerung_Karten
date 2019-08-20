package main.util;

import main.jsonObjects.ElementDefinition;

public interface CalculatesElementEffectivities {
	public double getEffectivity(String attackElement, String defendElement);
	public ElementDefinition getElementDefinition(String elementName);
}
