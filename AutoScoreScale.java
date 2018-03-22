package org.usfirst.frc.team815.robot;

import org.usfirst.frc.team815.robot.Claw.RollerDirection;
import org.usfirst.frc.team815.robot.Elevator.PresetTarget;
import org.usfirst.frc.team815.robot.Tilt.State;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class AutoScoreScale extends Autonomous {
	private static final Movement HALF 	= new Movement(0.5, 00.0, 00.0, 2.5);
	private static final Movement CLOSE = new Movement(0.5, 00.0, 00.0, 2.5);
	private static final Movement FAR   = new Movement(0.25, 90.0, 00.0, 5.0);
	private static final double H_FACTOR = 2.8;
	private final char target = GameLayout.charAt(1);
	
	public AutoScoreScale(Gyro gyroIn, Claw claw, Tilt tilt, Elevator elevator, SwitchState switchStateIn) {
		super(gyroIn, switchStateIn);
		timer = new Timer();
		this.claw = claw;
		this.tilt = tilt;
		this.elevator = elevator;
		this.switchState = switchStateIn;
	}

	@Override
	public void StartAuto() {
		if(switchState == SwitchState.HALF_SCORE_SCALE_LEFT || switchState == SwitchState.HALF_SCORE_SCALE_RIGHT) {
			speed = HALF.SPEED;
			angleStart = HALF.ANGLE_START;
			angleEnd = HALF.ANGLE_END;
			timeout = HALF.TIMEOUT;
		} else if(scoreOnNearSide(target)) {
			speed = CLOSE.SPEED;
			angleStart = CLOSE.ANGLE_START;
			angleEnd = CLOSE.ANGLE_END;
			timeout = CLOSE.TIMEOUT;
		/* !!! Movement for this case has not been configured yet !!! */
		} else {
			speed = FAR.SPEED;
			angleStart = FAR.ANGLE_START;
			angleEnd = FAR.ANGLE_END;
			timeout = FAR.TIMEOUT;
		}
		action = Action.APPROACH_SCALE;
		rotation = 0;
		tilt.StartTilting();
		timer.start();
	}

	@Override
	public void Update() {
		switch (action) {
		case APPROACH_SCALE:
			boolean isStraight = IsStraight();
			boolean atScale = AtSwitch();
			if(isStraight && atScale) {
				if(doScore()) {
				elevator.SetPresetTarget(PresetTarget.SCALE);
				action = Action.RAISE_ELEVATOR;
				timer.reset();
				}
				else action = Action.STOP;
			}
			break;
		case RAISE_ELEVATOR:
			if(elevator.getEncoderValue() >= Elevator.ENCODER_VALUE_SCALE) {
				action = Action.DROP_POWERCUBE;
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
			if(switchState == SwitchState.SCORE_SCALE_RIGHT) {
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
