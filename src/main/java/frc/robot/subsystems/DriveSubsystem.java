/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/** A class implementing the robot drive subsystem. */
public class DriveSubsystem extends SubsystemBase {

    private SpeedController leftMotor = new Victor(0);
    private SpeedController rightMotor = new Victor(1);

    private DifferentialDrive driveBase = new DifferentialDrive(leftMotor, rightMotor);

    private AHRS navx = new AHRS();

    private Encoder leftEncoder = new Encoder(0, 1);
    private Encoder rightEncoder = new Encoder(2, 3);

    private DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(new Rotation2d());
    
    /**
     * Constructs an instance of this class.
     */
    public DriveSubsystem() {

    }

    /** Called to periodically perform tasks. It is called once per scheduler run. */
    @Override
    public void periodic() {
        Rotation2d heading = Rotation2d.fromDegrees(getHeading());

        odometry.update(heading, leftEncoder.getDistance(), rightEncoder.getDistance());
    }

    /**
     * Returns the total accumulated yaw (Z-axis) angle of the gyro.
     * 
     * @return The robot heading in degrees.
     */
    public double getHeading() {
        return navx.getAngle();
    }

    /**
    * Returns the position of the robot on the field.
    *
    * @return The pose of the robot (x and y are in meters).
    */
    public Pose2d getPosition() {
        return odometry.getPoseMeters();
    }

    /**
     * Returns the rate of change of yaw (Z-axis) of the gyro.
     * 
     * @return The rate of change of yaw in degress per second.
     */
    public double getTurnRate() {
        return navx.getRate();
    }

    /**
     * Drives the robot using tank-style control.
     * @param leftSpeed The left-side speed.
     * @param rightSpeed The right-side speed.
     * @param squareInputs If true, input sensitivity is decreased at lower speeds.
     */
    public void tankDrive(double leftSpeed, double rightSpeed, boolean squareInputs) {
        driveBase.tankDrive(leftSpeed, rightSpeed, squareInputs);
    }

    /**
     * Drives the robot using arcade-style control.
     * @param xSpeed The speed along the x-axis of the robot.
     * @param zRotation The rotational speed.
     * @param squareInputs If true, input sensitivity is decreased at lower speeds.
     */
    public void arcadeDrive(double xSpeed, double zRotation, boolean squareInputs) {
        driveBase.arcadeDrive(xSpeed, zRotation, squareInputs);
    }
}
