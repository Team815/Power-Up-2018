package org.usfirst.frc.team815.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Joystick;

public class Controller {
	
	public enum ButtonName {
		A(1),
		B(2),
		X(3),
		Y(4),
		LB(5),
		RB(6),
		Select(7),
		Start(8),
		LJ(9),
		RJ(10);
		
		private final int index;
		
		ButtonName(int indexIn) {
			index = indexIn;
		}
		
		public int GetIndex() {
			return index;
		}
	}
	
	public enum AnalogName {
		LeftJoyX(0),
		LeftJoyY(1),
		LeftTrigger(2),
		RightTrigger(3),
		RightJoyX(4),
		RightJoyY(5);
		
		private final int index;
		
		AnalogName(int indexIn) {
			index = indexIn;
		}
		
		public int GetIndex() {
			return index;
		}
	}
	
	private final double analogThreshold = 0.1;
	
	private Joystick stick;
	
	private ArrayList<Button> buttons = new ArrayList<Button>();
	private ArrayList<Analog> analogs = new ArrayList<Analog>();
	private Dpad dpad = new Dpad();
	
	public Controller(int port) {
		
		stick = new Joystick(port);
		
		for(ButtonName i : ButtonName.values()) {
			buttons.add(new Button(i.GetIndex()));
		}
		
		for(AnalogName i : AnalogName.values()) {
			analogs.add(new Analog(i.GetIndex()));
		}
	}
	
	public void Update() {
		
		for(Button i : buttons) {
			i.Update(stick);
		}
		
		for(Analog i : analogs) {
			i.Update(stick, analogThreshold);
		}
		
		dpad.Update(stick);
	}
	
	public boolean IsPressed(ButtonName button) {
		return buttons.get(button.GetIndex()-1).IsPressed();
	}
	
	public boolean WasClicked(ButtonName button) {
		return buttons.get(button.GetIndex()-1).WasClicked();
	}
	
	public boolean WasReleased(ButtonName button) {
		return buttons.get(button.GetIndex()-1).WasReleased();
	}
	
	public boolean IsToggled(ButtonName button) {
		return buttons.get(button.GetIndex()-1).IsToggled();
	}
	
	public double GetValue(AnalogName analog) {
		return analogs.get(analog.GetIndex()).GetValue();
	}
	
	public boolean JustZeroed(AnalogName analog) {
		return analogs.get(analog.GetIndex()).JustZeroed();
	}
	
	public Dpad.Direction GetDpadDirection() {
		return dpad.GetDirection();
	}
	
	public boolean WasDpadDirectionClicked(Dpad.Direction directionIn) {
		return dpad.WasDirectionClicked(directionIn);
	}
}
