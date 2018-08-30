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
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import org.opencv.videoio.VideoCapture;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
//import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class Robot extends IterativeRobot {
	public static final double AXIS_CONST = 1.0 / 180;
	private DifferentialDrive robotDrive
			= new DifferentialDrive(new Spark(0), new Spark(1));
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
	String plateLocate; //Retrieves data for location of the switch
	//private Auto auto = new Auto(plateLocate);
	boolean climberFlag = false;
	JoystickButton aButton;
	JoystickButton bButton;
	JoystickButton triggerJoystick;
	double timeNow;
	double timeSum;
	double rTrigger;
	double lTrigger;
	double Triggers; 
	double armSpeed;
	int gripButtonNumb;
	double speedMod;
	int autoDistance;
	
	@Override
	public void robotInit() {
		
		UsbCamera cam = CameraServer.getInstance().startAutomaticCapture();
	}
	
	private double timeForDist(int distance){
		double timeForDist = distance / 118;
		return timeForDist;
	}
	
	private double timeForTurn(int degree){
		int amtOfTurns = degree / 90;
		double timeForTurn = 0.159 * amtOfTurns;
		return timeForTurn;
	}
	
	private void leftSideAuto(String plateLocate){
		
		if(plateLocate.charAt(0) == 'L'){
			if(timer.get() < 0.5){
				armSole.set(DoubleSolenoid.Value.kForward);
				timeNow = timer.get();
			}else if(timer.get() > 0.61 && timer.get() < 0.51 + 0.25){
				//Does nothing
				timeNow = timer.get();
			}else if(timer.get() > 0.61 + 0.26 && timer.get() < (0.51 + 0.26) + timeForDist(200)){
				robotDrive.arcadeDrive(-1.0, 0.0);//Drive Forward
				timeNow = timer.get();
				timeSum = (0.51 + 0.26) + timeForDist(200);
			}else if(timer.get() > timeSum + 0.1 && timer.get() < timeSum + 0.159 ){
				robotDrive.arcadeDrive(0.0, 1.0); //Turn right 90 degrees
				timeNow = timer.get();
				timeSum = ((0.51 + 0.26) + timeForDist(200)) + 0.159;
			}else if(timer.get() > timeSum + 0.1 && timer.get() < timeSum + timeForDist(16)){
				robotDrive.arcadeDrive(-1.0, 0.0);//Drive forward for .05 secs
				timeNow = timer.get();
				timeSum = (((0.51 + 0.26) + timeForDist(200)) + 0.159) + timeForDist(16);
			}else if(timer.get() > timeSum + 0.1 && timer.get() < timeSum + timeForTurn(90)){
				robotDrive.arcadeDrive(0.0, 1.0);
				timeNow = timer.get();
				timeSum = ((((0.51 + 0.26) + timeForDist(200)) + 0.159) + timeForDist(16)) + timeForTurn(90);
			}else if(timer.get() > timeSum + 0.1 && + timer.get() < timeSum + 0.25){
				robotDrive.arcadeDrive(0.5, 0.0);
				timeNow = timer.get();
				timeSum = (((((0.51 + 0.26) + timeForDist(200)) + 0.159) + timeForDist(16)) + timeForTurn(90)) + 0.25;
			}else if(timer.get() < timeSum + 1.0){
				gripperSole.set(DoubleSolenoid.Value.kForward);	
			}
			
		}else{
			if(timer.get() < 0.5){
				armSole.set(DoubleSolenoid.Value.kForward);//Brings arm up halfway
			}else if(timer.get() > 0.5 && timer.get() < 0.5 + 25){
				timeSum = 0.5 + 0.25;
			}else if(timer.get() > timeSum + 0.1 && timer.get() < timeSum + timeForDist(200)){
				robotDrive.arcadeDrive(-1.0, 0.0);
				timeSum = (0.5 + 0.25) + timeForDist(200);
			}else if(timer.get() > timeSum + 0.1 && timer.get() < timeSum + timeForTurn(90)){
				robotDrive.arcadeDrive(0.0, 1.0);
				timeSum = ((0.5 + 0.25) + timeForDist(200)) + timeForTurn(90);
			}else if(timer.get() > timeSum + 0.1 && timer.get() < timeSum + timeForDist(128)){
				robotDrive.arcadeDrive(-1.0, 0.0);
				timeSum = (((0.5 + 0.25) + timeForDist(200)) + timeForTurn(90)) + timeForDist(128);
			}else if(timer.get() > timeSum + 0.1 && timer.get() < timeSum + timeForTurn(90)){
				robotDrive.arcadeDrive(0.0, 1.0);
				timeSum = ((((0.5 + 0.25) + timeForDist(200)) + timeForTurn(90)) + timeForDist(128)) + timeForTurn(90);
			}else if(timer.get() > timeSum + 0.1 && timer.get() < timeSum + 0.25){
				robotDrive.arcadeDrive(0.5, 0.0);
				timeSum = (((((0.5 + 0.25) + timeForDist(200)) + timeForTurn(90)) + timeForDist(128)) + timeForTurn(90)) + 0.25;
			}else if(timer.get() < timeSum + 1.0){
				gripperSole.set(DoubleSolenoid.Value.kForward);	
			}
			
			
			
		}
	}
	
	private void rightSideAuto(String plateLocate){
		
		if(plateLocate.charAt(0) == 'L'){
			while(timer.get() < 0.5){
				armSole.set(DoubleSolenoid.Value.kForward);
				timeNow = timer.get();
			}
			while(timer.get() < timeNow + 0.25){
				//Does nothing
				timeNow = timer.get();
			}
			while(timer.get() < timeNow + timeForDist(200)){
				robotDrive.arcadeDrive(-1.0, 0.0);//Drive Forward
				timeNow = timer.get();
			} 
			while(timer.get() < timeNow + 0.159){
				robotDrive.arcadeDrive(0.0, -1.0); //Turn right 90 degrees
				timeNow = timer.get();
			} 
			while(timer.get() < timeNow + timeForDist(16)){
				robotDrive.arcadeDrive(-1.0, 0.0);//Drive forward for .05 secs
				timeNow = timer.get();
			}
			while(timer.get() < timeNow + timeForTurn(90)){
				robotDrive.arcadeDrive(0.0, -1.0);
				timeNow = timer.get();
			}
			while(timer.get() < timeNow + 0.25){
				robotDrive.arcadeDrive(0.5, 0.0);
				timeNow = timer.get();
			}
			
			gripperSole.set(DoubleSolenoid.Value.kForward);
			
			
			/*while(true){
				robotDrive.arcadeDrive(0.0, 0.0);//Does nothing
				timeNow = timer.get();
			}*/
			
		}else{
			while(timer.get() < 0.5){
				armSole.set(DoubleSolenoid.Value.kForward);//Brings arm up halfway
				timeNow = timer.get();
			}
			while(timer.get() < timeNow + 0.25){
				//Do nothing
			}
			while(timer.get() < timeNow + timeForDist(200)){
				robotDrive.arcadeDrive(-1.0, 0.0);
				timeNow = timer.get();
			}
			while(timer.get() < timeNow + timeForTurn(90)){
				robotDrive.arcadeDrive(0.0, -1.0);
				timeNow = timer.get();
			}
			while(timer.get() < timeNow + timeForDist(128)){
				robotDrive.arcadeDrive(-1.0, 0.0);
				timeNow = timer.get();
			}
			while(timer.get() < timeNow + timeForTurn(90)){
				robotDrive.arcadeDrive(0.0, -1.0);
				timeNow = timer.get();
			}
			while(timer.get() < timeNow + 0.25){
				robotDrive.arcadeDrive(0.5, 0.0);
				timeNow = timer.get();
			}
			
			gripperSole.set(DoubleSolenoid.Value.kForward);
			
			
		}
	}
	
	public void showcase(){
		robotDrive.arcadeDrive(0.0, 0.5);
	}
	
	@Override
	public void autonomousInit() {
		gripperSole.set(DoubleSolenoid.Value.kReverse); //Closes gripper 
		plateLocate = DriverStation.getInstance().getGameSpecificMessage();
		timer.reset();
		timer.start();
		
		/*
		 * 118 inches/sec(Full speed)
		 * 9.83 feet/sec(Full speed)
		 * 
		 */
	}

	@Override
	public void autonomousPeriodic() {
		
		if(timer.get() < 1000.0) {robotDrive.arcadeDrive(0.0, 0.5);}
		
//		if(plateLocate.length() > 0){
//			//leftSideAuto(plateLocate);
//			if(timer.get() < 0.5) {robotDrive.arcadeDrive(-1.0, 0.0);}
//		}else{
//			System.out.println("Invalid Plate Data");
//		}	
		
		

	}


	@Override
	public void teleopInit() {
		
		gripButtonNumb = 1;
		gateSole.set(false);
		armSole.set(DoubleSolenoid.Value.kReverse);
		speedMod = 1.0;
		
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
		
		//Controls speed
		if(xbox.getAButton()) {
			speedMod = 0.5;
		}
		if(xbox.getBButton()) {
			speedMod = 0.75;
		} 
		if(xbox.getYButton()) {
			speedMod = 1.0;
		}
		if(xbox.getXButton()){
			speedMod = 0.0;
		}
				
		//Drives Robot
		robotDrive.arcadeDrive(xbox.getRawAxis(1) * speedMod, xbox.getRawAxis(0) * speedMod);
		
		
		/*rTrigger = xbox.getRawAxis(3);
		lTrigger = -1*xbox.getRawAxis(2);
		Triggers = rTrigger + lTrigger;
		robotDrive.arcadeDrive(Triggers, xbox.getRawAxis(0));*/

		//Controls the arm
		if(xbox.getY() < -0.2) {
			gateSole.set(true);
			armSole.set(DoubleSolenoid.Value.kForward);                                                                                                                                                                                                                                                                                                                                                   
		}else if(xbox.getY() > 0.2) {
			gateSole.set(true);
			armSole.set(DoubleSolenoid.Value.kReverse);
		}else {
			
			armSole.set(DoubleSolenoid.Value.kOff);
			gateSole.set(false);
		}
		
		
		/*if(xbox.getY() < 0) {
			armSole.set(DoubleSolenoid.Value.kForward);
		}
		if(xbox.getY() > 0) {
			armSole.set(DoubleSolenoid.Value.kReverse);
		}*/
		
		/*if(joystick.getRawButton(11)) {
			//gateSole.set(false);
			armSole.set(DoubleSolenoid.Value.kForward);
		}
		/*}else if(joystick.getRawButton(12)) {
			//gateSole.set(false);
			armSole.set(DoubleSolenoid.Value.kReverse);
		}else {
			//gateSole.set(true);
		}
		if(joystick.getRawButton(12)) {
			armSole.set(DoubleSolenoid.Value.kReverse);
		}*/
		
		
		//Controls the arm wrist
		if(xbox.getRawButton(6)){
			armWristMotor.set(-0.45); //Moves arm wrist up
		}else {
			armWristMotor.set(0.0);
		}
		
		//Controls the gripper
		if(xbox.getRawAxis(3) > 0) {
			gripperSole.set(DoubleSolenoid.Value.kForward); //Open
		}else if(xbox.getRawAxis(2) > 0) {
			gripperSole.set(DoubleSolenoid.Value.kReverse); //Close
		}
		
		
	}

	@Override
	public void testPeriodic() {
	}
	
	

	
}
