package entities;

public enum EntityType {

	OakTree("tree_oak"),
	GrassGrassland("grass_grassland"),
	Player("player");
	
	String entityFile;
	
	private EntityType(String entityFile) {
		this.entityFile = entityFile;
	}
	
	public String getEntityFile() {
		return entityFile;
	}
	
}
