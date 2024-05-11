package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import model.User;
import model.UserDAO;
import view.DeleteUserPanel;

/**
 *
 * @author migue
 */
public class DeleteUserController implements MouseListener {

    UserDAO dao = new UserDAO();
    User user = new User();
    DeleteUserPanel deletePanel = new DeleteUserPanel();

    public DeleteUserController(DeleteUserPanel deletePanel) {
        this.deletePanel = deletePanel;
        this.deletePanel.getDeleteButton().addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        deletUser();
    }

    public void deletUser() {
        String uid = deletePanel.getUidTextField().getText();
        char[] pwdChars = deletePanel.getPwdField().getPassword();
        String pwd = new String(pwdChars);
        if (uid.isEmpty() || uid.isBlank()) {
            JOptionPane.showMessageDialog(deletePanel, "Debe ingresar su identificación.");
        } else if (pwd.isEmpty() || pwd.isBlank()) {
            JOptionPane.showMessageDialog(deletePanel, "Debe ingresar una contraseña.");
        } else {
            int records = dao.deleteUser(uid, pwd);
            if (records == 1) {
                JOptionPane.showMessageDialog(deletePanel, "El usuario ha sido eliminado.");
            }
            deletePanel.getUidTextField().setText("");
            deletePanel.getPwdField().setText("");
        }
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
