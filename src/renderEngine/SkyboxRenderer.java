package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import main.Configs;
import models.RawModel;
import shaders.SkyboxShader;
import shaders.SunShader;
import timing.Timing;
import world.sky.SkyColor;

public class SkyboxRenderer {

	private static final float SIZE = Configs.FAR_PLANE;
	
	private static final float[] VERTICES_SKYBOX = {        
		    -SIZE,  SIZE, -SIZE * 0.6f,
		    -SIZE, -SIZE, -SIZE * 0.6f,
		    SIZE, -SIZE, -SIZE * 0.6f,
		     SIZE, -SIZE, -SIZE * 0.6f,
		     SIZE,  SIZE, -SIZE * 0.6f,
		    -SIZE,  SIZE, -SIZE * 0.6f,
	};
	
	private static final float[] VERTICES_SUN = {
			1, 0, -1,
			-1, 0, -1,
			-1, 0, 1,
			-1, 0, 1,
			1, 0, 1,
			1, 0, -1,
	};
	
	
	private RawModel cube;
	private RawModel sun;
	private SkyboxShader shaderSkybox;
	private SunShader shaderSun;
	
	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix) {
		cube = loader.loadToVAO(VERTICES_SKYBOX, 3);
		sun = loader.loadToVAO(VERTICES_SUN, 3);
		shaderSkybox = new SkyboxShader();
		shaderSun = new SunShader();
		shaderSkybox.start();
		shaderSkybox.loadProjectionMatrix(projectionMatrix);
		shaderSkybox.stop();
		shaderSun.start();
		shaderSun.loadProjectionMatrix(projectionMatrix);
		shaderSun.stop();
	}
	
	public void render(Camera camera, Vector4f clipPlane) {
		shaderSkybox.start();
		shaderSkybox.loadClipPlane(clipPlane);
		shaderSkybox.loadViewMatrix(camera, SIZE);
		shaderSkybox.loadSkyColors(SkyColor.getSkyColor().topColor, SkyColor.getSkyColor().bottomColor);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shaderSkybox.stop();
		shaderSun.start();
		shaderSun.loadViewMatrix(camera, SIZE);
		GL30.glBindVertexArray(sun.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, sun.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shaderSun.stop();
	}
	
	public void cleanUp() {
		shaderSkybox.cleanUp();
	}
}
