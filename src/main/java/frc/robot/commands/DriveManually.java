/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.utilities.NRGPreferences.BooleanValue;

public class DriveManually extends RunCommand {

    static BooleanValue USING_TANK_CONTROL = new BooleanValue("DriveManually/UsingTankControl", true);
    static BooleanValue SQUARE_CONTROL_INPUTS = new BooleanValue("DriveManually/SquareControlInputs", true);

    public DriveManually(final DriveSubsystem driveSubsystem, final Joystick leftJoystick, final Joystick rightJoystick,
            final XboxController xboxController) {
        super(() -> {
            final boolean squareInputs = SQUARE_CONTROL_INPUTS.getValue();

            if (USING_TANK_CONTROL.getValue()) {
                driveSubsystem.tankDrive(-leftJoystick.getY(), -rightJoystick.getY(), squareInputs);
            } else {
                driveSubsystem.arcadeDrive(-xboxController.getY(Hand.kLeft), xboxController.getX(Hand.kRight),
                        squareInputs);
            }
        });

        addRequirements(driveSubsystem);
    }
}
