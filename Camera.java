 package org.usfirst.frc.team815.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Camera {
	NetworkTableInstance inst = NetworkTableInstance.getDefault();
	NetworkTable table = inst.getTable("datatable");
	NetworkTableEntry xEntry = table.getEntry("x");
	NetworkTableEntry yEntry = table.getEntry("y");
	
	
	public Camera() {
		inst.startClientTeam(815);
		inst.startDSClient();
	}
}
