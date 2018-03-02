package org.usfirst.frc.team815.robot;

import org.usfirst.frc.team815.robot.Claw.RollerDirection;
import org.usfirst.frc.team815.robot.Controller.AnalogName;
import org.usfirst.frc.team815.robot.Controller.ButtonName;
import org.usfirst.frc.team815.robot.Elevator.PresetTarget;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	Controller controller0 = new Controller(0);
	Controller controller1 = new Controller(1);
	Controller controllerClaw;
	Controller controllerElevator;
	Controller controllerTilt;
	Controller controllerDrive;
	Switchboard switchboard = new Switchboard(2);
	Drive drive = new Drive(4, 7, 10, 3);
	Autonomous auto;
	Claw claw = new Claw();
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
		switchboard.Update();
		Autonomous.SwitchState switchState = Autonomous.SwitchState.get(switchboard.GetBinaryValue());
		
		switch (switchState) {
		case CROSS_LINE_RIGHT:
		case CROSS_LINE_CENTER:
		case CROSS_LINE_LEFT:
			auto = new AutoCrossLine(drive.getGyro());
			break;
		case SCORE_SWITCH_CENTER:
			auto = new AutoScoreSwitch(drive.getGyro(), claw, tilt, elevator);
			break;
		case SCORE_SCALE_CENTER:
			auto = new AutoScoreScale(drive.getGyro(), claw, tilt, elevator);
			break;
		default:
			auto = new AutoTest(drive.getGyro());
			break;
		}
		
		drive.ResetPlayerAngle();
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
		double rotation = auto.GetRotation();
		
		drive.Update(horizontal, vertical, rotation);
	}
	
	/**
	 * This function is called once each time the robot enters tele-operated mode
	 */
	@Override
	public void teleopInit(){
		
		controllerClaw = controller0;
		controllerDrive = controller0;
		controllerElevator = controller1;
		controllerTilt = controller0;
		
		drive.ResetPlayerAngle();
	}
	
	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		controller0.Update();
		controller1.Update();
		
		// Claw Section
		
		if(controllerClaw.WasClicked(ButtonName.X)) {
			claw.toggleClaw();
		}
		
		claw.update();
		
		// Roller Subsection
		
		if(controllerClaw.WasReleased(ButtonName.LB) || controllerClaw.WasReleased(ButtonName.RB)) {
			if(controllerClaw.IsPressed(ButtonName.LB)) {
				claw.setRollerDirection(RollerDirection.BACKWARD);
			} else if(controllerClaw.IsPressed(ButtonName.RB)) {
				claw.setRollerDirection(RollerDirection.FORWARD);
			} else {
				claw.setRollerDirection(RollerDirection.STOPPED);
			}
		} else if(controllerClaw.WasClicked(ButtonName.LB)) {
			claw.setRollerDirection(RollerDirection.BACKWARD);
		} else if(controllerClaw.WasClicked(ButtonName.RB)) {
			claw.setRollerDirection(RollerDirection.FORWARD);
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
		
		
		// Drive Section
		
		if(controllerDrive.WasClicked(Controller.ButtonName.B)) {
			drive.ResetPlayerAngle();
		}
		
		double horizontal = controllerDrive.GetValue(AnalogName.LeftJoyX);		
		double vertical = -controllerDrive.GetValue(AnalogName.LeftJoyY);
		double rotation = controllerDrive.GetValue(AnalogName.RightJoyX);
		
		drive.Update(horizontal, vertical, rotation);
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
