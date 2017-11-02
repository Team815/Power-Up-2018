package org.usfirst.frc.team815.robot;

import java.util.HashMap;
import java.util.Map;
import edu.wpi.first.wpilibj.Joystick;

public class Dpad {
	
	public enum Direction {
		None(-1),
		Up(0),
		UpRight(45),
		Right(90),
		DownRight(135),
		Down(180),
		DownLeft(225),
		Left(270),
		UpLeft(315);
		
		private int angle;
		private static Map<Integer, Direction> map = new HashMap<Integer, Direction>();
		
		private Direction(int angleIn) {
			angle = angleIn;
		}
		
		static {
			for(Direction direction : Direction.values()) {
				map.put(direction.angle, direction);
			}
		}
		
		public static Direction valueOf(int angleIn) {
			return map.get(angleIn);
		}
	}
	
	Direction direction;
	Direction previousDirection;
	
	public Direction GetDirection() {
		return direction;
	}
	
	public boolean WasDirectionClicked(Direction directionIn) {
		return directionIn == direction && directionIn != previousDirection;
	}
	
	public void Update(Joystick stick) {
		previousDirection = direction;
		direction = Direction.valueOf(stick.getPOV());
	}
}
