package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import main.Configs;
import timing.Timing;

public class DisplayManager {
	
	private static long lastFrame;
	private static int deltaTime;
	private static float averageFrameRate;
	
	private static float timer = 0;
	
	public static void createDisplay() {
		ContextAttribs attribs = new ContextAttribs(3,2)
				.withForwardCompatible(true)
				.withProfileCore(true);
		
		try {
			DisplayMode displayMode = new DisplayMode(Configs.SCREEN_WIDTH, Configs.SCREEN_HEIGHT);
			if(Configs.FULL_SCREEN) {
				displayMode = null;
		        DisplayMode[] modes = Display.getAvailableDisplayModes();
		        Configs.SCREEN_WIDTH = Display.getDesktopDisplayMode().getWidth();
		        Configs.SCREEN_HEIGHT = Display.getDesktopDisplayMode().getHeight();


		         for (int i = 0; i < modes.length; i++)
		         {
		             if (modes[i].getWidth() == Configs.SCREEN_WIDTH
		             && modes[i].getHeight() == Configs.SCREEN_HEIGHT
		             && modes[i].isFullscreenCapable())
		               {
		                    displayMode = modes[i];
		               }
		         }

			}
			Display.setDisplayMode(displayMode);
			Display.setFullscreen(Configs.FULL_SCREEN);
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("GameEngine");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, Configs.SCREEN_WIDTH, Configs.SCREEN_HEIGHT);
		lastFrame = getTime();
	}
	
	public static void updateDisplay() {
		Display.sync(Configs.FPS_CAP);
		Display.update();
		long d = getDeltaTime();
		averageFrameRate = 1000f / (float)d;
		timer += getFrameTimeSeconds();
		Timing.updateTimer(deltaTime);
		if(timer >= 1f) {
			Display.setTitle("Average FPS: " + averageFrameRate);
			timer = 0;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
			Display.setTitle("Day " + (int)(Timing.getInGameHours()/24f) + " Hour " + (int)(Timing.getInGameHours()%24) + " Second " + (int)(Timing.getInGameSeconds()%3600));
		}
	}
	
	public static void closeDisplay() {
		Display.destroy();
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
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
}
