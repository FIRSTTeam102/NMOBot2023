package frc.robot.subsystems;

import static frc.robot.constants.IntakeConstants.ballSpeed;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.Logger;

public class Intake extends SubsystemBase {
	private final DoubleSolenoid piston = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 1, 2);
	private final CANSparkMax motor = new CANSparkMax(5, MotorType.kBrushless);

	public Intake() {}

	@Override
	public void periodic() {
		updateInputs(inputs);
		Logger.getInstance().processInputs(getName(), inputs);
	}

	public void setPiston(boolean state) {
		if (state) {
			piston.set(DoubleSolenoid.Value.kForward);
		} else {
			piston.set(DoubleSolenoid.Value.kReverse);
		}
	}

	public void setMotorSpeed(double motorSpeed) {
		motor.set(motorSpeed);
	}

	public void intakeBall() {
		this.setMotorSpeed(ballSpeed);
	}

	public void stopMotor() {
		motor.stopMotor();
	}

	/**
	* inputs
	*/
	@AutoLog
	public static class IntakeIOInputs {
		public boolean pistonForwardState = false;
		public boolean pistonReverseState = false;

		public double motorPercentOutput = 0.0;
	}

	public IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();

	private void updateInputs(IntakeIOInputs inputs) {
		inputs.pistonForwardState = piston.get() == DoubleSolenoid.Value.kForward;
		inputs.pistonReverseState = piston.get() == DoubleSolenoid.Value.kReverse;

		inputs.motorPercentOutput = motor.get();
	}
}
