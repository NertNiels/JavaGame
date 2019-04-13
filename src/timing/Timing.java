package timing;

import org.lwjgl.input.Keyboard;

import main.Configs;

public class Timing {

	private static long inGameSeconds = 0;
	private static float inGameSecondsPast = 0;
	private static float inGameHours = 0;
	private static float inGameHoursPast = 0;
	private static float timeScaler = 0.5f;
	
	public static void updateTimer(long millisPast) {
		inGameSecondsPast = millisPast * timeScaler;
		inGameSeconds += Math.round(inGameSecondsPast);
		inGameHoursPast = inGameSecondsPast / 60f;
		inGameHours += inGameHoursPast;
		if(Keyboard.isKeyDown(Keyboard.KEY_ADD)) timeScaler += 0.01f;
		if(Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) timeScaler -= 0.01f;
		if(timeScaler < Configs.MIN_TIME_SCALE) timeScaler = Configs.MIN_TIME_SCALE;
	}

	public static long getInGameSeconds() {
		return inGameSeconds;
	}
	
	public static float getInGameSecondsPast() {
		return inGameSecondsPast;
	}
	
	public static float getInGameHours() {
		return inGameHours;
	}
	
	public static float getInGameHoursPast() {
		return inGameHoursPast;
	}
	
	public static float getTimeScaler() {
		return timeScaler;
	}
	
	public static void setTimeScaler(float scaler) {
		timeScaler = scaler;
	}
	
}
