package com.galzuris.rescraft;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class ResCraft extends MIDlet implements CommandListener {
	public static final String version = "3.0.6";
	
	private GameEngine engine;
	private Display display;
	private Form formText;
	private TextField formTextField;

	public ResCraft() {
		this.display = Display.getDisplay(this);

		formText = new Form("FormTitle");
		formTextField = new TextField("TextField", "Initial Text", 256, TextField.ANY);
		formText.append(formTextField);
		formText.addCommand(new Command("OK", Command.OK, 4));
		formText.setCommandListener(this);

		this.engine = new GameEngine();
	}

	public String GetVersion() {
		return ResCraft.version;
	}

	protected void startApp() throws MIDletStateChangeException {
		this.display.setCurrent(GameEngineCanvas.getInstance());
		this.engine.start();
	}

	public void commandAction(Command c, Displayable d) {
		if (c.getCommandType() == Command.OK || c.getCommandType() == Command.CANCEL) {
//			if (this.formTextListener != null) {
//				this.display.setCurrent(this.engine.getDrawingArea());
//				this.engine.setPause(false);
//				this.formTextListener.OnFormTextResult(formTextTag, formTextField.getString());
//				this.formTextListener = null;
//			}
		}
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
	}

	protected void pauseApp() {
		this.engine.pause();
	}

//	public void FormTextOpen(
//		final FormTextResultInterface listener, 
//		final String tag, 
//		final String title, 
//		final String fieldTitle,
//		final String fieldValue, 
//		final int maxSize,
//		final int constraints
//	){
//		formText = new Form("FormTitle");
//		formTextField = new TextField("TextField", "Initial Text", 256, TextField.ANY);
//		formText.append(formTextField);
//		formText.addCommand(new Command("OK", Command.OK, 4));
//		formText.setCommandListener(this);
//		
//		this.formTextListener = listener;
//		this.formTextTag = tag;
//		formText.setTitle(title);
//		formTextField.setLabel(fieldTitle);
//		formTextField.setString(fieldValue);
//		formTextField.setMaxSize(maxSize);
//		formTextField.setConstraints(constraints);
//
//		this.engine.setPause(true);
//		this.display.setCurrent(formText);
//		// formText.setCommandListener(this);
//	}
}
