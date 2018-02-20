package org.usfirst.frc.team815.robot;


import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Timer;

public class Claw {
	public final static double CLAW_MOVEMENT_TIME = 1;
	
	private WPI_VictorSPX clawMotor;
	private WPI_VictorSPX rollerMotor1;
	private WPI_VictorSPX rollerMotor2;
	private Boolean open;
	private Boolean isRolling;
	
	public Claw() {
		clawMotor = new WPI_VictorSPX(1);
		rollerMotor1 = new WPI_VictorSPX(2);
		rollerMotor2 = new WPI_VictorSPX(11);
		rollerMotor2.setInverted(true);
		open = false;
		isRolling = false;
	}
	
	public void openClaw() {
		clawMotor.set(-1);
		open = true;
	}
	
	public void closeClaw() {
		clawMotor.set(1);
		open = false;
	}
	
	public void stopClaw() {
		clawMotor.set(0);
	}
	
	public void rollForwards() {
		rollerMotor1.set(1);
		rollerMotor2.set(1);
		isRolling = true;
	}
	
	public void rollBackwards() {
		rollerMotor1.set(-1);
		rollerMotor2.set(-1);
		isRolling = true;
	}
	
	public void stopRolling() {
		rollerMotor1.set(0);
		rollerMotor2.set(0);
		isRolling = false;
	}

	public Boolean getOpen() {
		return open;
	}
	public Boolean isRolling() {
		return isRolling;
	}
}
