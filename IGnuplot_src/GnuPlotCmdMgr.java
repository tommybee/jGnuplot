package com.tobee.iplot.core;

import java.io.File;
import java.util.Properties;

public class GnuPlotCmdMgr {
 	private static jGnuPlot jplot;
	private static EnvMgr envmgr;
	
	public GnuPlotCmdMgr(final Properties prps)
	{
		envmgr = new EnvMgr(prps);
		jplot = new jGnuPlot(envmgr);
	}
	
	public void generateLinePlot(
		File inFile, String outFile, 
		String rangeStart, String rangeEnd, 
		String[] vars
	)
	{
		boolean isLoaded = false;
		try
		{
			isLoaded = jplot.startIGnuPlot(envmgr);
			
			if(isLoaded == false)
				System.out.println("----------------Fail to Start gnuplot.exe");
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if(isLoaded) jplot.setupPlotEngine(inFile, outFile, rangeStart, rangeEnd, vars);
	}
}


