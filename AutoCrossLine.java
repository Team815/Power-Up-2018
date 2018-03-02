package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class AutoCrossLine extends Autonomous {
	
	private static class Movement {
		public final double SPEED;
		public final double ANGLE;
		public final double TIMEOUT;
		
		public Movement(double speedIn, double angleIn, double timeoutIn) {
			SPEED = speedIn;
			ANGLE = angleIn;
			TIMEOUT = timeoutIn;
		}
	}
	
	private static final Movement STRAIGHT = new Movement(0.5, 00.0, 2.0);
	private static final Movement CLOSE    = new Movement(0.5, 00.0, 2.0);
	private static final Movement FAR      = new Movement(0.7, 45.0, 4.0);
	
	private Timer timer;
	private double speed;
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
			angle = STRAIGHT.ANGLE;
			timeout = STRAIGHT.TIMEOUT;
		} else if(switchState == SwitchState.CROSS_LINE_RIGHT && target == 'R'
		       || switchState == SwitchState.CROSS_LINE_LEFT  && target == 'L') {
			speed = CLOSE.SPEED;
			angle = CLOSE.ANGLE;
			timeout = CLOSE.TIMEOUT;
		} else {
			speed = FAR.SPEED;
			angle = FAR.ANGLE;
			timeout = FAR.TIMEOUT;
			if(switchState == SwitchState.CROSS_LINE_RIGHT) {
				angle *= -1;
			}
		}
		horizontal = speed * Math.sin(angle);
		vertical = speed * Math.cos(angle);
		rotation = 0;
		timer.start();
	}

	@Override
	public void Update() {
		if(timer.hasPeriodPassed(timeout)) {
			horizontal = 0;
			vertical = 0;
			rotation = 0;
			timer.stop();
			timer.reset();
		}
	}
}
