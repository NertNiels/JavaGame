package input;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import net.java.games.input.Rumbler;

public class ControllerManager {

	private static Controller controller;

	private static EventQueue eventQueue;
	private static Event event;
	
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
				if (id == Identifier.Button._0) a = data;
				else if(id == Identifier.Button._1) b = data;
				else if(id == Identifier.Button._2) x = data;
				else if(id == Identifier.Button._3) y = data;
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
	
	

}
