package timing;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import main.Configs;
import toolbox.Maths;
import world.SkyColor;

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
		System.out.println(getTimeName());
	}
	
	public static void setTimeScaleDown() {
		if(timeIndex != 0) {
			timeIndex--;
			timeScaler = Configs.TIME_SCALE_TABLE[timeIndex];
		}
		System.out.println(getTimeName());
	}
	
	public static String getTimeName() {
		return Configs.TIME_NAME_TABLE[timeIndex];
	}
	
	public static SkyColor getSkyColor() {
		SkyColor color = null;
		float hourOfDay= inGameHours%24;
		
		SkyColor color1 = null;
		SkyColor color2 = null;
		
		float blend = 0;
		if(inGameHours%24 <= Configs.SKY_COLORS[0].hourOfDay) {
			float range = 24 - Configs.SKY_COLORS[Configs.SKY_COLORS.length-1].hourOfDay + Configs.SKY_COLORS[0].hourOfDay;
			blend = (24 - Configs.SKY_COLORS[Configs.SKY_COLORS.length-1].hourOfDay + hourOfDay) / range;
			color1 = Configs.SKY_COLORS[Configs.SKY_COLORS.length-1];
			color2 = Configs.SKY_COLORS[0];
		} else if(inGameHours%24 > Configs.SKY_COLORS[Configs.SKY_COLORS.length-1].hourOfDay) {
			float range = 24 - Configs.SKY_COLORS[Configs.SKY_COLORS.length-1].hourOfDay + Configs.SKY_COLORS[0].hourOfDay;
			blend = (hourOfDay - Configs.SKY_COLORS[Configs.SKY_COLORS.length-1].hourOfDay) / range;
			color1 = Configs.SKY_COLORS[Configs.SKY_COLORS.length-1];
			color2 = Configs.SKY_COLORS[0];
		} else {
			for(int i = 0; i < Configs.SKY_COLORS.length-1; i++) {
				color1 = Configs.SKY_COLORS[i];
				color2 = Configs.SKY_COLORS[i+1];
				
				if(!(inGameHours%24 <= color2.hourOfDay)) {
					continue;
				}
				blend = ((inGameHours%24) - color1.hourOfDay) / (color2.hourOfDay - color1.hourOfDay);
				break;
			}
		}
		
		
		Vector3f top = Maths.interpolateColor(color1.topColor, color2.topColor, blend);
		Vector3f bottom = Maths.interpolateColor(color1.bottomColor, color2.bottomColor, blend);
		color = new SkyColor(top, bottom, 0);
		return color;
	}
	
}
