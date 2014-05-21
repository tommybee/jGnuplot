package com.tobee.iplot.core;

import java.io.*;
import java.util.*;

public class EnvMgr
{
	private static Properties props;
	private static final String OSTYPE;
	
	static
	{
		OSTYPE = System.getProperty("os.name");
	}
	
	public EnvMgr(final Properties props)
	{
		this.props = props;
	}
	
	public String getNativeLibName(){
		return props.getProperty("NativeLib");
	}
	
	public String getPlotLocation(){
		return props.getProperty("PlotLoc");
	}
	
	public String getScriptLocation(){
		return props.getProperty("PlotScript");
	}
	
	public String getNativeLibPath(){
		return props.getProperty("NativePath");
	}
	
	public String getSrcEncoding(){
		return props.getProperty("src.encoding");
	}
	
	public String getDestEncoding(){
		return props.getProperty("dest.encoding");
	}
	
	
	public String getDocEncoding(){
		return props.getProperty("doc.encoding");
	}
	
	public String getOSType()
	{
		return OSTYPE;
	}
	
	public String getProps(String name){
		return props.getProperty(name);
	}
	
}