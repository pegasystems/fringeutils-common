/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.xmltreetable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import com.pega.gcs.fringecommon.guiutilities.MyColor;
import com.pega.gcs.fringecommon.guiutilities.RightClickMenuItem;
import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;
import com.pega.gcs.fringecommon.utilities.KnuthMorrisPrattAlgorithm;

public abstract class XMLElementTextDetailsPanel extends JPanel implements ListSelectionListener {

	private static final long serialVersionUID = 2833789190370268564L;

	private static final Log4j2Helper LOG = new Log4j2Helper(XMLNode.class);

	// contains xml element names that need to be displayed as table on details
	// window.
	private Map<String, XMLElementType> xmlElementTypeMap;

	private JPanel detailsPanel;

	private Highlighter.HighlightPainter primarySearchHighlightPainter;

	private Highlighter.HighlightPainter secondarySearchHighlightPainter;

	public XMLElementTextDetailsPanel(Map<String, XMLElementType> xmlElementTypeMap) {
		super();

		this.xmlElementTypeMap = xmlElementTypeMap;

		this.primarySearchHighlightPainter = new DefaultHighlighter.DefaultHighlightPainter(MyColor.LIGHT_YELLOW);

		this.secondarySearchHighlightPainter = new DefaultHighlighter.DefaultHighlightPainter(MyColor.LIME);

		setLayout(new BorderLayout());

		detailsPanel = new JPanel();

		add(detailsPanel);

		Border loweredEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

		setBorder(BorderFactory.createTitledBorder(loweredEtched, "Details"));
	}

	public void populatePanel(XMLNode xmlNode, String[] primarySearchArray, String secondarySearch, Font font) {

		detailsPanel.removeAll();
		detailsPanel.setLayout(new BorderLayout());

		if (xmlNode != null) {

			String nodeName = xmlNode.getNodeName();

			if ((xmlElementTypeMap != null) && (xmlElementTypeMap.containsKey(nodeName))) {
				JPanel xmlNodeTablePanel = getXMLNodeTablePanel(xmlNode, primarySearchArray, secondarySearch, font);

				detailsPanel.add(xmlNodeTablePanel);
			} else {
				JPanel xmlNodeTextPanel = getXMLNodeTextPanel(xmlNode, primarySearchArray, secondarySearch, font);

				detailsPanel.add(xmlNodeTextPanel);
			}
		}

		detailsPanel.revalidate();
		detailsPanel.repaint();

	}

	private JPanel getXMLNodeTextPanel(XMLNode xmlNode, String[] primarySearchArray, String secondarySearch,
			Font font) {

		JPanel xmlNodeTextPanel = new JPanel();
		xmlNodeTextPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 1.0D;
		gbc1.weighty = 0.0D;
		gbc1.fill = GridBagConstraints.HORIZONTAL;
		gbc1.anchor = GridBagConstraints.NORTHWEST;
		gbc1.insets = new Insets(2, 2, 1, 2);

		Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);

		String text = xmlNode.getNodeName();

		JTextArea nameJTextArea = getNameJTextArea(text, font);

		Dimension dim = new Dimension(22, 22);
		nameJTextArea.setPreferredSize(dim);

		JPanel nameComponent = new JPanel();
		LayoutManager layout = new BoxLayout(nameComponent, BoxLayout.X_AXIS);
		nameComponent.setLayout(layout);

		nameComponent.add(nameJTextArea);
		nameComponent.setBorder(border);
		nameComponent.setPreferredSize(nameJTextArea.getPreferredSize());

		xmlNodeTextPanel.add(nameComponent, gbc1);

		// Name - highlight text if required
		performTextAreaHighlight(xmlNode, 0, nameJTextArea, primarySearchArray, secondarySearch);

		for (int count = 0; count < xmlNode.getNodeElements().length; count++) {

			int index = count + 1;

			GridBagConstraints gbc2 = new GridBagConstraints();
			gbc2.gridx = 0;
			gbc2.gridy = index;
			gbc2.weightx = 1.0D;
			gbc2.weighty = 1.0D;
			gbc2.fill = GridBagConstraints.BOTH;
			gbc2.anchor = GridBagConstraints.NORTHWEST;
			gbc2.insets = new Insets(1, 2, 1, 2);

			text = xmlNode.getNodeValue(index);

			JTextArea valueTextArea = getValueJTextArea(text, font);

			JScrollPane valueComponent = new JScrollPane(valueTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

			valueComponent.setBorder(border);
			valueComponent.setPreferredSize(valueTextArea.getPreferredSize());

			xmlNodeTextPanel.add(valueComponent, gbc2);

			// Value - highlight text if required
			performTextAreaHighlight(xmlNode, index, valueTextArea, primarySearchArray, secondarySearch);

		}

		return xmlNodeTextPanel;
	}

