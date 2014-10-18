package com.kamilu.pirobot;

import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.vaadin.ui.Notification;

public class Robot implements RobotService {

	private GpioStepperMotorComponent motorcamGD, motorcamLR, motorLeweKolo,
			motorPraweKolo;
	static byte[] half_step_sequence;
	static byte[] single_step_sequence;
	static byte[] double_step_sequence;
	private boolean robotLoaded = false;

	@Override
	public void motorForward() {
		motorLeweKolo.reverse();
		motorPraweKolo.forward();
	}

	@Override
	public void motorReverse() {
		motorPraweKolo.reverse();
		motorLeweKolo.forward();
	}

	@Override
	public void motorLeft() {
		motorPraweKolo.forward();
		motorLeweKolo.forward();
	}

	@Override
	public void motorRight() {
		motorPraweKolo.reverse();
		motorLeweKolo.reverse();
	}

	@Override
	public void motorStop() {
		motorLeweKolo.stop();
		motorPraweKolo.stop();
		motorcamGD.stop();
		motorcamLR.stop();
	}

	@Override
	public void loadRobot() {
		if (!robotLoaded) {
			final GpioController gpio = GpioFactory.getInstance();

			final GpioPinDigitalOutput[] pins1 = { // KAMERA GÓRA DÓ£
					gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05,
							PinState.LOW),
					gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06,
							PinState.LOW),
					gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10,
							PinState.LOW),
					gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11,
							PinState.LOW) };
			final GpioPinDigitalOutput[] pins2 = { // LEWE KO£O
					gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_03,
							PinMode.DIGITAL_OUTPUT),
					gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_12,
							PinMode.DIGITAL_OUTPUT),
					gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_13,
							PinMode.DIGITAL_OUTPUT),
					gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_14,
							PinMode.DIGITAL_OUTPUT) };

			final GpioPinDigitalOutput[] pins3 = { // PRAWE KO£O
					gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_09,
							PinMode.DIGITAL_OUTPUT),
					gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_07,
							PinMode.DIGITAL_OUTPUT),
					gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_00,
							PinMode.DIGITAL_OUTPUT),
					gpio.provisionDigitalMultipurposePin(RaspiPin.GPIO_02,
							PinMode.DIGITAL_OUTPUT) };

			final GpioPinDigitalOutput[] pins4 = { // KAMERA LEWO PRAWO
					gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15,
							PinState.LOW),
					gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16,
							PinState.LOW),
					gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01,
							PinState.LOW),
					gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04,
							PinState.LOW) };

			motorcamGD = new GpioStepperMotorComponent(pins1);
			motorLeweKolo = new GpioStepperMotorComponent(pins2);
			motorPraweKolo = new GpioStepperMotorComponent(pins3);
			motorcamLR = new GpioStepperMotorComponent(pins4);
			gpio.setShutdownOptions(true, PinState.LOW, pins1);
			gpio.setShutdownOptions(true, PinState.LOW, pins2);
			gpio.setShutdownOptions(true, PinState.LOW, pins3);
			gpio.setShutdownOptions(true, PinState.LOW, pins4);

			half_step_sequence = new byte[8];
			/*
			 * half_step_sequence[0] = (byte) 0b0001; half_step_sequence[1] =
			 * (byte) 0b0011; half_step_sequence[2] = (byte) 0b0010;
			 * half_step_sequence[3] = (byte) 0b0110; half_step_sequence[4] =
			 * (byte) 0b0100; half_step_sequence[5] = (byte) 0b1100;
			 * half_step_sequence[6] = (byte) 0b1000; half_step_sequence[7] =
			 * (byte) 0b1001;
			 */
			half_step_sequence[0] = 1;
			half_step_sequence[1] = 3;
			half_step_sequence[2] = 2;
			half_step_sequence[3] = 6;
			half_step_sequence[4] = 4;
			half_step_sequence[5] = 12;
			half_step_sequence[6] = 8;
			half_step_sequence[7] = 9;

			single_step_sequence = new byte[4];
			/*
			 * single_step_sequence[0] = (byte) 0b0001; single_step_sequence[1]
			 * = (byte) 0b0010; single_step_sequence[2] = (byte) 0b0100;
			 * single_step_sequence[3] = (byte) 0b1000;
			 */
			single_step_sequence[0] = 1;
			single_step_sequence[1] = 2;
			single_step_sequence[2] = 4;
			single_step_sequence[3] = 8;

			double_step_sequence = new byte[4];
			/*
			 * double_step_sequence[0] = (byte) 0b0011; double_step_sequence[1]
			 * = (byte) 0b0110; double_step_sequence[2] = (byte) 0b1100;
			 * double_step_sequence[3] = (byte) 0b1001;
			 */
			double_step_sequence[0] = 3;
			double_step_sequence[1] = 6;
			double_step_sequence[2] = 12;
			double_step_sequence[3] = 9;

			motorcamGD.setStepInterval(3);
			motorcamGD.setStepSequence(single_step_sequence);
			motorcamGD.setStepsPerRevolution(2038);
			motorLeweKolo.setStepInterval(3);
			motorLeweKolo.setStepSequence(single_step_sequence);
			motorLeweKolo.setStepsPerRevolution(2038);
			motorPraweKolo.setStepInterval(3);
			motorPraweKolo.setStepSequence(single_step_sequence);
			motorPraweKolo.setStepsPerRevolution(2038);
			motorcamLR.setStepInterval(3);
			motorcamLR.setStepSequence(single_step_sequence);
			motorcamLR.setStepsPerRevolution(2038);
			Notification.show("Robot loaded");
			robotLoaded = true;
		}
	}

	@Override
	public void camUp() {
		motorcamGD.forward();
	}

	@Override
	public void camDown() {
		motorcamGD.reverse();
	}

	@Override
	public void camLeft() {
		motorcamLR.reverse();
	}

	@Override
	public void camRight() {
		motorcamLR.forward();
	}
}
