package com.tobee.plot.drawplot;

import java.io.PrintWriter   ;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.InvalidPropertiesFormatException;
import java.util.StringTokenizer;

import com.tobee.iplot.core.GnuPlotCmdMgr;
import com.tobee.plot.drawplot.common.PlotCommon;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class DrawPlot {
	
	private static final String PLOT_DATA_ID   = "PTPlot"; 
	private static final int    PNG_DATE_SIZE  = 10;
	private static final int  	VAR_LENGTH     = 2;
	
    /** Log4j */
    private static final Logger logger = Logger.getLogger(DrawPlot.class);
    
    private GnuPlotCmdMgr plotmgr;
		
		public static void main(String[] args)
		{
			BasicConfigurator.configure();

			DrawPlot plots = new DrawPlot();
			try
			{
				logger.debug("Retrieving properties from input stream");
				Properties props = new Properties();
				InputStream in = new FileInputStream("conf/test/test.props.xml");
				props.loadFromXML(in);
				
				in.close();
				in = null;
				
				
				PlotCommon.setMyProperties(props);
				
				logger.debug("...Success");
				
				new DrawPlot().drawPlot();
			}
			catch(InvalidPropertiesFormatException e)
			{
				logger.debug("...Fail");
				logger.info("[InvalidPropertiesFormatException]");
				
				System.exit(0);
			}
			catch(IOException e)
			{
				logger.debug("...Fail");
				logger.info("[IOException]");
				System.exit(0);
			}
			catch(Exception e)
			{
				logger.debug("...Fail");
				logger.info("[Exception]");
				System.exit(0);
			}
		}
		
		
    public void drawPlot() throws Exception {
      logger.debug("== DrawPlot.drawPlot [ Data ] Start ==");
        
			String[] vars = new String[VAR_LENGTH];
    	
    	String imageFileName = "";
    	
    	// Get Propertity Information
			String outFilePath   = PlotCommon.GetProp_OutputFilePath();
			File   xmlFileName   = PlotCommon.GetProp_ScriptFilePath();
			String inFilePath    = PlotCommon.GetProp_InputFilePath();
		
			String inFileName  = inFilePath + PLOT_DATA_ID + ".DAT";
			
			String xRangeStart = PlotCommon.GetDailyUTC(0, true);
			String xRangeEnd   = PlotCommon.GetDailyUTC(-1, false);
		
			String utc = PlotCommon.GetCurrentDate("UTC");
		
			String outFileName = "";

			imageFileName = PLOT_DATA_ID + "_" + utc.substring(0, PNG_DATE_SIZE) + ".png";
			outFileName = outFilePath + imageFileName;
			outFileName = outFileName.replace("//", "/");
		
			vars[0] = "\"" + inFileName + "\"";
			vars[1] = "\"" + "Updated " + PlotCommon.GetFormattedCurrentDate("KST") + " KST" + "\"";
			
			logger.debug("==============================================================");
			logger.debug("DrawPlotPT00007.drawPlot [ Data ]");
			logger.debug("==============================================================");
			logger.debug("xmlFileName     : " + xmlFileName);
			logger.debug("inputFilePath   : " + inFileName);
			logger.debug("outFilePath   : " + outFileName);
			logger.debug("xRangeStart   : " + xRangeStart);
			logger.debug("xRangeEnd     : " + xRangeEnd);
			logger.debug("inFilePath    : " + vars[0]);
			logger.debug("updateDate    : " + vars[1]);
			logger.debug("==============================================================");		
			
			
    	
			plotmgr = new GnuPlotCmdMgr(PlotCommon.getGnuplotProperties());
			plotmgr.generateLinePlot(xmlFileName, outFileName, xRangeStart, xRangeEnd, vars);
		
		  logger.debug("== DrawPlotPT00007.drawPlot [ Data ] End ==");
        
      return;
    }

}
