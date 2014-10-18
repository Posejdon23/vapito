package com.kamilu.pirobot;

import java.io.Serializable;

import org.vaadin.addons.guice.ui.ScopedUI;
import org.vaadin.teemu.webcam.Webcam;

import com.google.inject.Inject;
import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("vaadinguice")
public class RaspberryPiUI extends ScopedUI implements Serializable  {

	private static final String box_unit = "120px";
	private static final String unit = "40px";
	private Button moveBack;
	private Button moveForward;
	private Button turnLeft;
	private Button turnRight;
	private Webcam webcam;

	@Override
	protected void init(VaadinRequest request) {
	}

	@Inject
	public RaspberryPiUI(final RobotController robot) {
		Label emptyLeft = new Label("", ContentMode.HTML);
		Label emptyRight = new Label("", ContentMode.HTML);
		moveForward = new Button("W");
		moveBack = new Button("S");
		turnLeft = new Button("A");
		turnRight = new Button("D");
		final HorizontalLayout hTop = new HorizontalLayout(emptyLeft,
				moveForward, emptyRight);
		final HorizontalLayout hBot = new HorizontalLayout(turnLeft, moveBack,
				turnRight);
		final VerticalLayout layout = new VerticalLayout();
		final VerticalLayout layout1 = new VerticalLayout(hTop, hBot);
		final VerticalLayout layout2 = new VerticalLayout();
		layout1.setWidth(box_unit);
		emptyLeft.setHeight(unit);
		emptyRight.setHeight(unit);
		emptyLeft.setWidth(unit);
		emptyRight.setWidth(unit);
		hTop.setWidth(box_unit);
		hBot.setWidth(box_unit);
		addListeners(robot, layout2);
		layout.addComponents(layout1, layout2);
		setContent(layout);
	}

	private void addListeners(final RobotController robot,
			final VerticalLayout layout) {
		moveForward.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				robot.motorForward();
				Notification.show("motorForward", Type.TRAY_NOTIFICATION);
			}
		});
		moveBack.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				robot.motorReverse();
				Notification.show("motorReverse", Type.TRAY_NOTIFICATION);
			}
		});
		turnLeft.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				robot.motorLeft();
				Notification.show("motorLeft", Type.TRAY_NOTIFICATION);
			}
		});
		turnRight.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				robot.motorRight();
				Notification.show("motorRight", Type.TRAY_NOTIFICATION);
			}
		});

		webcam = new Webcam();
		webcam.setWidth("400px");
		// webcam.detach();
		// webcam.setReceiver(new Receiver() {
		//
		// @Override
		// public OutputStream receiveUpload(String filename, String mimeType) {
		// try {
		// targetFile = File.createTempFile(filename, ".jpeg");
		// targetFile.deleteOnExit();
		// return new FileOutputStream(targetFile);
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// return null;
		// }
		// });

		// Add an event listener to be called after a successful capture.
		// webcam.addCaptureSucceededListener(new CaptureSucceededListener() {
		//
		// @Override
		// public void captureSucceeded(CaptureSucceededEvent event) {
		// Image img = new Image("Captured image", new FileResource(
		// targetFile));
		// img.setWidth("400px");
		// layout.addComponent(img);
		// }
		// });

		Button button = new Button("Otwórz kamerê");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				webcam.capture();
			} 
		});
		HorizontalLayout webcamLayout = new HorizontalLayout(webcam, button);
		webcamLayout.setWidth("400px");
		layout.addComponent(webcamLayout);

		moveForward.setHeight(unit);
		moveBack.setHeight(unit);
		turnLeft.setHeight(unit);
		turnRight.setHeight(unit);
		moveForward.setWidth(unit);
		moveBack.setWidth(unit);
		turnLeft.setWidth(unit);
		turnRight.setWidth(unit);
		moveForward.setClickShortcut(KeyCode.W);
		moveBack.setClickShortcut(KeyCode.S);
		turnLeft.setClickShortcut(KeyCode.A);
		turnRight.setClickShortcut(KeyCode.D);
	}
}