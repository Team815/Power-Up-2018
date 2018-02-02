package org.usfirst.frc.team815.robot;

import org.usfirst.frc.team815.robot.BallPickup.BPState;
import org.usfirst.frc.team815.robot.Controller.AnalogName;
import org.usfirst.frc.team815.robot.Controller.ButtonName;
import org.usfirst.frc.team815.robot.Dpad.Direction;
import org.usfirst.frc.team815.robot.Switchboard.PotName;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Relay;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	Controller controller0 = new Controller(0);
	//Controller controller1 = new Controller(1);
	Controller controllerShoot;
	Controller controllerLift;
	Controller controllerPickup;
	Controller controllerDrive;
	//Switchboard switchboard = new Switchboard(2);
	Drive drive = new Drive(4, 7, 0, 3);
	Relay lightRelay = new Relay(0, Relay.Direction.kForward);
	Gyro gyro = new Gyro(1);
	Autonomous auto = new Autonomous();
	Lift lift = new Lift(30, 31);
	Shooter shooter = new Shooter(3, 2);
	BallPickup ballpickup = new BallPickup(1);
	WPI_TalonSRX agitator = new WPI_TalonSRX(2);
	//CameraServer server = CameraServer.getInstance();
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	@Override
    public void robotInit() {
        //server.startAutomaticCapture(0);
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    @Override
    public void autonomousInit() {
    	auto.StartAuto();
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
    	auto.Update();
    	
    	double horizontal = auto.GetHorizontal();
    	double vertical = auto.GetVertical();
    	double rotation = 0;
    	rotation = rotation == 0 ? gyro.GetCompensation() : rotation;
    	double gyroValue = 0;
    	
    	drive.Update(horizontal, vertical, rotation, gyroValue);
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
    		controllerShoot = controller0;
        	controllerLift = controller0;
    	}
    	
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
    	
    	controller0.Update();
    	//controller1.Update();
    	//switchboard.Update();
    	
    	if(controller0.WasClicked(ButtonName.Start)) {
    		if(controller0.IsToggled(ButtonName.Start)) {
        		controllerShoot = controller0;
            	controllerLift = controller0;
        	} else {
        		controllerShoot = controller0;
            	controllerLift = controller0;
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
    	
    	//shooter.SetSpeeds(switchboard.GetAnalog(PotName.TopPot), switchboard.GetAnalog(PotName.BottomPot));
    	
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
    	
    	// Speed Control Section
    	
    	if(controllerDrive.IsPressed(ButtonName.RB) || controllerDrive.IsPressed(ButtonName.LB)) {
    		drive.SetMaxSpeed(controllerDrive);
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
	    		horizontal = controllerDrive.GetValue(AnalogName.LeftJoyX);
	    		vertical = -controllerDrive.GetValue(AnalogName.LeftJoyY);
	    		rotation = controllerDrive.GetValue(AnalogName.RightJoyX);
	    		rotation = rotation == 0 ? gyro.GetCompensation() : rotation;
	    		gyroValue = controllerDrive.IsToggled(ButtonName.A) ? 0 : gyro.GetAngle();
	    	}
    	}
    	
    	drive.Update(horizontal, vertical, rotation, gyroValue);
    }
    
    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
    	
    }
    
    @Override
    public void disabledInit() {
    	
    }
}
