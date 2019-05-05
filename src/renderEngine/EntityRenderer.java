package renderEngine;

import java.util.ArrayList;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import main.Configs;
import models.Model;
import models.RawModel;
import shaders.StaticShader;
import textures.ModelTexture;
import timing.Timing;
import toolbox.Maths;
import world.sky.SkyColor;

public class EntityRenderer {

	private StaticShader shader;

	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Map<Model, ArrayList<Entity>> entities) {
		shader.loadSkyColor(SkyColor.getSkyColor().bottomColor.x, SkyColor.getSkyColor().bottomColor.y, SkyColor.getSkyColor().bottomColor.z);
		shader.loadDensity(Configs.FOG_DENSITY);
		shader.loadDistortionTime(((float)Timing.getInGameSeconds() / 100f) % (float)(Math.PI * 2));
		
		for (Model model : entities.keySet()) {
			if(model == null) continue;
			prepareModel(model);
			ArrayList<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindModel();
		}
	}

	private void prepareModel(Model model) {
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);

		ModelTexture texture = model.getTexture();
		shader.loadShineVariables(model.getShineDamper(), model.getReflectivity());

		if (texture != null) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
		}
	}

	private void unbindModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationmatrix(entity.getPosition(), entity.getRotX(),
				entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadDistortionFactor(entity.getDistortionFactor());
	}

}
