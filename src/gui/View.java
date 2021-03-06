package gui;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

public class View {

	GuiTexture texture = null;
	boolean isSelected = false;
	boolean isHovering = false;
	private OnClickListener onClickListener;
	
	public View(int texture, Vector2f position, Vector2f scale) {
		this.texture = new GuiTexture(texture, position, scale);
		onClickListener = new OnClickListener() {
			
			@Override
			public void onHover(View view, Vector2f mousePos, boolean hovering) {
				
			}
			
			@Override
			public void onClick(View view, Vector2f mousePos) {
				
			}
			
			@Override
			public void onSelected(View view, Vector2f mousePos) {
				
			}
		};
	}
	
	public View(int texture, Vector2f position, Vector2f scale, Vector4f color, float blendFactor) {
		this.texture = new GuiTexture(texture, position, scale, color, blendFactor);
		onClickListener = new OnClickListener() {
			
			@Override
			public void onHover(View view, Vector2f mousePos, boolean hovering) {
				
			}
			
			@Override
			public void onClick(View view, Vector2f mousePos) {
				
			}
			
			@Override
			public void onSelected(View view, Vector2f mousePos) {
				
			}
		};
	}
	
	public boolean mouse(Vector2f mousePos, boolean mouseIsDown) {
		boolean nowHovering = false;
		if(		mousePos.x >= texture.getTopLeft().x &&
				mousePos.y >= texture.getTopLeft().y &&
				mousePos.x <= texture.getBottomRight().x &&
				mousePos.y <= texture.getBottomRight().y) {
			if(mouseIsDown) {
				if(isSelected == false) onClickListener.onSelected(this, mousePos);
				isSelected = true;
				return true;
			} else if(isSelected) {
				isSelected = false;
				onClickListener.onClick(this, mousePos);
				return true;
			} else {
				nowHovering = true;
			}
		} else {
			nowHovering = false;
		}
		if(isHovering != nowHovering) onClickListener.onHover(this, mousePos, nowHovering);
		isHovering = nowHovering;
		return false;
	}
	
	public OnClickListener getOnClickListener() {
		return onClickListener;
	}
	
	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}
	
	public GuiTexture getTexture() {
		return texture;
	}
}
