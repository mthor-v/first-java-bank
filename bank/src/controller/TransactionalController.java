package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import model.User;
import model.UserDAO;
import view.TransactionalPanel;

/**
 *
 * @author migue
 */
public class TransactionalController implements ActionListener{

    UserDAO dao;
    User user = null;
    TransactionalPanel transactionalPanel;
    JTextPane infoPane;
    JTextField depositField;
    JTextField withdrawField;
    JTextField transferField;
    JTextField receiverField;
    JButton loginBtn;
    JButton logoutBtn;
    JButton depositBtn;
    JButton withdrawBtn;
    JButton transferBtn;
    DecimalFormat formatter = new DecimalFormat("#,###.##");
    
    public TransactionalController(TransactionalPanel transactionalPanel, UserDAO dao) {
        System.out.println("Constructor controlador");
        depositField = transactionalPanel.getDepositField();
        withdrawField = transactionalPanel.getWithdrawField();
        transferField = transactionalPanel.getTransferField();
        receiverField = transactionalPanel.getReceiverUIDField();
        loginBtn = transactionalPanel.getLoginButton();
        logoutBtn = transactionalPanel.getLogoutButton();
        depositBtn = transactionalPanel.getDepositButton();
        withdrawBtn = transactionalPanel.getWithdrawButton();
        transferBtn = transactionalPanel.getTransferButton();
        this.dao = dao;
        this.transactionalPanel = transactionalPanel;
        this.transactionalPanel.getLoginButton().addActionListener(this);
        this.transactionalPanel.getLogoutButton().addActionListener(this);
        this.transactionalPanel.getDepositButton().addActionListener(this);
        this.transactionalPanel.getWithdrawButton().addActionListener(this);
        this.transactionalPanel.getTransferButton().addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == transactionalPanel.getLoginButton()) {
            userLogin();
        } else if (e.getSource() == transactionalPanel.getLogoutButton()) {
            userLogout();
        } else if (e.getSource() == transactionalPanel.getDepositButton()) {
            makeDeposit();
        } else if (e.getSource() == transactionalPanel.getWithdrawButton()) {
            makeWithdraw();
        } else if (e.getSource() == transactionalPanel.getTransferButton()) {
            makeTransfer();
        }
    }   
    
    public void userLogin(){
        String uid = transactionalPanel.getUserUIDField().getText();
        char[] pwdChars = transactionalPanel.getUserPwdField().getPassword();
        String pwd = new String(pwdChars);
        if (uid.isEmpty() || uid.isBlank()) {
            JOptionPane.showMessageDialog(transactionalPanel, "Debe ingresar su identificación.");
        } else if (pwd.isEmpty() || pwd.isBlank()) {
            JOptionPane.showMessageDialog(transactionalPanel, "Debe ingresar una contraseña.");
        } else {
            boolean match = dao.checkPassword(uid, pwd);
            if (match) {
                try {
                    User newUser = dao.getUserByUID(uid);
                    user = newUser;
                    updateTextPane(user);
                    transactionalPanel.getUserPwdField().setText("");
                    changeCommands(match);
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                    JOptionPane.showMessageDialog(transactionalPanel, e.getMessage());
                }
            }
        }
    }
    
    public void updateTextPane(User user) {
        String amount = formatter.format(user.getBalance());
        String info = String.format("Bienvenido %s.\nSaldo actual $ %s", 
                user.getName(), amount);
        infoPane = transactionalPanel.getInfoPane();
        infoPane.setText(info);
    }
    
    public void userLogout() {
        transactionalPanel.getUserUIDField().setText("");
        infoPane = transactionalPanel.getInfoPane();
        infoPane.setText("");
        changeCommands(false);
    }
    
    public void makeDeposit() {
        String amount = depositField.getText();
        if (amount.isEmpty() || amount.isBlank()){
            JOptionPane.showMessageDialog(transactionalPanel, "Debe ingresar un valor.");
        } else { 
            try {
                Integer intAmount = Integer.valueOf(amount);
                String formatAmount = formatter.format(intAmount);
                int confirm = JOptionPane.showConfirmDialog(transactionalPanel, 
                        String.format("¿Realizar consignación por $%s?", formatAmount));
                if (confirm == JOptionPane.YES_OPTION){
                    int value = user.getBalance() + intAmount;
                    int updated = dao.updateBalance(value, user.getUid());
                    if (updated > 0){
                        JOptionPane.showMessageDialog(transactionalPanel, 
                            String.format("Consignación exitosa: $%s.", formatAmount));
                    }
                    user = dao.getUserByUID(user.getUid());
                    infoPane.setText("");
                    updateTextPane(user);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(transactionalPanel, "Ingrese un monto numérico válido.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(transactionalPanel, e.getMessage());
            } finally {
                depositField.setText("");
            }
        }
    }
    
    public void makeWithdraw() {
        String amount = withdrawField.getText();
        if (amount.isEmpty() || amount.isBlank()){
            JOptionPane.showMessageDialog(transactionalPanel, "Debe ingresar un valor.");
        } else { 
            try {
                if (Integer.valueOf(amount) > user.getBalance()) {
                    JOptionPane.showMessageDialog(transactionalPanel, "Saldo insuficiente");
                } else {
                    Integer intAmount = Integer.valueOf(amount);
                    String formatAmount = formatter.format(intAmount);
                    int confirm = JOptionPane.showConfirmDialog(transactionalPanel, 
                            String.format("¿Realizar retiro de $%s?", formatAmount));
                    if (confirm == JOptionPane.YES_OPTION){
                        int value = user.getBalance() - Integer.valueOf(amount);
                        int updated = dao.updateBalance(value, user.getUid());
                        if (updated > 0){
                            JOptionPane.showMessageDialog(transactionalPanel, 
                                String.format("Retiro exitoso: $%s.", formatAmount));
                        }
                        user = dao.getUserByUID(user.getUid());
                        infoPane.setText("");
                        updateTextPane(user);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(transactionalPanel, "Ingrese un monto numérico válido.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(transactionalPanel, e.getMessage());
            } finally {
                withdrawField.setText("");
            }
        }
    }
    
    public void makeTransfer() {
        String amount = transferField.getText();
        String receiver = receiverField.getText();
        if (amount.isEmpty() || amount.isBlank()){
            JOptionPane.showMessageDialog(transactionalPanel, "Debe ingresar un valor.");
        } else if (receiver.isEmpty() || receiver.isBlank()){
            JOptionPane.showMessageDialog(transactionalPanel, "Debe ingresar identificación del destinatario.");
        } else { 
            try {
                User receiverUser = dao.getUserByUID(receiver);
                if (Integer.valueOf(amount) > user.getBalance()) {
                    JOptionPane.showMessageDialog(transactionalPanel, "Saldo insuficiente.");
                } else if (receiverUser == null) {
                    JOptionPane.showMessageDialog(transactionalPanel, "Destinatario no existe.");
                } else {
                    Integer intAmount = Integer.valueOf(amount);
                    String formatAmount = formatter.format(intAmount);
                    int confirm = JOptionPane.showConfirmDialog(transactionalPanel, 
                            String.format("¿Realizar transferencia de $%s a usuario %s?", formatAmount, receiver));
                    if (confirm == JOptionPane.YES_OPTION){
                        int newUserBalance = user.getBalance() - Integer.valueOf(amount);
                        int newReceiverBalance = receiverUser.getBalance() + Integer.valueOf(amount);
                        int userUpdated = dao.updateBalance(newUserBalance, user.getUid());
                        int receiverUpdated = dao.updateBalance(newReceiverBalance, receiverUser.getUid());
                        if (userUpdated > 0 && receiverUpdated > 0){
                            JOptionPane.showMessageDialog(transactionalPanel, 
                                String.format("Transfirió $%s a %s.", formatAmount,receiverUser.getName()));
                        }
                        user = dao.getUserByUID(user.getUid());
                        infoPane.setText("");
                        updateTextPane(user);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(transactionalPanel, "Ingrese un monto numérico válido.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(transactionalPanel, e.getMessage());
            } finally {
                transferField.setText("");
                receiverField.setText("");
            }
        }
    }
    
    public void changeCommands(boolean action) {
        depositField.setEditable(action);
        withdrawField.setEditable(action);
        transferField.setEditable(action);
        receiverField.setEditable(action);
        loginBtn.setEnabled(!action);
        logoutBtn.setEnabled(action);
        depositBtn.setEnabled(action);
        withdrawBtn.setEnabled(action);
        transferBtn.setEnabled(action);
    }
}
