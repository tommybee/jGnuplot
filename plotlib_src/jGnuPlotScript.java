package com.tobee.iplot.core;

import org.xml.sax.InputSource;
import org.apache.commons.net.util.Base64;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class jGnuPlotScript
{	
	private final String SRC_ENC;
	private final String DEST_ENC;
	private final EnvMgr ENVMGR;

	public jGnuPlotScript(final EnvMgr envmgr)
	{
		SRC_ENC = envmgr.getSrcEncoding();
		DEST_ENC = envmgr.getDestEncoding();
		this.ENVMGR = envmgr;
	}
	
	/**
   * Parses a stream into a dom.
   */
  protected Document dom( InputSource input ) throws Exception {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
      factory.setNamespaceAware( true );
      //factory.setValidating( true );
      
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document dom = builder.parse( input );
      
      return dom;
  }
	
	
	/**
   * Parses a stream into a dom.
   */
  protected Document dom( InputStream input ) throws Exception {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
      factory.setNamespaceAware( true );
      //factory.setValidating( true );
      
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document dom = builder.parse( input );
      
      return dom;
  }
	
	private final InputStream getInputStreamWithoutBOM(final File templatePath)
	{
		FileInputStream fin = null;
		FileOutputStream fout = null;
		InputStream result = null;
		
		File fi = null;
		File fo = null;
		
		int fSize = -1;
		
		ByteBuffer bbuf = ByteBuffer.allocate(4);
		
		String strFilePath = null;
		String strFilePathOrg = null;
		try
		{
			fi = templatePath;
			strFilePathOrg = templatePath.getAbsolutePath();
			strFilePath = templatePath.getAbsolutePath() + ".cpy";
			
			fo = new File(strFilePath);
			fin = new FileInputStream(fi);
			fout = new FileOutputStream(fo);
			
			FileChannel fchin = fin.getChannel();
			fchin.read(bbuf);
			fSize = (int) fchin.size();
			
			byte [] bb = bbuf.array();
			
			boolean isOneOfThem = false;

			if((bb[0] & 0x00FF) == 0x3C && 
				(bb[1] & 0x00FF) == 0x3F)
			{
				fin.close();
				fchin.close();
				fchin = null;
				bbuf = null;
				fin = null;
				
				return new FileInputStream(strFilePathOrg);
			}

			FileChannel fchout = fout.getChannel();
			
			if((bb[0] & 0x00FF) == 0xEF && 
				(bb[1] & 0x00FF) ==0xBB && 
				(bb[2] & 0x00FF) == 0xBF
			) //UTF-8
			{
				
				fchin.transferTo(3, fSize - 3, fchout);
				isOneOfThem = true;
				
				fchin.close();
				fchout.close();
				
				fin.close();
				fout.close();
				
				fchin = null;
				fchout = null;
				
				fin = null;
				fout = null;
			}
			
			if((bb[0] & 0x00FF) == 0xFE && 
				(bb[1] & 0x00FF) ==0xFF
			) //UTF-16(BE)
			{
				fchin.transferTo(2, fSize - 2, fchout);
				isOneOfThem = true;
				
				fchin.close();
				fchout.close();
				
				fin.close();
				fout.close();
				
				fchin = null;
				fchout = null;
				
				fin = null;
				fout = null;
			}
			
			if((bb[0] & 0x00FF) == 0xFF && 
				(bb[1] & 0x00FF) ==0xFE
			) //UTF-16(LE)
			{
				fchin.transferTo(2, fSize - 2, fchout);
				isOneOfThem = true;
				
				fchin.close();
				fchout.close();
				
				fin.close();
				fout.close();
				
				fchin = null;
				fchout = null;
				
				fin = null;
				fout = null;
			}
			
			if((bb[0] & 0x00FF) == 0x00 && 
				(bb[1] & 0x00FF) ==0x00 &&
				(bb[2] & 0x00FF) == 0xFE &&
				(bb[3] & 0x00FF) == 0xFF
			) //UTF-32(BE)
			{
				fchin.transferTo(4, fSize - 4, fchout);
				isOneOfThem = true;
				
				fchin.close();
				fchout.close();
				
				fin.close();
				fout.close();
				
				fchin = null;
				fchout = null;
				
				fin = null;
				fout = null;
			}
			
			if((bb[0] & 0x00FF) == 0xFF && 
				(bb[1] & 0x00FF) ==0xFE &&
				(bb[2] & 0x00FF) == 0x00 &&
				(bb[3] & 0x00FF) == 0x00
			) //UTF-32(LE)
			{
				fchin.transferTo(4, fSize - 4, fchout);
				isOneOfThem = true;
				
				fchin.close();
				fchout.close();
				
				fin.close();
				fout.close();
				
				fchin = null;
				fchout = null;
				
				fin = null;
				fout = null;
			}
			
			if(isOneOfThem)
			{
				if(fi.delete())
				{
					
					fi = new File(strFilePath);
					fo = new File(strFilePathOrg);
					
					fin = new FileInputStream(fi);
					fout = new FileOutputStream(fo);
					
					fchin = fin.getChannel();
					fchout = fout.getChannel();
					
					fSize = (int)fchin.size();
					
					fchin.transferTo(0, fSize, fchout);
					
					fchin.close();
					fchout.close();
					
					fin.close();
					fout.close();
					
					fchin = null;
					fchout = null;
					
					fin = null;
					fout = null;
					
					fi.delete();
					
					result = new FileInputStream(fo);
				}
			}
		}catch(IOException ie)
		{
			ie.printStackTrace();
		}
		
		return result;
	}
	
	private final Document getGnuplotDocument(final File templatePath)
	{
		Document xmlDocument = null;
		java.nio.charset.Charset charset = null;
		
		try {
			charset = java.nio.charset.Charset.forName(ENVMGR.getDocEncoding());
			
			xmlDocument = dom(
				new InputSource(
					new InputStreamReader(
						getInputStreamWithoutBOM(templatePath), charset)));
			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xmlDocument;
	}
	
	
	public final String generatePlotScript(
		File templatePath, String outFileName, 
		String rangeStart, String rangeEnd, String[] vars
	)throws Exception 
	{
		System.out.println("----------------------");
		String localName = null;
		java.nio.charset.Charset charset = null;
		
		StringBuffer plotCmdBuf = null;
		
		Document xmlDocument = null;
		int idx = -1;
		
		try {
			System.out.println("Src  Enc: " + ENVMGR.getSrcEncoding());
			System.out.println("Dest Enc: " + ENVMGR.getDestEncoding());
			System.out.println("Doc  Enc: " + ENVMGR.getDocEncoding());
			
			charset = java.nio.charset.Charset.forName(ENVMGR.getDocEncoding());
			
			plotCmdBuf = new StringBuffer();
			
			xmlDocument = getGnuplotDocument(templatePath);
			
			
			if(vars != null)
				idx++;
				
			xmlDocument.getDocumentElement().normalize();
			NodeList nodeLst = xmlDocument.getElementsByTagName("plotcmd");

			String attrName = null;

			Base64 b64 = new Base64();
			
			String suffix = null;
			
			if (ENVMGR.getOSType().startsWith("Window")) {
				suffix = ".win";
	        } 
	        else {
	        	suffix = ".linux";
	        }
			
			for (int s = 0; s < nodeLst.getLength(); s++) {
				Node fstNode = nodeLst.item(s);
				NodeList nodeLst2 = fstNode.getChildNodes();
				
				for(int j = 0; j < nodeLst2.getLength(); j++)
				{
					
					if(nodeLst2.item(j).getLocalName() != null)
					{
						localName = nodeLst2.item(j).getLocalName();
						
						if(localName.equals("set"))
						{
							plotCmdBuf.append(localName + " " + nodeLst2.item(j).getAttributes().item(0).getNodeValue() + " " + nodeLst2.item(j).getTextContent()).append("\n");
						} else if(localName.equals("declare"))
						{	
							attrName = nodeLst2.item(j).getAttributes().item(0).getNodeValue();
							
							if(attrName.equals("OUTPUT_FILE_NAME"))
							{
								plotCmdBuf.append(attrName);
								plotCmdBuf.append("=");
								plotCmdBuf.append("\"").append(outFileName).append("\"");
							}
							else if(attrName.startsWith("INPUT_FILE_NAME"))
							{
								plotCmdBuf.append(attrName);
								plotCmdBuf.append(nodeLst2.item(j).getAttributes().item(1).getNodeValue());
								plotCmdBuf.append("=");
								plotCmdBuf.append(nodeLst2.item(j).getTextContent());
							}
							else if(attrName.startsWith("VAR"))
							{
								String nodename = nodeLst2.item(j).getAttributes().item(1).getNodeName();
								int seq = Integer.valueOf(nodeLst2.item(j).getAttributes().item(1).getNodeValue()).intValue();
								
								if(idx < seq && !nodename.equals("no"))
								{
									System.out.println("ERROR!! idx != seq ");
									continue;
								}
								
								plotCmdBuf.append(attrName);
								plotCmdBuf.append(String.valueOf(seq));
								plotCmdBuf.append("=");
								plotCmdBuf.append(vars[idx++]);
								
							}
							else if(attrName.startsWith("RC_NAME"))
							{
								byte [] decodes 
									= b64.decode(
										ENVMGR.getProps(attrName).getBytes());
								
								String str = new String(decodes);
								String decodeStr = new String(str.getBytes(SRC_ENC),DEST_ENC);
								
								plotCmdBuf.append(attrName);
								plotCmdBuf.append("=");
								plotCmdBuf.append("\"")
								.append(decodeStr).append("\"");
							}
							else if(attrName.startsWith("FONT"))
							{
								plotCmdBuf.append(attrName);
								plotCmdBuf.append("=");
								plotCmdBuf.append("\"")
								.append(ENVMGR.getProps(attrName+suffix)).append("\"");
							}
							else if(rangeStart != null && attrName.startsWith("XRANGE_START"))
							{
								plotCmdBuf.append(attrName);
								plotCmdBuf.append("=");
								plotCmdBuf.append("\"").append(rangeStart).append("\"");
							}
							else if(rangeEnd != null && attrName.startsWith("XRANGE_END"))
							{
								plotCmdBuf.append(attrName);
								plotCmdBuf.append("=");
								plotCmdBuf.append("\"").append(rangeEnd).append("\"");
							}
							else
							{
								if(attrName.startsWith("OUTPUT_FILE_NAME") || 
									attrName.startsWith("INPUT_FILE_NAME") || 
									attrName.startsWith("VAR") || 
									attrName.startsWith("XRANGE_START") || 
									attrName.startsWith("XRANGE_END")  ||
									attrName.startsWith("FONT")  ||
									attrName.startsWith("RC_NAME")
									) continue;
									
								plotCmdBuf.append(attrName);
								plotCmdBuf.append("=");
								plotCmdBuf.append(nodeLst2.item(j).getTextContent());
							}
							plotCmdBuf.append("\n");
							attrName = null;
							
							
						} else if(localName.equals("plot"))
						{
							plotCmdBuf.append(localName + " " + nodeLst2.item(j).getTextContent()).append("\n");
						}
						else if(localName.equals("command"))
						{
							plotCmdBuf.append(" " + nodeLst2.item(j).getTextContent()).append("\n") ;
						}
					}
				}
			}
		} catch (RuntimeException ex) {
			throw ex;
		} catch(Exception e){
			throw e;
		}
		System.out.println("----------------------");//+plotCmdBuf.toString());
		return plotCmdBuf.toString();
	}
}