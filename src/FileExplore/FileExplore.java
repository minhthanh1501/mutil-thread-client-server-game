package FileExplore;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import Client.ClientGameplay;
import DataPacket.DataPacket;
import DataPacket.RequestSendFileData;
import DataPacket.RequestSendMessenger;
import Storage.Save;
import Storage.UserInfor;


import javax.swing.JTree;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JTabbedPane;
import javax.swing.JRadioButton;

public class FileExplore extends JFrame {

	private JPanel contentPane;
	private ScrollPane scrollPane;
	private JScrollPane jScrollPane;
	
	public static ClientGameplay clientGameplay;
	public ClientGameplay getClientGameplay() {
		return clientGameplay;
	}
	public void setClientGameplay(ClientGameplay clientGameplay) {
		this.clientGameplay = clientGameplay;
	}
	
	private JTabbedPane jtabbedPane;
	private JPanel subJPanelOpenFunction = new JPanel(), subJPanelEditFunction = new JPanel(),
	subJPanelPrintFunction = new JPanel(), subJPanelNewFunction = new JPanel(),
	subJPanelCopyFunction = new JPanel(), subJPanelCutFunction = new JPanel(),
	subJPanelRenameFunction = new JPanel(), subJPanelDeleteFunction = new JPanel(),
	subJPanelCompressFunction = new JPanel(), subJPanelExtractFunction = new JPanel();
	
	private JTable jtable;
	private JTree jtree;
	private JButton btnTypeLayout, btnBack, btnForward, btnSearch, btnGoTo;
	private JButton btnOpen, btnRename, btnDelete, btnCopy, btnCut, btnCompress, btnExtract;
	
	private File fileRoot;
	
	//----------------------
	private boolean isListLayout = true;
	//private String resourcePath = "F:\\ecilpse-workspace\\GiuaKyJavaNangCao\\Resource\\";
	
	//private String resourcePath = "F:"+File.separator+"ecilpse-workspace"+File.separator+"GiuaKyJavaNangCao"+File.separator+"Resource"+File.separator;
	private String resourcePath = "Resource"+File.separator;
	//----------------------
	private String startRootPath = "D:\\";
	//private String startRootPath = "C:"+File.separator+"Users"+File.separator+"Acer"+File.separator+"Desktop";
//	private String startRootPath = System.getProperty("user.home")+File.separator+"Desktop";
	public String getStartRootPath() {
		return startRootPath;
	}
	
	public void setStartRootPath(String startRootPath) {
		this.startRootPath = startRootPath;
	}
	//----------------------
	// presentPath l?? ???????ng d???n con tr??? ??ang tr??? ?????n
	private String presentPath;
	private boolean isExistPresentPath = false;
	
	private Stack<String> stackOfForwardPath = new Stack<String>();
	private Stack<String> stackOfBackPath = new Stack<String>();
	
	public Stack<String> getStackOfForwardPath() {
		return stackOfForwardPath;
	}
	
	public void setStackOfForwardPath(Stack<String> stackOfForwardPath) {
		this.stackOfForwardPath = stackOfForwardPath;
	}
	
	public Stack<String> getStackOfBackPath() {
		return stackOfBackPath;
	}
	
	public void setStackOfPath(Stack<String> stackOfBackPath) {
		this.stackOfBackPath = stackOfBackPath;
	}
	//----------------------
	// 2 n??t lui v?? t???i
	private boolean isBackCanClick;
	private boolean isForwardCanClick;
	
	private JTextField textFieldPresentPath;
	private JTextField textFieldSearchPath;

	public boolean getIsBackCanClick() {
		return isBackCanClick;
	}

	public void setIsBackCanClick(boolean isBackCanClick) {
		this.isBackCanClick = isBackCanClick;
	}

	public boolean getIsForwardCanClick() {
		return isForwardCanClick;
	}

