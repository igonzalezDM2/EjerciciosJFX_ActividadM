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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.Aeropuerto;
import model.Avion;

public class ActivarAvionController implements Initializable {
	
	private Scene escena;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnGuardar;

    @FXML
    private ComboBox<Aeropuerto> cbAeropuertos;

    @FXML
    private ComboBox<Avion> cbAviones;

    @FXML
    private RadioButton rbActivado;

    @FXML
    private RadioButton rbDesactivado;

    @FXML
    private ToggleGroup tgActivo;

	private AeropuertosController controladorPrincipal;

    @FXML
    void cancelar(ActionEvent event) {
    	((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void guardar(ActionEvent event) {
    	try {
    		Avion avion = cbAviones.getSelectionModel().getSelectedItem();
    		avion.setActivado(rbActivado.equals(tgActivo.getSelectedToggle()));
			DAOAviones.modificarAvion(avion);
			Alert alert = new Alert(AlertType.INFORMATION, "El avión fue modificado correctamente", ButtonType.OK);
			alert.show();
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
			cambiarActivado(null);
		} catch (AeropuertosException e) {
			Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			alert.showAndWait();
			e.printStackTrace();
		}
    }
    
    @FXML
    void cambiarActivado(ActionEvent event) {
		Avion avion = cbAviones.getSelectionModel().getSelectedItem();	
		if (avion != null) {			
			if (avion.isActivado()) {
				tgActivo.selectToggle(rbActivado);
			} else {
				tgActivo.selectToggle(rbDesactivado);
			}
		}
    }
    
	public ActivarAvionController setControladorPrincipal(AeropuertosController controladorPrincipal) {
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
    	Mnemonic mnGuardar = new Mnemonic(btnGuardar, kccGuardar);
    	escena.addMnemonic(mnGuardar);
    	
    	KeyCharacterCombination kccCerrar = new KeyCharacterCombination("C", KeyCombination.ALT_DOWN);
    	Mnemonic mnCerrar = new Mnemonic(btnCancelar, kccCerrar);
    	escena.addMnemonic(mnCerrar);
	}
	
}
