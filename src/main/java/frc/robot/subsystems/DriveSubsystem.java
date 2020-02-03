/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.DriveStraight;

/** A class implementing the robot drive subsystem. */
public class DriveSubsystem extends SubsystemBase {

    private SpeedController leftMotor = new SpeedControllerGroup(new WPI_VictorSPX(1),
                                                                 new WPI_VictorSPX(2),
                                                                 new WPI_VictorSPX(3));
    private SpeedController rightMotor = new SpeedControllerGroup(new WPI_VictorSPX(4),
                                                                  new WPI_VictorSPX(5),
                                                                  new WPI_VictorSPX(6));

    private DifferentialDrive driveBase = new DifferentialDrive(leftMotor, rightMotor);

    private AHRS navx = new AHRS();

    private Encoder leftEncoder = new Encoder(2, 3);
    private Encoder rightEncoder = new Encoder(0, 1);

    private DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(new Rotation2d());

    /**
     * Constructs an instance of this class.
     */
    public DriveSubsystem() {
        leftEncoder.setDistancePerPulse(Constants.kEncoderDistancePerPulse);
        leftEncoder.setReverseDirection(false);
        rightEncoder.setDistancePerPulse(Constants.kEncoderDistancePerPulse);
        rightEncoder.setReverseDirection(true);
    }

    /**
     * Add this subsystem's tab to the Shuffleboard.
     */
    public void initShuffleboard() {
        // Create drive subsystem's tab.
        ShuffleboardTab driveTab = Shuffleboard.getTab("DriveSubsystem");

        // Add the DifferentialDrive object and encoders to a list layout in the tab.
        ShuffleboardLayout driveBaseLayout = driveTab.getLayout("Base", BuiltInLayouts.kList).
            withPosition(0, 0).
            withSize(4, 5);

        driveBaseLayout.add("Differential Drive", driveBase).withWidget(BuiltInWidgets.kDifferentialDrive);
        driveBaseLayout.add("Left Encoder", leftEncoder).withWidget(BuiltInWidgets.kEncoder);
        driveBaseLayout.add("Right Encoder", rightEncoder).withWidget(BuiltInWidgets.kEncoder);

        // Add the odometry to a layout in the tab.
        ShuffleboardLayout positionLayout = driveTab.getLayout("Position", BuiltInLayouts.kList).
            withPosition(4, 0).
            withSize(2, 2);

        positionLayout.addNumber("X", () -> getPosition().getTranslation().getX());
        positionLayout.addNumber("Y", () -> getPosition().getTranslation().getY());
        positionLayout.addNumber("Heading", () -> getHeading());

        // Add test buttons to a layout in the tab
        ShuffleboardLayout testLayout = driveTab.getLayout("Test", BuiltInLayouts.kList).
            withPosition(6, 0).
            withSize(3, 2);

        testLayout.add("Drive Straight Slow 3m", new DriveStraight(this).withSpeed(0.5).forMeters(3.0)).
            withWidget(BuiltInWidgets.kCommand);
        testLayout.add("Drive Straight Fast 3m", new DriveStraight(this).withSpeed(1.0).forMeters(3.0)).
            withWidget(BuiltInWidgets.kCommand);
    }

    /**
     * Called to periodically perform tasks. It is called once per scheduler run.
     */
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
     * 
     * @param leftSpeed    The left-side speed.
     * @param rightSpeed   The right-side speed.
     * @param squareInputs If true, input sensitivity is decreased at lower speeds.
     */
    public void tankDrive(double leftSpeed, double rightSpeed, boolean squareInputs) {
        driveBase.tankDrive(leftSpeed, rightSpeed, squareInputs);
    }

    /**
     * Drives the robot using arcade-style control.
     * 
     * @param xSpeed       The speed along the x-axis of the robot.
     * @param zRotation    The rotational speed.
     * @param squareInputs If true, input sensitivity is decreased at lower speeds.
     */
    public void arcadeDrive(double xSpeed, double zRotation, boolean squareInputs) {
        driveBase.arcadeDrive(xSpeed, zRotation, squareInputs);
    }
}
