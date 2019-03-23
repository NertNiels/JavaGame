package models;

import textures.ModelTexture;

public class Model {

	private RawModel rawModel;
	private TexturedModel texturedModel;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	public Model(RawModel rawModel) {
		this.rawModel = rawModel;
	}
	
	public Model(RawModel rawModel, ModelTexture modelTexture) {
		this.texturedModel = new TexturedModel(rawModel, modelTexture);
	}
	
	public RawModel getRawModel() {
		return texturedModel == null ? rawModel : texturedModel.getRawModel();
	}
	
	public ModelTexture getTexture() {
		return texturedModel == null ? null : texturedModel.getTexture();
	}
	
	public TexturedModel getTexturedModel() {
		return texturedModel;
	}
	
	public float getShineDamper() {
		return shineDamper;
	}
	
	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}
	
	public float getReflectivity() {
		return reflectivity;
	}
	
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
}
