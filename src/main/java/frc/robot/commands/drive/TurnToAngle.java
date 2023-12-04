package frc.robot.commands.drive;

import frc.robot.subsystems.Drive;

import frc.robot.commands.Autos;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.PIDCommand;

public class TurnToAngle extends PIDCommand {
	/** turns to a field angle, 0 is away from driver station */
	public TurnToAngle(double angle_rad, Drive drive) {
		super(
			Autos.ppRotationController,
			() -> MathUtil.angleModulus(drive.inputs.rawYaw_rad),
			() -> MathUtil.angleModulus(angle_rad),
			(double output) -> drive.drive(0, 0, output, false),
			drive);
		/** controller already configured in {@link frc.robot.Autos} */
	}

	@Override
	public boolean isFinished() {
		return m_controller.atSetpoint();
	}
}
