package org.usfirst.frc.team815.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Encoder;

public class Tilt {
	private static final int MAX_INWARD_TILT_ROTATION_VALUE = 0;
	private static final int NO_TILT_ROTATION_VALUE = -5200;
	private WPI_VictorSPX tiltMotor;
	private Encoder leftEncoder;
	private Encoder rightEncoder;
	private State state;
	
	public enum State {
		UP,
		DOWN,
		MOVING_UP,
		MOVING_DOWN
	}
	
	public Tilt(int motorPort) {
		tiltMotor = new  WPI_VictorSPX(motorPort);
		leftEncoder = new Encoder(4, 5);
		rightEncoder = new Encoder(2,3);
		state = State.DOWN;
	}
	
	public void startTilting() {
		switch(state) {
		case UP:
		case MOVING_UP:
			tiltMotor.set(1);
			state = State.MOVING_DOWN;
			break;
		case DOWN:
		case MOVING_DOWN:
			tiltMotor.set(-1);
			state = State.MOVING_UP;
			break;
		}
	}
	
	public void Update() {
		double minValue = Math.min(leftEncoder.get(), rightEncoder.get());
		double maxValue = Math.max(leftEncoder.get(), rightEncoder.get());
		System.out.println(state.toString() + " " + minValue + " " + maxValue);
		switch(state) {
		case MOVING_UP:
			if(minValue < NO_TILT_ROTATION_VALUE)
				stopTilting();
			break;
		case MOVING_DOWN:
			if(maxValue > MAX_INWARD_TILT_ROTATION_VALUE)
				stopTilting();
			break;
		default:
			break;
		}
	}
	
	public void stopTilting() {		
		tiltMotor.set(0);
		switch(state) {
		case UP:
		case MOVING_UP:
			state = State.UP;
			break;
		case DOWN:
		case MOVING_DOWN:
			state = State.DOWN;
		}
	}
}
