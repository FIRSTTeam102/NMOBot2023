package frc.robot.subsystems;

import static frc.robot.constants.DriveConstants.*;

import frc.robot.Robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.Logger;

import com.kauailabs.navx.frc.AHRS;

public class Drive extends SubsystemBase {
	private final WPI_TalonSRX flMotor = new WPI_TalonSRX(flMotorId);
	private final WPI_TalonSRX blMotor = new WPI_TalonSRX(blMotorId);
	private final WPI_TalonSRX frMotor = new WPI_TalonSRX(frMotorId);
	private final WPI_TalonSRX brMotor = new WPI_TalonSRX(brMotorId);

	private final MecanumDrive mecanum = new MecanumDrive(flMotor, blMotor, frMotor, brMotor);

	private final AHRS gyro = new AHRS(SerialPort.Port.kMXP);

	public Drive() {
		flMotor.setInverted(false);
		frMotor.setInverted(false);
		blMotor.setInverted(true);
		brMotor.setInverted(true);
	}

	@Override
	public void periodic() {
		updateInputs(inputs);
		Logger.getInstance().processInputs(getName(), inputs);
	}

	public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
		if (fieldRelative) {
			mecanum.driveCartesian(xSpeed, ySpeed, rot, Rotation2d.fromRadians(inputs.trackedYaw_rad));
		} else {
			mecanum.driveCartesian(xSpeed, ySpeed, rot);
		}
	}

	public void stop() {
		mecanum.stopMotor();
	}

	public void zeroYaw() {
		gyro.zeroYaw();
	}

	/**
	* inputs
	*/
	@AutoLog
	public static class DriveIOInputs {
		public double flPercentOutput = 0.0;
		public double flCurrent_A = 0.0;

		public double blPercentOutput = 0.0;
		public double blCurrent_A = 0.0;

		public double frPercentOutput = 0.0;
		public double frCurrent_A = 0.0;

		public double brPercentOutput = 0.0;
		public double brCurrent_A = 0.0;

		public double rawYaw_rad = 0.0;
		public double trackedYaw_rad = 0.0;
	}

	public DriveIOInputsAutoLogged inputs = new DriveIOInputsAutoLogged();

	private void updateInputs(DriveIOInputs inputs) {
		inputs.flPercentOutput = Robot.isSimulation() ? flMotor.get() : flMotor.getMotorOutputPercent();
		inputs.flCurrent_A = flMotor.getStatorCurrent();

		inputs.blPercentOutput = Robot.isSimulation() ? blMotor.get() : blMotor.getMotorOutputPercent();
		inputs.blCurrent_A = blMotor.getStatorCurrent();

		inputs.frPercentOutput = Robot.isSimulation() ? frMotor.get() : frMotor.getMotorOutputPercent();
		inputs.frCurrent_A = frMotor.getStatorCurrent();

		inputs.brPercentOutput = Robot.isSimulation() ? brMotor.get() : brMotor.getMotorOutputPercent();
		inputs.brCurrent_A = brMotor.getStatorCurrent();

		inputs.rawYaw_rad = Math.toRadians(gyro.getYaw());
		inputs.trackedYaw_rad = Math.toRadians(gyro.getAngle());
	}
}
