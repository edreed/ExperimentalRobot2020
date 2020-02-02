/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.utilities.NRGPreferencesValue;
import frc.robot.utilities.NRGPreferences.DoubleValue;

/**
 * Drives the robot on the current heading in a straight line.
 */
public class DriveStraight extends CommandBase {

    /**
     * An interface for controlling the distance travelled and speed along the X-axis of the robot.
     */
    private interface TranslationController {

        /**
         * Initialize the state of the controller.
         */
        public void initialize();

        /**
         * Returns the current speed at which to drive the robot.
         * 
         * @return The current speed.
         */
        public double getSpeed();

        /**
         * Returns true when the end conditions have been met.
         * 
         * @return If true, the end conditions have been met.
         */
        public boolean isFinished();
    }

    /**
     * A translation controller used to drive the robot at a constant speed.
     */
    private static class ConstantSpeedTranslation implements TranslationController {

        protected double speed;

        /**
         * Contructs an instance of this class.
         * 
         * @param speed The speed at which to drive the robot.
         */
        ConstantSpeedTranslation(double speed) {
            this.speed = speed;
        }

        @Override
        public void initialize() {

        }

        @Override
        public double getSpeed() {
            return speed;
        }

        @Override
        public boolean isFinished() {
            return false;
        }

    }

    /**
     * A translation controller that drives the robot using the default speed stored in the preferences.
     */
    private static class PreferencesSpeedTranslation extends ConstantSpeedTranslation {

        PreferencesSpeedTranslation() {
            super(0.0);
        }

        @Override
        public void initialize() {
            speed = DRIVE_STRAIGHT_DEFAULT_SPEED.getValue();
        }
    }

    /**
     * A translation controller that drives the robot a specified distance.
     */
    private class DistanceTranslation implements TranslationController {

        protected DriveSubsystem drive;
        protected ConstantSpeedTranslation maxSpeed;
        protected double distance;
        protected Translation2d initialPosition;

        DistanceTranslation(DriveSubsystem drive, ConstantSpeedTranslation maxSpeed, double distance) {
            this.drive = drive;
            this.maxSpeed = maxSpeed;
        }

        @Override
        public void initialize() {
            initialPosition = drive.getPosition().getTranslation();
        }

        @Override
        public double getSpeed() {
            return maxSpeed.getSpeed();
        }

        @Override
        public boolean isFinished() {
            return drive.getPosition().getTranslation().getDistance(initialPosition) >= distance;
        }
    }

    @NRGPreferencesValue
    public static DoubleValue DRIVE_STRAIGHT_DEFAULT_SPEED = new DoubleValue("DriveStraight/DefaultSpeed", 1.0);
    @NRGPreferencesValue
    public static DoubleValue DRIVE_STRAIGHT_P = new DoubleValue("DriveStraight/P", 0.081);
    @NRGPreferencesValue
    public static DoubleValue DRIVE_STRAIGHT_I = new DoubleValue("DriveStraight/I", 0.00016);
    @NRGPreferencesValue
    public static DoubleValue DRIVE_STRAIGHT_D = new DoubleValue("DriveStraight/D", 0.0072);

    private static final ConstantSpeedTranslation DEFAULT_SPEED_CONTROLLER = new PreferencesSpeedTranslation();

    private final DriveSubsystem drive;
    private PIDController pid;
    private TranslationController translationController = DEFAULT_SPEED_CONTROLLER;

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
        this.translationController = new ConstantSpeedTranslation(speed);

        return this;
    }

    /**
     * Set the distance to drive the robot.
     * 
     * @param distance The distance to drive, in meters.
     * 
     * @return This object.
     */
    public DriveStraight forDistance(double distance) {
        this.translationController = new DistanceTranslation(this.drive,
                (ConstantSpeedTranslation) this.translationController, distance);
        
        return this;
    }

    /**
     * Called when command is schedule to set up the initial conditions.
     */
    @Override
    public void initialize() {
        this.pid = new PIDController(DRIVE_STRAIGHT_P.getValue(), DRIVE_STRAIGHT_I.getValue(),
                DRIVE_STRAIGHT_D.getValue());
        this.pid.setSetpoint(this.drive.getHeading());
    }

    /**
     * Called on each iteration of the main robot thread to drive the robot in a
     * straight line.
     */
    @Override
    public void execute() {
        double rotation = this.pid.calculate(this.drive.getHeading());

        this.drive.arcadeDrive(this.translationController.getSpeed(), rotation, false);
    }

    /**
     * Returns true when the end conditions have been met.
     */
    @Override
    public boolean isFinished() {
        return this.translationController.isFinished();
    }
}
