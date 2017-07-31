/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

public class AutoCompleteJComboBox<T> extends JComboBox<T> {

	private static final long serialVersionUID = 4197556001817311743L;

	private boolean selecting;

	private JTextComponent editor;

	private KeyListener editorKeyListener;

	private FocusListener editorFocusListener;

	private AutocompleteDocument autocompleteDocument;

	public AutoCompleteJComboBox() {
		super();
		initialize();
	}

	public AutoCompleteJComboBox(ComboBoxModel<T> aModel) {
		super(aModel);
		initialize();
	}

	public AutoCompleteJComboBox(T[] items) {
		super(items);
		initialize();
	}

	public AutoCompleteJComboBox(Vector<T> items) {
		super(items);
		initialize();
	}

	protected boolean isSelecting() {
		return selecting;
	}

	protected void setSelecting(boolean aSelecting) {
		selecting = aSelecting;
	}

	private void initialize() {

		selecting = false;

		setEditable(true);

		autocompleteDocument = new AutocompleteDocument();

		editorKeyListener = new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {

				if (isDisplayable()) {
					setPopupVisible(true);
				}
			}
		};

		editorFocusListener = new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				highlightCompletedText(0);
			}
		};

		configureEditor(getEditor());

		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isSelecting()) {
					highlightCompletedText(0);
				}
			}
		});

		addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {

				if (evt.getPropertyName().equals("editor")) {
					configureEditor((ComboBoxEditor) evt.getNewValue());
				}
			}
		});

		// highlightCompletedText(0);
	}

	protected void highlightCompletedText(int start) {

		int length = editor.getDocument().getLength();

		editor.setCaretPosition(length);
		editor.moveCaretPosition(start);
	}

	protected void configureEditor(ComboBoxEditor newEditor) {

		if (editor != null) {
			editor.removeKeyListener(editorKeyListener);
			editor.removeFocusListener(editorFocusListener);
		}

		if (newEditor != null) {
			editor = (JTextComponent) newEditor.getEditorComponent();
			editor.addKeyListener(editorKeyListener);
			editor.addFocusListener(editorFocusListener);
			editor.setDocument(autocompleteDocument);
		}
	}

	private class AutocompleteDocument extends PlainDocument {

		private static final long serialVersionUID = -5517539871854864808L;

		public AutocompleteDocument() {
			super();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.text.PlainDocument#insertString(int,
		 * java.lang.String, javax.swing.text.AttributeSet)
		 */
		@Override
		public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {

			int offs = offset;

			// return immediately when selecting an item
			if (isSelecting()) {
				return;
			}

			// insert the string into the document
			super.insertString(offs, str, a);
			// lookup and select a matching item
			Object item = lookupItem(getText(0, getLength()));

			if (item != null) {
				setSelectedItem(item);
			} else {
				// keep old item selected if there is no match
				item = AutoCompleteJComboBox.this.getSelectedItem();
				// imitate no insert (later on offs will be incremented by
				// str.length(): selection won't move forward)
				offs = offs - str.length();

				AutoCompleteJComboBox.this.getToolkit().beep();
			}

			setText(item.toString());
			// select the completed part
			highlightCompletedText(offs + str.length());
		}

		private Object lookupItem(String pattern) {

			ComboBoxModel<T> comboBoxModel = AutoCompleteJComboBox.this.getModel();

			Object selectedItem = comboBoxModel.getSelectedItem();
			// only search for a different item if the currently selected does
			// not match
			if (selectedItem != null && startsWithIgnoreCase(selectedItem.toString(), pattern)) {
				return selectedItem;
			} else {
				// iterate over all items
				for (int i = 0, n = comboBoxModel.getSize(); i < n; i++) {
					Object currentItem = comboBoxModel.getElementAt(i);
					// current item starts with the pattern?
					if (currentItem != null && startsWithIgnoreCase(currentItem.toString(), pattern)) {
						return currentItem;
					}
				}
			}
			// no item starts with the pattern => return null
			return null;
		}

		private boolean startsWithIgnoreCase(String str1, String str2) {
			return str1.toUpperCase().startsWith(str2.toUpperCase());
		}

		private void setText(String text) {
			try {
				// remove all text and insert the completed string
				super.remove(0, getLength());
				super.insertString(0, text, null);
			} catch (BadLocationException e) {
				throw new RuntimeException(e.toString());
			}
		}
	}
}
