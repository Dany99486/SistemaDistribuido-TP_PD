package pt.isec.PD.ClientUI.Controllers;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static pt.isec.PD.ClientUI.MainJFX.controller;

public class InserirPresencasController {
    public TextField nomeField;
    public TextField emailField;
    public Button btnInserir;

    public void handleInserir(){
        String nome = nomeField.getText();
        String email = emailField.getText();
        if(!nome.isEmpty() && !email.isEmpty()) {
            if(!controller.inserirPresencas(nome, email)){
                System.out.println("Erro ao inserir presenças");
                nomeField.clear();
                emailField.clear();
            } else {
                System.out.println("Presenças inseridas com sucesso");
                // Fechar o modal
                Stage stage = (Stage) btnInserir.getScene().getWindow();
                stage.close();
            }
        }
    }

}
