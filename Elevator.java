package org.usfirst.frc.team815.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Timer;

public class Elevator {
	private WPI_VictorSPX elevatorMotor1;
	private WPI_VictorSPX elevatorMotor2;
	private Timer timer;
	private final double MAX_SPEED = 1;
	private final double SPEEDUP_TIME = 3;
	private Boolean rightTriggerPressed = false;
	private Boolean leftTriggerPressed = false;
 	
	public Elevator(int motorPort1, int motorPort2) {
		elevatorMotor1 = new  WPI_VictorSPX(motorPort1);
		elevatorMotor2 = new WPI_VictorSPX(motorPort2);
	
	}
	
	public void StartElevator() {
		timer.reset();
		timer.start();
	}
	
	public void StopElevator() {
		timer.stop();
		elevatorMotor1.set(0);
		elevatorMotor2.set(0);
	}
	
	public void RaiseElevator() {
		if(leftTriggerPressed == true) {
			elevatorMotor1.setInverted(true);
			elevatorMotor2.setInverted(true);
			leftTriggerPressed = false;
		}
		
		if(timer.get() < SPEEDUP_TIME) {
			elevatorMotor1.set(timer.get() * MAX_SPEED);
			elevatorMotor2.set(timer.get() * MAX_SPEED);
		} else {
			timer.stop();
			elevatorMotor1.set(MAX_SPEED);
			elevatorMotor2.set(MAX_SPEED);
		}
		rightTriggerPressed = true;
	}
		
	public void LowerElevator() {
		if(rightTriggerPressed == true) {
			elevatorMotor1.setInverted(true);
			elevatorMotor2.setInverted(true);
			rightTriggerPressed = false;
		}
		
		if(timer.get() < SPEEDUP_TIME) {
			elevatorMotor1.set(timer.get() * MAX_SPEED);
			elevatorMotor2.set(timer.get() * MAX_SPEED);
		} else {
			timer.stop();
			elevatorMotor1.set(MAX_SPEED);
			elevatorMotor2.set(MAX_SPEED);
		}
		
		leftTriggerPressed = true;
	}
		
}
