package org.usfirst.frc.team815.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Encoder;

public class Elevator {
	
	private WPI_VictorSPX elevatorMotor1;
	private WPI_VictorSPX elevatorMotor2;
	private Encoder encoder;
	private double speed;
	private PresetTarget presetTarget = PresetTarget.NONE;
	private AutoState autoState = AutoState.STOPPED;
	
	private static final double MAX_SPEED = 1;
	private static final int ENCODER_VALUE_NONE = -1;
	private static final int ENCODER_VALUE_SCALE = 3400;
	private static final int ENCODER_VALUE_SWITCH = 700;
	private static final int ENCODER_VALUE_BOTTOM = 0;
	
	public static enum AutoState {
		STOPPED,
		RAISING,
		LOWERING
	}
	
	public static enum PresetTarget {
		NONE(ENCODER_VALUE_NONE),
		SCALE(ENCODER_VALUE_SCALE),
		SWITCH(ENCODER_VALUE_SWITCH),
		BOTTOM(ENCODER_VALUE_BOTTOM);
		
		int encoderTarget;
		
		private PresetTarget(int encoderTargetIn) {
			encoderTarget = encoderTargetIn;
		}
		
		public int getEncoderTarget() {
			return encoderTarget;
		}
	}
 	
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
		autoState = AutoState.STOPPED;
		presetTarget = PresetTarget.NONE;
	}
	public void SetPresetTarget(PresetTarget presetTargetIn) {
		presetTarget = presetTargetIn;
		if (presetTargetIn.getEncoderTarget() > encoder.get()) {
			autoState = AutoState.RAISING;
		} else if  (presetTargetIn.getEncoderTarget() < encoder.get()) {
			autoState = AutoState.LOWERING;
		} else {
			StopAuto();
		}
	}
	
	public boolean PassedTarget() {
		switch(autoState)
		{
		case RAISING:
			return presetTarget.getEncoderTarget() < encoder.get();
		case LOWERING:
			return presetTarget.getEncoderTarget() > encoder.get();
		default:
			return true;
		}
	}
	
	public void Update() {
		switch(autoState) {
		case RAISING:
		case LOWERING:
			if(PassedTarget()) {
				StopAuto();
			} else {
				speed = MAX_SPEED * Math.signum(presetTarget.encoderTarget - encoder.get());
			}
			break;
		case STOPPED:
			break;
		}
		SetElevatorMotor(); 
	}
}
