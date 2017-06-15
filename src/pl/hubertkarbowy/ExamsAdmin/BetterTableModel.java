package pl.hubertkarbowy.ExamsAdmin;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

abstract class BetterTableModel extends DefaultTableModel {
	
	abstract void clear();
	abstract void update(List<List<String>> newData);
	abstract void addRow2(List<String> newrow);
}
