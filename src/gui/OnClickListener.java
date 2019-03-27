package gui;

import org.lwjgl.util.vector.Vector2f;

public interface OnClickListener {

	public abstract void onClick(View view, Vector2f mousePos);
	public abstract void onHover(View view, Vector2f mousePos);
	public abstract void onSelected(View view, Vector2f mousePos);
	
}
