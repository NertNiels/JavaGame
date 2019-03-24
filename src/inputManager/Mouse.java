package inputManager;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import main.Configs;
import renderEngine.DisplayManager2;

public class Mouse {

	private static GLFWCursorPosCallback cursorPosCallback;
	private static GLFWMouseButtonCallback mouseButtonCallback;
	private static GLFWScrollCallback scrollCallback;
	
	private static float currentX = 0;
	private static float currentY = 0;
	private static float deltaX = 0;
	private static float deltaY = 0;
	
	private static boolean button0 = false;
	private static boolean button1 = false;
	private static boolean button2 = false;
	
	private static float deltaWheel = 0;
	private static float wheel = 0;
	
	
	public static float getX() {
		return currentX;
	}
	
	public static float getY() {
		return currentY;
	}
	
	public static float getDX() {
		return deltaX;
	}
	
	public static float getDY() {
		return deltaY;
	}
	
	public static float getDWheel() {
		return deltaWheel;
	}
	
	public static boolean isButtonDown(int button) {
		if(button == 0) return button0;
		if(button == 1) return button1;
		if(button == 2) return button2;
		return false;
	}
	
	
	public static void initMouse() {
		GLFW.glfwSetCursorPosCallback(DisplayManager2.getWindow(), (cursorPosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				deltaX = (float)(xpos - currentX);
				deltaY = (float)(ypos - currentY);
		        currentX = (float)xpos;
		        currentY = (float)ypos;
		    }
		}));
		
		GLFW.glfwSetMouseButtonCallback(DisplayManager2.getWindow(), (mouseButtonCallback = new GLFWMouseButtonCallback() {

		    @Override
		    public void invoke(long window, int button, int action, int mods) {
		        if(button == 0) button0 = action == GLFW.GLFW_PRESS;
		        if(button == 1) button1 = action == GLFW.GLFW_PRESS;
		        if(button == 2) button2 = action == GLFW.GLFW_PRESS;
		    }

		}));
		
		GLFW.glfwSetScrollCallback(DisplayManager2.getWindow(), (scrollCallback = new GLFWScrollCallback() {

			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				deltaWheel = (float)(yoffset - wheel);
				wheel = (float) yoffset;
			}
			
		}));
	}
	
}