	@SuppressWarnings("deprecation")
	private JPanel getXMLNodeTablePanel(XMLNode xmlNode, String[] primarySearchArray, String secondarySearch,
			Font font) {

		JPanel xmlNodeTablePanel = new JPanel();
		xmlNodeTablePanel.setLayout(new GridBagLayout());

		String nodeName = xmlNode.getNodeName();

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 1.0D;
		gbc1.weighty = 0.0D;
		gbc1.fill = GridBagConstraints.HORIZONTAL;
		gbc1.anchor = GridBagConstraints.NORTHWEST;
		gbc1.insets = new Insets(2, 2, 1, 2);
		gbc1.gridwidth = GridBagConstraints.REMAINDER;

		Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);

		JTextArea nameJTextArea = getNameJTextArea(nodeName, font);

		Dimension dim = new Dimension(22, 22);
		nameJTextArea.setPreferredSize(dim);

		JPanel nameComponent = new JPanel();
		LayoutManager layout = new BoxLayout(nameComponent, BoxLayout.X_AXIS);
		nameComponent.setLayout(layout);

		nameComponent.add(nameJTextArea);
		nameComponent.setBorder(border);
		nameComponent.setPreferredSize(nameJTextArea.getPreferredSize());

		xmlNodeTablePanel.add(nameComponent, gbc1);

		// Name - highlight text if required
		performTextAreaHighlight(xmlNode, 0, nameJTextArea, primarySearchArray, secondarySearch);

		XMLElementType xmlElementType = xmlElementTypeMap.get(nodeName);

		String seperator = xmlElementType.getSeperatorforLevel(0);
		boolean decodeHTML = xmlElementType.isDecodeHTML();

		for (int count = 0; count < xmlNode.getNodeElements().length; count++) {

			int index = count + 1;

			TreeMap<String, String> nameValueMap = new TreeMap<String, String>();

			String nodeValue = xmlNode.getNodeValue(index);

			if (nodeValue != null) {

				String[] nameValueTextArray = nodeValue.split(seperator);

				for (String nameValueText : nameValueTextArray) {

					String[] nameValueArray = nameValueText.split("=");

					String name = null;
					String value = null;

					switch (nameValueArray.length) {

					case 1:
						name = nameValueArray[0];
						break;

					case 2:
						name = nameValueArray[0];
						value = nameValueArray[1];

						if (decodeHTML) {
							value = URLDecoder.decode(value);
						}
						break;

					default:
						break;

					}

					if (name != null) {

						nameValueMap.put(name, value);
					}
				}
			}

			GridBagConstraints gbc2 = new GridBagConstraints();
			gbc2.gridx = count;
			gbc2.gridy = 1;
			gbc2.weightx = 1.0D;
			gbc2.weighty = 1.0D;
			gbc2.fill = GridBagConstraints.BOTH;
			gbc2.anchor = GridBagConstraints.NORTHWEST;
			gbc2.insets = new Insets(1, 2, 1, 2);

			JTable nameValueTable = getTablefromMap(nodeName, nameValueMap, xmlNode.isSearchFound(index),
					primarySearchArray[count], xmlNode.isSecondarySearchFound(index), secondarySearch);

			JScrollPane valueComponent = new JScrollPane(nameValueTable,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

			valueComponent.setBorder(border);
			valueComponent.setPreferredSize(nameValueTable.getPreferredSize());

			xmlNodeTablePanel.add(valueComponent, gbc2);
		}

		return xmlNodeTablePanel;
	}

