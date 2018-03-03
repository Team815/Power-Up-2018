package org.usfirst.frc.team815.robot;

import org.usfirst.frc.team815.robot.Autonomous.SwitchState;
import org.usfirst.frc.team815.robot.Claw.RollerDirection;
import org.usfirst.frc.team815.robot.Elevator.PresetTarget;
import org.usfirst.frc.team815.robot.Tilt.State;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class AutoScoreSwitch extends Autonomous {
	private static class Movement {
		public final double SPEED;
		public final double ANGLE_START;
		public final double ANGLE_END;
		public final double TIMEOUT;
		
		public Movement(double speedIn, double angleStartIn, double angleEndIn, double timeoutIn) {
			SPEED = speedIn;
			ANGLE_START = angleStartIn;
			ANGLE_END = angleEndIn;
			TIMEOUT = timeoutIn;
		}
	}
	
	private static final Movement STRAIGHT = new Movement(0.5, 00.0, 00.0, 2.0);
	private static final Movement CLOSE    = new Movement(0.5, 00.0, 00.0, 2.0);
	private static final Movement FAR      = new Movement(0.5, 90.0, 00.0, 5.0);
	private static final double H_FACTOR = 1.3;
	
	private Timer timer;
	private double speed;
	private double angleStart;
	private double angleEnd;
	private double angle;
	private double timeout;
	
	private Action nextAction;
	
	public AutoScoreSwitch(Gyro gyroIn, Claw claw, Tilt tilt, Elevator elevator, SwitchState switchStateIn) {
		super(gyroIn, switchStateIn);
		timer = new Timer();
		this.claw = claw;
		this.tilt = tilt;
		this.elevator = elevator;
	}

	@Override
	public void StartAuto() {
		char target = GameLayout.charAt(0);
		if(switchState == SwitchState.SCORE_SWITCH_CENTER) {
			speed = STRAIGHT.SPEED;
			angleStart = STRAIGHT.ANGLE_START;
			angleEnd = STRAIGHT.ANGLE_END;
			timeout = STRAIGHT.TIMEOUT;
		} else if(switchState == SwitchState.SCORE_SWITCH_RIGHT && target == 'R'
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
				claw.setRollerDirection(RollerDirection.FORWARD);
				action = Action.DROP_POWERCUBE;
				timer.reset();
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
			if(switchState == SwitchState.CROSS_LINE_RIGHT) {
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
}
