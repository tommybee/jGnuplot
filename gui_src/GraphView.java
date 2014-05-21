package com.tobee.iplot.ui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.tobee.plot.drawplot.DrawPlot;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class GraphView extends javax.swing.JFrame {
	private static final Logger logger = Logger.getLogger(GraphView.class);

	private JScrollPane plotView;
	private JButton jButton3;
	private JSeparator jSeparator2;
	private JLabel jLabel1;
	private JTextField jTextField4;
	private JSeparator jSeparator3;
	private JPanel jPanel1;
	private ImagePane jPanel3;
	private JButton jButton5;
	private JTextField configPath;
	private JButton jButton4;
	private JPanel jPanel2;
	private SearchListener searchListen;
	private String imgTemp;
	
	private static final Properties commandProps = new Properties();
	private static final File CURRENT_DIR = new File("D:/MyProject/Dev/plot/latest/bin");
	
	static
	{
		commandProps.setProperty("configFile", "N/A");
		commandProps.setProperty("searchTemplate", "N/A");
		commandProps.setProperty("setOutput", "N/A");
	}

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GraphView inst = new GraphView();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public GraphView() {
		super();
		
		imgTemp =  System.getProperty("img.plot.tmp");
		
		searchListen = new SearchListener();
		
		initPlotImg();
		initGUI();
	}
	
	private void initPlotImg()
	{
		jPanel3 = new ImagePane();
		jPanel3.OnMapUpdate(getTmpPlotImg(null));
	}
	
	private BufferedImage getTmpPlotImg(String imgName)
	{
		//String tmpImage = commandProps.getProperty("setOutput") +"/"+ "tempImage.png";
		BufferedImage in = null;
		try {
			if(imgName == null)
				in = ImageIO.read(new File(imgTemp));
			else
				in = ImageIO.read(new File(imgName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		//GraphicsConfiguration gc =
		//in.createGraphics().getDeviceConfiguration();
		//BufferedImage out =
		//gc.createCompatibleImage(targetWidth, targetHeight, BITMASK);
		//Graphics2D g2d = out.createGraphics();
		//g2d.setComposite(AlphaComposite.Src);
		//g2d.drawImage(in, 0, 0, targetWidth, targetHeight, null);
		//g2d.dispose();
		//ImageIO.write(out, "png", destFile);
		return in;
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				jPanel1 = new JPanel();
				jPanel1.setBounds(691, 6, 213, 148);
				jPanel1.setLayout(null);
				
				jTextField4 = new JTextField();
				jPanel1.add(jTextField4);
				jTextField4.setText("테스트");
				jTextField4.setBounds(97, 62, 116, 22);
				
				jLabel1 = new JLabel();
				jPanel1.add(jLabel1);
				jLabel1.setText("제목");
				jLabel1.setBounds(12, 63, 73, 19);
				
				jSeparator2 = new JSeparator();
				jPanel1.add(jSeparator2);
				jSeparator2.setBounds(12, 94, 201, 10);
				
				jButton3 = new JButton();
				jPanel1.add(jButton3);
				jButton3.setText("종료");
				jButton3.setBounds(148, 104, 64, 22);
				jButton3.setActionCommand("bye");
				jButton3.addActionListener(searchListen);
				
				jButton4 = new JButton();
				jPanel1.add(jButton4);
				jButton4.setText("생성");
				jButton4.setBounds(76, 104, 67, 22);
				jButton4.setActionCommand("plotIt");
				jButton4.addActionListener(searchListen);
				
				configPath = new JTextField();
				jPanel1.add(configPath);
				configPath.setText("config file path");
				configPath.setBounds(12, 21, 111, 22);
				
				jButton5 = new JButton();
				jPanel1.add(jButton5);
				jButton5.setText("search");
				jButton5.setBounds(129, 21, 84, 22);
				jButton5.setActionCommand("configFile");
				jButton5.addActionListener(searchListen);
				
				jSeparator3 = new JSeparator();
				jPanel1.add(jSeparator3);
				jSeparator3.setBounds(12, 10, 202, 10);
				
				getContentPane().add(jPanel1, BorderLayout.EAST);
				jPanel1.setPreferredSize(new java.awt.Dimension(262, 496));
			}
			{
				jPanel2 = new JPanel();
				plotView = new JScrollPane();
				
				jPanel2.add(plotView);
				jPanel2.setLayout(null);
				
				plotView.getHorizontalScrollBar().setBounds(0, 0, 0, 0);
				plotView.getVerticalScrollBar().setBounds(0, 0, 0, 0);
				plotView.setViewportBorder(BorderFactory.createLoweredBevelBorder());
				plotView.getViewport().setBackground(Color.white);
				
				//plotView.setLayout(null);
				
				plotView.setBounds(0, 0, 608, 489);
				{
					plotView.setViewportView(jPanel3);
				}
				plotView.setBorder(BorderFactory.createEtchedBorder());
				
				getContentPane().add(jPanel2);
				jPanel2.setPreferredSize(new java.awt.Dimension(650, 496));
			}

			//getContentPane().add(jPanel2, BorderLayout.WEST);
			pack();
			this.setSize(1014, 530);
			
			
			
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	final class SearchListener implements ActionListener
	{
		private String commnand;
		
		public void actionPerformed(ActionEvent e) 
		{
			commnand = (String)e.getActionCommand();
			
			
			if(commnand.equals("configFile"))
			{
				JFileChooser fileopen = new JFileChooser();
				fileopen.setCurrentDirectory(CURRENT_DIR);
				
			    FileFilter filter = new FileNameExtensionFilter("plot config files(*.xml)", "xml");
			    fileopen.addChoosableFileFilter(filter);
			    
			    int ret = fileopen.showDialog(null, "Open file");

			    if (ret == JFileChooser.APPROVE_OPTION) {
			      String path = null;
			      File file = fileopen.getSelectedFile();
			      path = file.getAbsolutePath();
			      commandProps.setProperty(commnand, file.getAbsolutePath());
			      configPath.setText(path);
			    }
				
			}
			else if(commnand.equals("plotIt"))
			{
				boolean isCfSet = commandProps.getProperty("configFile").equals("N/A");
				//boolean isTSet = commandProps.getProperty("searchTemplate").equals("N/A");
				//boolean isOutSet = commandProps.getProperty("setOutput").equals("N/A");
				
				if(isCfSet)
				{
					JOptionPane.showMessageDialog(null, "환경파일경로를 지정하십시요!", "주의", JOptionPane.ERROR_MESSAGE);
					return;
				}
			/*	
				if(isTSet)
				{
					JOptionPane.showMessageDialog(null, "템플릿경로를 지정하십시요!", "주의", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(isOutSet)
				{
					JOptionPane.showMessageDialog(null, "파일이 저장될 위치를 지정하십시요!", "주의", JOptionPane.ERROR_MESSAGE);
					return;
				}
			*/	
				DrawPlot plotter = new DrawPlot(commandProps);
				
				try {
					String outFile = plotter.getDrawPlot(jTextField4.getText());
					
					jPanel3.OnMapUpdate(getTmpPlotImg(outFile));
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "그래프를 생성하는 동안 에러가 발생하였습니다", "에러", JOptionPane.ERROR_MESSAGE);
				}
			}
			else if(commnand.equals("bye"))
			{
				try {
					this.finalize();
					System.exit(0);
				} catch (Throwable e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			//logger.debug(e.getActionCommand());
			return;
		}
	}
}
