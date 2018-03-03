package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class AutoCrossLine extends Autonomous {
	
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
	
	private Timer timer;
	private double speed;
	private double angleStart;
	private double angleEnd;
	private double angle;
	private double timeout;
	
	public AutoCrossLine(Gyro gyroIn, SwitchState switchStateIn) {
		super(gyroIn, switchStateIn);
		timer = new Timer();
	}

	@Override
	public void StartAuto() {
		char target = GameLayout.charAt(0);
		if(switchState == SwitchState.CROSS_LINE_CENTER) {
			speed = STRAIGHT.SPEED;
			angleStart = STRAIGHT.ANGLE_START;
			angleEnd = STRAIGHT.ANGLE_END;
			timeout = STRAIGHT.TIMEOUT;
		} else if(switchState == SwitchState.CROSS_LINE_RIGHT && target == 'R'
		       || switchState == SwitchState.CROSS_LINE_LEFT  && target == 'L') {
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
		rotation = 0;
		timer.start();
	}

	@Override
	public void Update() {
		if(timer.get() >= timeout) {
			horizontal = 0;
			vertical = 0;
			rotation = 0;
		} else {
			angle = (angleEnd - angleStart) * (timer.get() / timeout) + angleStart;
			if(switchState == SwitchState.CROSS_LINE_RIGHT) {
				angle *= -1;
			}
			horizontal = 1.3 * speed * Math.sin(Math.toRadians(angle));
			vertical = speed * Math.cos(Math.toRadians(angle));
		}
	}
}
