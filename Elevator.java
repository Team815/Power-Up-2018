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
	private boolean foundBottom;
	
	private static final int ENCODER_VALUE_NONE = -1;
	private static final int ENCODER_VALUE_SCALE = 2400;
	private static final int ENCODER_VALUE_SWITCH = 600;
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
		foundBottom = false;
		EnablePID();
	}
	
	public void SetPresetTarget(PresetTarget presetTargetIn) {
		if(foundBottom) {
			elevatorController.setSetpoint(presetTargetIn.getEncoderTarget());
		}
	}
	
	public void EnablePID() {
		elevatorController.setSetpoint(encoder.get());
		elevatorController.enable();
	}
	
	public void SetSpeed(double speed) {
		if ((speed < 0 && limitSwitch.get()) || (speed > 0 && encoder.get() >= ENCODER_VALUE_SCALE)) {
			elevatorMotors.SetSpeed(0);
		} else {
			elevatorMotors.SetSpeed(speed);
		}
	}
	
	public void InitiateManual() {
		elevatorController.disable();
		calibrating = false;
	}
	
	public void Calibrate() {
		calibrating = true;
		foundBottom = false;
		elevatorController.disable();
		elevatorMotors.SetSpeed(SPEED_CALIBRATE);
	}
	
	public void CheckCalibration() {
		if(calibrating && limitSwitch.get()) {
			calibrating = false;
			foundBottom = true;
			encoder.reset();
			EnablePID();
		}
	}
	
	public boolean isCalibrating() {
		return calibrating;
	}
}
