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

	private ArrayList<View> views;
	private View cross;
	
	public GuiManager(Loader loader) {
		views = new ArrayList<View>();
		cross = new View(loader.loadTexture("cross"), new Vector2f(0, 0), new Vector2f(10f / Configs.SCREEN_WIDTH, 10f / Configs.SCREEN_HEIGHT));
		cross.getTexture().setOpacity(0);
		views.add(cross);
	}
	
	public void update(MasterRenderer renderer) {
		for(View view:views) {
			view.mouse(MouseManager.getPositionScaled(), MouseManager.getMouseLeft());
			renderer.processGui(view);
		}
	}
	
	public View getCross() {
		return cross;
	}
}
