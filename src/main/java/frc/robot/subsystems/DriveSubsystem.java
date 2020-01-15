/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/** A class implementing the robot drive subsystem. */
public class DriveSubsystem extends SubsystemBase {

    private SpeedController leftMotor = new Victor(0);
    private SpeedController rightMotor = new Victor(1);

    private DifferentialDrive driveBase = new DifferentialDrive(leftMotor, rightMotor);

    /**
     * Constructs an instance of this class.
     */
    public DriveSubsystem() {

    }

    /** Called to periodically perform tasks. It is called once per scheduler run. */
    @Override
    public void periodic() {

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
