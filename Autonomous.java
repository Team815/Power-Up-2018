package org.usfirst.frc.team815.robot;

import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team815.robot.Autonomous.Action;
import org.usfirst.frc.team815.robot.Dpad.Direction;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public abstract class Autonomous {
	
	public enum SwitchState {
		CROSS_LINE_RIGHT(1),
		CROSS_LINE_CENTER(2),
		CROSS_LINE_LEFT(4),
		SCORE_SWITCH_RIGHT(9),
		SCORE_SWITCH_CENTER(10),
		SCORE_SWITCH_LEFT(12),
		SCORE_SCALE_RIGHT(6),
		SCORE_SCALE_CENTER(5),
		SCORE_SCALE_LEFT(3);
		
		static Map<Integer, SwitchState> map = new HashMap<>();
		int switchboard_value;
		
		private SwitchState(int switchboard_value_in) {
			switchboard_value = switchboard_value_in;
		}
		
		static {
			for(SwitchState switchState : SwitchState.values()) {
				map.put(switchState.switchboard_value, switchState);
			}
		}
		
		static public SwitchState get(int switchboard_value) {
			return map.get(switchboard_value);
		}
	}
	
	String GameLayout = DriverStation.getInstance().getGameSpecificMessage();
	
	protected Timer routineTimer;
	protected Timer driveTimer;
	protected Claw claw;
	protected Tilt tilt;
	protected Elevator elevator;
	
	protected double horizontal, vertical, rotation;
	Gyro gyro;
	
	protected enum Action {
		TILT_FORWARD,
		GRAB_POWERCUBE,
		SECURE_POWERCUBE,
		APPROACH_SWITCH,
		APPROACH_SCALE,
		RAISE_ELEVATOR,
		DROP_POWERCUBE,
		STOP;
	}
	
	Action action;
	
	public Autonomous(Gyro gyroIn) {
		gyro = gyroIn;
		horizontal = vertical = rotation = 0;
	}
	
	public abstract void StartAuto();
		
	public abstract void Update();
	
	protected Action setAction() {
		return null;
	}
	
	public double GetHorizontal() {
		return horizontal;
	}
	
	public double GetVertical() {
		return vertical;
	}
	
	public double GetRotation() {
		return rotation;
	}
}
