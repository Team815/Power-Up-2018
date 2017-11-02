package org.usfirst.frc.team815.robot;

import java.util.Scanner;

import org.usfirst.frc.team815.robot.Autonomous.State;
import org.usfirst.frc.team815.robot.BallPickup.BPState;
import org.usfirst.frc.team815.robot.Controller.AnalogName;
import org.usfirst.frc.team815.robot.Controller.ButtonName;
import org.usfirst.frc.team815.robot.Dpad.Direction;
import org.usfirst.frc.team815.robot.Switchboard.PotName;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	RobotDrive robot;
	Controller controller0 = new Controller(0);
	Controller controller1 = new Controller(1);
	Controller controllerShoot;
	Controller controllerLift;
	Controller controllerPickup;
	Controller controllerDrive;
	Switchboard switchboard = new Switchboard(2);
	Relay lightRelay = new Relay(0, Relay.Direction.kForward);
	Gyro gyro = new Gyro(1);
	Autonomous auto = new Autonomous(gyro, lightRelay);
	Lift lift = new Lift(30, 31);
	Shooter shooter = new Shooter(3, 2);
	BallPickup ballpickup = new BallPickup(1);
	CANTalon agitator = new CANTalon(2);
	double speedMultiplier = 1;
	//CameraServer server = CameraServer.getInstance();
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	@Override
    public void robotInit() {
		CANTalon talonFrontRight = new CANTalon(13);
		CANTalon talonRearRight = new CANTalon(12);
		CANTalon talonFrontLeft = new CANTalon(14);
		CANTalon talonRearLeft = new CANTalon(15);
    	
    	talonFrontRight.setInverted(true);
    	talonRearRight.setInverted(true);
    	  	
    	robot = new RobotDrive(talonFrontLeft, talonRearLeft, talonFrontRight, talonRearRight);
    	
        //server.startAutomaticCapture(0);
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    @Override
    public void autonomousInit() {
    	switchboard.Update();
    	gyro.SetPlayerAngle();
    	int autoState = switchboard.GetBinaryValue();
    	
    	System.out.println(autoState);
    	
    	if(autoState == 1) {
    		auto.SetTurningLeft(true);
    		auto.StartAuto(State.Positioning);
    	} else if(autoState == 4) {
    		auto.SetTurningLeft(false);
    		auto.StartAuto( State.Positioning);
    	} else if(autoState == 2) {
    		auto.StartAuto(State.Aligning);
    	}
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
    	
    	auto.Update();
    	
    	double horizontal = auto.GetHorizontal();
    	double vertical = auto.GetVertical();
    	double rotation = gyro.GetCompensation();
    	double gyroValue = auto.GetState() == State.Positioning ? gyro.GetAngle() : 0;
    	
    	//System.out.println("Target Angle:" + gyro.GetTargetAngle() + ", Angle: " + gyro.GetAngle() + ", Compensation: " + gyro.GetCompensation());
    	
    	robot.mecanumDrive_Cartesian(horizontal, vertical, rotation, gyroValue);
    	//Drive(horizontal, vertical, rotation, gyroValue);
    }
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    @Override
    public void teleopInit(){
    	
    	gyro.ResetTargetAngle();
    	lightRelay.set(Relay.Value.kOff);
    	//agitator.set(1);
    	
    	controllerDrive = controller0;
    	controllerPickup =  controller0;
    	
    	if(controller0.IsToggled(ButtonName.Start)) {
    		controllerShoot = controller0;
        	controllerLift = controller0;
    	} else {
    		controllerShoot = controller1;
        	controllerLift = controller1;
    	}
    	
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
    	
    	controller0.Update();
    	controller1.Update();
    	switchboard.Update();
    	
    	if(controller0.WasClicked(ButtonName.Start)) {
    		if(controller0.IsToggled(ButtonName.Start)) {
        		controllerShoot = controller0;
            	controllerLift = controller0;
        	} else {
        		controllerShoot = controller1;
            	controllerLift = controller1;
        	}
    	}
    	
    	// Ball Pickup Section
    	
    	if(controllerPickup.WasDpadDirectionClicked(Direction.Up)) {
    		ballpickup.Toggle(BPState.Suck);
    	} else if(controllerPickup.WasDpadDirectionClicked(Direction.Down)) {
    		ballpickup.Toggle(BPState.Blow);
    	} else if(controllerPickup.WasDpadDirectionClicked(Direction.Right)) {
    		ballpickup.Toggle(BPState.Off);
    	}
    	
    	// Lift Section
    	
    	if(controllerLift.WasClicked(ButtonName.Y)) {
    		lift.StartClimb();
    	}
    	
    	if(controllerLift.WasReleased(ButtonName.Y)) {
    		lift.StopClimb();
    	}
    	
    	if(controllerLift.IsPressed(ButtonName.Y)) {
    		lift.Climb();
    	}
    	
    	if(controllerLift.GetValue(AnalogName.RightTrigger) != 0 || controllerLift.JustZeroed(AnalogName.RightTrigger)){
    		lift.SetSpeed(controllerLift.GetValue(AnalogName.RightTrigger));
    	}
    	
    	// Shooter Section
    	
    	shooter.SetSpeeds(switchboard.GetAnalog(PotName.TopPot), switchboard.GetAnalog(PotName.BottomPot));
    	
    	if(controllerShoot.IsPressed(ButtonName.A)) {
    		shooter.SimpleUpdateShooter();
    	}
    	
    	if(controllerShoot.WasReleased(ButtonName.A)) {
    		shooter.SimpleStopShooter();
    	}
    	
    	if(controllerShoot.IsPressed(ButtonName.LB)) {
    		shooter.SimpleUpdateAgitator();
    	}
    	
    	if(controllerShoot.WasReleased(ButtonName.LB)) {
    		shooter.SimpleStopAgitator();
    	}
    	
    	if(controllerShoot.WasClicked(ButtonName.B)) {
    		shooter.startShooter();
    	}
    	
    	if(controllerShoot.IsPressed(ButtonName.B)) {
    		shooter.UpdateShooter();
    	}
    	
    	if(controllerShoot.WasReleased(ButtonName.B)) {
    		shooter.stopShooter();
    	}
    	
    	// Gyro Section
    	
    	if(controllerDrive.WasClicked(ButtonName.B)) {
    		gyro.SetPlayerAngle();
    	}
    	
    	/*
    	if(controllerDrive.WasClicked(ButtonName.Select)) {
    		gyro.Calibrate();
    		gyro.SetPlayerAngle();
    	}
    	*/
    	
    	if(controllerDrive.JustZeroed(AnalogName.RightJoyX)){
    		gyro.ResetTargetAngle();
    	}
    	
    	gyro.Update(controllerDrive.GetValue(AnalogName.RightJoyX) != 0);
    	
    	// Auto Align Section
    	
    	if(controllerDrive.WasClicked(ButtonName.X)) {
    		if(controllerDrive.IsToggled(ButtonName.X)) {
    			auto.StartAuto(State.Aligning);
    		} else {
    			lightRelay.set(Relay.Value.kOff);
    		}
    	}
    	
    	// Speed Control Section
    	
    	if(controllerDrive.IsPressed(ButtonName.RB) || controllerDrive.IsPressed(ButtonName.LB)) {
    		SetMaxSpeed();
    	}
    	
    	
    	// Drive Section
    	
    	double horizontal = 0;
    	double vertical = 0;
    	double rotation = 0;
    	double gyroValue = 0;
    	
    	if(!controllerDrive.IsToggled(ButtonName.Select)) {
	    	if(controllerDrive.IsToggled(ButtonName.X)) {
	    		auto.Update();
    			horizontal = auto.GetHorizontal();
	    		vertical = auto.GetVertical();
	    		rotation = gyro.GetCompensation();
	    		gyroValue = 0;
	    	} else {
	    		horizontal = -controllerDrive.GetValue(AnalogName.LeftJoyX);
	    		vertical = -controllerDrive.GetValue(AnalogName.LeftJoyY);
	    		rotation = controllerDrive.GetValue(AnalogName.RightJoyX);
	    		rotation = rotation == 0 ? gyro.GetCompensation() : rotation;
	    		gyroValue = controllerDrive.IsToggled(ButtonName.A) ? 0 : gyro.GetAngle();
	    	}
    	}
    	
    	robot.mecanumDrive_Cartesian(horizontal, vertical, rotation, gyroValue);
    	//Drive(horizontal, vertical, rotation, gyroValue);
    }
    
    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
    	LiveWindow.run();
    }
    
    @Override
    public void disabledInit() {
    	
    }
    
    private void SetMaxSpeed() {
    	final double MIN_MULTIPLIER = 0.2;
    	final double MAX_MULTIPLIER = 1;
    	final double MULTIPLIER_INCREMENT = 0.01;
    	if(controllerDrive.IsPressed(ButtonName.LB) && speedMultiplier > MIN_MULTIPLIER) {
    		speedMultiplier -= MULTIPLIER_INCREMENT;
    	} else if (controllerDrive.IsPressed(ButtonName.RB) && speedMultiplier < MAX_MULTIPLIER) {
    		speedMultiplier += MULTIPLIER_INCREMENT;
    	}
    	
		robot.setMaxOutput(speedMultiplier);
    }
    
    private void Drive(double horizontal, double vertical, double rotation, double gyroValue) {
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

    	robot.mecanumDrive_Cartesian(horizontal, vertical, rotation, gyroValue);
    }
}