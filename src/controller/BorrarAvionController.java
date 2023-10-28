package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import dao.DAOAeropuertos;
import dao.DAOAviones;
import excepciones.AeropuertosException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.Aeropuerto;
import model.Avion;

public class BorrarAvionController implements Initializable {
	
	private Scene escena;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnBorrar;

    @FXML
    private ComboBox<Aeropuerto> cbAeropuertos;

    @FXML
    private ComboBox<Avion> cbAviones;

	private AeropuertosController controladorPrincipal;

    @FXML
    void cancelar(ActionEvent event) {
    	((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void borrar(ActionEvent event) {
    	try {
    		Avion avion = cbAviones.getSelectionModel().getSelectedItem();
			DAOAviones.borrarAvion(avion);
			Alert alert = new Alert(AlertType.INFORMATION, "El avión fue borrado correctamente", ButtonType.OK);
			alert.show();
			((Stage)((Node)event.getSource()).getScene().getWindow()).close();
			controladorPrincipal.filtrarFilas();
		} catch (AeropuertosException | SQLException e) {
			Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			alert.showAndWait();
			e.printStackTrace();
		}
    }
    
    @FXML
    void cambiarAviones(ActionEvent event) {
		Aeropuerto aeropuerto = cbAeropuertos.getSelectionModel().getSelectedItem();
		try {
			cbAviones.getItems().clear();
			cbAviones.getItems().addAll(DAOAviones.getAviones(aeropuerto));
			cbAviones.getSelectionModel().selectFirst();
		} catch (AeropuertosException e) {
			Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			alert.showAndWait();
			e.printStackTrace();
		}
    }
    
	public BorrarAvionController setControladorPrincipal(AeropuertosController controladorPrincipal) {
		this.controladorPrincipal = controladorPrincipal;
		return this;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			cbAeropuertos.getItems().addAll(DAOAeropuertos.getAeropuertos(null));
			cbAeropuertos.getSelectionModel().selectFirst();
			cambiarAviones(null);
		} catch (AeropuertosException e) {
			Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			alert.showAndWait();
		}
	}

	public void setEscena(Scene escena) {
		this.escena = escena;
    	KeyCharacterCombination kccGuardar = new KeyCharacterCombination("G", KeyCombination.ALT_DOWN);
    	Mnemonic mnGuardar = new Mnemonic(btnBorrar, kccGuardar);
    	escena.addMnemonic(mnGuardar);
    	
    	KeyCharacterCombination kccCerrar = new KeyCharacterCombination("C", KeyCombination.ALT_DOWN);
    	Mnemonic mnCerrar = new Mnemonic(btnCancelar, kccCerrar);
    	escena.addMnemonic(mnCerrar);
	}
	
}
