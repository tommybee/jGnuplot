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

import org.apache.commons.net.util.Base64;
import org.apache.log4j.Logger;

public class PlotCommon {
	
    /** Log4j */
    private static final Logger logger = Logger.getLogger(PlotCommon.class);
		private static Properties MyProperties;
		
		public static void setMyProperties(final Properties props)
		{
			MyProperties = props;
		}
    
    public static void ImageFileCopy(String InFile, String outFile) throws IOException {
    	
    	int i, len = 0;
    	
    	FileInputStream fis = new FileInputStream(InFile); 
        FileOutputStream fos = new FileOutputStream(outFile); 
         
         while((i=fis.read()) != -1) { 
             fos.write(i); 
             len++; 
         } 
         
         fis.close(); 
         fos.close(); 
    }
    
    public static String GetUTCString(String format) {
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());
        return utcTime;
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
    
    
    /**
     * T¡¤ ?O¡Æ?¢¯¢® ¥ì???¢¬| ¡í?    * 
     * @param path
     * @return
     */
    public static boolean makeDirectory(String path) {

        String localPath = path;

        if (path.indexOf("\\") != -1) {
            path = path.replace("\\", "/");
        }

        if (path.endsWith("/")) {
            localPath = path;
        } else {
            // ?O¢¬Éð ¡Æ? ?O¢¬ÉÜ f¢¯??¢¬¢Ò¢Ò¢¬¡¤ ¨¬¥ê¬Ò? ?¢¥?¡í?           localPath = path + "/";
        }

        File dir = new File(localPath);

        try {
            if (dir.exists() && dir.isDirectory()) {
                return true;
            } else {
                return dir.mkdirs();
            }
        } catch (Exception e) {
            return false;
        }
    } 
    
    public static String GetCurrentKST() {
    	
    	String rtnValue = "", dtmp = "";
    	
    	Calendar oc = Calendar.getInstance(TimeZone.getTimeZone("GMT+09:00"));
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
    
    public static String GetCurrentUTC() {
    	
    	String rtnValue = "", dtmp = "";
    	
    	Calendar oc = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
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
    
    public static String GetCurrentDate(String prarmTimeZone, int days) {
    	
    	String rtnValue = "", dtmp = "";
    	String timeZone = "";
    	
    	if (prarmTimeZone == "UTC" || prarmTimeZone == "GMT")
    		timeZone = "GMT";
    	else if (prarmTimeZone == "KST")
    		timeZone = "GMT+09:00";
    	
    	Calendar oc = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
    	
    	oc.add(oc.DATE, -days);
    	
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
 			return getGnuplotProperties(null);
    }
    
    
    public static final String encodeTitle(final String title)
		{
			Base64 b64 = new Base64();
			logger.debug("Getting title " + title);
			
			byte[] encodes = (byte[])b64.encode(title.getBytes());
			
			return new String(encodes);
		}
    
    public static Properties getGnuplotProperties(String title) throws Exception
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
				
				if(title != null)
				{
					props.setProperty("RC_NAME", encodeTitle(title));
				}
				
				
			}catch(IOException ie)
			{
				System.out.println(ie);
				throw ie;
			}
			
			return props;
    }
    
    public static Date stringToDate(String StrDate, String format) {
        Date dateToReturn = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            dateToReturn = (Date) sdf.parse(StrDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateToReturn;
    }
    
}
