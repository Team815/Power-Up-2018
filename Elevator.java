package org.usfirst.frc.team815.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;

public class Elevator {
	
	private PIDOutputMulti<WPI_VictorSPX> elevatorMotors;
	private Encoder encoder;
	private PIDController elevatorController;
	private DigitalInput limitSwitch;
	private boolean calibrating;
	
	private static final int ENCODER_VALUE_NONE = -1;
	private static final int ENCODER_VALUE_SCALE = 2400;
	private static final int ENCODER_VALUE_SWITCH = 500;
	private static final int ENCODER_VALUE_BOTTOM = 0;
	private static final double P = 0.003;
	private static final double I = 0.0;
	private static final double D = 0.0;
	private static final double SPEED_CALIBRATE = -0.5;
	
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
		encoder = new Encoder(0, 1);
		elevatorMotors = new PIDOutputMulti<WPI_VictorSPX>();
		elevatorMotors.AddMotor(new WPI_VictorSPX(motorPort1));
		elevatorMotors.AddMotor(new WPI_VictorSPX(motorPort2));
		elevatorController = new PIDController(P, I, D, encoder, elevatorMotors);
		limitSwitch = new DigitalInput(9);
		calibrating = false;
		EnablePID();
	}
	
	public void SetPresetTarget(PresetTarget presetTargetIn) {
		elevatorController.setSetpoint(presetTargetIn.getEncoderTarget());
	}
	
	public void EnablePID() {
		elevatorController.setSetpoint(encoder.get());
		elevatorController.enable();
	}
	
	public void SetSpeed(double speed) {
		elevatorMotors.SetSpeed(speed);
	}
	
	public void InitiateManual() {
		elevatorController.disable();
		calibrating = false;
	}
	
	public void Calibrate() {
		calibrating = true;
		elevatorController.disable();
		elevatorMotors.SetSpeed(SPEED_CALIBRATE);
	}
	
	public void CheckCalibration() {
		if(calibrating && limitSwitch.get()) {
			calibrating = false;
			encoder.reset();
			EnablePID();
		}
	}
}
