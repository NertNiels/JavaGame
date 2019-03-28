package gui;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import input.MouseManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;

public class GuiManager {

	ArrayList<View> views;
	
	public GuiManager(Loader loader) {
		views = new ArrayList<View>();
		
	}
	
	public void update(MasterRenderer renderer) {
		for(View view:views) {
			view.mouse(MouseManager.getPositionScaled(), MouseManager.getMouseLeft());
			renderer.processGui(view);
		}
	}
}
