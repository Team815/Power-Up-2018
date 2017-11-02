package org.usfirst.frc.team815.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Timer;

public class Shooter {
	private CANTalon shooter;
	private CANTalon agitator;
	private double shooterSpeed = 1;
	private double agitatorSpeed = 1;
	private Timer timer = new Timer();
	
	
	public Shooter(int shooterPort, int agitatorPort) {
		shooter = new CANTalon(shooterPort);
		agitator = new CANTalon(agitatorPort);
		
		shooter.setInverted(true);
		agitator.setInverted(true);
	}
	
	public void SimpleUpdateShooter() {
		shooter.set(shooterSpeed);
	}
	
	public void SimpleUpdateAgitator() {
		agitator.set(agitatorSpeed);
	}
	
	public void SimpleStopShooter() {
		shooter.set(0);
	}
	
	public void SimpleStopAgitator() {
		agitator.set(0);
	}
	
	public void startShooter() {
		timer.start();
	}
	
	public void UpdateShooter() {
		shooter.set(shooterSpeed);
		if(timer.get() >= 1) {
			timer.stop();
			agitator.set(agitatorSpeed);
		}
	}
	
	public void stopShooter() {
		shooter.set(0);
		agitator.set(0);
	}
	
	public void SetSpeeds(double shooterSpeedIn, double agitatorSpeedIn) {
		shooterSpeed = shooterSpeedIn;
		agitatorSpeed = agitatorSpeedIn;
	}
}