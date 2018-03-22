package org.usfirst.frc.team815.robot;

public class Log {
	//	Components
	private Tilt tilt;
	private Drive drive;
	private Elevator elevator;
	
	// Disable/Enable Logging
	private final boolean tiltModeInfo = false;
	private final boolean speedControlInfo = false;
	private final boolean elevatorEncoderInfo = false;
	private final boolean tiltEncoderInfo = false;
	private final boolean tiltLimitSwitchInfo = false;

	public Log(Tilt tilt, Drive drive, Elevator elevator) {
		this.tilt = tilt;
		this.drive = drive;
		this.elevator = elevator;
	}
	
	public void print() {
		System.out.println("**************************************************");
		
		if(tiltModeInfo) {
			if(tilt.getTiltOnOff())
				System.out.println("Tilt Enabled");
			else System.out.println("Tilt Disabled");
		}
		
		if(speedControlInfo) {
			System.out.println("Current Max Speed: " + drive.getSpeedMultiplier()*100 + "%");
		}
		
		if(elevatorEncoderInfo) {
			System.out.println("Current elvator encoder value: " + elevator.getEncoderValue());
		}
		
		if(tiltEncoderInfo) {
			System.out.println("Current tilt encoder values: Left=" + tilt.getEncoderValues().get("left") + " Right=" + tilt.getEncoderValues().get("right"));
		}
		
		if(tiltLimitSwitchInfo) {
			System.out.println("Current tilt limit switch values: Left=" + tilt.getLimitSwitchValues().get("left") + " Right=" + tilt.getLimitSwitchValues().get("right"));
		}
		
		System.out.println("**************************************************");
	}
	
}
