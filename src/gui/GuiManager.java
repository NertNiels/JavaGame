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
		View guiTest = new View(loader.loadTexture("white"), new Vector2f(-0.5f, 0.5f), new Vector2f(0.25f, 0.25f), new Vector4f(0.15f, 0.15f, 0.15f, 1), 0.7f);
		guiTest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onSelected(View view, Vector2f mousePos) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHover(View view, Vector2f mousePos) {
				view.getTexture().setOpacity(1);
			}
			
			@Override
			public void onClick(View view, Vector2f mousePos) {
				view.getTexture().setOpacity(1);
			}
		});
		views.add(guiTest);
	}
	
	public void update(MasterRenderer renderer) {
		for(View view:views) {
			view.mouse(MouseManager.getPositionScaled(), MouseManager.getMouseLeft());
			renderer.processGui(view);
		}
	}
}
