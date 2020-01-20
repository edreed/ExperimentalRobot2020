/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.utilities.NRGPreferencesValue;
import frc.robot.utilities.NRGPreferences.DoubleValue;

/**
 * Drives the robot on the current heading in a straight line.
 */
public class DriveStraight extends CommandBase {

    @NRGPreferencesValue
    public static DoubleValue DRIVE_STRAIGHT_DEFAULT_SPEED = new DoubleValue("DriveStraight/DefaultSpeed", 1.0);
    @NRGPreferencesValue
    public static DoubleValue DRIVE_STRAIGHT_P = new DoubleValue("DriveStraight/P", 1.0);
    @NRGPreferencesValue
    public static DoubleValue DRIVE_STRAIGHT_I = new DoubleValue("DriveStraight/I", 0.0);
    @NRGPreferencesValue
    public static DoubleValue DRIVE_STRAIGHT_D = new DoubleValue("DriveStraight/D", 0.0);

    private final DriveSubsystem drive;
    private DoubleSupplier speed;
    private PIDController pid;

    /**
     * Constructs an instance of this class.
     * 
     * @param drive The drive subsystem.
     */
    public DriveStraight(DriveSubsystem drive) {
        this.drive = drive;
        addRequirements(drive);
    }

    /**
     * Sets the speed at which to drive the robot.
     * 
     * @param speed The speed.
     * 
     * @return This object.
     */
    public DriveStraight withSpeed(double speed) {
        this.speed = () -> speed;

        return this;
    }

    /**
     * Called when command is schedule to set up the initial conditions.
     */
    @Override
    public void initialize() {
        if (this.speed == null) {
            this.speed = () -> DRIVE_STRAIGHT_DEFAULT_SPEED.getValue();
        }

        this.pid = new PIDController(DRIVE_STRAIGHT_P.getValue(), DRIVE_STRAIGHT_I.getValue(), DRIVE_STRAIGHT_D.getValue());
        this.pid.setSetpoint(0.0);
    }

    /**
     * Called on each iteration of the main robot thread to drive the robot in a straight line.
     */
    @Override
    public void execute() {
        double rotation = this.pid.calculate(this.drive.getTurnRate());

        this.drive.arcadeDrive(this.speed.getAsDouble(), rotation, false);
    }

    /**
     * Returns true when the end conditions have been met.
     */
    @Override
    public boolean isFinished() {
        return false;
    }
}
