/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.ProgressMonitor;
import javax.swing.UIManager;

public class ModalProgressMonitor extends ProgressMonitor {

    private ModalProgressMonitor root;
    private JDialog dialog;
    private JOptionPane pane;
    private JProgressBar myBar;
    private JLabel noteLabel;
    private Component parentComponent;
    private String note;
    private Object[] cancelOption = null;
    private Object message;
    private long time0;
    private int millisToDecideToPopup = 500;
    private int millisToPopup = 2000;
    private int min;
    private int max;
    private boolean indeterminate;

    /**
     * Constructs a graphic object that shows progress, typically by filling in a rectangular bar as the process nears completion.
     * 
     * @param parentComponent the parent component for the dialog box
     * @param message         a descriptive message that will be shown to the user to indicate what operation is being monitored. This
     *                        does not change as the operation progresses. See the message parameters to methods in
     *                        {@link JOptionPane#message} for the range of values.
     * @param note            a short note describing the state of the operation. As the operation progresses, you can call setNote to
     *                        change the note displayed. This is used, for example, in operations that iterate through a list of files
     *                        to show the name of the file being processes. If note is initially null, there will be no note line in the
     *                        dialog box and setNote will be ineffective
     * @param min             the lower bound of the range
     * @param max             the upper bound of the range
     * @see JDialog
     * @see JOptionPane
     */
    public ModalProgressMonitor(Component parentComponent, Object message, String note, int min, int max) {
        this(parentComponent, message, note, min, max, null);
    }

    private ModalProgressMonitor(Component parentComponent, Object message, String note, int min, int max,
            ModalProgressMonitor group) {

        super(parentComponent, message, note, min, max);

        this.min = min;
        this.max = max;
        this.indeterminate = false;
        this.parentComponent = parentComponent;

        cancelOption = new Object[1];
        cancelOption[0] = UIManager.getString("OptionPane.cancelButtonText");

        this.message = message;
        this.note = note;
        if (group != null) {
            root = (group.root != null) ? group.root : group;
            time0 = root.time0;
            dialog = root.dialog;
        } else {
            time0 = System.currentTimeMillis();
        }
    }

    public ModalProgressMonitor(Component parentComponent, Object message, String note) {
        this(parentComponent, message, note, null);
    }

    private ModalProgressMonitor(Component parentComponent, Object message, String note, ModalProgressMonitor group) {

        super(parentComponent, message, note, 0, 0);

        this.indeterminate = true;
        this.parentComponent = parentComponent;

        cancelOption = new Object[1];
        cancelOption[0] = UIManager.getString("OptionPane.cancelButtonText");

        this.message = message;
        this.note = note;
        if (group != null) {
            root = (group.root != null) ? group.root : group;
            dialog = root.dialog;
        }
    }

    /**
     * Used for indeterminate type ModalProgressMonitor.
     */
    @SuppressWarnings("deprecation")
    public void show() {

        if ((indeterminate) && (dialog == null)) {

            myBar = new JProgressBar();
            myBar.setIndeterminate(true);

            if (note != null) {
                noteLabel = new JLabel(note);
            }

            Object messageList = new Object[] { message, noteLabel, myBar };

            pane = new JOptionPane(messageList, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null,
                    ModalProgressMonitor.this.cancelOption, null);

            dialog = pane.createDialog(parentComponent, UIManager.getString("ModalProgressMonitor.progressText"));
            dialog.show();
        }

    }

    /**
     * Indicate the progress of the operation being monitored. If the specified value is &gt;= the maximum, the progress monitor is
     * closed.
     * 
     * @param nv an int specifying the current value, between the maximum and minimum specified for this component
     * @see #setMinimum
     * @see #setMaximum
     * @see #close
     */
    @Override
    @SuppressWarnings("deprecation")
    public void setProgress(int nv) {

        if (!indeterminate) {

            if (nv >= max) {
                close();
            } else {

                if (myBar != null) {
                    myBar.setValue(nv);
                } else {

                    long currT = System.currentTimeMillis();
                    long diffT = (int) (currT - time0);

                    if (diffT >= millisToDecideToPopup) {
                        int predictedCompletionTime;

                        if (nv > min) {
                            predictedCompletionTime = (int) ((long) diffT * (max - min) / (nv - min));
                        } else {
                            predictedCompletionTime = millisToPopup;
                        }

                        if (predictedCompletionTime >= millisToPopup) {
                            myBar = new JProgressBar();
                            myBar.setMinimum(min);
                            myBar.setMaximum(max);
                            myBar.setValue(nv);

                            if (note != null) {
                                noteLabel = new JLabel(note);
                            }
                            Object messageList = new Object[] { message, noteLabel, myBar };

                            pane = new JOptionPane(messageList, JOptionPane.INFORMATION_MESSAGE,
                                    JOptionPane.DEFAULT_OPTION, null, ModalProgressMonitor.this.cancelOption, null);

                            dialog = pane.createDialog(parentComponent,
                                    UIManager.getString("ModalProgressMonitor.progressText"));
                            dialog.show();
                        }
                    }
                }
            }
        }
    }

