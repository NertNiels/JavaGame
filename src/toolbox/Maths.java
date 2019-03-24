package toolbox;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import entities.Camera;

public class Maths {

	public static Matrix4f createTransformationmatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.identity();
		matrix.translate(translation, matrix);
		matrix.rotate((float)Math.toRadians(rx), new Vector3f(1, 0, 0), matrix);
		matrix.rotate((float)Math.toRadians(ry), new Vector3f(0, 1, 0), matrix);
		matrix.rotate((float)Math.toRadians(rz), new Vector3f(0, 0, 1), matrix);
		matrix.scale(new Vector3f(scale, scale, scale), matrix);
		
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f matrix = new Matrix4f();
		matrix.identity();
		
		matrix.rotate((float)Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), matrix);
		matrix.rotate((float)Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), matrix);
		
		Vector3f negCamPos = new Vector3f(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);
		matrix.translate(negCamPos, matrix);
		return matrix;
	}
	
	public static float inverseLerp(float a, float b, float v) {
		return (v - a) / (b - a);
	}
	
	public static float lerp(float a, float b, float v) {
		return (1 - v) * a + v * b;
	}
	
	public static Vector3f interpolateColor(Vector3f color1, Vector3f color2, float blend) {
		float colour1Weight = 1 - blend;
		float r = (colour1Weight * color1.x) + (blend * color2.x);
		float g = (colour1Weight * color1.y) + (blend * color2.y);
		float b = (colour1Weight * color1.z) + (blend * color2.z);
		return new Vector3f(r, g, b);
	}
	
	public static float eveluate(float a, float b, float v) {
		double c = Math.pow(v, a);
		return (float)(c / (c + Math.pow(b - b * v, a)));
	}
	
	public static float clamp(float v, float min, float max) {
		return v > max ? max : v < min ? min : v;
	}
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
}
