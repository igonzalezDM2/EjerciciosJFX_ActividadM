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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.stage.Stage;
import model.Aeropuerto;
import model.Avion;
import utilities.Utilidades;

public class AnadirAvionController implements Initializable {
	
	private AeropuertosController controladorPrincipal;
	private Scene escena;

	@FXML
    Button btnCancelar;

    @FXML
    Button btnGuardar;

    @FXML
    private ComboBox<Aeropuerto> cbAeropuerto;

    @FXML
    private RadioButton rbActivado;

    @FXML
    private RadioButton rbDesactivado;

    @FXML
    private TextField tfAsientos;

    @FXML
    private TextField tfModelo;

    @FXML
    private TextField tfVelMax;

    @FXML
    private ToggleGroup tgActivo;
    
    public void setControladorPrincipal(AeropuertosController controladorPrincipal) {
		this.controladorPrincipal = controladorPrincipal;
	}
    
    public void setEscena(Scene escena) {
    	this.escena = escena;
    }

    @FXML
    void cancelar(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
    }

    @FXML
    void guardar(ActionEvent event) {
    	try {
			try {
				comprobarDatos();
				Avion avion = construirAvion();
				DAOAviones.checkExiste(avion);
				DAOAviones.anadirAvion(avion);
				Alert alert = new Alert(AlertType.INFORMATION, "El aeropuerto fue insertado", ButtonType.OK);
				alert.show();
				resetearCampos();
				controladorPrincipal.filtrarFilas();
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
	    		alert.showAndWait();
				e.printStackTrace();
			}			
			
		} catch (AeropuertosException e) {
			Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
    		alert.showAndWait();
    		e.printStackTrace();
		}
    	
    }
    
	private void comprobarDatos() throws AeropuertosException  {
		AnadirAeropuertoController.checkCampoStrNotNull(tfModelo);
		AnadirAeropuertoController.checkCampoStrNotNull(tfAsientos);
		AnadirAeropuertoController.checkCampoStrNotNull(tfVelMax);
		AnadirAeropuertoController.checkCampoInt(tfAsientos);
		AnadirAeropuertoController.checkCampoInt(tfVelMax);
		Aeropuerto aeropuerto = cbAeropuerto.getSelectionModel().getSelectedItem();
		if (aeropuerto == null) {
			Alert alert = new Alert(AlertType.ERROR, "Debes seleccionar un aeropuerto", ButtonType.OK);
    		alert.showAndWait();
		}
		
	}
	
	private Avion construirAvion() throws AeropuertosException {
		return new Avion()
				.setActivado(rbActivado.equals(tgActivo.getSelectedToggle()))
				.setAeropuerto(cbAeropuerto.getSelectionModel().getSelectedItem())
				.setModelo(tfModelo.getText())
				.setNumeroAsientos(Utilidades.parseInt(tfAsientos.getText()))
				.setVelocidadMaxima(Utilidades.parseInt(tfVelMax.getText()));
	}
	
	private void resetearCampos() {
		tfModelo.clear();
		tfAsientos.clear();
		tfVelMax.clear();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			cbAeropuerto.getItems().addAll(DAOAeropuertos.getAeropuertos(null));
			cbAeropuerto.getSelectionModel().selectFirst();
		} catch (AeropuertosException e) {
			Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
    		alert.showAndWait();
    		e.printStackTrace();
		}
		
		
	}

}
