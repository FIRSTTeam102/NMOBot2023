// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.Logger;

public class Drive extends SubsystemBase {
	private final WPI_TalonSRX flMotor = new WPI_TalonSRX(10);
	private final WPI_TalonSRX blMotor = new WPI_TalonSRX(11);
	private final WPI_TalonSRX frMotor = new WPI_TalonSRX(12);
	private final WPI_TalonSRX brMotor = new WPI_TalonSRX(13);

	private final MecanumDrive drive = new MecanumDrive(flMotor, blMotor, frMotor, brMotor);

	public Drive() {
		flMotor.setInverted(false);
		blMotor.setInverted(true);
		frMotor.setInverted(false);
		brMotor.setInverted(true);
	}

	@Override
	public void periodic() {
		updateInputs(inputs);
		Logger.getInstance().processInputs(getName(), inputs);
	}

	public void drive(double xSpeed, double ySpeed, double zRotation) {
		drive.driveCartesian(-xSpeed, ySpeed, zRotation);
	}

	/**
	* inputs
	*/
	@AutoLog
	public static class DriveIOInputs {
		public double flMotorSpeed = 0;
		public double blMotorSpeed = 0;
		public double frMotorSpeed = 0;
		public double brMotorSpeed = 0;
	}

	public DriveIOInputsAutoLogged inputs = new DriveIOInputsAutoLogged();

	private void updateInputs(DriveIOInputs inputs) {
		inputs.flMotorSpeed = flMotor.get();
		inputs.blMotorSpeed = blMotor.get();
		inputs.frMotorSpeed = frMotor.get();
		inputs.brMotorSpeed = brMotor.get();
	}
}
