package org.usfirst.frc.team815.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

public class Elevator {
	
	public enum State {
		STOPPED,
		RAISING,
		LOWERING
	}
	
	private WPI_VictorSPX elevatorMotor1;
	private WPI_VictorSPX elevatorMotor2;
	private Encoder encoder;
	private Timer timer;
	private final double MAX_SPEED = 1;
	private final double SPEEDUP_TIME = 3;
	private State state;
 	
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
		state = stateIn;
	}
	
	public void Update() {
		double speed = 0;
		switch(state) {
		case STOPPED:
			speed = 0;
			break;
		case RAISING:
			speed = 1;
			break;
		case LOWERING:
			speed = -1;
			break;
		}
		SetElevatorMotor(speed); 
	}
}
