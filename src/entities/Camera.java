package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import input.ControllerManager;
import input.MouseManager;
import main.Configs;
import renderEngine.DisplayManager;
import toolbox.Maths;
import world.World;

public class Camera {

	private Vector3f position = new Vector3f();
	private Vector3f target = new Vector3f();
	private Vector3f targetVel = new Vector3f();
	private float pitch = 20;
	private float yaw;

	private float velPitch;
	private float velYaw;
	private float velZoom;

	private float zoomFactor = 0.1f;

	private float zoom = 500;

	public Camera() {
	}

	public Camera(float x, float y, float z) {
		this.target.x = x;
		this.position.y = y;
		this.target.z = z;

	}

	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getLockPosition() {
		return target;
	}

	public void update(World world) {
		float mouseDX = MouseManager.getDelta().x;
		float mouseDY = MouseManager.getDelta().y;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			zoomFactor = 0.01f;
		}
		zoom(Mouse.getDWheel());
		zoom(-ControllerManager.getTrigger() * 20);
		if (Mouse.isButtonDown(1)) {
			calculateTargetPitch(mouseDY);
			calculateTargetYaw(mouseDX);
		}
		calculateTargetPitch(ControllerManager.getRightJoystickY() * 3);
		calculateTargetYaw(-ControllerManager.getRightJoystickX() * 3);
		if(Mouse.isButtonDown(0)) {
			calculateMovement(mouseDX, mouseDY, 0.1f);
		}
		calculateMovementWASD(-ControllerManager.getLeftJoystickX(), ControllerManager.getLeftJoystickY(), 0.4f);

		pitch -= velPitch;
		yaw += velYaw;
		zoom -= velZoom;
		target = Vector3f.add(target, targetVel, null);
		if(zoom < Configs.MIN_ZOOM) zoom = Configs.MIN_ZOOM;
		if(zoom > Configs.MAX_ZOOM) zoom = Configs.MAX_ZOOM;
		if(pitch < 0) pitch = 0;
		if(pitch > 90) pitch = 90;

		velPitch *= 0.8f;
		velYaw *= 0.8f;
		velZoom *= 0.8f;
		targetVel = new Vector3f(targetVel.x * 0.8f, targetVel.y * 0.8f, targetVel.z * 0.8f);
		
		calculatePosition();
		
		zoomFactor = 0.1f;
		
		
		float densityFactor = (zoom - Configs.MIN_ZOOM) / (Configs.MAX_ZOOM - Configs.MIN_ZOOM);
		Configs.FOG_DENSITY = Maths.lerp(Configs.MAX_FOG_DENSITY, Configs.MIN_FOG_DENSITY, densityFactor);
		Configs.FOG_DENSITY = Configs.MIN_FOG_DENSITY;
		
		
		manageWASD();
		
		world.setPlayerPosition(target);
		
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	private void zoom(float delta) {
		velZoom += delta * zoomFactor * (zoom / Configs.MAX_ZOOM);
	}

	private void calculateTargetPitch(float delta) {
		velPitch += delta * 6.25f * DisplayManager.getFrameTimeSeconds();
	}

	private void calculateTargetYaw(float delta) {
		velYaw += delta * 9.375f * DisplayManager.getFrameTimeSeconds();
	}

	private void calculatePosition() {
		float horizontalDistance = zoom * (float) Math.cos(Math.toRadians(pitch));
		position.y = (zoom * (float) Math.sin(Math.toRadians(pitch))) + target.y;

		position.x = (horizontalDistance * (float) Math.sin(Math.toRadians(-yaw))) + target.x;
		position.z = (horizontalDistance * (float) Math.cos(Math.toRadians(-yaw))) + target.z;
	}
	
	public void applyWater(float waterHeight) {
		position.y -= 2 * (position.y - waterHeight);
		pitch = -pitch;
	}
	
	public void calculateMovement(float dX, float dZ, float multiplier) {
		Vector3f rotated = rotateMovementXYZ(dX, dZ);
		rotated.x *= (zoom/Configs.MAX_ZOOM);
		rotated.y *= (zoom/Configs.MAX_ZOOM);
		rotated.z *= (zoom/Configs.MAX_ZOOM);

		targetVel.x -= rotated.x * multiplier;
		targetVel.y -= rotated.y * multiplier;
		targetVel.z += rotated.z * multiplier;
	}
	
	public void calculateMovementWASD(float dX, float dZ, float multiplier) {
		Vector3f rotated = rotateMovementXZ(dX, dZ);
		rotated.x *= (zoom/Configs.MAX_ZOOM);
		rotated.z *= (zoom/Configs.MAX_ZOOM);

		targetVel.x -= rotated.x * multiplier * 125 * DisplayManager.getFrameTimeSeconds();
		targetVel.z += rotated.z * multiplier * 125 * DisplayManager.getFrameTimeSeconds();
	}
	
	private Vector3f rotateMovementXZ(float dX, float dZ) {
		float x = dX * (float)Math.cos(Math.toRadians(-yaw)) + dZ * (float)Math.sin(Math.toRadians(yaw));
		float z = dZ * (float)Math.cos(Math.toRadians(yaw)) + dX * (float)Math.sin(Math.toRadians(-yaw));
		return new Vector3f(x, 0, z);
	}
	
	private Vector3f rotateMovementXYZ(float dX, float dZ) {
		float x = dX * (float)Math.cos(Math.toRadians(-yaw)) + dZ * (float)Math.sin(Math.toRadians(yaw)) * (float)Math.sin(Math.toRadians(pitch));
		float y = dZ * (float)Math.cos(Math.toRadians(pitch));
		float z = dZ * (float)Math.cos(Math.toRadians(yaw))  * (float)Math.sin(Math.toRadians(pitch)) + dX * (float)Math.sin(Math.toRadians(-yaw));
		return new Vector3f(x, y, z);
	}
	
	private void manageWASD() {
		float dX = 0;
		float dY = 0;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) dY = -1;
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) dX = 1;
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) dY = 1;
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) dX = -1;
		calculateMovementWASD(dX, dY, 0.5f);
		
	}

}
