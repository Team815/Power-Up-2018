package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class AutoCrossLine extends Autonomous {
	
	private static final double TIMER_TIMEOUT = 3;
	private static final double ROBOT_SPEED = 0.5;
	private Timer timer;
	
	public AutoCrossLine(Gyro gyroIn) {
		super(gyroIn);
		timer = new Timer();
	}

	@Override
	public void StartAuto() {
		timer.reset();
		timer.start();
	}

	@Override
	public void Update() {
		horizontal = 0;
		vertical = timer.get() > TIMER_TIMEOUT ? 0 : ROBOT_SPEED;
		rotation = 0;
	}

}
