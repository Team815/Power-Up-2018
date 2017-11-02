package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Analog {
	private int analogIndex;
	private double value = 0;
	private double previousValue = 0;
	
	public Analog(int analogIndexIn) {
		analogIndex = analogIndexIn;
	}
	
	public int GetIndex() {
		return analogIndex;
	}
	
	public double GetValue() {
		return value;
	}
	
	public boolean JustZeroed() {
		return value == 0 && !(previousValue == 0);
	}
	
	public void Update(Joystick stick, double analogThreshold) {
		previousValue = value;
		double valueIn = stick.getRawAxis(analogIndex);
		value = Math.abs(valueIn) > analogThreshold ? valueIn : 0;
	}
}
