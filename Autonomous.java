package org.usfirst.frc.team815.robot;

import java.util.HashMap;
import java.util.Map;

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
	
	protected enum Action {
		TILT_FORWARD,
		CALIBRATE_ELEVATOR,
		GRAB_POWERCUBE,
		SECURE_POWERCUBE,
		APPROACH_SWITCH,
		APPROACH_SCALE,
		RAISE_ELEVATOR,
		DROP_POWERCUBE,
		STOP;
	}	
	protected static class Movement {
		public final double SPEED;
		public final double ANGLE_START;
		public final double ANGLE_END;
		public final double TIMEOUT;
		
		public Movement(double speedIn, double angleStartIn, double angleEndIn, double timeoutIn) {
			SPEED = speedIn;
			ANGLE_START = angleStartIn;
			ANGLE_END = angleEndIn;
			TIMEOUT = timeoutIn;
		}
	}
	
	String GameLayout = DriverStation.getInstance().getGameSpecificMessage();
	
	protected Timer timer;
	protected Claw claw;
	protected Tilt tilt;
	protected Elevator elevator;
	
	protected double horizontal, vertical, rotation;
	Gyro gyro;
	
	protected double speed;
	protected double angleStart;
	protected double angleEnd;
	protected double angle;
	protected double timeout;
	
	
	protected Action action;
	protected SwitchState switchState;
	
	public Autonomous(Gyro gyroIn, SwitchState switchStateIn) {
		gyro = gyroIn;
		switchState = switchStateIn;
		horizontal = vertical = rotation = 0;
	}
	
	public abstract void StartAuto();
		
	public abstract void Update();
	
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
