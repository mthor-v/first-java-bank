package controller;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.User;
import model.UserDAO;
import view.MainJFrame;
import view.UsersListPanel;

/**
 *
 * @author migue
 */
public class UsersListController implements MouseListener {

    UserDAO dao;
    UsersListPanel listPanel = new UsersListPanel();
    DefaultTableModel tableModel = new DefaultTableModel();
    JTable genericTable = new JTable();

    public UsersListController(UsersListPanel listPanel, UserDAO dao, MainJFrame mainFrame) {
        this.dao = dao;
        this.listPanel = listPanel;
        genericTable = listPanel.getUsersTable();
        mainFrame.getUsersListBtn().addMouseListener(this);
    }

    public void displayUsersList(JTable table) {
        SwingUtilities.invokeLater(() -> {
            tableModel = (DefaultTableModel) table.getModel();
            tableModel.setRowCount(0);
            try {
                List<User> usersList = dao.getUsersList();
                for (User user : usersList) {
                    Object[] row = {user.getUid(), user.getName()};
                    tableModel.addRow(row);
                }
                tableModel.fireTableDataChanged();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        displayUsersList(genericTable);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
