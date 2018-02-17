package org.usfirst.frc.team815.robot;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;

public class PIDOutputMulti<T extends SpeedController> implements PIDOutput {
	private List<T> motors = new ArrayList<T>();
	
	public void AddMotor(T motor) {
		motors.add(motor);
	}
	
	@Override
	public void pidWrite(double output) {
		for(T motor : motors) {
			motor.pidWrite(output);
		}
	}
	
	public void SetSpeed(double speed) {
		for(T motor : motors) {
			motor.set(speed);
		}
	}
}