    /**
     * Indicate that the operation is complete. This happens automatically when the value set by setProgress is &gt;= max, but it may be
     * called earlier if the operation ends early.
     */
    @Override
    public void close() {
        if (dialog != null) {
            dialog.setVisible(false);
            dialog.dispose();
            dialog = null;
            pane = null;
            myBar = null;
        }
    }

    /**
     * Returns the minimum value -- the lower end of the progress value.
     * 
     * @return an int representing the minimum value
     * @see #setMinimum
     */
    @Override
    public int getMinimum() {
        return min;
    }

    /**
     * Specifies the minimum value.
     * 
     * @param minimum an int specifying the minimum value
     * @see #getMinimum
     */
    @Override
    public void setMinimum(int minimum) {
        if (myBar != null) {
            myBar.setMinimum(minimum);
        }
        min = minimum;
    }

    /**
     * Returns the maximum value -- the higher end of the progress value.
     * 
     * @return an int representing the maximum value
     * @see #setMaximum
     */
    @Override
    public int getMaximum() {
        return max;
    }

    /**
     * Specifies the maximum value.
     * 
     * @param maximum an int specifying the maximum value
     * @see #getMaximum
     */
    @Override
    public void setMaximum(int maximum) {
        if (myBar != null) {
            myBar.setMaximum(maximum);
        }
        max = maximum;
    }

    /**
     * Returns true if the user hits the Cancel button in the progress dialog.
     */
    @Override
    public boolean isCanceled() {
        if (pane == null) {
            return false;
        }

        Object value = pane.getValue();
        return ((value != null) && (cancelOption.length == 1) && (value.equals(cancelOption[0])));
    }

    /**
     * Specifies the amount of time to wait before deciding whether or not to popup a progress monitor.
     * 
     * @param millisToDecideToPopup an int specifying the time to wait, in milliseconds
     * @see #getMillisToDecideToPopup
     */
    @Override
    public void setMillisToDecideToPopup(int millisToDecideToPopup) {
        this.millisToDecideToPopup = millisToDecideToPopup;
    }

    /**
     * Returns the amount of time this object waits before deciding whether or not to popup a progress monitor.
     * 
     * @see #setMillisToDecideToPopup
     */
    @Override
    public int getMillisToDecideToPopup() {
        return millisToDecideToPopup;
    }

    /**
     * Specifies the amount of time it will take for the popup to appear. (If the predicted time remaining is less than this time, the
     * popup won't be displayed.)
     * 
     * @param millisToPopup an int specifying the time in milliseconds
     * @see #getMillisToPopup
     */
    @Override
    public void setMillisToPopup(int millisToPopup) {
        this.millisToPopup = millisToPopup;
    }

    /**
     * Returns the amount of time it will take for the popup to appear.
     * 
     * @see #setMillisToPopup
     */
    @Override
    public int getMillisToPopup() {
        return millisToPopup;
    }

    /**
     * Specifies the additional note that is displayed along with the progress message. Used, for example, to show which file the is
     * currently being copied during a multiple-file copy.
     * 
     * @param note a String specifying the note to display
     * @see #getNote
     */
    @Override
    public void setNote(String note) {
        this.note = note;
        if (noteLabel != null) {
            noteLabel.setText(note);
        }
    }

    /**
     * Specifies the additional note that is displayed along with the progress message.
     * 
     * @return a String specifying the note to display
     * @see #setNote
     */
    @Override
    public String getNote() {
        return note;
    }

    public boolean isIndeterminate() {
        return indeterminate;
    }

    public void setIndeterminate(boolean indeterminate) {

        if (this.indeterminate != indeterminate) {
            this.indeterminate = indeterminate;

            // close the existing dialog to setup new one
            close();
        }
    }

    public int getProgress() {
        int progress = -1;

        if (myBar != null) {
            progress = myBar.getValue();
        }

        return progress;
    }
}
