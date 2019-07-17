package main;

public interface Effect extends Stackable {
	public String getDescription();
	public boolean isExecutable();
}
