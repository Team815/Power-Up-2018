package org.usfirst.frc.team815.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

public class Elevator {
	
	private ElevatorOutput elevatorMotors;
	private Encoder encoder;
	private PIDController elevatorController;
	//private double speed;
	//private PresetTarget presetTarget = PresetTarget.NONE;
	//private AutoState autoState = AutoState.STOPPED;
	
	//private static final double MAX_SPEED = 1;
	private static final int ENCODER_VALUE_NONE = -1;
	private static final int ENCODER_VALUE_SCALE = 3400;
	private static final int ENCODER_VALUE_SWITCH = 700;
	private static final int ENCODER_VALUE_BOTTOM = 0;
	
	/*
	public static enum AutoState {
		STOPPED,
		RAISING,
		LOWERING
	}
	*/
	
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
	
	private class ElevatorOutput implements PIDOutput {
		private WPI_VictorSPX motor1;
		private WPI_VictorSPX motor2;
		
		public ElevatorOutput(int motorPort1, int motorPort2) {
			motor1 = new  WPI_VictorSPX(motorPort1);
			motor2 = new WPI_VictorSPX(motorPort2);
		}
		
		@Override
		public void pidWrite(double output) {
			motor1.pidWrite(output);
			motor2.pidWrite(output);
		}
		
	}
 	
	public Elevator(int motorPort1, int motorPort2) {
		//speed = 0;
		encoder = new Encoder(0, 1);
		elevatorMotors = new ElevatorOutput(motorPort1, motorPort2);
		elevatorController = new PIDController(0.1, 0, 0, encoder, elevatorMotors);
		elevatorController.setSetpoint(0);
		elevatorController.enable();
	}
	
	/*
	public void SetSpeed(double speedIn) {
		speed = speedIn;
	}
	*/
	
	/*
	public void StopAuto() {
		autoState = AutoState.STOPPED;
		presetTarget = PresetTarget.NONE;
	}
	*/
	
	public void SetPresetTarget(PresetTarget presetTargetIn) {
		/*
		presetTarget = presetTargetIn;
		if (presetTargetIn.getEncoderTarget() > encoder.get()) {
			autoState = AutoState.RAISING;
		} else if  (presetTargetIn.getEncoderTarget() < encoder.get()) {
			autoState = AutoState.LOWERING;
		} else {
			StopAuto();
		}
		*/
		elevatorController.setSetpoint(presetTargetIn.getEncoderTarget());
	}
	
	/*
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
	*/
	
	/*
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
	*/
}
