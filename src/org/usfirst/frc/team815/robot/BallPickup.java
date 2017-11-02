package org.usfirst.frc.team815.robot;

import com.ctre.CANTalon;

public class BallPickup {
	enum BPState {
		Suck(.7),
		Blow(-1),
		Off(0);
		
		private double value;
		
		private BPState(double valueIn) {
			value = valueIn;
		}
		
		public double GetValue() {
			return value;
		}
	}
	
	CANTalon ballPickup;
	BPState state = BPState.Off;
	
	public BallPickup(int motorPort) {
		ballPickup = new CANTalon(motorPort);
	}
	
	public void Toggle(BPState stateIn) {
		if(state != stateIn) {
			state = stateIn;
		} else {
			state = BPState.Off;
		}
		ballPickup.set(state.GetValue());
	}
}
