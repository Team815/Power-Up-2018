package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Button {
	private int buttonIndex;
	private boolean isPressed = false;
	private boolean wasPressed = false;
	private boolean toggle = false;
	
	public Button(int buttonIndexIn) {
		buttonIndex = buttonIndexIn;
	}
	
	public int GetIndex() {
		return buttonIndex;
	}
	
	public boolean IsPressed() {
		return isPressed;
	}
	
	public boolean WasClicked() {
		return isPressed && !wasPressed;
	}
	
	public boolean WasReleased() {
		return wasPressed && !isPressed;
	}
	
	public boolean IsToggled() {
		return toggle;
	}
	
	public void Update(Joystick stick) {
		wasPressed = isPressed;
		isPressed = stick.getRawButton(buttonIndex);
		if(WasClicked()) {
			toggle = !toggle;
		}
	}
}
