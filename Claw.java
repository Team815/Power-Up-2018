package org.usfirst.frc.team815.robot;


import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Timer;

public class Claw {
	public final static double CLAW_MOVEMENT_TIME = 1;
	
	private WPI_VictorSPX clawMotor;
	private WPI_VictorSPX rollerMotor;
	private Boolean open;
	private Boolean isRolling;
	private Timer clawTimer;
	
	public Claw() {
		clawMotor = new WPI_VictorSPX(1);
		rollerMotor = new WPI_VictorSPX(2);
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
	
	public void rollForward() {
		rollerMotor.set(1);
		isRolling = true;
	}
	
	public void rollBackwards() {
		rollerMotor.set(-1);
		isRolling = true;
	}
	
	public void stopRolling() {
		rollerMotor.set(0);
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
