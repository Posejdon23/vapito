package com.kamilu.pirobot;

public interface RobotService {

	public void motorForward();

	public void motorReverse();

	public void motorLeft();

	public void motorRight();

	public void motorStop();

	// public boolean isMotorLoaded();

	public void loadRobot();

	public void camUp();

	public void camDown();

	public void camLeft();

	public void camRight();
}
