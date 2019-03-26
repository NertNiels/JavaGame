package renderEngine;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import gui.GuiTexture;
import gui.View;
import models.RawModel;
import shaders.GuiShader;
import toolbox.Maths;

public class GuiRenderer {

	private final RawModel quad;
	private GuiShader shader;
	
	public GuiRenderer(Loader loader, GuiShader shader) {
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = loader.loadToVAO(positions);
		this.shader = shader;
	}
	
	public void render(ArrayList<View> guis) {
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		for(View gui : guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture().getTexture());
			Matrix4f matrix = Maths.createTransformationMatrix(gui.getTexture().getPosition(), gui.getTexture().getScale());
			shader.loadTransformation(matrix);
			shader.loadBlendColor(gui.getTexture().getColor());
			shader.loadOpacity(gui.getTexture().getOpacity());
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
}
