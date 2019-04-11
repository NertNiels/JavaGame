package entities;

public enum EntityType {

	OakTree("tree_oak"),
	GrassGrassland("grass_grassland");
	
	String entityFile;
	
	private EntityType(String entityFile) {
		this.entityFile = entityFile;
	}
	
	public String getEntityFile() {
		return entityFile;
	}
	
}
