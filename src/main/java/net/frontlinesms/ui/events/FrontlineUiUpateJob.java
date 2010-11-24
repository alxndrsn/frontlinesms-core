package net.frontlinesms.ui.events;

import java.awt.EventQueue;

public abstract class FrontlineUiUpateJob implements Runnable {
	public void execute() {
		EventQueue.invokeLater(this);
	}
}
