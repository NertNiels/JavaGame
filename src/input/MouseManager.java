package input;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import main.Configs;
import renderEngine.MasterRenderer;
import toolbox.MousePicker;
import world.World;

public class MouseManager {

	private static Vector2f mousePos;
	private static Vector2f mouseDelta;
	private static Vector2f mousePosScaled;
	private static boolean left = false;
	private static boolean right = false;
	private static boolean middle = false;

	private static MousePicker mousePicker;
	
	public static void init(MasterRenderer renderer, Camera camera, World world) {
		mousePos = new Vector2f();
		mousePosScaled = new Vector2f();
		mousePicker = new MousePicker(camera, renderer.getProjectionMatrix(), world);
	}

	public static void update() {
		mousePos = new Vector2f(Mouse.getX(), Mouse.getY());
		mouseDelta = new Vector2f(Mouse.getDX(), Mouse.getDY());
		mousePosScaled = new Vector2f(Mouse.getX() / (float) Configs.SCREEN_WIDTH * 2 - 1,
				Mouse.getY() / (float) Configs.SCREEN_HEIGHT  * 2 - 1);
		left = Mouse.isButtonDown(0);
		right = Mouse.isButtonDown(1);
		middle = Mouse.isButtonDown(2);
		
		mousePicker.update();
	}

	public static Vector2f getPosition() {
		return mousePos;
	}

	public static Vector2f getPositionScaled() {
		return mousePosScaled;
	}

	public static Vector2f getDelta() {
		return mouseDelta;
	}

	public static boolean getMouseLeft() {
		return left;
	}

	public static boolean getMouseRight() {
		return right;
	}

	public static boolean getMouseMiddle() {
		return middle;
	}

	public static Vector3f getMouseRay() {
		return mousePicker.getCurrentRay();
	}
	
	public static Vector3f getPointOnTerrain() {
		return mousePicker.getPointOnTerrain();
	}
	
}
