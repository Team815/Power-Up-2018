package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public abstract class Autonomous {
	
	String GameLayout = DriverStation.getInstance().getGameSpecificMessage();
	
	protected double horizontal, vertical, rotation;
	Gyro gyro;
	
	public Autonomous(Gyro gyroIn) {
		gyro = gyroIn;
		horizontal = vertical = rotation = 0;
	}
	
	public abstract void StartAuto();
		
	public abstract void Update();
	
	public double GetHorizontal() {
		return horizontal;
	}
	
	public double GetVertical() {
		return vertical;
	}
	
	public double GetRotation() {
		return rotation;
	}
}
