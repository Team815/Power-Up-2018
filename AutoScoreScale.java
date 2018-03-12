package org.usfirst.frc.team815.robot;

import org.usfirst.frc.team815.robot.Autonomous.Action;
import org.usfirst.frc.team815.robot.Claw.RollerDirection;
import org.usfirst.frc.team815.robot.Elevator.PresetTarget;
import org.usfirst.frc.team815.robot.Tilt.State;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class AutoScoreScale extends Autonomous {
	private static final double DRIVE_TIME = 3;	// Experiment with this value
	private static final double ROBOT_SPEED = 0.5;
	private static final double ROBOT_ANGLE = 72;
	
	public AutoScoreScale(Gyro gyroIn, Claw claw, Tilt tilt, Elevator elevator, SwitchState switchStateIn) {
		super(gyroIn, switchStateIn);
		action = Action.TILT_FORWARD;
		timer = new Timer();
		this.claw = claw;
		this.tilt = tilt;
		this.elevator = elevator;
	}

	@Override
	public void StartAuto() {
	}

	@Override
	public void Update() {
		switch (action) {
		case TILT_FORWARD:
			if(tilt.state == State.DOWN)
				tilt.StartTilting();
			tilt.Update();
			if(tilt.state == State.UP) {
				elevator.Calibrate();
				action = Action.CALIBRATE_ELEVATOR;
			}
			break;
		case CALIBRATE_ELEVATOR:
			elevator.CheckCalibration();
			if(!elevator.isCalibrating()) {
				elevator.SetPresetTarget(PresetTarget.SWITCH);
				double robot_horizontal_speed = ROBOT_SPEED * Math.cos(ROBOT_ANGLE);
				if(GameLayout.charAt(0) == 'L') {
					robot_horizontal_speed *= -1;
				}
				horizontal = robot_horizontal_speed;
				vertical = ROBOT_SPEED * Math.sin(ROBOT_ANGLE);
				rotation = 0;
				timer.start();
				action = Action.APPROACH_SWITCH;
			}
			break;
		case APPROACH_SWITCH:
			System.out.println(timer.get());
			if(timer.hasPeriodPassed(DRIVE_TIME)) {
				horizontal = 0;
				vertical = 0;
				rotation = 0;
				claw.setRollerDirection(RollerDirection.FORWARD);
				timer.reset();
				action = Action.DROP_POWERCUBE;
			}
			break;
		case RAISE_ELEVATOR:
			break;
		case DROP_POWERCUBE:
			if(timer.hasPeriodPassed(0.5)) {
				action = Action.STOP;
			}
			break;
		case STOP:
			claw.setRollerDirection(RollerDirection.STOPPED);
			timer.stop();
			timer.reset();
			break;
		}
	}
}
