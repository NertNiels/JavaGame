package entities.camera;

import org.lwjgl.util.vector.Vector3f;

import world.World;

public abstract class Camera {
	
	protected Vector3f position;
	protected float pitch = 20;
	protected float yaw;
	
	protected Camera() {
		position = new Vector3f();
	}
	
	public abstract void update(World world);
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public void applyWater(float waterHeight) {
		position.y -= 2 * (position.y - waterHeight);
		pitch = -pitch;
	}
}