	public void setIsForwardCanClick(boolean isForwardCanClick) {
		this.isForwardCanClick = isForwardCanClick;
	}
	//----------------------
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileExplore frame = new FileExplore();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FileExplore() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1188, 709);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textFieldPresentPath = new JTextField();
		textFieldPresentPath.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldPresentPath.setBounds(87, 6, 733, 33);
		contentPane.add(textFieldPresentPath);
		textFieldPresentPath.setColumns(10);
		
		textFieldSearchPath = new JTextField();
		textFieldSearchPath.setBounds(889, 6, 170, 33);
		textFieldSearchPath.setText("T??m ki???m trong th?? m???c n??y");
		contentPane.add(textFieldSearchPath);
		textFieldSearchPath.setColumns(10);
		
		// ?????i t?????ng file g???c
		//fileRoot = new File(startRootPath);
		
		// t???o ra root mother
		jtree = configureJTree(startRootPath);
		presentPath = new String(startRootPath);
		textFieldPresentPath.setText(presentPath);
		jtree.setBounds(10, 60, 221, 601);
		contentPane.add(jtree);
		
		//jtable = new JTable();
		jtable = configureJTable(startRootPath);
		jtable.setBounds(241, 61, 923, 464);
		contentPane.add(jtable);
		
		JLabel lblBack = new JLabel("");
		lblBack.setBounds(10, 6, 45, 13);
		contentPane.add(lblBack);
		
		JLabel lblForward = new JLabel("");
		lblForward.setBounds(65, 6, 45, 13);
		contentPane.add(lblForward);
		
		/////////////////////////////////////////
		scrollPane = new ScrollPane();
		scrollPane.setBounds(10, 60, 221, 601);
		
		// Disable this, if you want to use design mode
		//scrollPane.add(jtree);
		contentPane.add(scrollPane);
		
		/////////////////////////////////////////
		jScrollPane = new JScrollPane(jtable);
		jScrollPane.setBounds(241, 61, 923, 464);
		jtable.setFillsViewportHeight(true);
		contentPane.add(jScrollPane);
		
		////////////////////////////////////////
		
		btnTypeLayout = new JButton("");
		btnTypeLayout.setBounds(1119, 6, 45, 45);
		btnTypeLayout.setIcon(createFormatImageIcon(resourcePath + "listicon.png", btnTypeLayout.getWidth(), btnTypeLayout.getHeight()));
		btnTypeLayout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (isListLayout == true) {
					isListLayout = false;
					btnTypeLayout.setIcon(createFormatImageIcon(resourcePath + "gridicon.png", btnTypeLayout.getWidth(), btnTypeLayout.getHeight()));
					System.out.println(stackOfBackPath.toString());
					System.out.println(stackOfForwardPath.toString());
				} else {
					isListLayout = true;
					btnTypeLayout.setIcon(createFormatImageIcon(resourcePath + "listicon.png", btnTypeLayout.getWidth(), btnTypeLayout.getHeight()));
					System.out.println(stackOfBackPath.toString());
					System.out.println(stackOfForwardPath.toString());
				}
			}
		});
		contentPane.add(btnTypeLayout);
		
		btnBack = new JButton("");
		btnBack.setBounds(10, 5, 33, 34);
		btnBack.setIcon(createFormatImageIcon(resourcePath + "backoff.png", btnTypeLayout.getWidth(), btnTypeLayout.getHeight()));
		btnBack.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (isBackCanClick == true) {
					
					
					if (!stackOfBackPath.empty()) {
						// n???u stack kh??ng r???ng
						
						// push element to stackOfForwardPath
						stackOfForwardPath.push(presentPath);
						//System.out.println("back say " + presentPath);
						// ??i???u ch???nh icon btForward sang tr???ng th??i on
						btnForward.setIcon(createFormatImageIcon(resourcePath + "forwardon.png", btnForward.getWidth(), btnForward.getHeight()));
						isForwardCanClick = true;
						
						
						// pop element out from stackOfBackPath, show to present textfield
						presentPath = stackOfBackPath.pop();
						textFieldPresentPath.setText(presentPath);
						
						if (stackOfBackPath.empty()) {
							isBackCanClick = false;
							btnBack.setIcon(createFormatImageIcon(resourcePath + "backoff.png", btnBack.getWidth(), btnBack.getHeight()));
						}
						
						repaintTable(presentPath);
						repaintTree(presentPath);
					} 
				}
			}
		});
		contentPane.add(btnBack);
		
		btnForward = new JButton("");
		btnForward.setBounds(44, 5, 33, 34);
		btnForward.setIcon(createFormatImageIcon(resourcePath + "forwardoff.png", btnTypeLayout.getWidth(), btnTypeLayout.getHeight()));
		btnForward.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (isForwardCanClick == true) {
					
					if (!stackOfForwardPath.empty()) {
						// n???u stack kh??ng r???ng
						// pop element out from stackOfBackPath, show to present textfield
						stackOfBackPath.push(presentPath);
						btnBack.setIcon(createFormatImageIcon(resourcePath + "backon.png", btnBack.getWidth(), btnBack.getHeight()));
						isBackCanClick = true;
							
						presentPath = stackOfForwardPath.pop();
						textFieldPresentPath.setText(presentPath);
						
						//System.out.println("forward say " + presentPath);
						if (stackOfForwardPath.empty()) {
							isForwardCanClick = false;
							//System.out.println("forward say " + presentPath);
							// ??i???u ch???nh icon btnForward sang tr???ng th??i off
							btnForward.setIcon(createFormatImageIcon(resourcePath + "forwardoff.png", btnForward.getWidth(), btnForward.getHeight()));
						}
						
						repaintTable(presentPath);
						repaintTree(presentPath);
						
					}
				}
			}
		});
		contentPane.add(btnForward);
		
		btnSearch = new JButton("");
		btnSearch.setBounds(1069, 6, 33, 33);
		btnSearch.setIcon(createFormatImageIcon(resourcePath + "searchicon.png", btnSearch.getWidth(), btnSearch.getHeight()));
		btnSearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String fileNameNeedToSearch = null;
				if (tempDirectoryPath == null) {
					fileNameNeedToSearch = startRootPath + textFieldSearchPath.getText().trim();
				} else {
					fileNameNeedToSearch = tempDirectoryPath + textFieldSearchPath.getText().trim();
				}
				File file = new File(fileNameNeedToSearch);
				
			}
		});
		contentPane.add(btnSearch);
		
		btnGoTo = new JButton("");
		btnGoTo.setBounds(830, 6, 49, 33);
		btnGoTo.setIcon(createFormatImageIcon(resourcePath + "goicon.png", btnGoTo.getWidth() - 10, btnGoTo.getHeight() - 10));
		btnGoTo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(!new File(textFieldPresentPath.getText().trim()).exists()) {
					System.out.println("???????ng d???n sai");
					return;
				}
				
				// c?? th??? s??? d???ng n??t n??y ????? open file, n???u ???????ng d???n ?????n m???t file.
				if (new File(textFieldPresentPath.getText().trim()).isFile()) {
					// n???u ???????ng d???n ng?????i d??ng nh???p v??o l?? m???t ???????ng d???n c???a file
					
					//textFieldPresentPath.setText(presentPath);
				} else {
					if (!textFieldPresentPath.getText().trim().equals(tempPath)) {
						// n???u ???????ng d???n ng?????i d??ng nh???p v??o kh??c v???i ???????ng d???n tr?????c ???? (l??u ?? ??? ????y ???????ng d???n tr?????c ???? l ???????ng d??n c???a th?? m???c)
						System.out.println("textFieldPresentPath: " + textFieldPresentPath.getText().trim());
						if (!stackOfBackPath.isEmpty()) {
							System.out.println(stackOfBackPath.peek());
						}
						
						if (stackOfBackPath.isEmpty()) {
							isBackCanClick = true;
							btnBack.setIcon(createFormatImageIcon(resourcePath+"backon.png", btnBack.getWidth(), btnBack.getHeight()));
							
							stackOfBackPath.push(startRootPath);
							presentPath = textFieldPresentPath.getText().trim();
							
							stackOfForwardPath.clear();
							isForwardCanClick = false;
							btnForward.setIcon(createFormatImageIcon(resourcePath+"forwardoff.png", btnForward.getWidth(), btnForward.getHeight()));
							
							repaintTable(textFieldPresentPath.getText().trim());

							// l??u ?? ch??? v??? l???i tree khi nh???p m???t ???????ng g???n mother kh??c ho??n to??n
							// n???u ch??? kh??c ??? ph???n con th?? v??? l???i tree v???i ???????ng d???n mother ban ?????u.
							repaintTree(textFieldPresentPath.getText().trim());
							
							// hi???n th??? ???????ng d???n th?? m???c
							textFieldPresentPath.setText(textFieldPresentPath.getText().trim());
							return;
						}
						
						stackOfForwardPath.clear();
						isForwardCanClick = false;
						btnForward.setIcon(createFormatImageIcon(resourcePath + "forwardoff.png", btnForward.getWidth(), btnForward.getHeight()));
						
						stackOfBackPath.push(tempPath);
						System.out.println("tempPath: " + tempPath);
						System.out.println(presentPath);
						tempPath          = new String(presentPath);
						tempDirectoryPath = new String(tempPath);
						presentPath       = textFieldPresentPath.getText().trim();
						
						repaintTable(textFieldPresentPath.getText().trim());
						
						// l??u ?? ch??? v??? l???i tree khi nh???p m???t ???????ng g???n mother kh??c ho??n to??n
						// n???u ch??? kh??c ??? ph???n con th?? v??? l???i tree v???i ???????ng d???n mother ban ?????u.
						repaintTree(textFieldPresentPath.getText().trim());
						
						// hi???n th??? ???????ng d???n th?? m???c
						textFieldPresentPath.setText(textFieldPresentPath.getText().trim());
					} else {
						System.out.println("go there");
						
					}
					
				}
				
			}
		});
		contentPane.add(btnGoTo);
		
		
		jtabbedPane = new JTabbedPane(JTabbedPane.TOP);
		jtabbedPane.setBounds(241, 531, 923, 130);
		
		jtabbedPane.add("Open", subJPanelOpenFunction);
		subJPanelOpenFunction.setLayout(null);
		setUpJPanelOpen();
		
		
		jtabbedPane.add("Edit", subJPanelEditFunction);
		subJPanelEditFunction.setLayout(null);
		
		jtabbedPane.add("Print", subJPanelPrintFunction);
		subJPanelPrintFunction.setLayout(null);
		
		jtabbedPane.add("New", subJPanelNewFunction);
		subJPanelNewFunction.setLayout(null);
		setUpJPanelNew();
		
		
		
		jtabbedPane.add("Copy", subJPanelCopyFunction);
		subJPanelCopyFunction.setLayout(null);
		setUpJPanelCopy();
		
		
		
		jtabbedPane.add("Cut", subJPanelCutFunction);
		subJPanelCutFunction.setLayout(null);
		setUpJPanelCut();
		
		
		
		jtabbedPane.add("Rename", subJPanelRenameFunction);
		subJPanelRenameFunction.setLayout(null);
		setUpJPanelRename();
		
		
		
		jtabbedPane.add("Delete", subJPanelDeleteFunction);
		subJPanelDeleteFunction.setLayout(null);
		setUpJPanelDelete();
		
		
		jtabbedPane.add("Compress", subJPanelCompressFunction);
		subJPanelCompressFunction.setLayout(null);
		setUpJPanelCompress();
		
		
		
		jtabbedPane.add("Extract", subJPanelExtractFunction);
		subJPanelExtractFunction.setLayout(null);
		setUpJPanelExtract();
			
	}
	
	// --Th??nh ph???n component cho subtab
	public void setUpJPanelOpen() {
		JLabel lblNewLabel = new JLabel("???????ng d???n hi???n t???i:");
		lblNewLabel.setBounds(10, 13, 128, 13);
		subJPanelOpenFunction.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Lo???i file");
		lblNewLabel_1.setBounds(10, 36, 45, 13);
		subJPanelOpenFunction.add(lblNewLabel_1);
		
		btnOpen = new JButton("OPEN");
		btnOpen.setBounds(10, 72, 85, 21);
		btnOpen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (textFieldSubJPanelOpenLinkPath.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(FileExplore.this, "???????ng d???n b??? tr???ng");
				} else {
					// open this file
					File file = new File(textFieldSubJPanelOpenLinkPath.getText().trim());
					if (file.exists()) {
						if(!Desktop.isDesktopSupported()){
				            System.out.println("Desktop is not supported");
				        }
				    	Desktop desktop = Desktop.getDesktop();
				        try {
							desktop.open(file);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {JOptionPane.showMessageDialog(FileExplore.this, "File kh??ng t???n t???i v???i ???????ng d???n ???? cho");}
				}
			}
		});
		subJPanelOpenFunction.add(btnOpen);
		
		lblSubJPanelOpenTypeFile = new JLabel("");
		lblSubJPanelOpenTypeFile.setBounds(150, 36, 223, 13);
		subJPanelOpenFunction.add(lblSubJPanelOpenTypeFile);
		
		textFieldSubJPanelOpenLinkPath = new JTextField();
		textFieldSubJPanelOpenLinkPath.setBounds(150, 10, 484, 19);
		subJPanelOpenFunction.add(textFieldSubJPanelOpenLinkPath);
		textFieldSubJPanelOpenLinkPath.setColumns(10);
		
		lblNewLabel_2 = new JLabel("H?????ng d???n:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(644, 5, 75, 24);
		subJPanelOpenFunction.add(lblNewLabel_2);
		
		lblNewLabel_3 = new JLabel("*Nh???n chu???t tr??i 2 l???n ????? m??? file");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_3.setBounds(663, 34, 223, 13);
		subJPanelOpenFunction.add(lblNewLabel_3);
		
		lblNewLabel_4 = new JLabel("ho???c v??o tab open ????? s??? d???ng");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_4.setBounds(673, 50, 223, 13);
		subJPanelOpenFunction.add(lblNewLabel_4);
		
		lblNewLabel_5 = new JLabel("ch???c n??ng open file.");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_5.setBounds(673, 66, 137, 13);
		subJPanelOpenFunction.add(lblNewLabel_5);
		
		btnGetPath = new JButton("Upload file n??y");
		btnGetPath.setBounds(115, 72, 148, 21);
		btnGetPath.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Save.chosingImagePath = textFieldSubJPanelOpenLinkPath.getText().trim();
				File file = new File(Save.chosingImagePath);
				
				//storeHouseActivity.textFieldAddUrl.setText(Save.chosingImagePath);
				//storeHouseActivity.textFieldProductURLToFix.setText(Save.chosingImagePath);
				
				// g???i th??ng tin v??? file.
				RequestSendMessenger requestSendMessenger = new RequestSendMessenger(clientGameplay.accountName, "???? g???i ?????n b???n file " + file.getName());
				DataPacket dataPacket = (DataPacket) requestSendMessenger;
				
				// g???i data c???a file.
				RequestSendFileData requestSendFileData = new RequestSendFileData(Save.chosingImagePath);
				DataPacket dataPacket2 = (DataPacket) requestSendFileData;
				
				clientGameplay.clientThread.doWrite(dataPacket);
				clientGameplay.clientThread.doWrite(dataPacket2);
				
				String string = "B???n ???? g???i m???t file: " + file.getName() + " \n";
				
				clientGameplay.logMessenger.append(string);
				
				clientGameplay.textPaneDisplayChat.setText(clientGameplay.logMessenger.toString());
				clientGameplay.textFieldInputMessenger.setText("");
				dispose();
			}
		});
		subJPanelOpenFunction.add(btnGetPath);
	}
	
	public void setUpJPanelEdit() {
		
	}
	
	public void setUpJPanelPrint() {
		
	}
	
	// 1 l?? l??u d???ng file, 0 l?? l??u d???ng folder
	int typeToNew = 1;
	public void setUpJPanelNew() {
		lblHngDn = new JLabel("H?????ng d???n:");
		lblHngDn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHngDn.setBounds(530, 0, 75, 23);
		subJPanelNewFunction.add(lblHngDn);
		
		lblCo = new JLabel("*Nh???n chu???t ph???i ch???n ch???c n??ng new");
		lblCo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCo.setBounds(614, 5, 278, 13);
		subJPanelNewFunction.add(lblCo);
		
		lblHocVoTab = new JLabel("ho???c v??o tab new ????? s??? d???ng ch???c n??ng new.");
		lblHocVoTab.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHocVoTab.setBounds(614, 28, 294, 13);
		subJPanelNewFunction.add(lblHocVoTab);
		
		lblNewLabel_6 = new JLabel("???????ng d???n n??i l??u:");
		lblNewLabel_6.setBounds(10, 7, 134, 13);
		subJPanelNewFunction.add(lblNewLabel_6);
		
		textFieldSubJPanelNewLinkPath = new JTextField();
		textFieldSubJPanelNewLinkPath.setText("V?? d???: C:\\Users\\Acer\\Desktop");
		textFieldSubJPanelNewLinkPath.setBounds(154, 4, 362, 19);
		subJPanelNewFunction.add(textFieldSubJPanelNewLinkPath);
		textFieldSubJPanelNewLinkPath.setColumns(10);
		
		lblNewLabel_7 = new JLabel("T???o File hay Folder");
		lblNewLabel_7.setBounds(10, 30, 122, 13);
		subJPanelNewFunction.add(lblNewLabel_7);
		
		btnButtonChoseNewAsFileOrDir = new JButton("L??u d???ng file");
		btnButtonChoseNewAsFileOrDir.setBounds(154, 26, 134, 21);
		btnButtonChoseNewAsFileOrDir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (typeToNew == 1) {
					typeToNew = 0;
					btnButtonChoseNewAsFileOrDir.setText("L??u d???ng folder");
				} else {
					typeToNew = 1;
					btnButtonChoseNewAsFileOrDir.setText("L??u ?????ng file");
				}
			}
		});
		subJPanelNewFunction.add(btnButtonChoseNewAsFileOrDir);
		
		lblNewLabel_8 = new JLabel("Lo???i File mu???n t???o");
		lblNewLabel_8.setBounds(10, 54, 122, 13);
		subJPanelNewFunction.add(lblNewLabel_8);
		
		textFieldSubJPanelNewFileType = new JTextField();
		textFieldSubJPanelNewFileType.setText("V?? d???: doc, txt        (N???u l??u folder, ph???n n??y c?? th??? ????? tr???ng)");
		textFieldSubJPanelNewFileType.setBounds(154, 51, 402, 19);
		subJPanelNewFunction.add(textFieldSubJPanelNewFileType);
		textFieldSubJPanelNewFileType.setColumns(10);
		
		lblNewLabel_9 = new JLabel("nh???n ????? ch???n l??u ki???u file hay folder");
		lblNewLabel_9.setBounds(296, 30, 260, 13);
		subJPanelNewFunction.add(lblNewLabel_9);
		
		lblNewLabel_10 = new JLabel("T??n l??u c???a file/folder");
		lblNewLabel_10.setBounds(10, 77, 142, 13);
		subJPanelNewFunction.add(lblNewLabel_10);
		
		textFieldSubJPanelNewName??f = new JTextField();
		textFieldSubJPanelNewName??f.setText("V?? d???: myword, ch??? ghi t??n (myword) kh??ng ghi ki???u file (.doc) ??? d??ng n??y");
		textFieldSubJPanelNewName??f.setBounds(154, 74, 402, 19);
		subJPanelNewFunction.add(textFieldSubJPanelNewName??f);
		textFieldSubJPanelNewName??f.setColumns(10);
		
		btnNew = new JButton("New");
		btnNew.setBounds(565, 72, 75, 23);
		btnNew.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String path = textFieldSubJPanelNewLinkPath.getText().trim() + File.separator;
				if (typeToNew == 1) {
					path += textFieldSubJPanelNewName??f.getText().trim() + "." + textFieldSubJPanelNewFileType.getText().trim();
					createFile(path);
				} else {
					path += textFieldSubJPanelNewName??f.getText().trim();
					createDir(path);
				}
				repaintTable(presentPath);
				repaintTree(presentPath);
				System.out.println("path: " + path);
				
			}
		});
		subJPanelNewFunction.add(btnNew);
	}
	
	public void setUpJPanelCopy() {
		JLabel lblNewLabel_6_1 = new JLabel("???????ng d???n File/Folder g???c:");
		lblNewLabel_6_1.setBounds(10, 10, 176, 13);
		subJPanelCopyFunction.add(lblNewLabel_6_1);
		
		textFieldSubJPanelCopyOriginLinkPath = new JTextField();
		textFieldSubJPanelCopyOriginLinkPath.setText("V?? d???: C:\\Users\\Acer\\Desktop\\MyWord.doc");
		textFieldSubJPanelCopyOriginLinkPath.setColumns(10);
		textFieldSubJPanelCopyOriginLinkPath.setBounds(297, 7, 362, 19);
		subJPanelCopyFunction.add(textFieldSubJPanelCopyOriginLinkPath);
		
		JLabel lblNewLabel_6_1_1 = new JLabel("???????ng d???n folder n??i l??u File/Folder b???n copy:");
		lblNewLabel_6_1_1.setBounds(10, 29, 277, 13);
		subJPanelCopyFunction.add(lblNewLabel_6_1_1);
		
		textFieldSubJPanelCopyNewLinkPath = new JTextField();
		textFieldSubJPanelCopyNewLinkPath.setText("V?? d???: C:\\Users\\Acer\\Desktop\\MyWordFolder");
		textFieldSubJPanelCopyNewLinkPath.setColumns(10);
		textFieldSubJPanelCopyNewLinkPath.setBounds(297, 26, 362, 19);
		subJPanelCopyFunction.add(textFieldSubJPanelCopyNewLinkPath);
		
		btnCopy = new JButton("Copy");
		btnCopy.setBounds(10, 55, 85, 21);
		btnCopy.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String originLinkPath = textFieldSubJPanelCopyOriginLinkPath.getText().trim();
				String newLinkPath    = textFieldSubJPanelCopyNewLinkPath.getText().trim();
				
				String resultOriginLink = getExtension(originLinkPath);
				String resultNewLink = getExtension(newLinkPath);
				
				if (new File(originLinkPath).exists()) {
					if (new File(originLinkPath).isFile()) {
						// n???u b???n g???c l?? file, ki???m tra ti???p ???????ng d???n ??? b???n sao.
						if (new File(newLinkPath).exists() && new File(newLinkPath).isFile()) {
							copyFile(originLinkPath, newLinkPath);
							repaintTable(presentPath);
							repaintTree(presentPath);
						} else if (new File(newLinkPath).exists() && new File(newLinkPath).isDirectory()) {
							if (deleteLastSymbol(newLinkPath, File.separator) != "-1") {
								newLinkPath = new String(deleteLastSymbol(newLinkPath, File.separator));
							}
							newLinkPath += File.separator + "New " + new File(originLinkPath).getName();
							System.out.println("newLinkPath: " + newLinkPath);
							copyFile(originLinkPath, newLinkPath);
							repaintTable(presentPath);
							repaintTree(presentPath);
						} else {
							System.out.println("???????ng d???n b???n sao kh??ng h???p l??? ho???c kh??ng ????ng");
							JOptionPane.showMessageDialog(FileExplore.this, "???????ng d???n b???n sao kh??ng h???p l??? ho???c kh??ng ????ng");
						}
					} else {
						// n???u b???n g???c l?? folder, ki???m tra ti???p ???????ng d???n ??? b???n sao.
						if (new File(newLinkPath).exists() && new File(newLinkPath).isFile()) {
							System.out.println("b???n g???c l?? folder, b???n sao kh??ng th??? l?? d???ng file");
							JOptionPane.showMessageDialog(FileExplore.this, "b???n g???c l?? folder, b???n sao kh??ng th??? l?? d???ng file");
						}
						else if (new File(newLinkPath).exists() && new File(newLinkPath).isDirectory()) {
							// folder b???n sao t???n t???i,v???y c?? ngh??a ng?????i d??ng mu???n
							// sao ch??p folder b???n sao t???i ????y, v???i t??? kh??a ph??n bi???t l?? new.
							System.out.println("th??ch th?? t??n v???i t??? new");
							newLinkPath += File.separator + "New " + new File(originLinkPath).getName();
							System.out.println("newLinkPath: " + newLinkPath);
							try {
								copyDir(originLinkPath, newLinkPath);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							repaintTable(presentPath);
							repaintTree(presentPath);
						}
						else if (new File(newLinkPath).exists() == false && new File(new File(newLinkPath).getParent()).isDirectory()) {
							// folder kh??ng t???n t???i, nh??ng ???????ng d???n cha l???i t???n t???i, 
							// ch???ng t??? ????y l?? t??n m?? ng?????i d??ng mu???n ?????t cho folder b???n sao.
							System.out.println("ok c?? r???i");
							System.out.println("newLinkPath: " + newLinkPath);
							try {
								copyDir(originLinkPath, newLinkPath);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							repaintTable(presentPath);
							repaintTree(presentPath);
						} else {
							System.out.println("???????ng d???n b???n sao kh??ng t???n t???i ho???c kh??ng h???p l???");
							JOptionPane.showMessageDialog(FileExplore.this, "???????ng d???n b???n sao kh??ng t???n t???i ho???c kh??ng h???p l???");
						}
					}
				} else {
					System.out.println("???????ng d???n b???n g???c kh??ng t???n t???i ho???c kh??ng h???p l???");
					JOptionPane.showMessageDialog(FileExplore.this, "???????ng d???n b???n g???c kh??ng t???n t???i ho???c kh??ng h???p l???");
				}

			}
		});
		subJPanelCopyFunction.add(btnCopy);
		
		JLabel lblNewLabel_2_1 = new JLabel("H?????ng d???n:");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2_1.setBounds(752, 23, 75, 24);
		subJPanelCopyFunction.add(lblNewLabel_2_1);
		
		JLabel lblnhnChutPhi = new JLabel("*Nh???n chu???t ph???i ch???n ch???c n??ng copy");
		lblnhnChutPhi.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblnhnChutPhi.setBounds(614, 57, 278, 13);
		subJPanelCopyFunction.add(lblnhnChutPhi);
		
		JLabel lblHocVoTab_4 = new JLabel("ho???c v??o tab new ????? s??? d???ng ch???c n??ng copy");
		lblHocVoTab_4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHocVoTab_4.setBounds(614, 80, 294, 13);
		subJPanelCopyFunction.add(lblHocVoTab_4);
	}
	
	public void setUpJPanelCut() {
		JLabel lblNewLabel_6_1 = new JLabel("???????ng d???n File/Folder g???c:");
		lblNewLabel_6_1.setBounds(10, 10, 176, 13);
		subJPanelCutFunction.add(lblNewLabel_6_1);
		
		JLabel lblNewLabel_6_1_1 = new JLabel("???????ng d???n folder n??i l??u File/Folder b???n cut");
		lblNewLabel_6_1_1.setBounds(10, 27, 277, 13);
		subJPanelCutFunction.add(lblNewLabel_6_1_1);
		
		textFieldSubJPanelCutOriginLinkPath = new JTextField();
		textFieldSubJPanelCutOriginLinkPath.setText("V?? d???: C:\\Users\\Acer\\Desktop\\MyWord.doc");
		textFieldSubJPanelCutOriginLinkPath.setColumns(10);
		textFieldSubJPanelCutOriginLinkPath.setBounds(294, 7, 362, 19);
		subJPanelCutFunction.add(textFieldSubJPanelCutOriginLinkPath);
		
		textFieldSubJPanelCutNewLinkPath = new JTextField();
		textFieldSubJPanelCutNewLinkPath.setText("V?? d???: C:\\Users\\Acer\\Desktop\\MyWordFolder");
		textFieldSubJPanelCutNewLinkPath.setColumns(10);
		textFieldSubJPanelCutNewLinkPath.setBounds(294, 24, 362, 19);
		subJPanelCutFunction.add(textFieldSubJPanelCutNewLinkPath);
		
		btnCut = new JButton("Cut");
		btnCut.setBounds(10, 56, 85, 21);
		btnCut.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String originLinkPath = textFieldSubJPanelCutOriginLinkPath.getText().trim();
				String newLinkPath    = textFieldSubJPanelCutNewLinkPath.getText().trim();
				
				String resultOriginLink = getExtension(originLinkPath);
				String resultNewLink = getExtension(newLinkPath);
				
				if (new File(originLinkPath).exists()) {
					if (new File(originLinkPath).isFile()) {
						// n???u b???n g???c l?? file, ki???m tra ti???p ???????ng d???n ??? b???n sao.
						if (new File(newLinkPath).exists() && new File(newLinkPath).isFile()) {
							copyFile(originLinkPath, newLinkPath);
							deleteFile(originLinkPath);
							repaintTable(presentPath);
							repaintTree(presentPath);
						} else if (new File(newLinkPath).exists() && new File(newLinkPath).isDirectory()) {
							if (deleteLastSymbol(newLinkPath, File.separator) != "-1") {
								newLinkPath = new String(deleteLastSymbol(newLinkPath, File.separator));
							}
							newLinkPath += File.separator + "New " + new File(originLinkPath).getName();
							System.out.println("newLinkPath: " + newLinkPath);
							copyFile(originLinkPath, newLinkPath);
							deleteFile(originLinkPath);
							repaintTable(presentPath);
							repaintTree(presentPath);
						} else {
							System.out.println("???????ng d???n b???n Cut kh??ng h???p l??? ho???c kh??ng ????ng");
							JOptionPane.showMessageDialog(FileExplore.this, "???????ng d???n b???n Cut kh??ng h???p l??? ho???c kh??ng ????ng");
						}
					} else {
						// n???u b???n g???c l?? folder, ki???m tra ti???p ???????ng d???n ??? b???n sao.
						if (new File(newLinkPath).exists() && new File(newLinkPath).isFile()) {
							System.out.println("b???n g???c l?? folder, b???n Cut kh??ng th??? l?? d???ng file");
							JOptionPane.showMessageDialog(FileExplore.this, "b???n g???c l?? folder, b???n Cut kh??ng th??? l?? d???ng file");
						}
						else if (new File(newLinkPath).exists() && new File(newLinkPath).isDirectory()) {
							// folder b???n sao t???n t???i,v???y c?? ngh??a ng?????i d??ng mu???n
							// sao ch??p folder b???n sao t???i ????y, v???i t??? kh??a ph??n bi???t l?? new.
							System.out.println("th??ch th?? t??n v???i t??? new");
							newLinkPath += File.separator + "New " + new File(originLinkPath).getName();
							System.out.println("newLinkPath: " + newLinkPath);
							try {
								copyDir(originLinkPath, newLinkPath);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							deleteDir(originLinkPath);
							repaintTable(presentPath);
							repaintTree(presentPath);
						}
						else if (new File(newLinkPath).exists() == false && new File(new File(newLinkPath).getParent()).isDirectory()) {
							// folder kh??ng t???n t???i, nh??ng ???????ng d???n cha l???i t???n t???i, 
							// ch???ng t??? ????y l?? t??n m?? ng?????i d??ng mu???n ?????t cho folder b???n sao.
							System.out.println("ok c?? r???i");
							System.out.println("newLinkPath: " + newLinkPath);
							try {
								copyDir(originLinkPath, newLinkPath);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							deleteDir(originLinkPath);
							repaintTable(presentPath);
							repaintTree(presentPath);
						} else {
							System.out.println("???????ng d???n b???n Cut kh??ng t???n t???i ho???c kh??ng h???p l???");
							JOptionPane.showMessageDialog(FileExplore.this, "???????ng d???n b???n Cut kh??ng t???n t???i ho???c kh??ng h???p l???");
						}
					}
				} else {
					System.out.println("???????ng d???n b???n Cut kh??ng t???n t???i ho???c kh??ng h???p l???");
					JOptionPane.showMessageDialog(FileExplore.this, "???????ng d???n b???n Cut kh??ng t???n t???i ho???c kh??ng h???p l???");
				}
			}
		});
		subJPanelCutFunction.add(btnCut);
		
		JLabel lblNewLabel_2_1 = new JLabel("H?????ng d???n:");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2_1.setBounds(744, 27, 75, 24);
		subJPanelCutFunction.add(lblNewLabel_2_1);
		
		JLabel lblnhnChutPhi_1 = new JLabel("*Nh???n chu???t ph???i ch???n ch???c n??ng cut");
		lblnhnChutPhi_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblnhnChutPhi_1.setBounds(630, 53, 278, 13);
		subJPanelCutFunction.add(lblnhnChutPhi_1);
		
		JLabel lblHocVoTab_4 = new JLabel("ho???c v??o tab new ????? s??? d???ng ch???c n??ng cut");
		lblHocVoTab_4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHocVoTab_4.setBounds(614, 76, 294, 13);
		subJPanelCutFunction.add(lblHocVoTab_4);
	}
	
	// 1 l?? file c??n ?????i t??n l?? d???ng file, 0 l?? file c???n ?????i t??n l?? d???ng folder
	int typeToRename = 1;
	public void setUpJPanelRename() {
		JLabel lblNewLabel_11 = new JLabel("???????ng d???n:");
		lblNewLabel_11.setBounds(10, 10, 152, 13);
		subJPanelRenameFunction.add(lblNewLabel_11);
		
		textFieldSubJPanelRenameLinkPath = new JTextField();
		textFieldSubJPanelRenameLinkPath.setText("V?? d???: C:\\Users\\Acer\\Desktop");
		textFieldSubJPanelRenameLinkPath.setBounds(172, 7, 335, 19);
		subJPanelRenameFunction.add(textFieldSubJPanelRenameLinkPath);
		textFieldSubJPanelRenameLinkPath.setColumns(10);
		
		JLabel lblNewLabel_12 = new JLabel("Lo???i file:");
		lblNewLabel_12.setBounds(10, 33, 152, 13);
		subJPanelRenameFunction.add(lblNewLabel_12);
		
		lblSubJPanelRenameTypeFile = new JLabel("");
		lblSubJPanelRenameTypeFile.setBounds(172, 33, 335, 13);
		subJPanelRenameFunction.add(lblSubJPanelRenameTypeFile);
		
		lblNewLabel_14 = new JLabel("T??n m???i c???a File/Folder:");
		lblNewLabel_14.setBounds(10, 56, 152, 13);
		subJPanelRenameFunction.add(lblNewLabel_14);
		
		textFieldSubJPanelRenameNewName = new JTextField();
		textFieldSubJPanelRenameNewName.setBounds(172, 53, 335, 19);
		subJPanelRenameFunction.add(textFieldSubJPanelRenameNewName);
		textFieldSubJPanelRenameNewName.setColumns(10);
		
		lblHngDn_1 = new JLabel("H?????ng d???n:");
		lblHngDn_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHngDn_1.setBounds(535, 3, 75, 23);
		subJPanelRenameFunction.add(lblHngDn_1);
		
		lblCo_1 = new JLabel("*Nh???n chu???t ph???i ch???n ch???c n??ng rename");
		lblCo_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCo_1.setBounds(573, 31, 278, 13);
		subJPanelRenameFunction.add(lblCo_1);
		
		lblHocVoTab_2 = new JLabel("ho???c v??o tab rename ????? s??? d???ng ch???c n??ng rename.");
		lblHocVoTab_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHocVoTab_2.setBounds(583, 54, 335, 13);
		subJPanelRenameFunction.add(lblHocVoTab_2);
		
		btnRename = new JButton("Rename");
		btnRename.setBounds(404, 84, 103, 19);
		btnRename.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String path = textFieldSubJPanelRenameLinkPath.getText().trim();
				File file = new File(path);
				if (file.exists()) {
					if (file.isFile()) {
						String extension = "";

	            		int i = file.getName().lastIndexOf('.');
	            		if (i > 0) {
	            		    extension = file.getName().substring(i+1);
	            		}
	            		
	            		String pathOfFileAfterRename = file.getParent() + File.separator + textFieldSubJPanelRenameNewName.getText().trim() +"."+ extension;
	            		rename(pathOfFileAfterRename);
					} else {
						String pathOfFileAfterRename = file.getParent() + File.separator + textFieldSubJPanelRenameNewName.getText().trim();
						rename(pathOfFileAfterRename);
					}
				}
				
			}
		});
		subJPanelRenameFunction.add(btnRename);
	}
	
	public void setUpJPanelDelete() {
		JLabel lblNewLabel_11 = new JLabel("???????ng d???n:");
		lblNewLabel_11.setBounds(10, 10, 152, 13);
		subJPanelDeleteFunction.add(lblNewLabel_11);
		
		textFieldSubJPanelDeleteLinkPath = new JTextField();
		textFieldSubJPanelDeleteLinkPath.setText("V?? d???: C:\\Users\\Acer\\Desktop");
		textFieldSubJPanelDeleteLinkPath.setBounds(176, 7, 346, 19);
		subJPanelDeleteFunction.add(textFieldSubJPanelDeleteLinkPath);
		textFieldSubJPanelDeleteLinkPath.setColumns(10);
		
		JLabel lblNewLabel_12 = new JLabel("Lo???i file:");
		lblNewLabel_12.setBounds(10, 33, 152, 13);
		subJPanelDeleteFunction.add(lblNewLabel_12);
		
		lblSubJPanelDeleteTypeFile = new JLabel("");
		lblSubJPanelDeleteTypeFile.setBounds(176, 36, 346, 13);
		subJPanelDeleteFunction.add(lblSubJPanelDeleteTypeFile);
		
		btnDelete = new JButton("Delete");
		btnDelete.setBounds(10, 56, 118, 37);
		btnDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (new File(textFieldSubJPanelDeleteLinkPath.getText().trim()).isFile()) {
					deleteFile(textFieldSubJPanelDeleteLinkPath.getText().trim());
					textFieldSubJPanelDeleteLinkPath.setText("");
					repaintTable(presentPath);
					repaintTree(presentPath);
				} else {
					deleteDir(textFieldSubJPanelDeleteLinkPath.getText().trim());
					textFieldSubJPanelDeleteLinkPath.setText("");
					repaintTable(presentPath);
					repaintTree(presentPath);
				}
			}
		});
		subJPanelDeleteFunction.add(btnDelete);
		
		lblHngDn_2 = new JLabel("H?????ng d???n:");
		lblHngDn_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHngDn_2.setBounds(545, 7, 75, 23);
		subJPanelDeleteFunction.add(lblHngDn_2);
		
		lblCo_2 = new JLabel("*Nh???n chu???t ph???i ch???n ch???c n??ng delete");
		lblCo_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCo_2.setBounds(555, 33, 278, 13);
		subJPanelDeleteFunction.add(lblCo_2);
		
		lblHocVoTab_1 = new JLabel("ho???c v??o tab delete ????? s??? d???ng ch???c n??ng delete.");
		lblHocVoTab_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHocVoTab_1.setBounds(565, 50, 335, 13);
		subJPanelDeleteFunction.add(lblHocVoTab_1);
	}
	
	int typeOfCompress = 0;
	public void setUpJPanelCompress() {
		
		ButtonGroup group = new ButtonGroup();
		
		final JRadioButton rdbtnZipCompress = new JRadioButton("Zip");
		rdbtnZipCompress.setBounds(6, 59, 57, 21);
		subJPanelCompressFunction.add(rdbtnZipCompress);
		
		final JRadioButton rdbtnJarCompress = new JRadioButton("Jar");
		rdbtnJarCompress.setBounds(65, 59, 51, 21);
		subJPanelCompressFunction.add(rdbtnJarCompress);
		
		final JRadioButton rdbtn7ZipCompress = new JRadioButton("7Zip");
		rdbtn7ZipCompress.setBounds(118, 59, 57, 21);
		subJPanelCompressFunction.add(rdbtn7ZipCompress);
		
		final JRadioButton rdbtnRarCompress = new JRadioButton("Rar");
		rdbtnRarCompress.setBounds(177, 59, 57, 21);
		subJPanelCompressFunction.add(rdbtnRarCompress);
		
		group.add(rdbtnZipCompress);
		group.add(rdbtnJarCompress);
		group.add(rdbtn7ZipCompress);
		group.add(rdbtnRarCompress);
		
		ActionListener actionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (e.getSource() == rdbtnZipCompress) {
                    typeOfCompress = 1;
                }
                if (e.getSource() == rdbtnJarCompress) {
                	typeOfCompress = 2;
                }
                if (e.getSource() == rdbtn7ZipCompress) {
                	typeOfCompress = 3;
                }
                if (e.getSource() == rdbtnRarCompress) {
                    typeOfCompress = 4;
                }
			}
		};
		
		rdbtnZipCompress.addActionListener(actionListener);
		rdbtnJarCompress.addActionListener(actionListener);
		rdbtn7ZipCompress.addActionListener(actionListener);
		rdbtnRarCompress.addActionListener(actionListener);
		
		
		JLabel lblngDnFilefolder = new JLabel("???????ng d???n File/Folder:");
		lblngDnFilefolder.setBounds(6, 10, 154, 13);
		subJPanelCompressFunction.add(lblngDnFilefolder);
		
		textFieldSubJPanelCompressLinkPath = new JTextField();
		textFieldSubJPanelCompressLinkPath.setText("V?? d???: C:\\Users\\Acer\\Desktop\\MyWord.doc");
		textFieldSubJPanelCompressLinkPath.setColumns(10);
		textFieldSubJPanelCompressLinkPath.setBounds(170, 7, 449, 19);
		subJPanelCompressFunction.add(textFieldSubJPanelCompressLinkPath);
		
		JLabel lblnhDngMun = new JLabel("?????nh d???ng mu???n n??n l??:   (File n??n s??? ???????c l??u v??o c??ng th?? m???c v???i File/Folder ???????c n??n )");
		lblnhDngMun.setBounds(6, 40, 613, 13);
		subJPanelCompressFunction.add(lblnhDngMun);
		
		btnCompress = new JButton("N??n");
		btnCompress.setBounds(253, 59, 85, 21);
		btnCompress.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String path = textFieldSubJPanelCompressLinkPath.getText().trim();
				switch (typeOfCompress) {
				case 0:
					JOptionPane.showMessageDialog(FileExplore.this, "Ch??a ch???n ?????nh d???ng n??n");
					break;
				case 1:
					if (new File(path).exists()) {
						if (new File(path).isFile()) {
							// zip
							try {
								compressFileToZip(path);
								
								repaintTable(presentPath);
								repaintTree(presentPath);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							try {
								compressDirToZip(path);

								repaintTable(presentPath);
								repaintTree(presentPath);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else {JOptionPane.showMessageDialog(FileExplore.this, "File/Folder kh??ng t???n t???i, ho???c ???????ng d???n kh??ng ch??nh x??c");}
					break;
				case 2:
					if (new File(path).exists()) {
						if (new File(path).isFile()) {
							
						} else {
							
						}
					} else {JOptionPane.showMessageDialog(FileExplore.this, "File/Folder kh??ng t???n t???i, ho???c ???????ng d???n kh??ng ch??nh x??c");}
					break;
				case 3:
					if (new File(path).exists()) {
						if (new File(path).isFile()) {
							
						} else {
							
						}
					} else {JOptionPane.showMessageDialog(FileExplore.this, "File/Folder kh??ng t???n t???i, ho???c ???????ng d???n kh??ng ch??nh x??c");}
					break;
				case 4:
					if (new File(path).exists()) {
//						if (new File(path).isFile()) {
//							
//						} else {
//							
//						}
						try {
							compressFileToRAR(path);
						} catch (Exception e) {
							e.getStackTrace();
						}
						
						repaintTable(presentPath);
						repaintTree(presentPath);
						
					} else {JOptionPane.showMessageDialog(FileExplore.this, "File/Folder kh??ng t???n t???i, ho???c ???????ng d???n kh??ng ch??nh x??c");}
					break;
				}
			}
		});
		subJPanelCompressFunction.add(btnCompress);
	}
	
	public void setUpJPanelExtract() {
		JLabel lblngDnFilefolder = new JLabel("???????ng d???n File/Folder:");
		lblngDnFilefolder.setBounds(10, 10, 154, 13);
		subJPanelExtractFunction.add(lblngDnFilefolder);
		
		textFieldSubJPanelExtractLinkPath = new JTextField();
		textFieldSubJPanelExtractLinkPath.setText("V?? d???: C:\\Users\\Acer\\Desktop\\MyWord.doc");
		textFieldSubJPanelExtractLinkPath.setColumns(10);
		textFieldSubJPanelExtractLinkPath.setBounds(173, 7, 362, 19);
		subJPanelExtractFunction.add(textFieldSubJPanelExtractLinkPath);
		
		JLabel lblnhDngMun = new JLabel("File/Folder sau khi gi???i n??n s??? ???????c l??u v??o c??ng th?? m???c v???i File/Folder ???????c gi???i n??n");
		lblnhDngMun.setBounds(10, 52, 580, 13);
		subJPanelExtractFunction.add(lblnhDngMun);
		
		JLabel lblNewLabel_12 = new JLabel("Lo???i file:");
		lblNewLabel_12.setBounds(10, 29, 152, 13);
		subJPanelExtractFunction.add(lblNewLabel_12);
		
		lblSubJPanelExtractTypeFile = new JLabel("");
		lblSubJPanelExtractTypeFile.setBounds(173, 29, 335, 13);
		subJPanelExtractFunction.add(lblSubJPanelExtractTypeFile);
		
		btnExtract = new JButton("Gi???i n??n");
		btnExtract.setBounds(10, 72, 104, 21);
		btnExtract.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					String extension = getExtension(textFieldSubJPanelExtractLinkPath.getText().trim());
					
//					if (extension.equals("-1") || extension.equals("zip") == false || extension.equals("jar") == false || extension.equals("7zip") == false || extension.equals("rar") == false) {
//						JOptionPane.showMessageDialog(MainActivity.this, "Kh??ng ph???i d???ng file n??n, ho???c ???????ng d???n kh??ng ch??nh x??c");
//					}
					
					if (extension.trim().equals("zip")) {
						if (new File(textFieldSubJPanelExtractLinkPath.getText().trim()).exists()) {
							Path source = Paths.get(textFieldSubJPanelExtractLinkPath.getText().trim());
							
							File file = new File(textFieldSubJPanelExtractLinkPath.getText().trim());
					        Path target = Paths.get(file.getParent());
					        
					        //System.out.println("asdasdaasd: " + file.getParentFile() + File.separator + file.getName());
							unzipFolder(source, target);
							
							
							repaintTable(presentPath);
							repaintTree(presentPath);
						} else {JOptionPane.showMessageDialog(FileExplore.this, "Kh??ng t???n t???i file n??n zip nh?? v???y, ki???m tra l???i ???????ng d???n");}
					}
					
					if (extension.trim().equals("jar")) {
						if (new File(textFieldSubJPanelExtractLinkPath.getText().trim()).exists()) {
							
							repaintTable(presentPath);
							repaintTree(presentPath);
						} else {JOptionPane.showMessageDialog(FileExplore.this, "Kh??ng t???n t???i file n??n jar nh?? v???y, ki???m tra l???i ???????ng d???n");}
					}
					
					if (extension.trim().equals("7z")) {
						if (new File(textFieldSubJPanelExtractLinkPath.getText().trim()).exists()) {
							extract7Zip(textFieldSubJPanelExtractLinkPath.getText().trim());
							
							repaintTable(presentPath);
							repaintTree(presentPath);
						} else {JOptionPane.showMessageDialog(FileExplore.this, "Kh??ng t???n t???i file n??n 7zip nh?? v???y, ki???m tra l???i ???????ng d???n");}
					}
					
					if (extension.trim().equals("rar")) {
						if (new File(textFieldSubJPanelExtractLinkPath.getText().trim()).exists()) {
							if (new File(textFieldSubJPanelExtractLinkPath.getText().trim()).exists()) {
								extractRAR(textFieldSubJPanelExtractLinkPath.getText().trim());
								
								repaintTable(presentPath);
								repaintTree(presentPath);
							} else {
								JOptionPane.showMessageDialog(FileExplore.this, "Kh??ng t???n t???i file n??n rar nh?? v???y, ki???m tra l???i ???????ng d???n");
							}
						} else {JOptionPane.showMessageDialog(FileExplore.this, "Kh??ng t???n t???i file n??n rar nh?? v???y, ki???m tra l???i ???????ng d???n");}
					}
					
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
		});
		subJPanelExtractFunction.add(btnExtract);
		contentPane.add(jtabbedPane);
	}
	
	
	// --------------------------------------------------
	// c???p nh???t l???i Tree m???i khi c?? thay ?????i nh?? th??m, x??a file, th?? m???c
	
	public void repaintTree(String path) {
		try {
			scrollPane.remove(jtree);
			contentPane.remove(jtree);
			contentPane.remove(scrollPane);
			//fileRoot = null;
			// ?????i t?????ng file g???c
			//fileRoot = new File(startRootPath);
			// t???o ra root mother
			jtree = configureJTree(path);
			//presentPath = new String(startRootPath);
			//textFieldPresentPath.setText(presentPath);
			jtree.setBounds(10, 60, 221, 601);
			contentPane.add(jtree);
			
			scrollPane = new ScrollPane();
			scrollPane.setBounds(10, 60, 221, 601);
			
			// disable this, if you want to use design mode
			
			contentPane.add(scrollPane);
			scrollPane.add(jtree);
			scrollPane.repaint();
			contentPane.repaint();
		} catch (Exception e) {
			e.getStackTrace();
		}
		
	}
	
	// c???p nh???t l???i Table m???i khi c?? thay ?????i nh?? th??m, x??a file, th?? m???c
	// tham s??? path ??? ????u l?? ????? l???y ???????ng d???n hi???n t???i, ph???c v??? cho vi???c li???t k?? l???i file, nh???m update
	
	public void repaintTable(String path) {
		try {
			contentPane.remove(jScrollPane);
			fileViewModelList.clear();
			jtable = null;
			jScrollPane = null;
			
//			fileViewModelList = getFileViewModel(path);
//			CustomFileTableModel customFileTableModel = new CustomFileTableModel(fileViewModelList);
//			jtable = new JTable(customFileTableModel);
//			jtable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
			
			jtable = configureJTable(path);
			
			jScrollPane = new JScrollPane(jtable);
			jScrollPane.setBounds(241, 61, 923, 464);
			jtable.setFillsViewportHeight(true);
			contentPane.add(jScrollPane);
			contentPane.repaint();
		} catch (Exception e) {
			e.getStackTrace();
		}
		
	}
	// --------------------------------------------------------------------
	public ArrayList<FileViewModel> fileViewModelList = new ArrayList<FileViewModel>();
	
	public JTable configureJTable(String path) {
		//DefaultTableModel defaultTableModel = new DefaultTableModel();
		
		fileViewModelList = getFileViewModel(path);
		CustomFileTableModel customFileTableModel = new CustomFileTableModel(fileViewModelList);
		//JTable jtable = new JTable(data, header);
		final JTable jtable = new JTable(customFileTableModel);
		jtable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
		jtable.setRowHeight(15);
		
		int[] columnsWidth = {
	            20, 200, 400, 75, 75, 75
	    };
		for (int i = 0; i < jtable.getColumnCount(); i++) {
			TableColumn tableColumn = jtable.getColumnModel().getColumn(i);
			tableColumn.setWidth(columnsWidth[i]);
			tableColumn.setPreferredWidth(columnsWidth[i]);
		}
		
		jtable.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				// TODO Auto-generated method stub
				
				// nh???p chu???t tr??i 2 l???n
				if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseEvent.BUTTON1) {
				    System.out.println("double left clicked");
				    int columnNumber = jtable.getSelectedColumn();
				    int rowNumber = jtable.getSelectedRow();
				    
				    FileViewModel fileViewModel = fileViewModelList.get(rowNumber);
				    System.out.println("path: " + fileViewModel.getPath());
				    
				    doStackJTableStuff(fileViewModel);
				}
				
				// nh???n chu???t tr??i 1 l???n
				if (mouseEvent.getClickCount() == 1 && mouseEvent.getButton() == MouseEvent.BUTTON1) {
					System.out.println("one left clicked");
					int columnNumber = jtable.getSelectedColumn();
				    int rowNumber = jtable.getSelectedRow();
				    
				    FileViewModel fileViewModel = fileViewModelList.get(rowNumber);
				    System.out.println("path: " + fileViewModel.getPath());
				    
				    if (new File(fileViewModel.getPath()).isFile()) {
				    	textFieldSubJPanelOpenLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelRenameLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelDeleteLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelCopyOriginLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelCutOriginLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelCompressLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelExtractLinkPath.setText(fileViewModel.getPath());
				    	String extension = "";
	            		int i = fileViewModel.getFile().getName().lastIndexOf('.');
	            		if (i > 0) {
	            		    extension = fileViewModel.getFile().getName().substring(i+1);
	            		}
					    lblSubJPanelOpenTypeFile.setText("File " + extension);
					    lblSubJPanelRenameTypeFile.setText("File " + extension);
					    lblSubJPanelDeleteTypeFile.setText("File " + extension);
					    lblSubJPanelExtractTypeFile.setText("File " + extension);
					    
					    
					    
				    } else {
				    	// n???u l?? folder
				    	textFieldSubJPanelRenameLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelDeleteLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelCopyOriginLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelCutOriginLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelCompressLinkPath.setText(fileViewModel.getPath());
				    	textFieldSubJPanelExtractLinkPath.setText(fileViewModel.getPath());
				    	
				    	lblSubJPanelRenameTypeFile.setText("????y l?? Folder");
				    	lblSubJPanelDeleteTypeFile.setText("????y l?? folder");
				    	lblSubJPanelExtractTypeFile.setText("????y l?? Folder");
				    }
				}  
				
				// nh???n chu???t ph???i 1 l???n, hi???n popup
				if (mouseEvent.getClickCount() == 1 && mouseEvent.getButton() == MouseEvent.BUTTON3) {
				    System.out.println("one right clicked");
				}
			}
		});
		return jtable;
	}
	
	public void doStackJTableStuff(FileViewModel fileViewModel) {
		File file = new File(fileViewModel.getPath()) ;
	    if (file.isDirectory()) {
	    	
	    	if (stackOfBackPath.empty()) {
	    		// tr?????ng h???p ch????ng tr??nh m???i kh???i ch???y
	    		isBackCanClick = true;
	    		btnBack.setIcon(createFormatImageIcon(resourcePath+"backon.png", btnBack.getWidth() , btnBack.getHeight()));
	    		
	    		stackOfBackPath.push(startRootPath);
	    		presentPath = fileViewModel.getPath();
	    		textFieldPresentPath.setText(fileViewModel.getPath().trim());
	    		
	    		repaintTable(fileViewModel.getPath());
				repaintTree(fileViewModel.getPath());
	    	} else {
	    		System.out.println("presentPath: " + presentPath);
	    		stackOfBackPath.push(presentPath);
	    		presentPath = fileViewModel.getPath();
	    		textFieldPresentPath.setText(fileViewModel.getPath().trim());
	    		
	    		repaintTable(fileViewModel.getPath());
	    		repaintTree(fileViewModel.getPath());
	    	}
	    	
	    }
	    
	    if (file.isFile()) {
	    	// open this file
	    	if(!Desktop.isDesktopSupported()){
	            System.out.println("Desktop is not supported");
	        }
	    	Desktop desktop = Desktop.getDesktop();
	        try {
				desktop.open(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
//  ---------------------------------------------------------------------------	
	// l??u ???????ng d???n hi???n t???i tr?????c ???? (c?? th??? l?? file ho???c directory), ph???c v??? cho vi???c l??u
	// trung gian gi???a 2 stack.
	private String tempPath;
	
	// l??u ???????ng d???n directory tr?????c ????
	private String tempDirectoryPath;
	
	
	private JLabel lblSubJPanelOpenTypeFile;
	private JTextField textFieldSubJPanelOpenLinkPath;
	private JLabel lblHngDn;
	private JLabel lblCo;
	private JLabel lblHocVoTab;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JLabel lblNewLabel_5;
	private JLabel lblNewLabel_6;
	private JTextField textFieldSubJPanelNewLinkPath;
	private JLabel lblNewLabel_7;
	private JButton btnButtonChoseNewAsFileOrDir;
	private JLabel lblNewLabel_8;
	private JTextField textFieldSubJPanelNewFileType;
	private JLabel lblNewLabel_9;
	private JLabel lblNewLabel_10;
	private JTextField textFieldSubJPanelNewName??f;
	private JButton btnNew;
	private JTextField textFieldSubJPanelRenameLinkPath;
	private JLabel lblSubJPanelRenameTypeFile;
	private JLabel lblNewLabel_14;
	private JTextField textFieldSubJPanelRenameNewName;
	private JLabel lblHngDn_1;
	private JLabel lblCo_1;
	private JLabel lblHocVoTab_2;
	private JTextField textFieldSubJPanelDeleteLinkPath;
	private JLabel lblSubJPanelDeleteTypeFile;
	private JLabel lblHngDn_2;
	private JLabel lblCo_2;
	private JLabel lblHocVoTab_1;
	private JLabel lblSubJPanelExtractTypeFile;
	private JTextField textFieldSubJPanelCopyOriginLinkPath;
	private JTextField textFieldSubJPanelCopyNewLinkPath;
	private JTextField textFieldSubJPanelCutOriginLinkPath;
	private JTextField textFieldSubJPanelCutNewLinkPath;
	private JTextField textFieldSubJPanelCompressLinkPath;
	private JTextField textFieldSubJPanelExtractLinkPath;
	private JButton btnGetPath;
//  ---------------------------------------------------------------------------
	
	public JTree configureJTree(String path) {
		File fileRoot = new File(path);
		DefaultMutableTreeNode defaultMutableTreeNodeTop1 = new DefaultMutableTreeNode(fileRoot);
		insertTreeNode(defaultMutableTreeNodeTop1, fileRoot);
		JTree jtree = new JTree(defaultMutableTreeNodeTop1);
		// ????? render h??nh ???nh t??y ??
		//jtree.setCellRenderer(new MyTreeCellRenderer());
		
		jtree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
				// TODO Auto-generated method stub
				System.out.println("Array: " + treeSelectionEvent.getPath());
				
				// ???????ng d???n hi???n t???i
				presentPath = treeSelectionEvent.getPath().getPathComponent(0).toString();
				for (int i = 1; i < treeSelectionEvent.getPath().getPathCount(); i++) {
					presentPath += "\\" + treeSelectionEvent.getPath().getPathComponent(i).toString();
				}
				
				// l??u tr??? ???????ng d???n
				doStackJtreeStuff(presentPath, treeSelectionEvent);
				
			}
		});
		return jtree;
	}
	
	public void doStackJtreeStuff(String presentPath, TreeSelectionEvent treeSelectionEvent) {
		// ?????y ???????ng d???n c?? v??o stack
		// n???u l?? directory th?? n???p path v??o stackBack
		if (new File(presentPath).isDirectory()) {
			
			System.out.println("hello");
			if (stackOfBackPath.empty()) {
				// trong tr?????ng h???p kh???i ?????ng ???ng d???ng l???n ????u
				// l??c ch????ng tr??nh m???i ch???y, n??y ???????ng d???n c?? ???? l?? startRootPath
	    		// l??c n??y startRootPath c??ng ch??nh l?? presentPath
				tempPath          = new String(presentPath);
				tempDirectoryPath = new String(tempPath);
				stackOfBackPath.push(startRootPath);
				isBackCanClick = true;
				btnBack.setIcon(createFormatImageIcon(resourcePath + "backon.png", btnBack.getWidth(), btnBack.getHeight()));
				// hi???n th??? ???????ng d???n th?? m???c
				textFieldPresentPath.setText(presentPath);
				repaintTable(presentPath);
			} else {
				// th???c hi???n th??m element v??o stack
				stackOfBackPath.push(tempPath);
				tempPath          = new String(presentPath);
				tempDirectoryPath = new String(tempPath);
				// hi???n th??? ???????ng d???n th?? m???c
				textFieldPresentPath.setText(presentPath);
				repaintTable(presentPath);
			}
		} else {
			// n???u l?? d???ng file, th?? check xem c??i file ????, c?? n???m trong folder ??ang xem kh??ng
			// n???u c?? th?? d??? nguy??n ???????ng d???n th?? m???c
			// n???u kh??ng th?? l???y ???????ng d???n th?? m???c ???? ra ????? l??u
			
			System.out.println("hi");
			String presentDirectoryPath = treeSelectionEvent.getPath().getPathComponent(0).toString();
			for (int i = 1; i < treeSelectionEvent.getPath().getPathCount() - 1; i++) {
				presentDirectoryPath += "\\" + treeSelectionEvent.getPath().getPathComponent(i).toString();
			}
			System.out.println(presentDirectoryPath);
			
			if (tempDirectoryPath == null) {
				// n???u null l?? do ch????ng tr??nh m???i kh???i ch???y
				tempDirectoryPath = new String(presentDirectoryPath);
				textFieldPresentPath.setText(presentDirectoryPath);
				repaintTable(presentDirectoryPath);
			} else {
				if (!presentDirectoryPath.equals(tempDirectoryPath)) {
					// n???u ???????ng d???n th?? m???c hi???n d???i kh??c v???i ???????ng d???n th?? m???c tr?????c ????.
					System.out.println("presentDirectoryPath: " + presentDirectoryPath);
					System.out.println("tempDirectoryPath: " + tempDirectoryPath);
					
					if (stackOfBackPath.empty()) {
						// trong tr?????ng h???p kh???i ?????ng ???ng d???ng l???n ????u
						// l??c ch????ng tr??nh m???i ch???y, n??y ???????ng d???n c?? ???? l?? startRootPath
						tempPath          = new String(presentPath);
						tempDirectoryPath = new String(tempPath);
						stackOfBackPath.push(startRootPath);
						isBackCanClick = true;
						btnBack.setIcon(createFormatImageIcon(resourcePath + "backon.png", btnBack.getWidth(), btnBack.getHeight()));
						// hi???n th??? ???????ng d???n th?? m???c
						//textFieldPresentPath.setText(presentPath);
						textFieldPresentPath.setText(presentDirectoryPath);
						repaintTable(presentDirectoryPath);
					} else {
						// th???c hi???n th??m element v??o stack
						stackOfBackPath.push(tempPath);
						tempPath          = new String(presentPath);
						tempDirectoryPath = new String(tempPath);
						// hi???n th??? ???????ng d???n th?? m???c
						//textFieldPresentPath.setText(presentPath);
						textFieldPresentPath.setText(presentDirectoryPath);
						repaintTable(presentDirectoryPath);
					}
				}
			}
			
		}
		// END
	}
	
	public void insertTreeNode(DefaultMutableTreeNode motherTreeNode ,File fileRoot) {
		File[] fileList = fileRoot.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isFile()) {
				motherTreeNode.add(new DefaultMutableTreeNode(fileList[i].getName()));
				//System.out.println(fileList[i].getAbsolutePath());
			}
			
			if (fileList[i].isDirectory() && fileList.length != 0) {
				DefaultMutableTreeNode bornNewTreeNode = new DefaultMutableTreeNode(fileList[i].getName());
				motherTreeNode.add(bornNewTreeNode);
				//System.out.println(fileList[i].getAbsoluteFile());
				//insertTreeNode(bornNewTreeNode, fileList[i].getAbsoluteFile());
				try {
					insertTreeNode(bornNewTreeNode, fileList[i].getAbsoluteFile());
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
		}
		
	}
	
	public ArrayList<FileViewModel> getFileViewModel(String path) {
		File file = new File(path);
		File[] files = file.listFiles();
		
		ArrayList<FileViewModel> fileViewModelList = new ArrayList<FileViewModel>();
		for (File inFile : files) {
			String url = "";
			if (inFile.isFile()) {
				url = resourcePath + "fileicon.png";
			} else {
				url = resourcePath + "foldericon.png";
			}
			Date date = new Date(inFile.lastModified());
			
			fileViewModelList.add(new FileViewModel(url, 
													inFile.getName(), 
													inFile.getAbsolutePath(), 
													date.toString(), 
													inFile.isFile(), 
													getFileSizeKiloBytes(inFile), 
													inFile));
		}
		
		return fileViewModelList;
	}
	
	private static String getFileSizeMegaBytes(File file) {
		return (double) file.length() / (1024 * 1024) + " mb";
	}
	
	private static String getFileSizeKiloBytes(File file) {
		return (double) file.length() / 1024 + "  kb";
	}

	private static String getFileSizeBytes(File file) {
		return file.length() + " bytes";
	}
	
	// Image Engine ----------------------------------
	public ImageIcon createFormatImageIcon(String url, int width, int height) {
		BufferedImage bufferdImage;
		try {
			bufferdImage = ImageIO.read(new File(url));
			Image imageScaled = bufferdImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			imageScaled.flush();
			return new ImageIcon(imageScaled);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// NEW file ho???c folder
	public void createDir(String pathString) {
		Path path = Paths.get(pathString);
		System.out.println("path inside: " + path.getRoot());
		try {
			Files.createDirectory(path);
			// c?? th??? l???i khi g???i h??m n??y l??c ch????ng tr??nh m???i kh???i t???o, c?? th??? presentPath s??? null
			// think about using textfield thay th???.
//			repaintTable(presentPath);
//			repaintTree(presentPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.out.println(e.getMessage());
			System.out.println("Folder ???? t???n t???i t??? tr?????c");
			JOptionPane.showMessageDialog(FileExplore.this, "Folder n??y ???? t???n t???i");
		}
	}
	
	public void createFile(String pathString) {
		Path newFilePath = Paths.get(pathString);
        try {
			Files.createFile(newFilePath);
			// c?? th??? l???i khi g???i h??m n??y l??c ch????ng tr??nh m???i kh???i t???o, c?? th??? presentPath s??? null
//			repaintTable(presentPath);
//			repaintTree(presentPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(FileExplore.this, "File n??y ???? t???n t???i");
		}
	}
	
	// ?????i t??n file ho???c th?? m???c
	public void rename(String pathString) {
//		Path fileToMovePath = Paths.get(textFieldSubJPanelRenameNewName.getText().trim());
//	    Path targetPath = Paths.get(pathString);
//	    
//	    try {
//			Files.move(fileToMovePath, targetPath);
//			repaintTable(presentPath);
//			repaintTree(presentPath);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println(textFieldSubJPanelRenameLinkPath.getText().trim());
		System.out.println(pathString);
		File oldName = new File(textFieldSubJPanelRenameLinkPath.getText().trim());
		File newName = new File(pathString);
		
		if(oldName.renameTo(newName)) {
			System.out.println("renamed");
			textFieldSubJPanelRenameLinkPath.setText(newName.getAbsolutePath());
			repaintTable(presentPath);
			repaintTree(presentPath);
	    } else {
	        System.out.println("Error");
	    }
	}
	
	// x??a file ho???c th?? m???c
	public void deleteFile(String path) {
		Path fileToDeletePath = Paths.get(path);
	    try {
			Files.delete(fileToDeletePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteDir(String path) {
		File fileDir = new File(path);
		try {
			if (fileDir.listFiles().length == 0) {
				boolean result = fileDir.delete();
				if (result == true) {
					System.out.println("x??a th?? m???c r???ng th??nh c??ng");
				} else {
					System.out.println("x??a th?? m???c r???ng kh??ng th??nh c??ng");
				}
			} else {
				File[] allFileInDir = fileDir.listFiles();
				for (File inFile : allFileInDir) {
					if (inFile.isFile()) {
					    deleteFile(inFile.getAbsolutePath());
					} else {
						deleteDir(inFile.getAbsolutePath());
					}
					
				}
				Path dirToDeletePath = Paths.get(fileDir.getAbsolutePath());
			    try {
					Files.delete(dirToDeletePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println("ph????ng ??n delete th?? m???c d??? ph??ng");
			//FileUtils.deleteDirectory(new File(destination));
			contingencyPlanForDelete(path);
		}
	}
	// d??? ph??ng
	public void contingencyPlanForDelete(String path) {
		Path directory = Paths.get(path);
		try {
			Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
					Files.delete(file); // this will work because it's always a File
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.delete(dir); //this will work because Files in the directory are already deleted
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void copyFile(String originLinkPath, String newLinkPath) {
		InputStream inStream = null;
        OutputStream outStream = null;
         
        try {
            inStream = new FileInputStream(new File(originLinkPath));
            outStream = new FileOutputStream(new File(newLinkPath));
 
            int length;
            byte[] buffer = new byte[1024];
 
            // copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            System.out.println("File is copied successful!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
				inStream.close();
				outStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
//		Path fileToCopyPath  = Paths.get(path);
//		Files.copy(fileToCopyPath.get, arg1);
        
//        repaintTable(presentPath);
//        repaintTree(presentPath);
	}
	
	public static void copyDir(final String sourceDirectoryLocation, final String destinationDirectoryLocation) 
			  throws IOException {
		Files.walk(Paths.get(sourceDirectoryLocation))
		.forEach(source -> {
			Path destination = Paths.get(destinationDirectoryLocation, source.toString()
							   .substring(sourceDirectoryLocation.length()));
			try {
				Files.copy(source, destination);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	// hi???n th??? ??u??i m??? r???ng.
	public String getExtension(String path) {
		String extension = "";

		int i = new File(path).getName().lastIndexOf('.');
		if (i > 0) {
		    extension = new File(path).getName().substring(i+1);
		    return extension;
		}
		return "-1";
	}
	
	// x??a d???u g???ch d??.
	public String deleteLastSymbol(String string, String symbol) {
		StringBuilder stringBuilder = new StringBuilder(string);
		String s = String.valueOf(stringBuilder.charAt(stringBuilder.length() - 1));
		if (s.equals(File.separator)) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			return stringBuilder.toString();
		} else {
			return "-1";
		}
	}
	
	public void compressFileToRAR(String path) {
		try {
			String arg1 = new String(path);
			String arg2 = new File(arg1).getParent() + File.separator;
			System.out.println("arg1 " + arg1);
			System.out.println("arg2 " + arg2);
			//String arg2 = "C:\\Users\\Acer\\Desktop";
			ProcessBuilder pb = new ProcessBuilder("python","Resource" + File.separator + "compressRAR.py",""+arg1,""+arg2);
			
			//ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c","Resource" + File.separator + "extractRAR.exe",""+arg1,""+arg2);
			
			Process p = pb.start();
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void extractRAR(String path) {
		try {
			String arg1 = new String(path);
			String arg2 = new File(arg1).getParent();
			//String arg2 = "C:\\Users\\Acer\\Desktop";
			ProcessBuilder pb = new ProcessBuilder("python","Resource" + File.separator + "extractRAR.py",""+arg1,""+arg2);
			
			//ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c","Resource" + File.separator + "extractRAR.exe",""+arg1,""+arg2);
			
			Process p = pb.start();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	//////////////////////////////////////////// compress Zip
	public void compressFileToZip(String filePath) throws IOException{
		try {
            File file = new File(filePath);
            String zipFileName = file.getParent() + File.separator +file.getName().concat(".zip");
            
            FileOutputStream fos = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);
 
            zos.putNextEntry(new ZipEntry(file.getName()));
 
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();
            zos.close();
 
        } catch (FileNotFoundException ex) {
            System.err.format("The file %s does not exist", filePath);
        } catch (IOException ex) {
            System.err.println("I/O error: " + ex);
        }
	}
	
	public void compressDirToZip(String filePath) throws IOException {
		//String sourceFile = "zipTest";
		String sourceFile = new String(filePath);
		File fileToZip = new File(sourceFile);
        //FileOutputStream fos = new FileOutputStream("dirCompressed.zip");
		FileOutputStream fos = new FileOutputStream(fileToZip.getParent() + File.separator + fileToZip.getName().concat(".zip"));
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        
        //File fileToZip = new File(sourceFile);

        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
	}
	
	private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith(File.separator)) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + File.separator));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + File.separator + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
	
	private static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to "+newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
	////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////// extract zip
	// protect zip slip attack
    public static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir)
        throws IOException {

        // test zip slip vulnerability
        // Path targetDirResolved = targetDir.resolve("../../" + zipEntry.getName());

        Path targetDirResolved = targetDir.resolve(zipEntry.getName());

        // make sure normalized file still has targetDir as its prefix
        // else throws exception
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }

        return normalizePath;
    }
	
	public static void unzipFolder(Path source, Path target) throws IOException {

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source.toFile()))) {

            // list files in zip
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {

                boolean isDirectory = false;
                // example 1.1
                // some zip stored files and folders separately
                // e.g data/
                //     data/folder/
                //     data/folder/file.txt
                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }

                Path newPath = zipSlipProtect(zipEntry, target);

                if (isDirectory) {
                    Files.createDirectories(newPath);
                } else {

                    // example 1.2
                    // some zip stored file path only, need create parent directories
                    // e.g data/folder/file.txt
                    if (newPath.getParent() != null) {
                        if (Files.notExists(newPath.getParent())) {
                            Files.createDirectories(newPath.getParent());
                        }
                    }

                    // copy files, nio
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);

                    // copy files, classic
                    /*try (FileOutputStream fos = new FileOutputStream(newPath.toFile())) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }*/
                }

                zipEntry = zis.getNextEntry();

            }
            zis.closeEntry();

        }

    }
	
	public void extract7Zip(String path) {
		try {
			String arg1 = new String(path);
			String arg2 = new File(arg1).getParent();
			//String arg2 = "C:\\Users\\Acer\\Desktop";
			ProcessBuilder pb = new ProcessBuilder("python","Resource" + File.separator + "extract7Zip.py",""+arg1,""+arg2);
			
			//ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c","Resource" + File.separator + "extractRAR.exe",""+arg1,""+arg2);
			
			Process p = pb.start();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//------------------------------------------------
	class CustomFileTableModel extends AbstractTableModel {
		
		private ArrayList<FileViewModel> fileViewModelList;
		//public File[] file;
		//public FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		//String[] column = {"", "Name", "Data Modified", "Type", "Size"};
		
		public CustomFileTableModel(ArrayList<FileViewModel> fileViewModelList) {
			super();
			this.fileViewModelList = fileViewModelList;
		}
		
		

		public ArrayList<FileViewModel> getFileViewModelList() {
			return fileViewModelList;
		}



		public void setFileViewModelList(ArrayList<FileViewModel> fileViewModelList) {
			this.fileViewModelList = fileViewModelList;
		}
		
		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return header.length;
		}

		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return fileViewModelList.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			FileViewModel fileViewModel = fileViewModelList.get(rowIndex);
	        switch(columnIndex){
	            case 0: return fileViewModel.getUrl();
	            case 1: return fileViewModel.getFileName();
	            case 2: return fileViewModel.getPath();
	            case 3: return fileViewModel.getDataModified();
	            case 4: 
	            	if (!fileViewModel.getIsFile()) {
	            		return "Folder directory";
	            	} else {
	            		String extension = "";

	            		int i = fileViewModel.getFile().getName().lastIndexOf('.');
	            		if (i > 0) {
	            		    extension = fileViewModel.getFile().getName().substring(i+1);
	            		}
	            		return "File " + extension;
	            	}
	            	
	            case 5: return fileViewModel.getSize();
	            default: return null;
	        }
		}
		
		String[] header = {"Icon", "Name", "Path", "Data Modified", "Type", "Size"};
		@Override
		public String getColumnName(int columnNumber) {
			// TODO Auto-generated method stub
			switch(columnNumber){
            	case 0: return "Icon";
            	case 1: return "Name";
            	case 2: return "Path";
            	case 3: return "Data Modified";
            	case 4: return "Type";
            	case 5: return "Size";
            	default: return "ahihi";
			}
			//return super.getColumnName(arg0);
		}
		
	}
///////////////////////////////////////////////////////////////////////////////
	
	class CustomTableCellRenderer implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable jtable, Object value, boolean isSelected, boolean hasFocus, int rowNumber,
				int columnNumber) {
			// TODO Auto-generated method stub
			JLabel jlabelCell = new JLabel();
	        // l???nh render h??nh ???nh icon t???i c???t s??? 0
	        if(columnNumber == 0){
	            //URL flagUrl = CountryCellRenderer.class.getResource("flags/" + value.toString() + ".png");
	            //URL flagUrl = CustomTableCellRenderer.class.getResource("flags/" + object.toString() + ".png");
	            
	        	//FileViewModel fileViewModel = (FileViewModel) value;
	        	String url = (String) value;
	            jlabelCell.setIcon(createFormatImageIcon(url, 20, 20));
	        }else{            
	        	jlabelCell.setText(value.toString());            
	        }
	        return jlabelCell;
		}
		
	}
///////////////////////////////////////////////////////////////////////////////
	class MyTreeCellRenderer extends DefaultTreeCellRenderer {

		  @Override
		public Component getTreeCellRendererComponent(JTree arg0, Object arg1, boolean arg2, boolean arg3, boolean arg4,
				int arg5, boolean arg6) {
			// TODO Auto-generated method stub
			
			return super.getTreeCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
		}
	}
}
