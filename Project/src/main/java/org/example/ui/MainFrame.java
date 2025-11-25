package org.example.ui;

import org.example.dao.BookDAO;
import org.example.model.Book;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private final BookDAO dao = new BookDAO();
    private final BookTableModel model = new BookTableModel();
    private final JTable table = new JTable(model);

    public MainFrame() {
        super("Library App");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        JButton add = new JButton("Add"), edit = new JButton("Edit"), del = new JButton("Delete"), issue = new JButton("Issue/Return"), refresh = new JButton("Refresh");
        top.add(add); top.add(edit); top.add(del); top.add(issue); top.add(refresh);
        add(top, BorderLayout.NORTH);

        add(new JScrollPane(table), BorderLayout.CENTER);

        add.addActionListener(e -> openAdd());
        edit.addActionListener(e -> openEdit());
        del.addActionListener(e -> deleteRow());
        issue.addActionListener(e -> toggleIssue());
        refresh.addActionListener(e -> loadData());

        loadData();
    }

    private void loadData() {
        try { model.setData(dao.getAll()); }
        catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void openAdd() {
        BookDialog d = new BookDialog(this);
        d.setVisible(true);
        if (!d.isSaved()) return;
        try { dao.add(d.getBook()); loadData(); } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void openEdit() {
        int r = table.getSelectedRow(); if (r < 0) return;
        int modelRow = table.convertRowIndexToModel(r);
        Book b = model.get(modelRow);
        BookDialog d = new BookDialog(this, b);
        d.setVisible(true);
        if (!d.isSaved()) return;
        try { dao.update(d.getBook()); loadData(); } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void deleteRow() {
        int r = table.getSelectedRow(); if (r < 0) return;
        int modelRow = table.convertRowIndexToModel(r);
        int id = model.get(modelRow).getId();
        if (JOptionPane.showConfirmDialog(this, "Delete?") != JOptionPane.YES_OPTION) return;
        try { dao.delete(id); loadData(); } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void toggleIssue() {
        int r = table.getSelectedRow(); if (r < 0) return;
        int modelRow = table.convertRowIndexToModel(r);
        Book b = model.get(modelRow);
        b.setIssued(!b.isIssued());
        try { dao.update(b); loadData(); } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private static class BookTableModel extends AbstractTableModel {
        private final String[] cols = {"ID","Title","Author","Copies","Issued"};
        private java.util.List<Book> data = java.util.Collections.emptyList();
        void setData(java.util.List<Book> d) { data = d; fireTableDataChanged(); }
        Book get(int row) { return data.get(row); }
        @Override public int getRowCount() { return data.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int col) { return cols[col]; }
        @Override public Object getValueAt(int row, int col) {
            Book b = data.get(row);
            return switch (col) {
                case 0 -> b.getId();
                case 1 -> b.getTitle();
                case 2 -> b.getAuthor();
                case 3 -> b.getCopies();
                case 4 -> b.isIssued() ? "Yes" : "No";
                default -> "";
            };
        }
    }
}
