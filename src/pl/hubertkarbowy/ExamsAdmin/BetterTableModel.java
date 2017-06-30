package pl.hubertkarbowy.ExamsAdmin;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * Provides additional functionality for the {@link DefaultTableModel}.
 * Some methods are overwritten and some are added for better handling of automatic table refreshing.
 *
 */
abstract class BetterTableModel extends DefaultTableModel {
	
	/**
	 * Deletes everything from the table model.
	 */
	abstract void clear();
	/**
	 * Extends the functionality of {@link DefaultTableModel#getValueAt(int, int)} by allowing to pass extra parameters as varargs. 
	 * @param rowIndex Row index
	 * @param columnIndex Column index
	 * @param args Varargs with additional parameters which can be handled by the implementing class. 
	 */
	public Object getValueAt(int rowIndex, int columnIndex, String... args) {return new Object();}
	/**
	 * Implementation-dependend method. 
	 * @param newData - row to be updated as list of Strings.
	 */
	abstract void update(List<List<String>> newData);
	/**
	 * User-friendly version of {@link DefaultTableModel#addRow(Vector)} from {@link DefaultTableModel}
	 * Works with lists rather than vectors or arrays of {@link Object}s.
	 * @param newrow row to be added as list of Strings.
	 */
	abstract void addRow2(List<String> newrow);
}
