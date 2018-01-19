 package org.usfirst.frc.team815.robot;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;

public class Camera {
	private SerialPort serialPort;
	private String buffer = "";
	Timer timer = new Timer();
	
	public Camera() {
		serialPort = new SerialPort(9600, SerialPort.Port.kMXP);
		buffer = "";
	}
	
	public void StartCamera() {
		serialPort.reset();
		buffer = "";
		timer.reset();
		timer.start();
	}
	
	public int ReadBuffer() {
		buffer += serialPort.readString();
		if(buffer.length() > 0) {
			System.out.println(buffer);
			if(buffer.charAt(0) != '<') {
				if(buffer.contains("<")) {
					buffer = buffer.substring(buffer.indexOf('<'));
				} else {
					buffer = "";
				} 
			} else if(buffer.contains(">")) {
				timer.reset();
				String value = buffer.substring(1, buffer.indexOf('>'));
				System.out.println(value);
				buffer = buffer.substring(buffer.indexOf('>')+1);
				if(value.equals("a")) {
					return -3;
				} else {
					return Integer.parseInt(value);
				}
			}
		}
		if(timer.get() < .5) {
			return -1;
		} else {
			return -2;
		}
	}
}
