package org.usfirst.frc.team815.robot;

import org.usfirst.frc.team815.robot.Controller.ButtonName;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class Drive {
	private MecanumDrive drive;
	private double speedMultiplier = 1;
	private static final double MIN_MULTIPLIER = 0.2;
	private static final double MAX_MULTIPLIER = 1;
	private static final double MULTIPLIER_INCREMENT = 0.01;
	
	public Drive(int frontRightId, int rearRightId, int frontLeftId, int rearLeftId) {
		WPI_VictorSPX talonFrontRight = new WPI_VictorSPX(frontRightId);
		WPI_VictorSPX talonRearRight = new WPI_VictorSPX(rearRightId);
		WPI_VictorSPX talonFrontLeft = new WPI_VictorSPX(frontLeftId);
		WPI_VictorSPX talonRearLeft = new WPI_VictorSPX(rearLeftId);
    	
    	talonFrontRight.setInverted(true);
    	talonRearRight.setInverted(true);
    	  	
    	drive = new MecanumDrive(talonFrontLeft, talonRearLeft, talonFrontRight, talonRearRight);
	}
	
	public void SetMaxSpeed(Controller controller) {
    	if(controller.IsPressed(ButtonName.LB) && speedMultiplier > MIN_MULTIPLIER) {
    		speedMultiplier -= MULTIPLIER_INCREMENT;
    	} else if (controller.IsPressed(ButtonName.RB) && speedMultiplier < MAX_MULTIPLIER) {
    		speedMultiplier += MULTIPLIER_INCREMENT;
    	}
    	
		drive.setMaxOutput(speedMultiplier);
    }
	
	public void Update(double horizontal, double vertical, double rotation, double gyroValue) {
		final double QUARTER = Math.PI / 2;
		final double MAX_MULTIPLIER = 2;
		
		double horizontalSign = Math.signum(horizontal);
		double verticalSign = Math.signum(vertical);
		
    	double magnitude = Math.sqrt(Math.pow(horizontal, 2) + Math.pow(vertical, 2));
    	double angle = horizontal == 0 ? QUARTER : Math.atan(Math.abs(vertical) / Math.abs(horizontal));
    	angle = QUARTER - angle;
    	double percent = angle / QUARTER;
    	double multiplier = percent * (MAX_MULTIPLIER - 1) + 1;
    	
    	magnitude *= multiplier;
    	
    	horizontal = Math.min(1, magnitude * Math.sin(angle));
    	horizontal *= horizontalSign;
    	vertical = Math.min(1, magnitude * Math.cos(angle));
    	vertical *= verticalSign;

    	drive.driveCartesian(horizontal, vertical, rotation, gyroValue);
	}
}
