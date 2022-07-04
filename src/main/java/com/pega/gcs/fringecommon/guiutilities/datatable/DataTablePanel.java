
package com.pega.gcs.fringecommon.guiutilities.datatable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.pega.gcs.fringecommon.guiutilities.BaseFrame;

public class DataTablePanel extends JPanel {

    private static final long serialVersionUID = 2412415813960579386L;

    private JButton exportToTsvButton;

    private AbstractDataTableModel<?, ?> abstractDataTableModel;

    private String context;

    private Component parent;

    public DataTablePanel(AbstractDataTableModel<?, ?> abstractDataTableModel, boolean showExportButton, String context,
            Component parent) {

        this.abstractDataTableModel = abstractDataTableModel;
        this.context = context;
        this.parent = parent;

        setLayout(new GridBagLayout());

        int gridy = 0;
        int top = 5;

        if (showExportButton) {

            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.gridx = 0;
            gbc1.gridy = 0;
            gbc1.weightx = 1.0D;
            gbc1.weighty = 0.0D;
            gbc1.fill = GridBagConstraints.BOTH;
            gbc1.anchor = GridBagConstraints.NORTHWEST;
            gbc1.insets = new Insets(top, 5, 0, 0);

            JPanel buttonPanel = getButtonPanel();

            add(buttonPanel, gbc1);

            gridy++;
            top = 0;
        }

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = gridy;
        gbc2.weightx = 1.0D;
        gbc2.weighty = 1.0D;
        gbc2.fill = GridBagConstraints.BOTH;
        gbc2.anchor = GridBagConstraints.NORTHWEST;
        gbc2.insets = new Insets(top, 5, 2, 0);

        DataTable<?, ?> dassInfoTable;
        dassInfoTable = new DataTable<>(abstractDataTableModel, JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(dassInfoTable);

        scrollPane.setPreferredSize(dassInfoTable.getPreferredSize());

        add(scrollPane, gbc2);

    }

    private AbstractDataTableModel<?, ?> getAbstractDataTableModel() {
        return abstractDataTableModel;
    }

    private String getContext() {
        return context;
    }

    private JButton getExportToTsvButton() {

        if (exportToTsvButton == null) {
            exportToTsvButton = new JButton("Export table as TSV");

            Dimension size = new Dimension(200, 26);
            exportToTsvButton.setPreferredSize(size);

            exportToTsvButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent actionEvent) {

                    AbstractDataTableModel<?, ?> abstractDataTableModel = getAbstractDataTableModel();
                    String context = getContext();

                    DataTableExportDialog dataTableExportDialog;
                    dataTableExportDialog = new DataTableExportDialog(abstractDataTableModel, context,
                            BaseFrame.getAppIcon(), parent);

                    dataTableExportDialog.setVisible(true);
                }
            });

        }

        return exportToTsvButton;
    }

    private JPanel getButtonPanel() {

        JPanel buttonPanel = new JPanel();

        LayoutManager layout = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
        buttonPanel.setLayout(layout);

        JButton exportToTsvButton = getExportToTsvButton();

        Dimension startDim = new Dimension(20, 40);

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(Box.createRigidArea(startDim));
        buttonPanel.add(exportToTsvButton);
        buttonPanel.add(Box.createRigidArea(startDim));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        return buttonPanel;
    }
}
