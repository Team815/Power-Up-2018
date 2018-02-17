package org.usfirst.frc.team815.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

public class Tilt {
	//	private static final int MAX_INWARD_TILT_ROTATION_VALUE = 0;
	private static final int NO_TILT_ROTATION_VALUE = -5200;
	private WPI_VictorSPX leftTiltMotor;
	private WPI_VictorSPX rightTiltMotor;
	private DigitalInput leftLimitSwitch;
	private DigitalInput rightLimitSwitch;
	private Encoder leftEncoder;
	private Encoder rightEncoder;
	private State state;
	
	public enum State {
		UP,
		DOWN,
		MOVING_UP,
		MOVING_DOWN
	}
	
	public Tilt () {
		leftTiltMotor = new  WPI_VictorSPX(8);
		rightTiltMotor = new WPI_VictorSPX(9);
		leftLimitSwitch = new DigitalInput(0);	// Adjust once input value is known
		rightLimitSwitch = new DigitalInput(0);	// Adjust once input value is known
		leftEncoder = new Encoder(4, 5);
		rightEncoder = new Encoder(2, 3);
		state = State.DOWN;
	}
	
	public void StartTilting() {
		switch(state) {
		case UP:
		case MOVING_UP:
			leftTiltMotor.set(1);
			rightTiltMotor.set(1);
			state = State.MOVING_DOWN;
			break;
		case DOWN:
		case MOVING_DOWN:
			leftTiltMotor.set(-1);
			rightTiltMotor.set(-1);
			state = State.MOVING_UP;
			break;
		}
	}
	
	public void Update() {
		double minValue = Math.min(leftEncoder.get(), rightEncoder.get());
		double maxValue = Math.max(leftEncoder.get(), rightEncoder.get());
		
		switch(state) {
		case MOVING_UP:
			if(minValue < NO_TILT_ROTATION_VALUE)
				StopTilting(true, true);
			break;
		case MOVING_DOWN:
			if(leftLimitSwitch.get()) {
				StopTilting(true, false);
				leftEncoder.reset();
			}
			if(rightLimitSwitch.get()) {
				StopTilting(false, true);
				rightEncoder.reset();
			}
			break;
		default:
			break;
		}
	}
	
	public void StopTilting(boolean stopLeftTiltMotor, boolean stopRightTiltMotor) {		
		if(stopLeftTiltMotor) {
			leftTiltMotor.set(0);
		}
		if(stopRightTiltMotor) {
			rightTiltMotor.set(0);
		}
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
