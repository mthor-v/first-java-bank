package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import model.User;
import model.UserDAO;
import view.CreateUserPanel;

/**
 *
 * @author migue
 */
public class CreateUserController implements MouseListener {

    UserDAO dao;
    User user;
    CreateUserPanel createPanel = new CreateUserPanel();

    public CreateUserController(CreateUserPanel createPanel, UserDAO dao) {
        this.dao = dao;
        this.createPanel = createPanel;
        this.createPanel.getRegisterBtn().addMouseListener(this);
    }

    public void insertUser() {
        String name = createPanel.getUserNameTextField().getText();
        String uid = createPanel.getIdTextField().getText();
        char[] pwdChars = createPanel.getPwdField().getPassword();
        String pwd = new String(pwdChars);
        if (name.isEmpty() || name.isBlank()) {
            JOptionPane.showMessageDialog(createPanel, "Debe ingresar su nombre.");
        } else if (uid.isEmpty() || uid.isBlank()) {
            JOptionPane.showMessageDialog(createPanel, "Debe ingresar su identificación.");
        } else if (pwd.isEmpty() || pwd.isBlank()) {
            JOptionPane.showMessageDialog(createPanel, "Debe ingresar una contraseña.");
        } else {
            user = new User(name, uid, pwd);
            int records = dao.createUser(user);
            if (records == 1) {
                JOptionPane.showMessageDialog(createPanel, "Registro exitoso");
            }
            createPanel.getUserNameTextField().setText("");
            createPanel.getIdTextField().setText("");
            createPanel.getPwdField().setText("");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        insertUser();
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
