package com.tobee.plot.drawplot.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.log4j.Logger;

public class PlotCommon {
	
    /** Log4j */
    private static final Logger logger = Logger.getLogger(PlotCommon.class);
		private static Properties MyProperties;
		
		public static void setMyProperties(final Properties props)
		{
			MyProperties = props;
		}
    
    public static String GetProp_OsType() throws Exception {
    	return System.getProperty("os.name");
    }
    
    public static String GetProp_InputFilePath() throws Exception {
    	String rtnValue = "";
    	
    	if (GetProp_OsType().startsWith("Window"))
    		rtnValue = MyProperties.getProperty("iplot.input.path.win");
    	else
    		rtnValue = MyProperties.getProperty("iplot.input.path.linux");

    	return rtnValue;
    }
    
    
    public static boolean isOSWindows() throws Exception {
    	boolean rtnValue = false;
    	
    	if (GetProp_OsType().startsWith("Window"))
    		rtnValue = true;
    	
    	return rtnValue;
    }
    
    public static String GetProp_OutputFilePath() throws Exception {
    	String rtnValue = "";
    	
    	if (isOSWindows())
    		rtnValue = MyProperties.getProperty("iplot.lineplot.path.win");
    	else
    		rtnValue = MyProperties.getProperty("iplot.lineplot.path.linux");

    	return rtnValue;
    }
    
    public static File GetProp_ScriptFilePath() throws Exception {
    	String path = null;
    	
			if(isOSWindows())
			{
				path = MyProperties.getProperty("TemplateLoc") + "/" + MyProperties.getProperty("TemplateName");
			}
			else
			{
				path = MyProperties.getProperty("TemplateLoc") + "/" + MyProperties.getProperty("TemplateName");
			}
    	
    	return new File(path);
    }
    
    public static String GetDailyUTC(int days, boolean isStart) {
    	
    	String rtnValue = "", dtmp = "";
    	
    	Calendar oc = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    	oc.add(oc.DATE, -days);
    	
    	dtmp = "0000" + oc.get(Calendar.YEAR);
    	rtnValue += dtmp.substring(dtmp.length()-4) + "-";
    	
    	dtmp = "00" + (oc.get(Calendar.MONTH)+1);
    	rtnValue += dtmp.substring(dtmp.length()-2) + "-";    	
    	
    	dtmp = "00" + oc.get(Calendar.DAY_OF_MONTH);
    	rtnValue += dtmp.substring(dtmp.length()-2) + " ";

    	if (isStart)
    		rtnValue += "00:00:00";
    	else
    		rtnValue += "23:59:59";
    	
    	return rtnValue;
    }
    
    public static String GetCurrentDate(String prarmTimeZone) {
    	
    	String rtnValue = "", dtmp = "";
    	String timeZone = "";
    	
    	if (prarmTimeZone == "UTC" || prarmTimeZone == "GMT")
    		timeZone = "GMT";
    	else if (prarmTimeZone == "KST")
    		timeZone = "GMT+09:00";
    	
    	Calendar oc = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
    	dtmp = "0000" + oc.get(Calendar.YEAR);
    	rtnValue += dtmp.substring(dtmp.length()-4);
    	
    	dtmp = "00" + (oc.get(Calendar.MONTH)+1);
    	rtnValue += dtmp.substring(dtmp.length()-2);    	
    	
    	dtmp = "00" + oc.get(Calendar.DAY_OF_MONTH);
    	rtnValue += dtmp.substring(dtmp.length()-2);
    	
    	dtmp = "00" + oc.get(Calendar.HOUR_OF_DAY);
    	rtnValue += dtmp.substring(dtmp.length()-2);
    	
    	dtmp = "00" + oc.get(Calendar.MINUTE);
    	rtnValue += dtmp.substring(dtmp.length()-2);
    	
    	dtmp = "00" + oc.get(Calendar.SECOND);
    	rtnValue += dtmp.substring(dtmp.length()-2);    	
    	
    	return rtnValue;
    }
    
    public static String GetFormattedCurrentDate(String prarmTimeZone) {
    	
    	String rtnValue = "", dtmp = "";
    	String timeZone = "";
    	
    	if (prarmTimeZone == "UTC" || prarmTimeZone == "GMT")
    		timeZone = "GMT";
    	else if (prarmTimeZone == "KST")
    		timeZone = "GMT+09:00";
    	
    	Calendar oc = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
    	dtmp = "0000" + oc.get(Calendar.YEAR);
    	rtnValue += dtmp.substring(dtmp.length()-4) + "-";
    	
    	dtmp = "00" + (oc.get(Calendar.MONTH)+1);
    	rtnValue += dtmp.substring(dtmp.length()-2)+ "-";    	
    	
    	dtmp = "00" + oc.get(Calendar.DAY_OF_MONTH);
    	rtnValue += dtmp.substring(dtmp.length()-2)+ " ";
    	
    	dtmp = "00" + oc.get(Calendar.HOUR_OF_DAY);
    	rtnValue += dtmp.substring(dtmp.length()-2)+ ":";
    	
    	dtmp = "00" + oc.get(Calendar.MINUTE);
    	rtnValue += dtmp.substring(dtmp.length()-2)+ ":";
    	
    	dtmp = "00" + oc.get(Calendar.SECOND);
    	rtnValue += dtmp.substring(dtmp.length()-2);    	
    	
    	return rtnValue;
    }
    
    
    public static Properties getGnuplotProperties() throws Exception
    {
    	FileInputStream fin = null;
			Properties props = new Properties();
			String resource = null;
			
			
			if (isOSWindows()) {
				resource = MyProperties.getProperty("swc.iplot.xml.path.win");
			} 
			else {
				resource = MyProperties.getProperty("swc.iplot.xml.path.linux");
			}
			
			try
			{
				fin = new FileInputStream(resource);
				props.loadFromXML(fin);
			}catch(IOException ie)
			{
				System.out.println(ie);
				throw ie;
			}
			
			return props;
    }
}
