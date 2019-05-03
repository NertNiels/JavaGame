package entities.camera;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import input.ControllerManager;
import input.MouseManager;
import renderEngine.DisplayManager;
import world.World;

public class CameraFirstPerson extends Camera {

	private Vector3f targetVel = new Vector3f();
	private float velPitch;
	private float velYaw;
	
	public CameraFirstPerson(float x, float y, float z) {
		super();
		position.x = x;
		position.y = y;
		position.z = z;
		pitch = 0;
	}
	
	@Override
	public void update(World world) {
		float mouseDX = MouseManager.getDelta().x;
		float mouseDY = MouseManager.getDelta().y;
		
		targetVel.y += -ControllerManager.getTrigger() * 20;
		calculateTargetPitch(-ControllerManager.getRightJoystickY() * 375 * DisplayManager.getFrameTimeSeconds() * 5);
		calculateTargetYaw(ControllerManager.getRightJoystickX() * 375 * DisplayManager.getFrameTimeSeconds() * 5);
		calculateTargetPitch(mouseDY * 3);
		calculateTargetYaw(mouseDX * 3);
		
		calculateMovementWASD(-ControllerManager.getLeftJoystickX(), ControllerManager.getLeftJoystickY(), 0.4f);
		
		pitch -= velPitch;
		yaw += velYaw;
		velPitch = 0;
		velYaw = 0;
		manageWASD();
		calculatePosition();
	}
	
	private void calculatePosition() {
		position.x += targetVel.x;
		position.y += targetVel.y;
		position.z += targetVel.z;
		targetVel = new Vector3f();
	}
	
	public void calculateMovementWASD(float dX, float dZ, float multiplier) {
		Vector3f rotated = rotateMovementXZ(dX, dZ);
		targetVel.x -= rotated.x * multiplier * 125 * DisplayManager.getFrameTimeSeconds();
		targetVel.z += rotated.z * multiplier * 125 * DisplayManager.getFrameTimeSeconds();
	}
	
	private Vector3f rotateMovementXZ(float dX, float dZ) {
		float x = dX * (float)Math.cos(Math.toRadians(-yaw)) + dZ * (float)Math.sin(Math.toRadians(yaw));
		float z = dZ * (float)Math.cos(Math.toRadians(yaw)) + dX * (float)Math.sin(Math.toRadians(-yaw));
		return new Vector3f(x, 0, z);
	}
	
	private void calculateTargetPitch(float delta) {
		velPitch += delta * 0.05f;
	}

	private void calculateTargetYaw(float delta) {
		velYaw += delta * 0.075f;
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
