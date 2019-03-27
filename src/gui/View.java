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
			public void onHover(View view, Vector2f mousePos) {
				
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
			public void onHover(View view, Vector2f mousePos) {
				
			}
			
			@Override
			public void onClick(View view, Vector2f mousePos) {
				
			}
			
			@Override
			public void onSelected(View view, Vector2f mousePos) {
				
			}
		};
	}
	
	public void mouse(Vector2f mousePos, boolean mouseIsDown) {
		System.out.println(mousePos.x + ", " + mousePos.y);
		if(		mousePos.x >= texture.getTopLeft().x &&
				mousePos.y >= texture.getTopLeft().y &&
				mousePos.x <= texture.getBottomRight().x &&
				mousePos.y <= texture.getBottomRight().y) {
			if(mouseIsDown) {
				if(isSelected == false) onClickListener.onSelected(this, mousePos);
				isSelected = true;
			} else if(isSelected) {
				isSelected = false;
				onClickListener.onClick(this, mousePos);
			} else {
				if(isHovering == false) onClickListener.onHover(this, mousePos);
				isHovering = true;
			}
		} else {
			isHovering = false;
		}
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
