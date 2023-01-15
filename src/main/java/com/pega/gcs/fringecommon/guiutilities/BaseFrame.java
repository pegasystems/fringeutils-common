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
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
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
        String lafClassName = UIManager.getSystemLookAndFeelClassName();

        LOG.info("Using LAF class: " + lafClassName);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        Border splitPaneDividerBorder = BorderFactory.createRaisedBevelBorder();
        UIManager.put("SplitPaneDivider.border", splitPaneDividerBorder);

        setPreferredSize(new Dimension(1200, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(null);
        setIconImage(getAppIcon().getImage());
        // setLocationRelativeTo(null);

        // setExtendedState(JFrame.MAXIMIZED_BOTH);
        // setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                exit(0);
            }
        });

        initialize();

        setJMenuBar(getToolMenuBar());

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

    public static ImageIcon getAppIcon() {

        if (appIcon == null) {
            appIcon = FileUtilities.getImageIcon(BaseFrame.class, "pega16.png");
        }

        return appIcon;
    }

    public void exit(int value) {
        release();
        System.exit(value);
    }

    protected JMenu getToolMenu(String name, int mnemonic) {

        int totalPaddedLen = 15;

        String menuName = GeneralUtilities.getPaddedString(name, ' ', totalPaddedLen, SwingConstants.CENTER);

        JMenu menu = new JMenu(menuName);
        menu.setMnemonic(mnemonic);

        return menu;
    }

    protected JMenuBar getToolMenuBar() {

        JMenu fileMenu = getFileMenu();
        JMenu helpMenu = getHelpMenu();

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    protected JMenu getFileMenu() {

        JMenu fileMenu = getToolMenu("File", KeyEvent.VK_F);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        exitMenuItem.setToolTipText("Exit application");
        ImageIcon ii = FileUtilities.getImageIcon(this.getClass(), "exit.png");

        exitMenuItem.setIcon(ii);
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                exit(0);
            }
        });

        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    protected JMenu getHelpMenu() {

        JMenu helpMenu = getToolMenu("Help", KeyEvent.VK_H);

        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.setToolTipText("About");

        ImageIcon ii = FileUtilities.getImageIcon(this.getClass(), "pega16.png");
        aboutMenuItem.setIcon(ii);

        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                ImageIcon ii = FileUtilities.getImageIcon(this.getClass(), "pega300.png");
                AboutDialog aboutDialog = new AboutDialog(ii.getImage(), getAppName(), BaseFrame.this);
                aboutDialog.setVisible(true);
            }
        });

        helpMenu.add(aboutMenuItem);

        return helpMenu;
    }

    protected JScrollPane getComponentJScrollPane(JComponent component, int vsbPolicy, int hsbPolicy) {

        JScrollPane scrollPane = new JScrollPane(component, vsbPolicy, hsbPolicy);
        return scrollPane;
    }

    public static FileFilter getDefaultFileFilter(final String description, final List<String> fileExtList) {

        FileFilter fileFilter = new FileFilter() {

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public boolean accept(File file) {

                boolean retVal = true;

                // pass through directories
                if (file.isFile()) {

                    if (fileExtList != null) {

                        String ext = FileUtilities.getFileExtension(file);

                        retVal = false;

                        for (String fileExt : fileExtList) {

                            if (fileExt.equalsIgnoreCase(ext)) {
                                retVal = true;
                                break;
                            }
                        }
                    }
                }

                return retVal;
            }
        };

        return fileFilter;
    }

    public static File openFileChooser(Component parent, Class<?> clazz, String title, FileFilter fileFilter,
            File prevSelectedFile) {

        File selectedFile = null;

        String prefName = PREF_LAST_VISITED_DIR;

        String lastVisitedDir = GeneralUtilities.getPreferenceString(clazz, prefName);

        File prevSelectedDirectory = getExistingDirectory(prevSelectedFile);

        if ((prevSelectedDirectory == null) && (!"".equals(lastVisitedDir))) {

            prevSelectedDirectory = getExistingDirectory(new File(lastVisitedDir));
        }

        JFileChooser fileChooser = new JFileChooser(prevSelectedDirectory);

        fileChooser.setDialogTitle(title);

        if (fileFilter != null) {
            fileChooser.setFileFilter(fileFilter);
        }

        int returnValue = fileChooser.showOpenDialog(parent);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();

            GeneralUtilities.setPreferenceString(clazz, prefName, selectedFile.getParent());
        }

        return selectedFile;
    }

    private static File getExistingDirectory(File file) {

        File existingDir = file;

        if (file != null) {

            if (file.exists()) {

                if (file.isFile()) {
                    existingDir = file.getParentFile();
                } else {
                    existingDir = file;
                }
            } else {
                existingDir = getExistingDirectory(file.getParentFile());
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
                    LOG.error("Error in BaseFrame", e);
                }
            }
        });

    }
}
