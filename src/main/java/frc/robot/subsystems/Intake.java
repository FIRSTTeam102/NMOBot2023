package frc.robot.subsystems;

import static frc.robot.constants.IntakeConstants.*;

import frc.robot.Robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;

import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.Logger;

public class Intake extends SubsystemBase {
	private DoubleSolenoid piston = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, forwardPisonId, reversePistonId);
	private CANSparkMax motor = new CANSparkMax(motorId, CANSparkMax.MotorType.kBrushless);

	public Intake() {
		motor.setInverted(false);
		motor.enableVoltageCompensation(12);

		// motor.setSmartCurrentLimit();
		// motor.setSecondaryCurrentLimit();

		motor.setIdleMode(CANSparkMax.IdleMode.kBrake);

		motor.burnFlash();

		// sim only works with velocity control
		// if (Robot.isSimulation())
		// REVPhysicsSim.getInstance().addSparkMax(motor, DCMotor.getNeo550(1));
	}

	public void move(double speed) {
		motor.set(speed);
	}

	public void stop() {
		motor.set(0);
	}

	public void setIntake(boolean out) {
		piston.set(out ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
	}

	@Override
	public void periodic() {
		updateInputs(inputs);
		Logger.getInstance().processInputs(getName(), inputs);
	}

	/**
	 * inputs
	 */
	@AutoLog
	public static class IntakeIOInputs {
		public boolean forwardPistonState = false;
		public boolean reversePistonState = false;

		public double motorPercentOutput = 0.0;
		public double motorCurrent_A = 0.0;
		public double motorTemperature_C = 0.0;
	}

	public IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();

	private void updateInputs(IntakeIOInputs inputs) {
		inputs.forwardPistonState = piston.get() == DoubleSolenoid.Value.kForward;
		inputs.reversePistonState = piston.get() == DoubleSolenoid.Value.kReverse;

		inputs.motorPercentOutput = Robot.isReal() ? motor.getAppliedOutput() : motor.get();
		inputs.motorCurrent_A = motor.getOutputCurrent();
		inputs.motorTemperature_C = motor.getMotorTemperature();
	}
}
