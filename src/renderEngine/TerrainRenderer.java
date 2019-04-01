package renderEngine;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import main.Configs;
import models.RawModel;
import shaders.TerrainShader;
import terrain.Terrain;
import toolbox.Maths;
import world.World;

public class TerrainRenderer {

	private TerrainShader shader;
	private Fbo skyBoxFbo;

	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix, Fbo skyBoxFbo) {
		this.shader = shader;
		this.skyBoxFbo = skyBoxFbo;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}

	public void render(ArrayList<Terrain> terrains, World world) {

		shader.loadSkyColor(Configs.SKY_COLOR_BOTTOM.x, Configs.SKY_COLOR_BOTTOM.y, Configs.SKY_COLOR_BOTTOM.z);
		shader.loadDensity(Configs.FOG_DENSITY);
		shader.loadSize();
		
		for (Terrain terrain : terrains) {
			prepareTerrain(terrain, world);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTerrain();
		}
	}

	private void prepareTerrain(Terrain terrain, World world) {
		RawModel rawModel = terrain.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, world.getBiomeTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, skyBoxFbo.getColourTexture());
	}

	private void unbindTerrain() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = Maths
				.createTransformationmatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
