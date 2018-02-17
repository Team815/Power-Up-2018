package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class Autonomous {
	
	String GameLayout = DriverStation.getInstance().getGameSpecificMessage();
	
	private Timer timer = new Timer();
	//private Timer boostTimer = new Timer();
	private double horizontal, vertical;
	
	public Autonomous() {
	}
	
	public void StartAuto() {
		horizontal = 0;
		vertical = 0;
		timer.start();
		//boostTimer.start();
	}
	
	public void Update() {
		CrossAutoLine();
	}
	
	// Get across the auto line
	public void CrossAutoLine() {
		if(timer.get() >= 5) {
			vertical = 0;
		} else {
			// Accelerate to target speed
			vertical = 0.2 * Math.min(1, timer.get());
		}
	}
	
	// Score on switch
	public void ScoreOnSwitch() {
		
	}
	
	// Score on scale
	public void ScoreOnScale() {
		
	}
	
	public double GetHorizontal() {
		return horizontal;
	}
	
	public double GetVertical() {
		return vertical;
	}
}