	private JTextArea getNameJTextArea(String text, Font font) {

		final JTextArea nameJTextArea = new JTextArea();
		nameJTextArea.setRows(1);
		nameJTextArea.setEditable(false);

		nameJTextArea.setText(text);

		// Font font = getXMLTreeTable().getFont();
		nameJTextArea.setFont(font);
		nameJTextArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));

		nameJTextArea.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getButton() == MouseEvent.BUTTON3) {

					final JPopupMenu popupMenu = new JPopupMenu();

					RightClickMenuItem copy = new RightClickMenuItem("Copy");

					copy.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							nameJTextArea.copy();
							popupMenu.setVisible(false);
						}
					});

					RightClickMenuItem selectAll = new RightClickMenuItem("Select All");

					selectAll.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							nameJTextArea.selectAll();
							popupMenu.setVisible(false);

						}
					});

					popupMenu.add(copy);
					popupMenu.add(selectAll);

					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				} else {
					super.mouseClicked(e);
				}
			}

		});

		return nameJTextArea;
	}

	private JTextArea getValueJTextArea(String text, Font font) {

		final JTextArea valueJTextArea = new JTextArea();

		valueJTextArea.setRows(4);
		valueJTextArea.setEditable(false);
		valueJTextArea.setWrapStyleWord(true);

		valueJTextArea.setText(text);

		// Font font = getXMLTreeTable().getFont();
		valueJTextArea.setFont(font);
		valueJTextArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));

		valueJTextArea.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getButton() == MouseEvent.BUTTON3) {
					final JPopupMenu popupMenu = new JPopupMenu();

					RightClickMenuItem copy = new RightClickMenuItem("Copy");

					copy.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							valueJTextArea.copy();
							popupMenu.setVisible(false);
						}
					});

					RightClickMenuItem selectAll = new RightClickMenuItem("Select All");

					selectAll.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							valueJTextArea.selectAll();
							popupMenu.setVisible(false);

						}
					});

					final boolean currentWrap = valueJTextArea.getLineWrap();
					String menuItemText = currentWrap ? "Undo word wrap" : "Word wrap";

					RightClickMenuItem wordWrap = new RightClickMenuItem(menuItemText);

					wordWrap.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {

							valueJTextArea.setLineWrap(!currentWrap);

							popupMenu.setVisible(false);

						}
					});

					popupMenu.add(copy);
					popupMenu.add(selectAll);
					popupMenu.add(wordWrap);

					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				} else {
					super.mouseClicked(e);
				}
			}

		});

		return valueJTextArea;
	}

	private JTable getTablefromMap(String nodeName, TreeMap<String, String> nameValueMap, boolean primarySearchFound,
			String primarySearch, boolean secondarySearchFound, String secondarySearch) {

		DefaultTableModel dtm = new DefaultTableModel(new String[] { "Name", "Value" }, 0);

		for (Map.Entry<String, String> entrySet : nameValueMap.entrySet()) {

			dtm.addRow(new String[] { entrySet.getKey(), entrySet.getValue() });
		}

		final JTable nameValueTable = new JTable(dtm);

		nameValueTable.setRowHeight(20);

		JTableHeader tableHeader = nameValueTable.getTableHeader();

		DefaultTableCellRenderer dtcr = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);

		Font existingFont = tableHeader.getFont();
		String existingFontName = existingFont.getName();
		int existFontSize = existingFont.getSize();
		Font newFont = new Font(existingFontName, Font.BOLD, existFontSize);
		tableHeader.setFont(newFont);

		tableHeader.setReorderingAllowed(false);

		TableColumnModel tcm = nameValueTable.getColumnModel();

		for (int column = 0; column < dtm.getColumnCount(); column++) {

			TableColumn tc = tcm.getColumn(column);
			DefaultTableCellRenderer cdtcr = getDefaultTableCellRenderer(primarySearchFound, primarySearch,
					secondarySearchFound, secondarySearch);

			tc.setCellRenderer(cdtcr);
		}

		nameValueTable.setTransferHandler(new TransferHandler() {

			private static final long serialVersionUID = -985293381481032831L;

			@SuppressWarnings("unchecked")
			@Override
			protected Transferable createTransferable(JComponent c) {

				int[] selectedRows = nameValueTable.getSelectedRows();

				StringBuffer dataSB = new StringBuffer();

				if (selectedRows != null) {

					for (int selectedRow : selectedRows) {

						DefaultTableModel dtm = (DefaultTableModel) nameValueTable.getModel();

						Vector<String> row = (Vector<String>) dtm.getDataVector().elementAt(selectedRow);

						for (String col : row) {
							dataSB.append(col);
							dataSB.append("\t");
						}

						dataSB.append("\n");
					}

				}

				return new StringSelection(dataSB.toString());
			}

			@Override
			public int getSourceActions(JComponent c) {
				return TransferHandler.COPY;
			}

		});

		nameValueTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				if (SwingUtilities.isRightMouseButton(e) && e.getSource().equals(nameValueTable)) {

					final List<Integer> selectedRowList = new ArrayList<Integer>();

					int[] selectedRows = nameValueTable.getSelectedRows();

					// in case the row was not selected when right clicking then
					// based on the point, select the row.
					Point point = e.getPoint();

					if ((selectedRows != null) && (selectedRows.length <= 1)) {

						int selectedRow = nameValueTable.rowAtPoint(point);

						if (selectedRow != -1) {
							// select the row first
							nameValueTable.setRowSelectionInterval(selectedRow, selectedRow);
							selectedRows = new int[] { selectedRow };
						}
					}

					for (int selectedRow : selectedRows) {
						selectedRowList.add(selectedRow);
					}

					final int size = selectedRowList.size();

					if (size > 0) {

						final DefaultTableModel dtm = (DefaultTableModel) nameValueTable.getModel();

						final JPopupMenu popupMenu = new JPopupMenu();

						final RightClickMenuItem copyRow = new RightClickMenuItem("Copy");

						copyRow.addActionListener(new ActionListener() {

							@SuppressWarnings("unchecked")
							@Override
							public void actionPerformed(ActionEvent e) {

								Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

								StringBuffer dataSB = new StringBuffer();

								for (int selectedRow : selectedRowList) {

									Vector<String> row = (Vector<String>) dtm.getDataVector().elementAt(selectedRow);

									for (String col : row) {
										dataSB.append(col);
										dataSB.append("\t");
									}

									dataSB.append("\n");
								}

								clipboard.setContents(new StringSelection(dataSB.toString()), copyRow);

								popupMenu.setVisible(false);
							}

						});

						popupMenu.add(copyRow);

						final RightClickMenuItem copyTable = new RightClickMenuItem("Copy Table");

						copyTable.addActionListener(new ActionListener() {

							@SuppressWarnings("unchecked")
							@Override
							public void actionPerformed(ActionEvent e) {

								Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

								StringBuffer dataSB = new StringBuffer();

								Vector<Vector<String>> rows = dtm.getDataVector();

								for (Vector<String> row : rows) {

									for (String col : row) {
										dataSB.append(col);
										dataSB.append("\t");
									}

									dataSB.append("\n");
								}

								clipboard.setContents(new StringSelection(dataSB.toString()), copyTable);

								popupMenu.setVisible(false);
							}

						});

						popupMenu.add(copyTable);

						popupMenu.show(e.getComponent(), e.getX(), e.getY());
					}

				}
			}

		});

		return nameValueTable;
	}

	private DefaultTableCellRenderer getDefaultTableCellRenderer(final boolean primarySearchFound,
			final String primarySearch, final boolean secondarySearchFound, final String secondarySearch) {

		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 7231010655893711987L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {

				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				setBorder(new EmptyBorder(1, 5, 1, 1));

				if (!isSelected) {

					if (primarySearchFound || secondarySearchFound) {

						boolean isBackgroundSet = false;

						if ((value != null) && (!"".equals(value)) && (primarySearchFound)) {

							String upperText = ((String) value).toUpperCase();
							String upperPrimarySearch = primarySearch.toUpperCase();

							if (upperText.indexOf(upperPrimarySearch) > -1) {
								setBackground(MyColor.LIGHT_YELLOW);
								isBackgroundSet = true;
							}
						}

						if ((value != null) && (!"".equals(value)) && (secondarySearchFound)) {
							String upperText = ((String) value).toUpperCase();
							String upperSecondarySearch = secondarySearch.toUpperCase();

							if (upperText.indexOf(upperSecondarySearch) > -1) {
								setBackground(MyColor.LIME);
								isBackgroundSet = true;
							}
						}

						if (!isBackgroundSet) {
							if ((row % 2) == 0) {
								setBackground(MyColor.LIGHTEST_LIGHT_GRAY);
							} else {
								setBackground(Color.WHITE);
							}
						}

					} else if ((row % 2) == 0) {
						setBackground(MyColor.LIGHTEST_LIGHT_GRAY);
					} else {
						setBackground(Color.WHITE);
					}
				}
				return this;
			}

		};

		return dtcr;
	}

	private void performTextAreaHighlight(XMLNode xmlNode, int index, JTextArea jTextArea, String[] primarySearchArray,
			String secondarySearch) {

		boolean searchFound = xmlNode.isSearchFound(index);

		if (searchFound) {

			for (String searchText : primarySearchArray) {
				highlightSearchText(jTextArea, searchText, primarySearchHighlightPainter);
			}
		}

		searchFound = xmlNode.isSecondarySearchFound(index);

		if (searchFound) {
			highlightSearchText(jTextArea, secondarySearch, secondarySearchHighlightPainter);
		}

	}

	private void highlightSearchText(JTextArea jTextArea, String searchStr,
			Highlighter.HighlightPainter highlightPainter) {

		Highlighter highlighter = jTextArea.getHighlighter();

		// highlighter.removeAllHighlights();

		if ((searchStr != null) && (!"".equals(searchStr))) {

			String searchText = searchStr;
			String textAreaText = jTextArea.getText();
			textAreaText = textAreaText.toUpperCase();

			searchText = searchText.toUpperCase();

			byte[] textAreaTextBytes = textAreaText.getBytes();
			byte[] searchTextBytes = searchText.getBytes();

			try {

				int index = KnuthMorrisPrattAlgorithm.indexOf(textAreaTextBytes, searchTextBytes);

				while (index != -1) {

					int endPos = index + searchTextBytes.length;

					highlighter.addHighlight(index, endPos, highlightPainter);

					index = KnuthMorrisPrattAlgorithm.indexOf(textAreaTextBytes, searchTextBytes, endPos);
				}

			} catch (BadLocationException ble) {
				LOG.error("Error in highlight Search Text: " + searchStr, ble);
			}
		}
	}
}
