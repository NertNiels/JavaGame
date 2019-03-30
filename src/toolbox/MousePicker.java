package toolbox;

import org.lwjgl.util.vector.Matrix;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import input.MouseManager;
import main.Configs;
import terrain.Terrain;
import world.World;

public class MousePicker {

	private Vector3f currentRay;
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Camera camera;
	
	private World world;
	private static final int RECURSION_COUNT = 200;
	private static final int RAY_RANGE = 600;
	private Vector3f currentTerrainPoint;
	
	public MousePicker(Camera camera, Matrix4f projectionMatrix, World world) {
		this.camera = camera;
		this.projectionMatrix = projectionMatrix;
		this.viewMatrix = Maths.createViewMatrix(camera);
		this.world = world;
		currentTerrainPoint = new Vector3f();
	}
	
	public Vector3f getCurrentRay() {
		return currentRay;
	}
	
	public void update() {
		viewMatrix = Maths.createViewMatrix(camera);
		currentRay = calculateMouseRay();
		if (intersectionInRange(0, RAY_RANGE, currentRay)) {
			currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
		} else {
			currentTerrainPoint = null;
		}
	}
	
	private Vector3f calculateMouseRay() {
		Vector2f normalizedCoords = MouseManager.getPositionScaled();
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1, 1);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}
	
	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f inverseView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(inverseView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}
	
	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1, 0);
	}
	
	private Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f camPos = camera.getPosition();
		Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return Vector3f.add(start, scaledRay, null);
	}
	
	private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
		float half = start + ((finish - start) / 2f);
		if (count >= RECURSION_COUNT) {
			Vector3f endPoint = getPointOnRay(ray, half);
			Terrain terrain = world.getTerrainAt(endPoint.getX(), endPoint.getZ());
			if (terrain != null) {
				return endPoint;
			} else {
				return null;
			}
		}
		if (intersectionInRange(start, half, ray)) {
			return binarySearch(count + 1, start, half, ray);
		} else {
			return binarySearch(count + 1, half, finish, ray);
		}
	}

	private boolean intersectionInRange(float start, float finish, Vector3f ray) {
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isUnderGround(Vector3f testPoint) {
		Terrain terrain = world.getTerrainAt(testPoint.getX(), testPoint.getZ());
		float height = 0;
		if (terrain != null) {
			height = world.getHeightAt(testPoint.getX(), testPoint.getZ()) * Configs.HEIGHT_SCALE_FACTOR;
		}
		if (testPoint.y < height) {
			return true;
		} else {
			return false;
		}
	}
	
	public Vector3f getPointOnTerrain() {
		return currentTerrainPoint;
	}
	
}
