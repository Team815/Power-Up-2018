package org.usfirst.frc.team815.robot;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Timer;

public class Lift {
	private CANTalon lift1;
	private CANTalon lift2;
	private Timer timer = new Timer();
	private final double MAX_SPEED = 1;
	private final double SPEEDUP_TIME = 3;
	
	public Lift(int motorPort1, int motorPort2) {
		lift1 = new CANTalon(motorPort1);
		lift2 = new CANTalon(motorPort2);
		lift1.setInverted(true);
		lift2.setInverted(true);
	}
	
	public void StartClimb() {
		timer.reset();
		timer.start();
	}
	
	public void StopClimb() {
		timer.stop();
		lift1.set(0);
		lift2.set(0);
	}
	
	public void SetSpeed(double speedIn){
		lift1.set(speedIn);
		lift2.set(speedIn);
	}
	
	public void Climb() {
		if(timer.get() < SPEEDUP_TIME) {
			lift1.set(timer.get() * MAX_SPEED);
			lift2.set(timer.get() * MAX_SPEED);
		} else {
			timer.stop();
			lift1.set(MAX_SPEED);
			lift2.set(MAX_SPEED);
		}
	}
}
