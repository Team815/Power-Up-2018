package org.usfirst.frc.team815.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Encoder;

public class Elevator {
	
	public enum AutoState {
		STOPPED,
		RAISING,
		LOWERING
	}
	
	public enum PresetTarget {
		NONE(-1),
		SCALE(3400),
		SWITCH(700),
		BOTTOM(0);
		
		int encoderTarget;
		
		private PresetTarget(int encoderTargetIn) {
			encoderTarget = encoderTargetIn;
		}
		
		public int getEncoderTarget() {
			return encoderTarget;
		}
	}
	
	public class Autovator {
		public PresetTarget target = PresetTarget.NONE;
		public AutoState autoState = AutoState.STOPPED;
		
		
		public boolean PassedTarget() {
			switch(autoState)
			{
			case RAISING:
				return target.getEncoderTarget() < encoder.get();
			case LOWERING:
				return target.getEncoderTarget() > encoder.get();
			default:
				return true;
			}
		}
	}
	
	private WPI_VictorSPX elevatorMotor1;
	private WPI_VictorSPX elevatorMotor2;
	private Encoder encoder;
	private final double MAX_SPEED = 1;
	private Autovator autovator = new Autovator();
	private double speed;
 	
	public Elevator(int motorPort1, int motorPort2) {
		speed = 0;
		elevatorMotor1 = new  WPI_VictorSPX(motorPort1);
		elevatorMotor2 = new WPI_VictorSPX(motorPort2);
		encoder = new Encoder(0, 1);
	
	}
	
	public void SetElevatorMotor() {
		elevatorMotor1.set(speed);
		elevatorMotor2.set(speed);
	}
	
	public void SetSpeed(double speedIn) {
		speed = speedIn;
	}
	
	public void StopAuto() {
		autovator.autoState = AutoState.STOPPED;
		autovator.target = PresetTarget.NONE;
	}
	public void SetPresetTarget(PresetTarget targetIn) {
		autovator.target = targetIn;
		if (targetIn.getEncoderTarget() > encoder.get()) {
			autovator.autoState = AutoState.RAISING;
		} else if  (targetIn.getEncoderTarget() < encoder.get()) {
			autovator.autoState = AutoState.LOWERING;
		} else {
			StopAuto();
		}
	}
	
	public void Update() {
		switch(autovator.autoState) {
		case RAISING:
		case LOWERING:
			if(autovator.PassedTarget()) {
				StopAuto();
			} else {
				speed = MAX_SPEED * Math.signum(autovator.target.encoderTarget - encoder.get());
			}
			break;
		case STOPPED:
			break;
		}
		SetElevatorMotor(); 
	}
}
