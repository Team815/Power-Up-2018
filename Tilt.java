package org.usfirst.frc.team815.robot;

import java.util.HashMap;
import java.util.Map;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

public class Tilt {
	private static final int NO_TILT_ROTATION_VALUE = -5000;
	private static final int LEFT_TILT_MOTOR_PORT = 8;
	private static final int RIGHT_TILT_MOTOR_PORT = 9;
	private static final int LEFT_LIMIT_SWITCH_PORT = 8;
	private static final int RIGHT_LIMIT_SWITCH_PORT = 7;
	private static final int LEFT_ENCODER_PORT_A = 4;
	private static final int LEFT_ENCODER_PORT_B = 5;
	private static final int RIGHT_ENCODER_PORT_A = 2;
	private static final int RIGHT_ENCODER_PORT_B = 3;
	private PIDOutputMulti<WPI_VictorSPX> motors;
	private DigitalInput leftLimitSwitch;
	private DigitalInput rightLimitSwitch;
	private Encoder leftEncoder;
	private Encoder rightEncoder;
	public State state;
	private boolean hitLimitSwitch;
	private boolean tiltOn;
	
	public enum State {
		UP,
		DOWN,
		MOVING_UP,
		MOVING_DOWN,
	}
	
	public Tilt () {
		WPI_VictorSPX leftTiltMotor = new WPI_VictorSPX(LEFT_TILT_MOTOR_PORT);
		WPI_VictorSPX rightTiltMotor = new WPI_VictorSPX(RIGHT_TILT_MOTOR_PORT);
		motors = new PIDOutputMulti<WPI_VictorSPX>();
		motors.AddMotor(leftTiltMotor);
		motors.AddMotor(rightTiltMotor);
		leftLimitSwitch = new DigitalInput(LEFT_LIMIT_SWITCH_PORT);
		rightLimitSwitch = new DigitalInput(RIGHT_LIMIT_SWITCH_PORT);
		leftEncoder = new Encoder(LEFT_ENCODER_PORT_A, LEFT_ENCODER_PORT_B);
		rightEncoder = new Encoder(RIGHT_ENCODER_PORT_A, RIGHT_ENCODER_PORT_B);
		leftTiltMotor.setInverted(true);
		state = State.DOWN;
		hitLimitSwitch = leftLimitSwitch.get() || rightLimitSwitch.get();
		tiltOn = hitLimitSwitch;
	}
	
	public void StartTilting() {
		if(tiltOn) {
			switch(state) {
			case DOWN:
			case MOVING_DOWN:
				if(hitLimitSwitch) {
					motors.SetSpeed(-1);
					state = State.MOVING_UP;
					break;
				} // else do what UP and MOVING_UP do (i.e. move down)
			case UP:
			case MOVING_UP:
				motors.SetSpeed(1);
				state = State.MOVING_DOWN;
				break;
			}
		}
	}
	
	public void Update() {
		double minEncoderValue = Math.min(leftEncoder.get(), rightEncoder.get());
		switch(state) {
		case MOVING_UP:
			if(minEncoderValue < NO_TILT_ROTATION_VALUE)
				StopTilting();
			break;
		case MOVING_DOWN:
			if(leftLimitSwitch.get() || rightLimitSwitch.get()) {
				hitLimitSwitch = true;
				StopTilting();
			}
			break;
		default:
			break;
		}
	}
	
	public void StopTilting() {	
		motors.SetSpeed(0);
		switch(state) {
		case UP:
		case MOVING_UP:
			state = State.UP;
			break;
		case DOWN:
		case MOVING_DOWN:
			leftEncoder.reset();
			rightEncoder.reset();
			state = State.DOWN;
		}
	}
	
	public void Set(double valueIn) {
		motors.SetSpeed(valueIn);
	}
	
	public void toggleTiltOnOff() {
		tiltOn = !tiltOn;
	}

	public boolean getTiltOnOff() {
		return tiltOn;
	}
	
	public Map<String, Integer> getEncoderValues() {
		Map<String, Integer> encoderValues = new HashMap<>();
		encoderValues.put("left", leftEncoder.get());
		encoderValues.put("right", rightEncoder.get());
		return encoderValues;
	}
	
	public Map<String, Boolean> getLimitSwitchValues() {
		Map<String, Boolean> limitSwitchValues = new HashMap<>();
		limitSwitchValues.put("left", leftLimitSwitch.get());
		limitSwitchValues.put("right", rightLimitSwitch.get());
		return limitSwitchValues;
	}
}
