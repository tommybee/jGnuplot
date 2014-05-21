package com.tobee.iplot.ui;

import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.*;
import org.apache.log4j.Logger;


public class ImagePane extends JPanel
{
	private final static Logger logger = Logger.getLogger(ImagePane.class);  
	
	private boolean isEmptyScreen = true; 
	
	boolean initialized = false;
	
	BufferedImage plotImg;
	
	int partImgW;
	int partImgH;
	
	public ImagePane() {
		super(true);
	
		plotImg = null;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(isEmptyScreen) return;
		
		if(plotImg !=null) 
			g.drawImage(plotImg, 0, 0, 600, 480, null);
		
	}
	
	public void OnMapUpdate(BufferedImage pixels) {
		ImagePane.this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

		if(pixels != null ) {
	
			plotImg = pixels;

			isEmptyScreen = false;
			repaint();
		
		}	else {
			logger.debug("[OnMapUpdate] Fail to create plot.");
		}
	}
}
