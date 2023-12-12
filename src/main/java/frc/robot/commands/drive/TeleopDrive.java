// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drive;

import frc.robot.subsystems.Drive;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class TeleopDrive extends CommandBase {
	/** Creates a new TeleopDrive. */
	private Drive drive;
	private XboxController controller;

	public TeleopDrive(Drive drive, XboxController controller) {
		addRequirements(drive);
		this.drive = drive;
		this.controller = controller;
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		drive.drive(inputCurve(-controller.getLeftX()),
			inputCurve(-controller.getLeftY()),
			inputCurve(-controller.getRightX()));
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}

	public double inputCurve(double input) {
		return MathUtil.applyDeadband(Math.copySign(Math.pow(input, 2), input), 0.05);
	}
}
