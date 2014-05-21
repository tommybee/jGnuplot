package com.tobee.iplot.jni;

import java.util.logging.*;


public class IGnuPlot 
{
	private static EventListener listener;
	
	public static interface EventListener 
	{
		void onMessage(String text, int level);
		void onInitGnuPlot();
		void onFatalError(String text);
		void onQuit (int code);
	}
	
	public static void setListener(EventListener l) {
		listener = l;
	}
	
	public static native int initIGnuPlotCmd(String locOfPlot);
	public static native int gnuPlotCmd(String cmd);
	public static native int gnuPlotCmdScript(String scriptfile, String datafile);
	public static native void gnuPlotClose();
	public static native void gnuPlotCmdBytes(byte[] bs);
	
	/***********************************************************
	 * C - Callbacks
	 ***********************************************************/
	
	/**
	 * This fires on messages from the C layer
	 * @param text
	 */
	@SuppressWarnings("unused")
	private static void onMessage(String text, int level) {
		if ( listener != null)
			listener.onMessage(text, level);
	}
	
	@SuppressWarnings("unused")
	private static void onInitGnuPlot() {
		if ( listener != null)
			listener.onInitGnuPlot();
	}
	
	
	/**
	 * Fires when the C lib calls exit()
	 * @param message
	 */
	@SuppressWarnings("unused")
	private static void onFatalError(String message) {
		if ( listener != null)
			listener.onFatalError(message);
	}

	/**
	 * Fires when Doom Quits
	 * TODO: No yet implemented in the JNI lib
	 * @param code
	 */
	@SuppressWarnings("unused")
	private static void onQuit(int code) {
		if ( listener != null)
			listener.onQuit(code);
	}
}
