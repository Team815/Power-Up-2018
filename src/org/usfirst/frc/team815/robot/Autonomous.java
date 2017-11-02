package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;

public class Autonomous {
	
	public enum State {
		Positioning,
		Aligning,
		Inserting,
		Done;
	}
	
	Gyro gyro;
	Camera camera = new Camera();
	Relay lightRelay;
	private double horizontal = 0;
	private double vertical = 0;
	private double startingAngle = 0;
	private boolean turningLeft;
	private Timer timer = new Timer();
	private State state = State.Positioning;
	
	public Autonomous(Gyro gyroIn, Relay lightRelayIn) {
		gyro = gyroIn;
		lightRelay = lightRelayIn;
	}
	public void StartAuto(State startingState) {
		state = startingState;
		startingAngle = gyro.GetAngle();
		if(startingState == State.Aligning) {
			camera.StartCamera();
    		lightRelay.set(Relay.Value.kOn);
		}
		timer.reset();
		timer.start();
	}
	
	public void Update() {
		if(state == State.Positioning) {
			GetIntoPosition();
		} else if(state == State.Aligning) {
			Align(camera.ReadBuffer());
		} else if(state == State.Inserting) {
			Insert();
		} else if(state == State.Done) {
			Done();
		}
		gyro.Update(false);
	}
	
	public void GetIntoPosition() {
		final double POSITION_SPEED = 1;
		final double TIMER_LIMIT = 0.6;
		double angle = turningLeft ? 60 : -60;
		if(timer.get() > TIMER_LIMIT) {
			timer.stop();
			state = State.Aligning;
			camera.StartCamera();
			lightRelay.set(Relay.Value.kOn);
		} else {
			vertical = POSITION_SPEED;
			horizontal = -Math.signum(angle) * POSITION_SPEED / 2;
			gyro.SetTargetAngle(startingAngle + timer.get() * -angle / TIMER_LIMIT);
		}
	}
	
	public void Align(int angle) {
		final double ALIGN_SPEED = 0.3;
		if(angle == -3) {
			horizontal = 0;
    		vertical = 0;
			state = State.Inserting;
			lightRelay.set(Relay.Value.kOff);
			timer.reset();
			timer.start();
		}
		if(angle == -2) {
    		horizontal = 0;
    		vertical = 0;
    	} else if (angle >= 0) {
    		horizontal = -ALIGN_SPEED * Math.cos(angle * 2 * Math.PI / 360);
    		vertical = ALIGN_SPEED * Math.sin(angle * 2 * Math.PI / 360);
    	}
	}
	
	public void Insert() {
		final double INSERT_SPEED = .5;
		final double INSERT_TIME = .5;
		horizontal = 0;
		vertical =  INSERT_SPEED;
		if(timer.get() >= INSERT_TIME) {
			timer.stop();
			state = State.Done;
		}
	}
	
	public void Done() {
		horizontal = 0;
		vertical =  0;
	}
	
	public State GetState() {
		return state;
	}
	
	public double GetHorizontal() {
		return horizontal;
	}
	
	public double GetVertical() {
		return vertical;
	}
	
	public void SetTurningLeft(boolean turningLeftIn) {
		turningLeft = turningLeftIn;
	}
}
