package com.tobee.iplot.core;

import java.io.File;

import org.apache.log4j.Logger;
import java.nio.charset.Charset;

import com.tobee.iplot.jni.IGnuPlot;
import com.tobee.iplot.classloader.JniLibraryLoader;

public class jGnuPlot implements IGnuPlot.EventListener {
	private static final Logger Log = Logger.getLogger(jGnuPlot.class);  
	
	private boolean initialized;
	private jGnuPlotScript script;
	
	public jGnuPlot(EnvMgr envmgr) {
		initialized = false;
	}
	
	/**
	* Load JNI library. 
	*/
	private boolean loadLibrary(String path, String name) throws Exception {
		if ( initialized ) return true;
		try{
			Log.debug( "Loading JNI librray from " + path + name); 
			JniLibraryLoader.loadLibrary(path,name); 
		}catch(Exception e)
		{
			throw e;
		}
		
		return true;
	}
    
	public boolean startIGnuPlot(EnvMgr envmgr) throws Exception
	{
		if (!loadLibrary(envmgr.getNativeLibPath(), envmgr.getNativeLibName())) {
			return false;
		}
		
		initialized = true;
		//Log.debug( "Location of gnuplot...... " + envmgr.getPlotLocation()); 
		IGnuPlot.initIGnuPlotCmd(envmgr.getPlotLocation());
		
		script = new jGnuPlotScript(envmgr);
		
		return true;
	}
	
	public void setupPlotEngine(
		File inFile, String outFile, 
		String rangeStart, String rangeEnd, String[] vars
	)
	{
		Log.debug("========= Enter setupPlotEngine ===========");
		
		try{
			System.setProperty("file.encoding", "UTF-8");
			String plotcmd = script.generatePlotScript(inFile, outFile, rangeStart, rangeEnd, vars);
			
			IGnuPlot.gnuPlotCmd(plotcmd);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			Log.info(e.toString());
		}
		
		Log.debug("========= Exit setupPlotEngine ===========");
	}
	
	private byte[] getBytesWithSystemCharset(String str) {
			Charset systemCharset = Charset.forName(System.getProperty("file.encoding"));
			return str.getBytes(systemCharset);
	}

	/**
	 * Fires on LIB message
	 */
	public void onMessage(String text, int level) {
		Log.info("jGnuPlot> " + text);
	}

	public void onInitGnuPlot() {}

	public void onFatalError(final String text) {
		Log.info("jGnuPlot> Fatal Error: Navi has terminated. Reason: " + text + " - Please report this error.");
		
		// Must quit here or the LIB will crash
		hardExit(-1);
	}
	
	public void onQuit(int code) {
		// TODO Not yet implemented in the JNI lib
    	Log.debug("OnQuit Hard Stop.");
    	hardExit(0);
	}
	
	static public void hardExit ( int code) {
		System.exit(code);
	}
}
