package gui;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

import entities.camera.Camera;
import entities.camera.CameraFirstPerson;
import input.ButtonListener;
import input.MouseManager;
import main.Configs;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import timing.Timing;

public class GuiManager {

	private ArrayList<View> views;
	private View cross;
	
	private boolean isMouseOnGui = false;
	
	public GuiManager(Loader loader, Camera camera) {
		views = new ArrayList<View>();
		
		View but1 = new View(loader.loadTexture("white"), new Vector2f(-0.5f, -0.75f), new Vector2f(0.5f, 0.25f));
		but1.getTexture().setColor(0.4f, 0.4f, 0.4f, 1);
		but1.getTexture().setOpacity(0.3f);
		MouseManager.addLeftButtonListener(new ButtonListener() {
			
			@Override
			public boolean onButtonReleased() {
				return isMouseOnGui;
			}
			
			@Override
			public boolean onButtonDown() {
				return isMouseOnGui;
			}
			
			@Override
			public boolean onButtonClicked() {
				return isMouseOnGui;
			}
		});
		but1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onSelected(View view, Vector2f mousePos) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHover(View view, Vector2f mousePos, boolean hovering) {
				if(hovering) {
					view.getTexture().setOpacity(0.6f);
				} else {
					view.getTexture().setOpacity(0.3f);
				}
			}
			
			@Override
			public void onClick(View view, Vector2f mousePos) {
				Timing.setTimeScaleDown();
			}
		});
		
		views.add(but1);
		
		View but2 = new View(loader.loadTexture("white"), new Vector2f(0.5f, -0.75f), new Vector2f(0.5f, 0.25f));
		but2.getTexture().setColor(0.4f, 0.4f, 0.4f, 1);
		but2.getTexture().setOpacity(0.3f);
		but2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onSelected(View view, Vector2f mousePos) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHover(View view, Vector2f mousePos, boolean hovering) {
				if(hovering) {
					view.getTexture().setOpacity(0.6f);
				} else {
					view.getTexture().setOpacity(0.3f);
				}
			}
			
			@Override
			public void onClick(View view, Vector2f mousePos) {
				Timing.setTimeScaleUp();
			}
		});
		
		views.add(but2);
		
		cross = new View(loader.loadTexture("cross"), new Vector2f(0, 0), new Vector2f(10f / Configs.SCREEN_WIDTH, 10f / Configs.SCREEN_HEIGHT));
		cross.getTexture().setOpacity(0);
		views.add(cross);
		
		if(camera instanceof CameraFirstPerson) {
			cross.getTexture().setOpacity(1);
		}
	}
	
	public void update(MasterRenderer renderer) {
		for(View view:views) {
			boolean mouseOnGui = view.mouse(MouseManager.getPositionScaled(), MouseManager.getMouseLeft());
			if(!isMouseOnGui) isMouseOnGui = mouseOnGui;
			renderer.processGui(view);
		}
	}
	
	public View getCross() {
		return cross;
	}
}
