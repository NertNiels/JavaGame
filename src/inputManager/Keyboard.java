package inputManager;

import org.lwjgl.glfw.GLFW;

import renderEngine.DisplayManager2;

public class Keyboard {

	public static boolean isKeyDown(int key) {
		return GLFW.glfwGetKey(DisplayManager2.getWindow(), key) == GLFW.GLFW_PRESS;
	}
	
}
