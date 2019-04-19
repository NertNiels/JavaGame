package world.sky;

import org.lwjgl.util.vector.Vector3f;

import main.Configs;
import timing.Timing;
import toolbox.Maths;

public class SkyColor {
	
	public Vector3f topColor;
	public Vector3f bottomColor;
	
	public int hourOfDay = 0;
	
	public SkyColor(Vector3f topColor, Vector3f bottomColor, int hourOfDay) {
		this.topColor = topColor;
		this.bottomColor = bottomColor;
		this.hourOfDay = hourOfDay;
		
	}
	
	public static SkyColor getSkyColor() {
		SkyColor color = null;
		float hourOfDay = Timing.getInGameHours()%24;
		
		SkyColor color1 = null;
		SkyColor color2 = null;
		
		float blend = 0;
		if(hourOfDay <= Configs.SKY_COLORS[0].hourOfDay) {
			float range = 24 - Configs.SKY_COLORS[Configs.SKY_COLORS.length-1].hourOfDay + Configs.SKY_COLORS[0].hourOfDay;
			blend = (24 - Configs.SKY_COLORS[Configs.SKY_COLORS.length-1].hourOfDay + hourOfDay) / range;
			color1 = Configs.SKY_COLORS[Configs.SKY_COLORS.length-1];
			color2 = Configs.SKY_COLORS[0];
		} else if(hourOfDay > Configs.SKY_COLORS[Configs.SKY_COLORS.length-1].hourOfDay) {
			float range = 24 - Configs.SKY_COLORS[Configs.SKY_COLORS.length-1].hourOfDay + Configs.SKY_COLORS[0].hourOfDay;
			blend = (hourOfDay - Configs.SKY_COLORS[Configs.SKY_COLORS.length-1].hourOfDay) / range;
			color1 = Configs.SKY_COLORS[Configs.SKY_COLORS.length-1];
			color2 = Configs.SKY_COLORS[0];
		} else {
			for(int i = 0; i < Configs.SKY_COLORS.length-1; i++) {
				color1 = Configs.SKY_COLORS[i];
				color2 = Configs.SKY_COLORS[i+1];
				
				if(!(hourOfDay <= color2.hourOfDay)) {
					continue;
				}
				blend = ((hourOfDay) - color1.hourOfDay) / (color2.hourOfDay - color1.hourOfDay);
				break;
			}
		}
		
		
		Vector3f top = Maths.interpolateColor(color1.topColor, color2.topColor, blend);
		Vector3f bottom = Maths.interpolateColor(color1.bottomColor, color2.bottomColor, blend);
		color = new SkyColor(top, bottom, 0);
		return color;
	}
	
}
