package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import model.UserDAO;
import view.EditUserPanel;

/**
 *
 * @author migue
 */
public class UpdateUserController implements MouseListener {

    UserDAO dao;
    EditUserPanel updatePanel = new EditUserPanel();

    public UpdateUserController(EditUserPanel updatePanel, UserDAO dao) {
        this.dao = dao;
        this.updatePanel = updatePanel;
        this.updatePanel.getUpdateBuetton().addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        updatePassword();
    }

    public void updatePassword() {
        String uid = updatePanel.getUidField().getText();
        char[] oldPwdChars = updatePanel.getOldPwdField().getPassword();
        String oldPwd = new String(oldPwdChars);
        char[] newPwdChars = updatePanel.getNewPwdField().getPassword();
        String newPwd = new String(newPwdChars);
        if (uid.isEmpty() || uid.isBlank()) {
            JOptionPane.showMessageDialog(updatePanel, "Debe ingresar su identificación.");
        } else if (oldPwd.isEmpty() || oldPwd.isBlank()) {
            JOptionPane.showMessageDialog(updatePanel, "Debe ingresar la contraseña actual.");
        } else if (newPwd.isEmpty() || newPwd.isBlank()) {
            JOptionPane.showMessageDialog(updatePanel, "Debe ingresar una nueva contraseña.");
        } else {
            int records = dao.updatePassword(uid, oldPwd, newPwd);
            if (records == 1) {
                JOptionPane.showMessageDialog(updatePanel, "Actualización exitosa.");
            }
            updatePanel.getUidField().setText("");
            updatePanel.getOldPwdField().setText("");
            updatePanel.getNewPwdField().setText("");
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
