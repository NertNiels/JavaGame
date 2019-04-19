package world;

import org.lwjgl.util.vector.Vector3f;

public class SkyColor {
	
	public Vector3f topColor;
	public Vector3f bottomColor;
	
	public int hourOfDay = 0;
	
	public SkyColor(Vector3f topColor, Vector3f bottomColor, int hourOfDay) {
		this.topColor = topColor;
		this.bottomColor = bottomColor;
		this.hourOfDay = hourOfDay;
		
	}
	
}
