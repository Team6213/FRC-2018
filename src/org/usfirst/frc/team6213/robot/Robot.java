package org.usfirst.frc.team6213.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import org.opencv.videoio.VideoCapture;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
//import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class Robot extends IterativeRobot {
	private DifferentialDrive robotDrive
			= new DifferentialDrive(new Spark(0), new Spark(1));
	//private Spark motor1 = new Spark(0);
	//private Spark motor2 = new Spark(1);
	private Spark armMotor1 = new Spark(2);
	private Spark armMotor2 = new Spark(3);
	private SpeedControllerGroup armMotors = new SpeedControllerGroup(armMotor1, armMotor2);
	private XboxController xbox = new XboxController(0);
	private Joystick joystick = new Joystick(1);
	private Timer timer = new Timer();
	private Compressor compressor = new Compressor(1);
	private Solenoid sole1 = new Solenoid(0);
	private Solenoid sole2 = new Solenoid(1);
	//private DoubleSolenoid gripperSole = new DoubleSolenoid(0, 1);
	//private DoubleSolenoid armSole = new DoubleSolenoid(2, 3);
	boolean climberFlag = false;
	JoystickButton aButton;
	JoystickButton bButton;
	JoystickButton triggerJoystick;
	double rTrigger;
	double lTrigger;
	double Triggers; 
	
	
	@Override
	public void robotInit() {
		
		UsbCamera cam = CameraServer.getInstance().startAutomaticCapture();
		VideoCapture vidCam = new VideoCapture(0);
		
	
	}
	
	@Override
	public void autonomousInit() {
		timer.reset();
		timer.start();
		
		
		
		
	}

	@Override
	public void autonomousPeriodic() {
		// Drive for 2 seconds
		if (timer.get() < 2.0) {
			robotDrive.arcadeDrive(0.5, 0.0); // drive forwards half speed
		} else {
			robotDrive.stopMotor(); // stop robot
		}
	}


	@Override
	public void teleopInit() {
		armMotor2.setInverted(true);
		//gripperSole.set(DoubleSolenoid.Value.kOff);
		sole1.set(false);
		sole2.set(false);
		sole1.setPulseDuration(0.05);
		sole2.setPulseDuration(0.05);
		timer.reset();
	}

	@Override
	public void teleopPeriodic() {
		
		/*
		 * For hand motors, use (0.5/180)*axes to keep max speed at half
		 */
		rTrigger = xbox.getRawAxis(3);
		lTrigger = -1*xbox.getRawAxis(2);
		Triggers = rTrigger + lTrigger;
		robotDrive.arcadeDrive(Triggers, xbox.getX());
		//robotDrive.arcadeDrive(joystick.getY(), joystick.getX());
		
		/*boolean aButton = xbox.getAButton();
		boolean bButton = xbox.getRawButton(2);
		boolean xButton = xbox.getRawButton(3);
		boolean yButton = xbox.getRawButton(4);
		boolean triggerJoystick = joystick.getRawButton(1);*/
		
		
		
		while(joystick.getRawButton(3)) {
			sole1.startPulse();
			timer.start();
			if(timer.get() == 0.05) {
				sole2.startPulse();
				break;
			} else if(timer.get() > 0.05) {
				xbox.setRumble(GenericHID.RumbleType.kLeftRumble, 1.0);
				break;
			}
		}
		


		
		if(joystick.getRawButton(5)) {
			sole1.set(true);
		} else {
			sole1.set(false);
		}
		
		if(joystick.getRawButton(6)) {
			sole2.set(true);
		} else {
			sole2.set(false);
		}
		
	
		/*if(joystick.getRawButton(5)) {
			gripperSole.set(DoubleSolenoid.Value.kForward);
		}else if(joystick.getRawButton(6)) {
			gripperSole.set(DoubleSolenoid.Value.kReverse);
		}else {
			gripperSole.set(DoubleSolenoid.Value.kOff);
		}
		
		if(joystick.getRawButton(3)) {
			armSole.set(DoubleSolenoid.Value.kForward);
		}else if(joystick.getRawButton(4)) {
			armSole.set(DoubleSolenoid.Value.kReverse);
		}else {
			armSole.set(DoubleSolenoid.Value.kOff);
		}*/
		
		
		
		if(xbox.getAButton()) {
			armMotors.set(1.0);
		}else {
			armMotors.set(0.0);
			
		}
		
		/*if(xbox.getBButton()) {
			xbox.setRumble(GenericHID.RumbleType.kLeftRumble, 1.0);
		}else {
			xbox.setRumble(GenericHID.RumbleType.kLeftRumble, 0.0);
		}
		
		if(xbox.getXButton()) {
			xbox.setRumble(GenericHID.RumbleType.kRightRumble, 1.0);
		}else {
			xbox.setRumble(GenericHID.RumbleType.kRightRumble, 0.0);
		}*/
		
		if(xbox.getBButton()) {
			armMotor1.set(1.0);
		}else {
			armMotor1.set(0.0);
		}
		
		

		
	}

	@Override
	public void testPeriodic() {
	}
}
