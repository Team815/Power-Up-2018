package org.usfirst.frc.team815.robot;

import org.usfirst.frc.team815.robot.Autonomous.Action;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public abstract class Autonomous {
	
	String GameLayout = DriverStation.getInstance().getGameSpecificMessage();
	
	protected Timer routineTimer;
	protected Timer driveTimer;
	protected Claw claw;
	protected Tilt tilt;
	protected Elevator elevator;
	
	protected double horizontal, vertical, rotation;
	Gyro gyro;
	
	protected enum Action {
		TILT_FORWARD,
		GRAB_POWERCUBE,
		SECURE_POWERCUBE,
		APPROACH_SWITCH,
		APPROACH_SCALE,
		RAISE_ELEVATOR,
		DROP_POWERCUBE,
		STOP;
	}
	
	Action action;
	
	public Autonomous(Gyro gyroIn) {
		gyro = gyroIn;
		horizontal = vertical = rotation = 0;
	}
	
	public abstract void StartAuto();
		
	public abstract void Update();
	
	protected Action setAction() {
		return null;
	}
	
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
