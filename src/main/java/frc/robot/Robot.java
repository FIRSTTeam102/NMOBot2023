package frc.robot;

import static frc.robot.constants.Constants.robotMode;

import frc.robot.constants.BuildConstants;

import edu.wpi.first.hal.AllianceStationID;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import com.revrobotics.REVPhysicsSim;

import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.inputs.LoggedPowerDistribution;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends LoggedRobot {
	private Command autonomousCommand;
	private RobotContainer robotContainer;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		if (Robot.isSimulation())
			DriverStationSim.setAllianceStationId(AllianceStationID.Blue1);

		var logger = Logger.getInstance();
		logger.recordMetadata("RuntimeType", getRuntimeType().toString());
		logger.recordMetadata("ProjectName", BuildConstants.MAVEN_NAME);
		logger.recordMetadata("BuildDate", BuildConstants.BUILD_DATE);
		logger.recordMetadata("GitSHA", BuildConstants.GIT_SHA);
		logger.recordMetadata("GitBranch", BuildConstants.GIT_BRANCH);
		logger.recordMetadata("GitDate", BuildConstants.GIT_DATE);
		logger.recordMetadata("GitDirty", switch (BuildConstants.DIRTY) {
			case 0 -> "Clean";
			case 1 -> "Dirty";
			default -> "Unknown";
		});

		switch (robotMode) {
			case Replay -> {
				setUseTiming(false); // run as fast as possible
				String logPath = LogFileUtil.findReplayLog();
				Logger.getInstance().setReplaySource(new WPILOGReader(logPath));
				Logger.getInstance().addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim")));
			}
			case Active -> {
				logger.addDataReceiver(new WPILOGWriter("/media/sda1/logs/"));
				logger.addDataReceiver(new NT4Publisher());
				if (isReal()) {
					LoggedPowerDistribution.getInstance(); // enable power distribution logging
				} else {
					// sim
				}
			}
		}

		// disable LiveWindow telemetry in favor of AdvantageKit to reduce processing each tick
		LiveWindow.disableAllTelemetry();

		logger.start();

		/*
		 * Instantiate our RobotContainer. This will perform all our button bindings,
		 * and put our autonomous chooser on the dashboard.
		 */
		robotContainer = RobotContainer.getInstance();
	}

	/**
	 * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
	 * that you want ran during disabled, autonomous, teleoperated and test.
	 * <p>
	 * This runs after the mode specific periodic functions, but before LiveWindow
	 * and SmartDashboard integrated updating.
	 */
	@Override
	public void robotPeriodic() {
		/*
		 * Runs the Scheduler. This is responsible for polling buttons, adding newly-scheduled
		 * commands, running already-scheduled commands, removing finished or interrupted commands,
		 * and running subsystem periodic() methods. This must be called from the robot's periodic
		 * block in order for anything in the Command-based framework to work.
		 */
		CommandScheduler.getInstance().run();
	}

	/** This function is called once each time the robot enters Disabled mode. */
	@Override
	public void disabledInit() {}

	@Override
	public void disabledPeriodic() {}

	/**
	 * This autonomous runs the autonomous command selected by your
	 * {@link RobotContainer} class.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = robotContainer.getAutonomousCommand();
		if (autonomousCommand != null)
			try {
				autonomousCommand.schedule();
			} catch (Exception e) {
				DriverStation.reportError("failed to schedule auto", e.getStackTrace());
			}
	}

	/** This function is called periodically during autonomous. */
	@Override
	public void autonomousPeriodic() {}

	@Override
	public void teleopInit() {
		/*
		 * This makes sure that the autonomous stops running when teleop starts running. If you want the
		 * autonomous to continue until interrupted by another command, remove this line or comment it
		 * out.
		 */
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/** This function is called periodically during operator control. */
	@Override
	public void teleopPeriodic() {}

	@Override
	public void testInit() {
		// Cancels all running commands at the start of test mode.
		CommandScheduler.getInstance().cancelAll();
	}

	/** This function is called periodically during test mode. */
	@Override
	public void testPeriodic() {}

	/** This function is called once when the robot is first started up. */
	@Override
	public void simulationInit() {}

	/** This function is called periodically whilst in simulation. */
	@Override
	public void simulationPeriodic() {
		REVPhysicsSim.getInstance().run();
	}
}
