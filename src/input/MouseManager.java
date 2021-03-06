package input;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import entities.camera.Camera;
import main.Configs;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import world.World;

public class MouseManager {

	private static Vector2f mousePos;
	private static Vector2f mouseDelta;
	private static Vector2f mousePosScaled;
	private static Vector2f stickPosition;

	private static boolean left = false;
	private static boolean right = false;
	private static boolean middle = false;

	private static ArrayList<ButtonListener> leftListeners = new ArrayList<ButtonListener>();
	private static ArrayList<ButtonListener> rightListeners = new ArrayList<ButtonListener>();
	private static ArrayList<ButtonListener> middleListeners = new ArrayList<ButtonListener>();

	private static long leftButtonDown = 0;
	private static long rightButtonDown = 0;
	private static long middleButtonDown = 0;
	
	private static boolean isDragging = false;

	public static void init(MasterRenderer renderer, Camera camera, World world) {
		mousePos = new Vector2f();
		mousePosScaled = new Vector2f();
		
		try {
			Mouse.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		leftListeners.add(new ButtonListener() {

			@Override
			public boolean onButtonReleased() {
				if (DisplayManager.getTime() - leftButtonDown <= 1000) {
					if (!isDragging) {
						for (ButtonListener listener : leftListeners) {
							listener.onButtonClicked();
						}
					}
					isDragging = false;
				}
				return false;
			}

			@Override
			public boolean onButtonDown() {
				leftButtonDown = DisplayManager.getTime();
				return false;
			}

			@Override
			public boolean onButtonClicked() {
				System.out.println("Left button clicked");
				return false;
			}
		});

		rightListeners.add(new ButtonListener() {

			@Override
			public boolean onButtonReleased() {
				if (DisplayManager.getTime() - rightButtonDown <= 1000) {
					if (!isDragging) {
						for (ButtonListener listener : rightListeners) {
							listener.onButtonClicked();
						}
					}
					isDragging = false;
				}
				return false;
			}

			@Override
			public boolean onButtonDown() {
				rightButtonDown = DisplayManager.getTime();
				return false;
			}

			@Override
			public boolean onButtonClicked() {
				System.out.println("Right button clicked");
				return false;
			}
		});

		middleListeners.add(new ButtonListener() {

			@Override
			public boolean onButtonReleased() {
				if (DisplayManager.getTime() - middleButtonDown <= 1000) {
					if (!isDragging) {
						for (ButtonListener listener : middleListeners) {
							listener.onButtonClicked();
						}
					}
					isDragging = false;
				}
				return false;
			}

			@Override
			public boolean onButtonDown() {
				middleButtonDown = DisplayManager.getTime();
				return false;
			}

			@Override
			public boolean onButtonClicked() {
				System.out.println("Middle mouse button clicked");
				return false;
			}
		});
	}

	public static void update() {
		mousePos = new Vector2f(Mouse.getX(), Mouse.getY());
		mouseDelta = new Vector2f(Mouse.getDX(), Mouse.getDY());
		if(mouseDelta.length() >= 5 && (left  || right || middle)) isDragging = true;
		mousePosScaled = new Vector2f(Mouse.getX() / (float) Configs.SCREEN_WIDTH * 2 - 1,
				Mouse.getY() / (float) Configs.SCREEN_HEIGHT * 2 - 1);
		onChangeLeft(Mouse.isButtonDown(0));
		onChangeRight(Mouse.isButtonDown(1));
		onChangeMiddle(Mouse.isButtonDown(2));
		
		if(stickPosition != null) Mouse.setCursorPosition((int)stickPosition.x, (int)stickPosition.y);
	}

	public static Vector2f getPosition() {
		return mousePos;
	}

	public static Vector2f getPositionScaled() {
		return mousePosScaled;
	}

	public static Vector2f getDelta() {
		return mouseDelta;
	}

	public static boolean getMouseLeft() {
		return left;
	}

	public static boolean getMouseRight() {
		return right;
	}

	public static boolean getMouseMiddle() {
		return middle;
	}

	private static void onChangeLeft(boolean change) {
		if (left == change)
			return;
		left = change;
		if (change) {
			for (ButtonListener listener : leftListeners) {
				if(listener.onButtonDown()) break;
			}
		} else {
			for (ButtonListener listener : leftListeners) {
				if(listener.onButtonReleased()) break;
			}
		}
	}

	private static void onChangeRight(boolean change) {
		if (right == change)
			return;
		right = change;
		if (change) {
			for (ButtonListener listener : rightListeners) {
				if(listener.onButtonDown()) break;
			}
		} else {
			for (ButtonListener listener : rightListeners) {
				if(listener.onButtonReleased()) break;
			}
		}
	}

	private static void onChangeMiddle(boolean change) {
		if (middle == change)
			return;
		middle = change;
		if (change) {
			for (ButtonListener listener : middleListeners) {
				if(listener.onButtonDown()) break;
			}
		} else {
			for (ButtonListener listener : middleListeners) {
				if(listener.onButtonReleased()) break;
			}
		}
	}

	public static void addLeftButtonListener(ButtonListener listener) {
		leftListeners.add(listener);
	}

	public static void addRightButtonListener(ButtonListener listener) {
		rightListeners.add(listener);
	}

	public static void addMiddleButtonListener(ButtonListener listener) {
		middleListeners.add(listener);
	}
	
	public static void setMouseVisible(boolean b) {
		Mouse.setGrabbed(!b);
	}
	
	public static void setMouseStickToPosition(boolean b, Vector2f position) {
		if(b) {
			stickPosition = position;
			return;
		}
		stickPosition = null;
	}
	
	public static void destroy() {
		Mouse.destroy();
	}

}
