package input;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

public class ControllerManager {

	private static Controller controller;
	
	public static void initControllers() {
		try {
			Controllers.create();
		} catch(LWJGLException e) {
			e.printStackTrace();
		}
		
		Controllers.poll();
		
		for(int i = 0;i<Controllers.getControllerCount(); i++){
            controller = Controllers.getController(i);
            System.out.println(controller.getName());
        }
	}
	
}
