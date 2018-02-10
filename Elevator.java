package org.usfirst.frc.team815.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

public class Elevator {
	
	public enum State {
		STOPPED,
		RAISING,
		LOWERING,
		AUTO
	}
	
	public enum PresetTarget {
		NONE(-1),
		SCALE(1000),
		SWITCH(500),
		BOTTOM(0);
		
		int encoderTarget;
		
		private PresetTarget(int encoderTargetIn) {
			encoderTarget = encoderTargetIn;
		}
		
		public int getEncoderTarget() {
			return encoderTarget;
		}
	}
	
	public class Autovator {
		public PresetTarget target = PresetTarget.NONE;
		public State initialState = State.STOPPED;
		
		
		public boolean PassedTarget() {
			switch(initialState)
			{
			case RAISING:
				return target.getEncoderTarget() < encoder.get();
			case LOWERING:
				return target.getEncoderTarget() > encoder.get();
			default:
				return true;
			}
		}
	}
	
	private WPI_VictorSPX elevatorMotor1;
	private WPI_VictorSPX elevatorMotor2;
	private Encoder encoder;
	private Timer timer;
	private final double MAX_SPEED = 1;
	private final double SPEEDUP_TIME = 3;
	private State state = State.STOPPED;
	private Autovator autovator = new Autovator();
 	
	public Elevator(int motorPort1, int motorPort2) {
		elevatorMotor1 = new  WPI_VictorSPX(motorPort1);
		elevatorMotor2 = new WPI_VictorSPX(motorPort2);
		encoder = new Encoder(0, 1);
	
	}
	
	public void SetElevatorMotor(double speed) {
		elevatorMotor1.set(speed);
		elevatorMotor2.set(speed);
	}
	
	public void SetState(State stateIn) {
		if(stateIn != State.STOPPED || state != State.AUTO) {
			state = stateIn;
		}
	}
	
	public void SetPresetTarget(PresetTarget targetIn) {
		state = State.AUTO;
		autovator.target = targetIn;
		if (targetIn.getEncoderTarget() > encoder.get()) {
			autovator.initialState = State.RAISING;
		} else if  (targetIn.getEncoderTarget() < encoder.get()) {
			autovator.initialState = State.LOWERING;
		} else {
			autovator.initialState = State.STOPPED;
		}
	}
	
	public void Update() {
		double speed = 0;
		System.out.println(state.toString());
		switch(state) {
		case STOPPED:
			speed = 0;
			break;
		case RAISING:
			speed = MAX_SPEED;
			break;
		case LOWERING:
			speed = -MAX_SPEED;
			break;
		case AUTO:
			if(autovator.PassedTarget()) {
				state = State.STOPPED;
			} else {
				speed = Math.signum(autovator.target.getEncoderTarget() - encoder.get()) * MAX_SPEED;
			}
		}
		
		SetElevatorMotor(speed); 
	}
}
