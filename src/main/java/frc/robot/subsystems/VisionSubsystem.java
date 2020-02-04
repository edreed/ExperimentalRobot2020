/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.cscore.HttpCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class VisionSubsystem extends SubsystemBase {

    private VideoSource camera = new HttpCamera("Raspberry Pi", "http://frcvision.local:1181/stream.mjpg");

    /**
     * Creates a new VisionSubsystem.
     */
    public VisionSubsystem() {

    }

    /**
     * Add this subsystem's tab to the Shuffleboard.
     */
    public void initShuffleboard() {
        ShuffleboardTab visionTab = Shuffleboard.getTab("Vision");

        visionTab.add("frcvision.local", camera).withWidget(BuiltInWidgets.kCameraStream).withSize(4, 3).withPosition(0, 0);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
