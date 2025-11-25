package org.example.ui;

import org.example.model.Book;

import javax.swing.*;
import java.awt.*;

public class BookDialog extends JDialog {
    private final JTextField idField = new JTextField(6);
    private final JTextField titleField = new JTextField(20);
    private final JTextField authorField = new JTextField(20);
    private final JSpinner copiesSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
    private boolean saved = false;

    public BookDialog(Window owner) { this(owner, null); }

    public BookDialog(Window owner, Book b) {
        super(owner, "Book", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout(8,8));
        JPanel p = new JPanel(new GridLayout(0,2,8,8));
        p.add(new JLabel("ID:")); p.add(idField);
        p.add(new JLabel("Title:")); p.add(titleField);
        p.add(new JLabel("Author:")); p.add(authorField);
        p.add(new JLabel("Copies:")); p.add(copiesSpinner);
        add(p, BorderLayout.CENTER);
        JPanel btn = new JPanel();
        JButton save = new JButton("Save"), cancel = new JButton("Cancel");
        btn.add(save); btn.add(cancel);
        add(btn, BorderLayout.SOUTH);

        if (b != null) {
            idField.setText(String.valueOf(b.getId())); idField.setEnabled(false);
            titleField.setText(b.getTitle()); authorField.setText(b.getAuthor()); copiesSpinner.setValue(b.getCopies());
        }

        save.addActionListener(e -> {
            if (titleField.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Title required"); return; }
            if (idField.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"ID required"); return; }
            saved = true; setVisible(false);
        });

        cancel.addActionListener(e -> { saved = false; setVisible(false); });

        pack(); setResizable(false);
    }

    public boolean isSaved() { return saved; }

    public Book getBook() {
        Book b = new Book();
        b.setId(Integer.parseInt(idField.getText().trim()));
        b.setTitle(titleField.getText().trim());
        b.setAuthor(authorField.getText().trim());
        b.setCopies((Integer) copiesSpinner.getValue());
        b.setIssued(false);
        return b;
    }
}
