package input;

import java.util.ArrayList;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import net.java.games.input.Rumbler;
import toolbox.ScreenPicker;

public class ControllerManager {

	private static Controller controller;

	private static EventQueue eventQueue;
	private static Event event;
	
	private static ArrayList<ControllerButtonListener> buttonAListeners = new ArrayList<ControllerButtonListener>();
	private static ArrayList<ControllerButtonListener> buttonBListeners = new ArrayList<ControllerButtonListener>();
	private static ArrayList<ControllerButtonListener> buttonXListeners = new ArrayList<ControllerButtonListener>();
	private static ArrayList<ControllerButtonListener> buttonYListeners = new ArrayList<ControllerButtonListener>();
	
	private static float leftX = 0, leftY = 0, rightX = 0, rightY = 0, a = 0, b = 0, x = 0, y = 0, trigger = 0;
	
	public static void initControllers() {
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		for (int i = 0; i < controllers.length; i++) {
			Controller c = controllers[i];
			System.out.println(c.getName());
			if (c.getType() == Controller.Type.GAMEPAD)
				controller = c;
		}

		if (controller == null) {
			System.err.println("There is no controller found.");
			return;
		}
		System.out.println("Controller chosen: " + controller.getName());
		
		eventQueue = controller.getEventQueue();
		event = new Event();
	}

	public static void update() {
		if(controller == null) return;
		controller.poll();

		while (eventQueue.getNextEvent(event)) {
			Component component = event.getComponent();

			if (component != null) {
				Component.Identifier id = component.getIdentifier();
				float data = component.getPollData();
				if(data >= -0.1f && data <= 0.1f) data = 0;
				if (id == Identifier.Button._0) onChangeA(data);
				else if(id == Identifier.Button._1) onChangeB(data);
				else if(id == Identifier.Button._2) onChangeX(data);
				else if(id == Identifier.Button._3) onChangeY(data);
				else if(id == Identifier.Axis.X) leftX = data;
				else if(id == Identifier.Axis.Y) leftY = data;
				else if(id == Identifier.Axis.RX) rightX = data;
				else if(id == Identifier.Axis.RY) rightY = data;
				else if(id == Identifier.Axis.Z) trigger = data;
			}
		}
	}
	
	public static float getLeftJoystickX() {
		return leftX;
	}
	
	public static float getLeftJoystickY() {
		return leftY;
	}
	
	public static float getRightJoystickX() {
		return rightX;
	}
	
	public static float getRightJoystickY() {
		return rightY;
	}
	
	public static boolean getA() {
		return a > 0.5f ? true : false;
	}
	
	public static boolean getB() {
		return b > 0.5f ? true : false;
	}
	
	public static boolean getX() {
		return x > 0.5f ? true : false;
	}
	
	public static boolean getY() {
		return y > 0.5f ? true : false;
	}
	
	public static float getTriggerLeft() {
		float value = -trigger;
		if(trigger < 0) value = 0;
		return value;
	}
	
	public static float getTriggerRight() {
		float value = trigger;
		if(value < 0) value = 0;
		return value;
	}
	
	public static float getTrigger() {
		return trigger;
	}
	
	private static void onChangeA(float data) {
		if(a == data) return;
		a = data;
		if(getA()) {
			for(ControllerButtonListener b : buttonAListeners) {
				b.onButtonPressed();
			}
		} else {
			for(ControllerButtonListener b : buttonAListeners) {
				b.onButtonReleased();
			}
		}
	}
	
	private static void onChangeB(float data) {
		if(b == data) return;
		b = data;
		if(getB()) {
			for(ControllerButtonListener b : buttonBListeners) {
				b.onButtonPressed();
			}
		} else {
			for(ControllerButtonListener b : buttonBListeners) {
				b.onButtonReleased();
			}
		}
	}
	
	private static void onChangeX(float data) {
		if(x == data) return;
		x = data;
		if(getX()) {
			for(ControllerButtonListener b : buttonXListeners) {
				b.onButtonPressed();
			}
		} else {
			for(ControllerButtonListener b : buttonXListeners) {
				b.onButtonReleased();
			}
		}
	}

	private static void onChangeY(float data) {
		if(y == data) return;
		y = data;
		if(getY()) {
			for(ControllerButtonListener b : buttonYListeners) {
				b.onButtonPressed();
			}
		} else {
			for(ControllerButtonListener b : buttonYListeners) {
				b.onButtonReleased();
			}
		}
	}
	
	public static void addListenerA(ControllerButtonListener b) {
		buttonAListeners.add(b);
	}
	
	public static void addListenerB(ControllerButtonListener b) {
		buttonBListeners.add(b);
	}
	
	public static void addListenerX(ControllerButtonListener b) {
		buttonXListeners.add(b);
	}
	
	public static void addListenerY(ControllerButtonListener b) {
		buttonYListeners.add(b);
	}
	
	

}
