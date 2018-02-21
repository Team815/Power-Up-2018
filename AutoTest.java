package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.interfaces.Gyro;

public class AutoTest extends Autonomous {

	public AutoTest(Gyro gyroIn) {
		super(gyroIn);
	}

	@Override
	public void StartAuto() {
	}

	@Override
	public void Update() {
		horizontal = 0;
		vertical = 0;
		rotation = 0;
	}

}
