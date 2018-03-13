package org.usfirst.frc.team815.robot;

import org.usfirst.frc.team815.robot.Claw.RollerDirection;
import org.usfirst.frc.team815.robot.Tilt.State;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class AutoScoreSwitch extends Autonomous {
	
	private static final Movement HALF 	= new Movement(0.28, 00.0, 00.0, 3.0);
	private static final Movement CLOSE = new Movement(0.28, 00.0, 00.0, 3.0);
	private static final Movement FAR   = new Movement(0.25, 90.0, 00.0, 5.0);
	private static final double H_FACTOR = 2.8;
	private final char target = GameLayout.charAt(0);
	
	public AutoScoreSwitch(Gyro gyroIn, Claw claw, Tilt tilt, Elevator elevator, SwitchState switchStateIn) {
		super(gyroIn, switchStateIn);
		timer = new Timer();
		this.claw = claw;
		this.tilt = tilt;
		this.elevator = elevator;
		this.switchState = switchStateIn;
	}

	@Override
	public void StartAuto() {
		if(switchState == SwitchState.HALF_SCORE_SWITCH_LEFT || switchState == SwitchState.HALF_SCORE_SWITCH_RIGHT) {
			speed = HALF.SPEED;
			angleStart = HALF.ANGLE_START;
			angleEnd = HALF.ANGLE_END;
			timeout = HALF.TIMEOUT;
		} else 
			if(switchState == SwitchState.SCORE_SWITCH_RIGHT && target == 'R'
		       || switchState == SwitchState.SCORE_SWITCH_LEFT  && target == 'L') {
			speed = CLOSE.SPEED;
			angleStart = CLOSE.ANGLE_START;
			angleEnd = CLOSE.ANGLE_END;
			timeout = CLOSE.TIMEOUT;
		} else {
			speed = FAR.SPEED;
			angleStart = FAR.ANGLE_START;
			angleEnd = FAR.ANGLE_END;
			timeout = FAR.TIMEOUT;
		}
		action = Action.APPROACH_SWITCH;
		rotation = 0;
		tilt.StartTilting();
		timer.start();
	}

	@Override
	public void Update() {
		switch (action) {
		case APPROACH_SWITCH:
			boolean isStraight = IsStraight();
			boolean atSwitch = AtSwitch();
			if(isStraight && atSwitch) {
				if(doScore()) {
					claw.setRollerDirection(RollerDirection.FORWARD);
					action = Action.DROP_POWERCUBE;
					timer.reset();
				}
				else {
					action = Action.STOP;
				}
			}
			break;
		case DROP_POWERCUBE:
			if(timer.get() >= 1) {
				action = Action.STOP;
			}
			break;
		case STOP:
			claw.setRollerDirection(RollerDirection.STOPPED);
			timer.stop();
			timer.reset();
			break;
		default:
			System.out.println(action);
			break;
		}
	}
	
	public boolean AtSwitch() {
		if(timer.get() >= timeout) {
			horizontal = 0;
			vertical = 0;
			rotation = 0;
			return true;
		} else {
			angle = (angleEnd - angleStart) * (timer.get() / timeout) + angleStart;
			if(switchState == SwitchState.SCORE_SWITCH_RIGHT) {
				angle *= -1;
			}
			horizontal = H_FACTOR * speed * Math.sin(Math.toRadians(angle));
			vertical = speed * Math.cos(Math.toRadians(angle));
			return false;
		}
	}
	
	public boolean IsStraight() {
		tilt.Update();
		if(tilt.state == State.UP) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean scoreOnNearSide(char target) {
		switch(switchState) {
		case HALF_SCORE_SCALE_LEFT:
		case SCORE_SCALE_LEFT:
			if(target == 'L')
				return true;
			else return false;
		case HALF_SCORE_SCALE_RIGHT:
		case SCORE_SCALE_RIGHT:
			if(target == 'R')
				return true;
			else return false;
		default:
			return true;
		}
	}
	
	private boolean doScore() {
		if((switchState == SwitchState.HALF_SCORE_SCALE_RIGHT || switchState == SwitchState.HALF_SCORE_SCALE_LEFT) 
			&& !scoreOnNearSide(target))
			return false;
		else return true;
	}
}
