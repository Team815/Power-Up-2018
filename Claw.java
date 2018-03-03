package org.usfirst.frc.team815.robot;


import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Timer;

public class Claw {
	private final static double CLAW_MOVEMENT_TIME = 0.5;
	
	private WPI_VictorSPX clawMotor;
	private PIDOutputMulti<WPI_VictorSPX> rollerMotors;
	private Boolean open;
	private Timer timer;
	
	public enum RollerDirection {
		FORWARD(.5),
		BACKWARD(-1),
		STOPPED(0);
		
		double directionValue;
		
		private RollerDirection(double directionValueIn) {
			directionValue = directionValueIn;
		}
		
		public double getDirectionValue() {
			return directionValue;
		}
	}
	
	public Claw() {
		clawMotor = new WPI_VictorSPX(1);
		WPI_VictorSPX rollerMotor1 = new WPI_VictorSPX(2);
		WPI_VictorSPX rollerMotor2 = new WPI_VictorSPX(11);
		rollerMotor2.setInverted(true);
		rollerMotors = new PIDOutputMulti<WPI_VictorSPX>();
		rollerMotors.AddMotor(rollerMotor1);
		rollerMotors.AddMotor(rollerMotor2);
		open = false;
		timer = new Timer();
	}
	
	public void toggleClaw() {
		if(open) {
			closeClaw();
		} else {
			openClaw();
		}
	}
	
	public void openClaw() {
		timer.start();
		clawMotor.set(-1);
		open = true;
	}
	
	public void closeClaw() {
		timer.start();
		clawMotor.set(1);
		open = false;
	}
	
	public void update() {
		if(timer.get() > CLAW_MOVEMENT_TIME)
			stopClaw();
	}
	
	private void stopClaw() {
		clawMotor.set(0);
		timer.stop();
		timer.reset();
	}
	
	public void setRollerDirection(RollerDirection rollerDirection) {
		rollerMotors.SetSpeed(rollerDirection.getDirectionValue());
	}
}