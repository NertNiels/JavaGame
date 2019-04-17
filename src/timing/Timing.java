package timing;

import org.lwjgl.input.Keyboard;

import main.Configs;

public class Timing {

	private static float inGameSeconds = 0;
	private static float inGameSecondsPast = 0;
	private static float inGameHours = 0;
	private static float inGameHoursPast = 0;
	private static float timeScaler = 3600f/60000f;
	private static int timeIndex = 0;
	
	public static void updateTimer(long millisPast) {
		inGameSecondsPast = millisPast * timeScaler;
		inGameSeconds += inGameSecondsPast;
		inGameHoursPast = inGameSecondsPast / 3600f;
		inGameHours += inGameHoursPast;
		if(Keyboard.isKeyDown(Keyboard.KEY_ADD)) timeScaler += 0.01f;
		if(Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) timeScaler -= 0.01f;
		if(timeScaler < Configs.MIN_TIME_SCALE) timeScaler = Configs.MIN_TIME_SCALE;
	}

	public static float getInGameSeconds() {
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
	
	public static void setTimeScaleUp() {
		if(timeIndex + 1 != Configs.TIME_SCALE_TABLE.length) {
			timeIndex++;
			timeScaler = Configs.TIME_SCALE_TABLE[timeIndex];
		}
	}
	
	public static void setTimeScaleDown() {
		if(timeIndex != 0) {
			timeIndex--;
			timeScaler = Configs.TIME_SCALE_TABLE[timeIndex];
		}
	}
	
}
