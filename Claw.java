package org.usfirst.frc.team815.robot;


import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Timer;

public class Claw {
	public final static double CLAW_MOVEMENT_TIME = .5;
	
	private WPI_VictorSPX clawMotor;
	private WPI_VictorSPX rollerMotor1;
	private WPI_VictorSPX rollerMotor2;
	private Boolean open;
	private Boolean isRolling;
	

	private Timer clawTimer;
	
	public Claw() {
		clawMotor = new WPI_VictorSPX(1);
		rollerMotor1 = new WPI_VictorSPX(2);
		rollerMotor2 = new WPI_VictorSPX(11);
		rollerMotor2.setInverted(true);
		open = true;
		isRolling = false;
		clawTimer = new Timer();
	}
	
	public void openClaw() {
		clawMotor.set(-1);
		clawTimer.start();
		open = true;
	}
	
	public void closeClaw() {
		clawMotor.set(1);
		clawTimer.start();
		open = false;
	}
	
	public void stopClaw() {
		clawMotor.set(0);
		clawTimer.stop();
		clawTimer.reset();
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

	public double getClawTimer() {
		return clawTimer.get();
	}

	public Boolean getOpen() {
		return open;
	}
	public Boolean isRolling() {
		return isRolling;
	}
}
