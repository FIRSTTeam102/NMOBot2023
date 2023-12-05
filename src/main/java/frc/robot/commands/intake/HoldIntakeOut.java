package frc.robot.commands.intake;

import frc.robot.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class HoldIntakeOut extends CommandBase {
	private Intake intake;

	public HoldIntakeOut(Intake intake) {
		addRequirements(intake);
		this.intake = intake;
	}

	@Override
	public void initialize() {
		intake.intakeBall();
		intake.setPiston(true);
	}

	@Override
	public void execute() {}

	@Override
	public void end(boolean interrupted) {
		intake.setPiston(false);
		intake.stopMotor();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
