package renderEngine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import main.Configs;

public class DisplayManager2 {

	private static GLFWErrorCallback errorCallback;
	
	private static long lastFrame;
	private static int deltaTime;
	private static float averageFrameRate;
	
	private static float timer = 0;
	
	private static long window;
	
	public static void createDisplay() {
		if(!GLFW.glfwInit()) {
			throw new IllegalStateException("Failed to initialize GLFW");
		}
		
		GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE); 
		long monitor = 0;
		if(Configs.FULL_SCREEN) monitor = GLFW.glfwGetPrimaryMonitor();
		window = GLFW.glfwCreateWindow(Configs.SCREEN_WIDTH, Configs.SCREEN_HEIGHT, Configs.TITLE, monitor, 0);
		if(window == 0) {
			throw new IllegalStateException("Failed to create window");
		}
		
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(window, (videoMode.width() - Configs.SCREEN_WIDTH) / 2, (videoMode.height() - Configs.SCREEN_HEIGHT) / 2);
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		GLFW.glfwShowWindow(window);
		
		GL11.glViewport(0, 0, Configs.SCREEN_WIDTH, Configs.SCREEN_HEIGHT);
	}
	
	public static void updateDisplay() {
		GLFW.glfwSwapBuffers(window);
		GLFW.glfwPollEvents();
		long d = getDeltaTime();
		averageFrameRate = (float)1000 / (float)d;
		timer += getFrameTimeSeconds();
		if(timer >= 1f) {
			System.out.println("Average FPS: " + averageFrameRate);
			timer = 0;
		}
	}
	
	public static void closeDisplay() {
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}
	
	/**
	 * Get the delta time without updating lastFrame
	 * 
	 * @return milliseconds passed since last frame
	 */
	public static int getFrameTimeMillis() {
		return deltaTime;
	}
	
	/**
	 * Gte the delta time in seconds without updating lastFrame
	 * 
	 * @return seconds passed since last frame
	 */
	public static float getFrameTimeSeconds() {
		return (float) deltaTime / 1000f;
	}
	
	/** 
     * Calculate how many milliseconds have passed 
     * since last frame.
     * 
     * @return milliseconds passed since last frame 
     */
	public static int getDeltaTime() {
		long time = getTime();
		deltaTime = (int) (time - lastFrame);
		lastFrame = time;
		
		return deltaTime;
	}
	
	public static float getAverageFrameRate() {
		return averageFrameRate;
	}
	
	/**
     * Get the accurate system time
     * 
     * @return The system time in milliseconds
     */
	public static long getTime() {
		return System.currentTimeMillis();
	}
	
	public static long getWindow() {
		return window;
	}
	
}
