package org.usfirst.frc.team815.robot;

import org.usfirst.frc.team815.robot.Controller.ButtonName;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class Drive {
	private MecanumDrive drive;
	private ADXRS450_Gyro gyro;
	private DriveOutput driveOutput;
	private PIDController driveController;
	private Timer timer;
	private double rotationCompensation;
	private double speedMultiplier = 1;
	private static final double MIN_MULTIPLIER = 0.2;
	private static final double MAX_MULTIPLIER = 1;
	private static final double MULTIPLIER_INCREMENT = 0.1;
	private static final double MIN_AUTO_SPEED_HEIGHT_ENCODER_VALUE = 1200;
	private static final double MAX_AUTO_SPEED_HEIGHT_ENCODER_VALUE = 2400;
	private static final double P = 0.03;
	private static final double I = 0.0;
	private static final double D = 0.0;
	private static final double PID_DELAY = 0.5;
	
	private class DriveOutput implements PIDOutput {

		@Override
		public void pidWrite(double output) {
			rotationCompensation = output;
		}
		
	}
	
	public Drive(int frontRightId, int rearRightId, int frontLeftId, int rearLeftId) {
		WPI_VictorSPX talonFrontRight = new WPI_VictorSPX(frontRightId);
		WPI_VictorSPX talonRearRight = new WPI_VictorSPX(rearRightId);
		WPI_VictorSPX talonFrontLeft = new WPI_VictorSPX(frontLeftId);
		WPI_VictorSPX talonRearLeft = new WPI_VictorSPX(rearLeftId);
		timer = new Timer();
		gyro = new ADXRS450_Gyro();
		driveOutput = new DriveOutput();
    	drive = new MecanumDrive(talonFrontLeft, talonRearLeft, talonFrontRight, talonRearRight);
    	driveController = new PIDController(P, I, D, gyro, driveOutput);
    	driveController.enable();
	}
	
	public void ManualSetMaxSpeed(Controller controller) {
    	if(controller.WasClicked(ButtonName.LB) && speedMultiplier > MIN_MULTIPLIER) {
    		speedMultiplier -= MULTIPLIER_INCREMENT;
    	} else if (controller.WasClicked(ButtonName.RB) && speedMultiplier < MAX_MULTIPLIER) {
    		speedMultiplier += MULTIPLIER_INCREMENT;
    	}
    	
		drive.setMaxOutput(speedMultiplier);
    }
	
	public void AutoSetMaxSpeed(int encoderValue) {
		if(encoderValue > MIN_AUTO_SPEED_HEIGHT_ENCODER_VALUE)
			speedMultiplier = MIN_MULTIPLIER + ((encoderValue-MIN_AUTO_SPEED_HEIGHT_ENCODER_VALUE)*((MAX_MULTIPLIER-MIN_MULTIPLIER)/(MAX_AUTO_SPEED_HEIGHT_ENCODER_VALUE-MIN_AUTO_SPEED_HEIGHT_ENCODER_VALUE)));
		else speedMultiplier = MAX_MULTIPLIER;
		drive.setMaxOutput(speedMultiplier);
	}
	
	public void ResetPlayerAngle() {
		gyro.reset();
		driveController.setSetpoint(gyro.getAngle());
	}
	
	public void Update(double horizontal, double vertical, double rotation) {
		
		if(rotation != 0 && driveController.isEnabled()) {
			timer.stop();
			timer.reset();
			driveController.disable();
		} else if (rotation == 0 && !driveController.isEnabled() && timer.get() == 0) { // TODO: prevent repeated starts
			timer.start();
		}
		
		if(timer.get() > PID_DELAY) {
			timer.stop();
			timer.reset();
			driveController.setSetpoint(gyro.getAngle());
			driveController.enable();
		}
		
		if(driveController.isEnabled()) {
			rotation += rotationCompensation;
		}
		
		drive.driveCartesian(horizontal, vertical, rotation, -gyro.getAngle());
	}
	
	public Gyro getGyro() {
		return gyro;
	}
}
