package com.nice.incontact.util;

import java.awt.Robot;
import java.awt.event.InputEvent;

public class Xlite {
	
	public static void answerCall(boolean isAgentLegConnected){
		try {
			if(!isAgentLegConnected){
				Thread.sleep(5000);
				Robot r = new Robot();
				r.mouseMove(1080, 130);
				r.mousePress(InputEvent.BUTTON1_MASK);
				r.mouseRelease(InputEvent.BUTTON1_MASK);
			}
			Thread.sleep(5000);
			Robot r = new Robot();
			r.mouseMove(1080, 130);
			r.mousePress(InputEvent.BUTTON1_MASK);
			r.mouseRelease(InputEvent.BUTTON1_MASK);
			Thread.sleep(5000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}