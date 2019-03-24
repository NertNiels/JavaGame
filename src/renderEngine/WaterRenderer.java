package renderEngine;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import main.Configs;
import models.RawModel;
import shaders.WaterShader;
import toolbox.Maths;
import water.GridSquare;
import water.WaterFrameBuffers;
import water.WaterTile;

public class WaterRenderer {
	private WaterShader shader;
	private RawModel tile;

	private WaterFrameBuffers fbos;
	private Fbo skyBoxFbo;

	public WaterRenderer(WaterShader shader, Matrix4f projectionMatrix, Loader loader, WaterFrameBuffers fbos, Fbo skyBoxFbo) {
		this.shader = shader;
		this.fbos = fbos;
		this.skyBoxFbo = skyBoxFbo;
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		this.tile = GridSquare.generatePlane(loader, Configs.VERTEX_COUNT, Configs.SIZE);
	}

	public void render(ArrayList<WaterTile> waters) {
		prepareWater();
		shader.loadFlowRate();
		shader.loadSkyColor(Configs.SKY_COLOR_BOTTOM.x, Configs.SKY_COLOR_BOTTOM.y, Configs.SKY_COLOR_BOTTOM.z);
		shader.loadDensity(Configs.FOG_DENSITY);
		for (WaterTile water : waters) {
			loadModelMatrix(water);
			shader.loadGridCoords(water.getGridCoords());
			GL11.glDrawElements(GL11.GL_TRIANGLES, tile.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		}
		unbindWater();
	}

	private void prepareWater() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL30.glBindVertexArray(tile.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, skyBoxFbo.getColourTexture());
	}

	private void unbindWater() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}

	private void loadModelMatrix(WaterTile waterTile) {
		Matrix4f transformationMatrix = Maths.createTransformationmatrix(
				new Vector3f(0, waterTile.getHeight(), 0), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
