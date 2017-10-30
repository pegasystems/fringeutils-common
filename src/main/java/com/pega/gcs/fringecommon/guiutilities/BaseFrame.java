/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;
import com.pega.gcs.fringecommon.utilities.FileUtilities;
import com.pega.gcs.fringecommon.utilities.GeneralUtilities;

public abstract class BaseFrame extends JFrame {

	private static final long serialVersionUID = 836535275459924503L;

	private static final Log4j2Helper LOG = new Log4j2Helper(BaseFrame.class);

	public static final String PREF_SETTINGS = "settings";

	public static final String PREF_LAST_VISITED_DIR = "last_visited_dir";

	public static final String PREF_OPEN_FILE_LIST = "open_file_list";

	private static ImageIcon appIcon;

	protected abstract void initialize() throws Exception;

	protected abstract JComponent getMainJPanel();

	protected abstract String getAppName();

	protected abstract void release();

	protected BaseFrame() throws Exception {
		// Metal : javax.swing.plaf.metal.MetalLookAndFeel
		// Nimbus : com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel
		// CDE/Motif : com.sun.java.swing.plaf.motif.MotifLookAndFeel
		// Windows : com.sun.java.swing.plaf.windows.WindowsLookAndFeel
		// Windows Classic :
		// com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		appIcon = FileUtilities.getImageIcon(this.getClass(), "pega16.png");

		setPreferredSize(new Dimension(1200, 700));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(null);
		setIconImage(appIcon.getImage());
		// setLocationRelativeTo(null);

		// setExtendedState(JFrame.MAXIMIZED_BOTH);
		// setVisible(true);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit(0);
			}
		});

		loadPlugins();

		initialize();

		setJMenuBar(getMenuJMenuBar());

		setContentPane(getMainJPanel());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Frame#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {

		String localTitle = title;

		if ((title != null) && (!"".equals(title))) {
			localTitle = " - " + title;
		} else {
			localTitle = "";
		}

		String finalTitle = getAppName() + localTitle;

		super.setTitle(finalTitle);
	}

	private void loadPlugins() {
		try {

			String pwd = System.getProperty("user.dir");
			URL pluginsUrl = getClass().getResource("/plugins");

			File pluginsDir = null;

			if (pluginsUrl != null) {
				pluginsDir = new File(pluginsUrl.getFile());
			} else {
				pluginsDir = new File(pwd, "plugins");
			}

			if (pluginsDir.exists() && pluginsDir.isDirectory()) {

				URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();

				Class<URLClassLoader> urlClassLoaderClass = URLClassLoader.class;

				Method addURLMethod = urlClassLoaderClass.getDeclaredMethod("addURL", new Class[] { URL.class });
				addURLMethod.setAccessible(true);

				File[] jarlist = pluginsDir.listFiles(new java.io.FileFilter() {
					public boolean accept(File file) {
						return file.getPath().toLowerCase().endsWith(".jar");
					}
				});

				for (File jarFile : jarlist) {

					URL jarFileURL = jarFile.toURI().toURL();
					LOG.info("loading external jar: " + jarFileURL);

					addURLMethod.invoke(systemClassLoader, new Object[] { jarFileURL });
				}
			}

		} catch (Exception e) {
			LOG.error("Error loading plugins", e);
		}
	}

	/**
	 * @return the appIcon
	 */
	public static ImageIcon getAppIcon() {
		return appIcon;
	}

	public void exit(int value) {
		release();
		System.exit(value);
	}

	protected JMenuBar getMenuJMenuBar() {
		JMenu fileJMenu = new JMenu("File");
		fileJMenu.setMnemonic(KeyEvent.VK_F);

		JMenuItem exitJMenuItem = new JMenuItem("Exit");
		exitJMenuItem.setMnemonic(KeyEvent.VK_X);
		exitJMenuItem.setToolTipText("Exit application");
		ImageIcon ii = FileUtilities.getImageIcon(this.getClass(), "exit.png");

		exitJMenuItem.setIcon(ii);
		exitJMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				exit(0);
			}
		});

		fileJMenu.addSeparator();
		fileJMenu.add(exitJMenuItem);

		JMenu helpJMenu = getHelpAboutJMenu();
		JMenuBar jMenuBar = new JMenuBar();
		jMenuBar.add(fileJMenu);
		jMenuBar.add(helpJMenu);

		return jMenuBar;
	}

	protected JMenu getHelpAboutJMenu() {

		JMenu helpJMenu = new JMenu("   Help   ");
		helpJMenu.setMnemonic(KeyEvent.VK_H);

		JMenuItem aboutJMenuItem = new JMenuItem("About");
		aboutJMenuItem.setToolTipText("About");

		ImageIcon ii = FileUtilities.getImageIcon(this.getClass(), "pega16.png");
		aboutJMenuItem.setIcon(ii);

		aboutJMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {

				ImageIcon ii = FileUtilities.getImageIcon(this.getClass(), "pega300.png");
				AboutDialog aboutDialog = new AboutDialog(ii.getImage(), getAppName());
				aboutDialog.setVisible(true);
			}
		});

		helpJMenu.add(aboutJMenuItem);

		return helpJMenu;
	}

	protected JScrollPane getComponentJScrollPane(JComponent jComponent, int vsbPolicy, int hsbPolicy) {

		JScrollPane jScrollPane = new JScrollPane(jComponent, vsbPolicy, hsbPolicy);
		return jScrollPane;
	}

	public static File openFileChooser(Component parent, Class<?> clazz, String title, final List<String> fileExtList,
			final String description, File prevSelectedFile) {
		return openFileChooser(parent, clazz, title, null, fileExtList, description, prevSelectedFile);
	}

	public static File openFileChooser(Component parent, Class<?> clazz, String title, String fileNameRegex,
			final List<String> fileExtList, final String description, File prevSelectedFile) {

		File selectedFile = null;

		FileFilter fileFilter = new FileFilter() {

			@Override
			public String getDescription() {
				return description;
			}

			@Override
			public boolean accept(File f) {

				String filename = FileUtilities.getNameWithoutExtension(f);
				String ext = FileUtilities.getExtension(f);

				boolean retVal = true;

				if (f.isFile()) {

					if (fileExtList != null) {

						retVal = false;

						for (String fileExt : fileExtList) {

							if (fileExt.equalsIgnoreCase(ext)) {
								retVal = true;
								break;
							}
						}
					}

					if ((retVal) && (fileNameRegex != null) && (!"".equals(fileNameRegex))) {

						Pattern fileNamePattern = Pattern.compile(fileNameRegex);

						Matcher fileNameMatcher = fileNamePattern.matcher(filename);

						if (fileNameMatcher.matches()) {
							retVal = true;
						} else {
							retVal = false;
						}
					}
				}

				return retVal;
			}
		};

		String prefName = PREF_LAST_VISITED_DIR;
		String lastVisitedDir = GeneralUtilities.getPreferenceString(clazz, prefName);

		File jChooserFile = getExistingDirectory(prevSelectedFile);

		if ((jChooserFile == null) && (!"".equals(lastVisitedDir))) {

			jChooserFile = getExistingDirectory(new File(lastVisitedDir));
		}

		JFileChooser jFileChooser = new JFileChooser(jChooserFile);

		jFileChooser.setDialogTitle(title);
		jFileChooser.setFileFilter(fileFilter);
		int returnValue = jFileChooser.showOpenDialog(parent);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			selectedFile = jFileChooser.getSelectedFile();

			GeneralUtilities.setPreferenceString(clazz, prefName, selectedFile.getParent());
		}

		return selectedFile;
	}

	private static File getExistingDirectory(File aFile) {

		File existingDir = aFile;

		if (aFile != null) {

			if (aFile.exists()) {

				if (aFile.isFile()) {
					existingDir = aFile.getParentFile();
				} else {
					existingDir = aFile;
				}
			} else {
				existingDir = getExistingDirectory(aFile.getParentFile());
			}
		}

		return existingDir;
	}

	public static void main(String[] args) {

		// Run GUI codes in Event-Dispatching thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					BaseFrame baseFrame = new BaseFrame() {

						private static final long serialVersionUID = -2105706529966669119L;

						@Override
						protected void release() {
							// do nothing
						}

						@Override
						protected JComponent getMainJPanel() {
							return new JPanel();
						}

						@Override
						protected String getAppName() {
							return "Test BaseFrame";
						}

						@Override
						protected void initialize() {
							// do nothing

						}
					};

					baseFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
}
