package frc.robot.commands;

import static frc.robot.constants.AutoConstants.*;

import frc.robot.RobotContainer;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public final class Autos {
	// used by pathplanner
	public static final PIDController ppXController = new PIDController(autoDriveKp, autoDriveKi, autoDriveKd);
	public static final PIDController ppYController = new PIDController(autoDriveKp, autoDriveKi, autoDriveKd);
	public static final PIDController ppRotationController = new PIDController(autoAngleKp, autoAngleKi, autoAngleKd); // rad
	static {
		ppRotationController.enableContinuousInput(-Math.PI, Math.PI);
		ppRotationController.setTolerance(0.01, 0.01);
	}

	public static Command testAuto(RobotContainer robo) {
		return new SequentialCommandGroup(Commands.print("hello from auto"));
	}
}
