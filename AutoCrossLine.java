package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class AutoCrossLine extends Autonomous {
	
	private static final Movement STRAIGHT = new Movement(0.5, 00.0, 00.0, 2.0	);
	
	public AutoCrossLine(Gyro gyroIn, SwitchState switchStateIn, Tilt tilt) {
		super(gyroIn, switchStateIn);
		this.tilt = tilt;
		timer = new Timer();
	}

	@Override
	public void StartAuto() {
		speed = STRAIGHT.SPEED;
		angleStart = STRAIGHT.ANGLE_START;
		angleEnd = STRAIGHT.ANGLE_END;
		timeout = STRAIGHT.TIMEOUT;
		rotation = 0;
		tilt.StartTilting();
		timer.start();
	}

	@Override
	public void Update() {
		tilt.Update();
		if(timer.get() >= timeout) {
			horizontal = 0;
			vertical = 0;
			rotation = 0;
		} else {
			angle = (angleEnd - angleStart) * (timer.get() / timeout) + angleStart;
			horizontal = 1.3 * speed * Math.sin(Math.toRadians(angle));
			vertical = speed * Math.cos(Math.toRadians(angle));
		}
	}
}
