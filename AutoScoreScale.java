package org.usfirst.frc.team815.robot;

import org.usfirst.frc.team815.robot.Autonomous.Action;
import org.usfirst.frc.team815.robot.Claw.RollerDirection;
import org.usfirst.frc.team815.robot.Elevator.PresetTarget;
import org.usfirst.frc.team815.robot.Tilt.State;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class AutoScoreScale extends Autonomous {
	private static final float DRIVE_TIME_LIMIT = 3;	// Experiment with this value
	
	public AutoScoreScale(Gyro gyroIn, Claw claw, Tilt tilt, Elevator elevator) {
		super(gyroIn);
		action = Action.TILT_FORWARD;
		routineTimer = new Timer();
		driveTimer = new Timer();
		this.claw = claw;
		this.tilt = tilt;
		this.elevator = elevator;
	}

	@Override
	public void StartAuto() {
		routineTimer.start();
	}

	@Override
	public void Update() {
		if (routineTimer.get() > 0) {
			action = setAction();
			
			switch (action) {
			case TILT_FORWARD:
				if(tilt.state == State.DOWN)
					tilt.StartTilting();
				tilt.Update();
				break;
			case APPROACH_SCALE:
				if(driveTimer.get() == 0)
					driveTimer.start();
				else {
					if(driveTimer.get() > DRIVE_TIME_LIMIT) {
						horizontal = 0;	
						vertical = 0;
						rotation = 0;
						driveTimer.start();
						driveTimer.reset();
					}
					else {
						horizontal = 0;	
						vertical = 1;
						rotation = 0;
					}
				}
				break;
			case RAISE_ELEVATOR:
				elevator.SetPresetTarget(PresetTarget.SCALE);
				break;
			case DROP_POWERCUBE:
				claw.setRollerDirection(RollerDirection.FORWARD);
				break;
			case STOP:
				claw.setRollerDirection(RollerDirection.STOPPED);
				routineTimer.stop();
				routineTimer.reset();
				driveTimer.stop();
				driveTimer.reset();
				break;
			}
		}
		
	}
	
	@Override
	protected Action setAction() {		// Need to combine actions after testing to get under 15 seconds
		if(routineTimer.get() < 8)
			return Action.TILT_FORWARD;
//		else if(routineTimer.get() < 11)
//			return Action.RAISE_ELEVATOR;
//		else if(routineTimer.get() < 15)
//			return Action.APPROACH_SCALE;
//		else if(routineTimer.get() < 17)
//			return Action.DROP_POWERCUBE;
//		else if(routineTimer.get() >= 17 || routineTimer.get() == 0)
//			return Action.STOP;
		else return Action.STOP;
	}

}
