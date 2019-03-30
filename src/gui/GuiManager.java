package gui;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import input.MouseManager;
import main.Configs;
import renderEngine.Loader;
import renderEngine.MasterRenderer;

public class GuiManager {

	ArrayList<View> views;
	
	public GuiManager(Loader loader) {
		views = new ArrayList<View>();
		View view = new View(loader.loadTexture("cross"), new Vector2f(0, 0), new Vector2f(10f / Configs.SCREEN_WIDTH, 10f / Configs.SCREEN_HEIGHT));
		views.add(view);
	}
	
	public void update(MasterRenderer renderer) {
		for(View view:views) {
			view.mouse(MouseManager.getPositionScaled(), MouseManager.getMouseLeft());
			renderer.processGui(view);
		}
	}
}
