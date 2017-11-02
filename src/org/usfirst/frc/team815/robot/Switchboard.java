package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Switchboard {
	
	public enum PotName {
		TopPot,
		BottomPot;
	}
	
	private Joystick stick;
	private Button switch1 = new Button(1);
	private Button switch2 = new Button(2);
	private Button switch3 = new Button(3);
	private Button switch4 = new Button(4);
	private Analog topPot = new Analog(0);
	private Analog bottomPot = new Analog(1);
	
	public Switchboard(int port) {
		stick = new Joystick(port);
	}
	
	public int GetBinaryValue() {
		int value = 0;
		value += switch4.IsPressed() ? 8 : 0;
		value += switch3.IsPressed() ? 4 : 0;
		value += switch2.IsPressed() ? 2 : 0;
		value += switch1.IsPressed() ? 1 : 0;
		return value;
	}
	
	public double GetAnalog(PotName potName) {
		if(potName == PotName.TopPot) {
			return topPot.GetValue();
		} else { // Assuming bottom pot
			return bottomPot.GetValue();
		}
	}
	
	public void Update() {
		switch1.Update(stick);
		switch2.Update(stick);
		switch3.Update(stick);
		switch4.Update(stick);
		topPot.Update(stick, 0);
		bottomPot.Update(stick, 0);
	}
}
