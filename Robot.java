package org.usfirst.frc.team815.robot;

import org.usfirst.frc.team815.robot.Controller.AnalogName;
import org.usfirst.frc.team815.robot.Controller.ButtonName;
import org.usfirst.frc.team815.robot.Dpad.Direction;
import org.usfirst.frc.team815.robot.Elevator.PresetTarget;
import org.usfirst.frc.team815.robot.Switchboard.PotName;

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
	Controller controllerElevator;
	Controller controllerTilt;
	Controller controllerDrive;
	Drive drive = new Drive(4, 7, 10, 3);
	Autonomous auto = new Autonomous();
	Elevator elevator = new Elevator(5,6);
	Tilt tilt = new Tilt();
	
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
	}
	
	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
	}
	
	/**
	 * This function is called once each time the robot enters tele-operated mode
	 */
	@Override
	public void teleopInit(){
		
		controllerDrive = controller0;
		controllerElevator = controller0;
		controllerTilt = controller0;
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
		    	controllerElevator = controller0;
			} else {
		    	controllerElevator = controller0;
			}
		}
		
		// Tilt Section
		
		if(controllerTilt.WasClicked(ButtonName.Start)) {
			tilt.StartTilting();
		}
		
		tilt.Update();
		
		// Elevator Section
		
		boolean leftTriggerActivated = controllerElevator.JustActivated(Controller.AnalogName.LeftTrigger);
		boolean rightTriggerActivated = controllerElevator.JustActivated(Controller.AnalogName.RightTrigger);
		boolean leftTriggerZeroed = controllerElevator.JustZeroed(Controller.AnalogName.LeftTrigger);
		boolean rightTriggerZeroed = controllerElevator.JustZeroed(Controller.AnalogName.RightTrigger);
		double rightTriggerValue = controllerElevator.GetValue(AnalogName.RightTrigger);
		double leftTriggerValue = controllerElevator.GetValue(AnalogName.LeftTrigger);
		double triggerValue = Math.max(rightTriggerValue, leftTriggerValue);
		
		if(leftTriggerActivated || rightTriggerActivated) {
			elevator.InitiateManual();
		} else if (leftTriggerZeroed || rightTriggerZeroed) {
			if(triggerValue == 0) {
				elevator.EnablePID();
			}
		}
		
		if(triggerValue == leftTriggerValue) {
			triggerValue *= -1;
		}
		if(triggerValue != 0) {
			elevator.SetSpeed(triggerValue);
		} else {
			if(controllerElevator.GetDpadDirection() == Dpad.Direction.Up) {
				elevator.SetPresetTarget(PresetTarget.SCALE);
			} else if(controllerElevator.GetDpadDirection() == Dpad.Direction.Right) {
				elevator.SetPresetTarget(PresetTarget.SWITCH);
			} else if(controllerElevator.GetDpadDirection() == Dpad.Direction.Down) {
				elevator.SetPresetTarget(PresetTarget.BOTTOM);
			}
		}
		
		if(controllerElevator.WasClicked(Controller.ButtonName.RJ)) {
			elevator.Calibrate();
		}
		
		elevator.CheckCalibration();
			
		// Gyro Section
		
		if(controllerDrive.WasClicked(ButtonName.B)) {
			//drive.SetPlayerAngle();
		}
		
		if(controllerDrive.JustZeroed(AnalogName.RightJoyX)){
			//drive.ResetTargetAngle();
		}
		
		// Speed Control Section
		
		if(controllerDrive.IsPressed(ButtonName.RB) || controllerDrive.IsPressed(ButtonName.LB)) {
			drive.SetMaxSpeed(controllerDrive);
		}
		
		
		// Drive Section
		
		drive.Update(controllerDrive);
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
