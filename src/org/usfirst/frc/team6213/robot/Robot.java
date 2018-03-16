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
	private Spark armWristMotor = new Spark(2);
	//private Spark armMotor2 = new Spark(3);
	//private SpeedControllerGroup armMotors = new SpeedControllerGroup(armMotor1, armMotor2);
	private XboxController xbox = new XboxController(0);
	private Joystick joystick = new Joystick(1);
	private Timer timer = new Timer();
	private DoubleSolenoid gripperSole = new DoubleSolenoid(0, 1);
	private DoubleSolenoid armSole = new DoubleSolenoid(2, 3);
	//private DoubleSolenoid gateSole = new DoubleSolenoid(4, 5);
	private Solenoid gateSole = new Solenoid(4);
	boolean climberFlag = false;
	JoystickButton aButton;
	JoystickButton bButton;
	JoystickButton triggerJoystick;
	double rTrigger;
	double lTrigger;
	double Triggers; 
	double armSpeed;
	int gripButtonNumb;
	
	
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
		
		gripButtonNumb = 1;
		gateSole.set(true);
		
		//armMotor2.setInverted(true);
		//gripperSole.set(DoubleSolenoid.Value.kOff);
		//sole2.set(false);
		//sole1.setPulseDuration(0.05);
		//sole2.setPulseDuration(0.05);
		/*sole1.setPulseDuration(0.01);
		sole2.setPulseDuration(0.001);
		Timer.delay(1.0);
		sole1.startPulse();
		Timer.delay(0.01);
		sole2.startPulse();*/
	}

	@Override
	public void teleopPeriodic() {
		
		//Drives Robot
		rTrigger = xbox.getRawAxis(3);
		lTrigger = -1*xbox.getRawAxis(2);
		Triggers = rTrigger + lTrigger;
		robotDrive.arcadeDrive(Triggers, xbox.getRawAxis(0));
		
		
		//Controls the arm
		if(joystick.getRawButton(11)) {
			//gateSole.set(false);
			armSole.set(DoubleSolenoid.Value.kForward);
		}
		/*}else if(joystick.getRawButton(12)) {
			//gateSole.set(false);
			armSole.set(DoubleSolenoid.Value.kReverse);
		}else {
			//gateSole.set(true);
		}*/
		if(joystick.getRawButton(12)) {
			armSole.set(DoubleSolenoid.Value.kReverse);
		}
		
		/*if(joystick.getY() < -50) {
			gateSole.set(true);
			armSole.set(DoubleSolenoid.Value.kForward);
		}else if(joystick.getY() > 50) {
			gateSole.set(true);
			armSole.set(DoubleSolenoid.Value.kReverse);
		}else {
			gateSole.set(false);
		}*/
		
		//Controls the arm wrist
		if(joystick.getRawButton(2)){
			armWristMotor.set(-0.7); //Moves arm wrist up
		}else {
			armWristMotor.set(0.0);
		}
		
		//Controls the gripper
		if(gripButtonNumb == 0) {
			if(joystick.getRawButton(1)) {
				gripperSole.set(DoubleSolenoid.Value.kForward);
				gripButtonNumb = 1;
			}
		}else if(gripButtonNumb == 1) {
			if(joystick.getRawButton(1)) {
				gripperSole.set(DoubleSolenoid.Value.kReverse);
				gripButtonNumb = 0;
			}
		}
		

		
		
		
	}

	@Override
	public void testPeriodic() {
	}
}
