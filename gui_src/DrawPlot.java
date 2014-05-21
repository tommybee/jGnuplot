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
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
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
		
		public DrawPlot(Properties configProps)
		{
			BasicConfigurator.configure();

			try
			{
		//		logger.debug("Retrieving properties from input stream");
				Properties props = new Properties();
				//InputStream in = new FileInputStream("conf/test/test.props.xml");
				InputStream in = new FileInputStream(configProps.getProperty("configFile"));
				props.loadFromXML(in);
				
				in.close();
				in = null;
				configProps.setProperty("setOutput", props.getProperty("iplot.lineplot.path.win"));
				PlotCommon.setMyProperties(props);
				
	//			logger.debug("...Success");
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
      drawPlot(null);
        
      return;
    }
    
    
    
    public void drawPlot(String title) throws Exception {
      logger.debug("== DrawPlot.drawPlot [ Data ] Start ==");
        
			String[] vars = new String[VAR_LENGTH];
    	
    	String imageFileName = "";
    	
    	// Get Propertity Information
			String outFilePath   = PlotCommon.GetProp_OutputFilePath();
			File   xmlFileName   = PlotCommon.GetProp_ScriptFilePath();
			String inFilePath    = PlotCommon.GetProp_InputFilePath();
		
			//String inFileName  = genInputFile(inFilePath);
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
			logger.debug("DrawPlot.drawPlot [ Data ]");
			logger.debug("==============================================================");
			logger.debug("xmlFileName     : " + xmlFileName);
			logger.debug("inputFilePath   : " + inFileName);
			logger.debug("outFilePath   : " + outFileName);
			logger.debug("xRangeStart   : " + xRangeStart);
			logger.debug("xRangeEnd     : " + xRangeEnd);
			logger.debug("inFilePath    : " + vars[0]);
			logger.debug("updateDate    : " + vars[1]);
			logger.debug("==============================================================");		
			
			
			plotmgr = new GnuPlotCmdMgr(PlotCommon.getGnuplotProperties(title));
			plotmgr.generateLinePlot(xmlFileName, outFileName, xRangeStart, xRangeEnd, vars);
		
		  logger.debug("== DrawPlot.drawPlot [ Data ] End ==");
        
      return;
    }
    
    
    public String getDrawPlot() throws Exception {
        
          
        return getDrawPlot(null);
      }
      
      
   	public String getDrawPlot(String title) throws Exception {
        logger.debug("== DrawPlot.drawPlot [ Data ] Start ==");
          
  			String[] vars = new String[VAR_LENGTH];
      	
      	String imageFileName = "";
      	
      	// Get Propertity Information
  			String outFilePath   = PlotCommon.GetProp_OutputFilePath();
  			File   xmlFileName   = PlotCommon.GetProp_ScriptFilePath();
  			String inFilePath    = PlotCommon.GetProp_InputFilePath();
  		
  			//String inFileName  = genInputFile(inFilePath);
  			String inFileName  = inFilePath + PLOT_DATA_ID + ".DAT";
  			genInputFile(inFilePath);
  			
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
  			logger.debug("getDrawPlot.drawPlot [ Data ]");
  			logger.debug("==============================================================");
  			logger.debug("xmlFileName     : " + xmlFileName);
  			logger.debug("inputFilePath   : " + inFileName);
  			logger.debug("outFilePath   : " + outFileName);
  			logger.debug("xRangeStart   : " + xRangeStart);
  			logger.debug("xRangeEnd     : " + xRangeEnd);
  			logger.debug("inFilePath    : " + vars[0]);
  			logger.debug("updateDate    : " + vars[1]);
  			logger.debug("==============================================================");		
  			 			
      	
  			plotmgr = new GnuPlotCmdMgr(PlotCommon.getGnuplotProperties(title));
  			plotmgr.generateLinePlot(xmlFileName, outFileName, xRangeStart, xRangeEnd, vars);
  		
  		  logger.debug("== getDrawPlot.drawPlot [ Data ] End ==");
          
        return outFileName;
      }

    
    public String genInputFile(String inFilePath) throws NumberFormatException, IOException
	{
    	String inFileName  = inFilePath + PLOT_DATA_ID + ".txt";
    	String convFileName  = inFilePath + PLOT_DATA_ID + ".DAT";
    	
    	PrintWriter out = 
			new PrintWriter(
				new BufferedWriter(
					new OutputStreamWriter(
						new FileOutputStream(convFileName)
						)
					)
				);
				
		BufferedReader rd = 
			new BufferedReader(
				new InputStreamReader(
					new FileInputStream(inFileName)
				)
			);
			
		String line = null;
		
		int id = 0, previd = 0;
		int prevday = 0;
		int curday = 0;
		
		while((line = rd.readLine()) != null)
		{
			//2011 07 12  0000
			StringTokenizer st = new StringTokenizer(line, " ");

			StringTokenizer st2 = new StringTokenizer(PlotCommon.GetFormattedCurrentDate("KST"), " ");
			
			st.nextToken();
			st.nextToken();
			
			int day = Integer.valueOf(st.nextToken());
			
			String thisTime = st2.nextToken();
			
			if(prevday != 0 && prevday - day != 0)
				id++;

			
			if(previd != id)
			{
				StringTokenizer st3 = new StringTokenizer(thisTime, "-");
				
				String yy = st3.nextToken();
				String mm = st3.nextToken();
				
				int day2 = Integer.valueOf(st3.nextToken());
				curday = day2 += id;
				
				
				thisTime = yy + "-" + mm + "-" + day2;
				
				String del = st.nextToken();
	    		thisTime += " " + del.substring(0,2) + ":" + del.substring(2,4);
				
				//logger.debug("Not today-> " +thisTime);
			}
			else
			{
				StringTokenizer st3 = new StringTokenizer(thisTime, "-");
				
				String yy = st3.nextToken();
				String mm = st3.nextToken();
				
				if(id==0)
					thisTime = yy + "-" + mm + "-" + st3.nextToken();
				else
					thisTime = yy + "-" + mm + "-" + curday;
				
				String del = st.nextToken();
	    		
	    		thisTime += " " + del.substring(0,2) + ":" + del.substring(2,4);
	    		
	    		//if(id == 0)
	    		//	logger.debug("today-> " +thisTime);
	    		
			}
			
			
			st.nextToken();
			st.nextToken();
			
			double Atype = Double.valueOf(st.nextToken()).doubleValue();
			double Btype = Double.valueOf(st.nextToken()).doubleValue();
			
			if(Atype < 0 || Btype < 0) continue;
			
			
			//YYYY-MM-DD HH24:MI:SS
			out.print(thisTime);
			out.print(" ");
			out.print(Atype);
			out.print(" ");
			out.println(Btype);
			
			
			prevday = day;
			previd = id;
			
		}
		
		rd.close();
		rd = null;
		
		out.flush();
		out.close();
		
		
		return convFileName;
	}
}
