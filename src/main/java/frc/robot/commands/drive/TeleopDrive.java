package frc.robot.commands.drive;

import frc.robot.constants.Constants.OperatorConstants;
import frc.robot.subsystems.Drive;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import org.littletonrobotics.junction.Logger;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class TeleopDrive extends CommandBase {
	private Drive drive;
	private DoubleSupplier driveSupplier;
	private DoubleSupplier strafeSupplier;
	private DoubleSupplier turnSupplier;
	private BooleanSupplier preciseModeSupplier;

	/**
	 * @param overrideSpeedSupplier forces swerve to run at normal speed when held, instead of slow if scoring mechanism is out
	 */
	public TeleopDrive(DoubleSupplier driveSupplier, DoubleSupplier strafeSupplier, DoubleSupplier turnSupplier,
		BooleanSupplier preciseModeSupplier, Drive drive) {
		addRequirements(drive);
		this.driveSupplier = driveSupplier;
		this.strafeSupplier = strafeSupplier;
		this.turnSupplier = turnSupplier;
		this.preciseModeSupplier = preciseModeSupplier;
		this.drive = drive;
	}

	@Override
	public void initialize() {}

	@Override
	public void execute() {
		var driveMax = 1.0;
		var turnMax = 1.0;

		var translationX = driveMax * modifyAxis(driveSupplier.getAsDouble());
		var translationY = driveMax * modifyAxis(strafeSupplier.getAsDouble());

		if (preciseModeSupplier.getAsBoolean()) {
			driveMax *= 0.3;
			turnMax *= 0.2;
		}

		var rotation = turnMax * modifyAxis(turnSupplier.getAsDouble());

		Logger.getInstance().recordOutput("TeleopDrive/translationX", translationX);
		Logger.getInstance().recordOutput("TeleopDrive/translationY", translationY);
		Logger.getInstance().recordOutput("TeleopDrive/rotation", rotation);

		drive.drive(translationX, translationY, rotation, fieldRelative);
	}

	@Override
	public void end(boolean interrupted) {
		drive.stop();
	}

	// custom input scaling
	private static double modifyAxis(double value) {
		value = MathUtil.applyDeadband(value, OperatorConstants.xboxStickDeadband);
		return Math.copySign(value * value, value);
	}

	public boolean fieldRelative = true;

	public CommandBase toggleFieldRelative() {
		return Commands.runOnce(() -> fieldRelative = !fieldRelative);
	};

	public CommandBase holdToggleFieldRelative() {
		return Commands.startEnd(
			() -> fieldRelative = !fieldRelative,
			() -> fieldRelative = !fieldRelative);
	};

	public class ZeroYaw extends InstantCommand {
		public ZeroYaw() {}

		@Override
		public void initialize() {
			drive.zeroYaw();
		}

		@Override
		public boolean runsWhenDisabled() {
			return true;
		}
	}
}
