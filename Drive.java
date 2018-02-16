package org.usfirst.frc.team815.robot;

import org.usfirst.frc.team815.robot.Controller.ButtonName;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class Drive {
	private MecanumDrive drive;
	private ADXRS450_Gyro gyro;
	private DriveOutput driveOutput;
	private PIDController driveController;
	private double speedMultiplier = 1;
	private static final double MIN_MULTIPLIER = 0.2;
	private static final double MAX_MULTIPLIER = 1;
	private static final double MULTIPLIER_INCREMENT = 0.01;
	
	private class DriveOutput implements PIDOutput {

		@Override
		public void pidWrite(double output) {
		}
		
	}
	
	public Drive(int frontRightId, int rearRightId, int frontLeftId, int rearLeftId) {
		WPI_VictorSPX talonFrontRight = new WPI_VictorSPX(frontRightId);
		WPI_VictorSPX talonRearRight = new WPI_VictorSPX(rearRightId);
		WPI_VictorSPX talonFrontLeft = new WPI_VictorSPX(frontLeftId);
		WPI_VictorSPX talonRearLeft = new WPI_VictorSPX(rearLeftId);
		gyro = new ADXRS450_Gyro();
    	drive = new MecanumDrive(talonFrontLeft, talonRearLeft, talonFrontRight, talonRearRight);
    	driveController = new PIDController(0.003, 0.0, 0.0, gyro, driveOutput);
    	driveController.enable();
	}
	
	public void SetMaxSpeed(Controller controller) {
    	if(controller.IsPressed(ButtonName.LB) && speedMultiplier > MIN_MULTIPLIER) {
    		speedMultiplier -= MULTIPLIER_INCREMENT;
    	} else if (controller.IsPressed(ButtonName.RB) && speedMultiplier < MAX_MULTIPLIER) {
    		speedMultiplier += MULTIPLIER_INCREMENT;
    	}
    	
		drive.setMaxOutput(speedMultiplier);
    }
	
	public void Update(double horizontal, double vertical, double rotation) {
    	drive.driveCartesian(horizontal, vertical, rotation);
	}
}
