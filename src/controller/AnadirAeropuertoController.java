package controller;

import static utilities.Utilidades.parseDouble;
import static utilities.Utilidades.parseInt;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dao.DAOAeropuertos;
import enums.TipoAeropuerto;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Aeropuerto;
import model.Direccion;

public class AnadirAeropuertoController implements Initializable {

	private TableView<Aeropuerto> tablaAeropuertos;
	private Aeropuerto seleccionado;
	private AeropuertosController controladorPrincipal;
	private Scene escena;
	
	private byte[] imagenCargada;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnGuardar;
    
    @FXML
    private Button btnImagen;
    
    @FXML
    private ImageView ivImagen;

    @FXML
    private GridPane gridVariable;

    @FXML
    private Label labelVariable1;

    @FXML
    private Label labelVariable2;

    @FXML
    private RadioButton rbPrivado;

    @FXML
    private RadioButton rbPublico;

    @FXML
    private TextField tfAno;

    @FXML
    private TextField tfCalle;

    @FXML
    private TextField tfCapacidad;

    @FXML
    private TextField tfCiudad;

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfNumero;

    @FXML
    private TextField tfPais;

    @FXML
    private TextField tfVariable1;

    @FXML
    private TextField tfVariable2;

    @FXML
    private ToggleGroup tgTipo;

    @FXML
    void cancelar(ActionEvent event) {
    	((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void guardar(ActionEvent event) {
    	try {
			comprobarDatos();
			if (seleccionado != null) {				
				DAOAeropuertos.modificarAeropuerto(construirAeropuerto());
			} else {
				DAOAeropuertos.anadirAeropuerto(construirAeropuerto());
			}
			//AVISAR DE QUE SE HA INSERTADO Y CERRAR LA MODAL
			Alert alert = new Alert(AlertType.INFORMATION, "El aeropuerto fue insertado", ButtonType.OK);
			alert.show();
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.close();
			controladorPrincipal.filtrarFilas();
		} catch (AeropuertosException e) {
    		Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
    		alert.showAndWait();
    		e.printStackTrace();
		} catch (SQLException e) {
			Alert alert = new Alert(AlertType.ERROR, "La conexión con la BD no se pudo cerrar", ButtonType.OK);
    		alert.showAndWait();
    		e.printStackTrace();
		}
    }
    
    @FXML
    void cargarImagen(ActionEvent event) {
    	//TODO: sacar el filechooser
    	FileChooser fc = new FileChooser();
    	fc.setInitialDirectory(new File(System.getProperty("user.dir")));
    	fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
    	File archivo = fc.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
    	if (archivo != null) {
    		try (FileInputStream fis = new FileInputStream(archivo)) {
    			imagenCargada = fis.readAllBytes();
				seleccionado.setImagen(imagenCargada);
				ponerImagen();
			} catch (FileNotFoundException e) {
	    		Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
	    		alert.showAndWait();
	    		e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tgTipo.selectedToggleProperty().addListener(e -> {
			if (rbPrivado == tgTipo.getSelectedToggle()) {
				labelVariable1.setText("Número de socios");
				tfVariable1.setText("");
				labelVariable2.setVisible(false);
				tfVariable2.setVisible(false);
			} else {
				labelVariable1.setText("Financiación:");
				tfVariable1.setText("");
				labelVariable2.setText("Número de trabajadores:");
				tfVariable2.setText("");
				labelVariable2.setVisible(true);
				tfVariable2.setVisible(true);
			}
		});
	}
	
	public AnadirAeropuertoController setTablaAeropuertos(TableView<Aeropuerto> tabla) {
		this.tablaAeropuertos = tabla;
		return this;
	}

	public AnadirAeropuertoController setControladorPrincipal(AeropuertosController controladorPrincipal) {
		this.controladorPrincipal = controladorPrincipal;
		return this;
	}
	
	public AnadirAeropuertoController setSeleccionado(Aeropuerto seleccionado) {
		this.seleccionado = seleccionado;
		rellenarEdicion();
		ponerImagen();
		return this;
	}
	
	private void comprobarDatos() throws AeropuertosException {
		checkCampoStrNotNull(tfNombre);
		checkCampoStrNotNull(tfPais);
		checkCampoStrNotNull(tfCiudad);
		checkCampoStrNotNull(tfCalle);
		checkCampoInt(tfNumero);
		checkCampoInt(tfAno);
		checkCampoInt(tfCapacidad);
		if (rbPrivado.isSelected()) {
			checkCampoInt(tfVariable1);
		} else {
			checkCampoDouble(tfVariable1);
			checkCampoInt(tfVariable2);
		}
	}
	
	private Aeropuerto construirAeropuerto() throws AeropuertosException {
		Aeropuerto aeropuerto = new Aeropuerto()
				.setNombre(tfNombre.getText())
				.setDireccion(new Direccion()
						.setPais(tfPais.getText())
						.setCiudad(tfCiudad.getText())
						.setCalle(tfCalle.getText())
						.setNumero(parseInt(tfNumero.getText()))
						)
				.setAnioInauguracion(parseInt(tfAno.getText()))
				.setCapacidad(parseInt(tfCapacidad.getText()))
				.setImagen(imagenCargada);
		if (rbPrivado.isSelected()) {
			aeropuerto.setTipo(TipoAeropuerto.PRIVADO);
			aeropuerto.setNumeroSocios(parseInt(tfVariable1.getText()));
		} else {
			aeropuerto.setTipo(TipoAeropuerto.PUBLICO);
			aeropuerto.setFinanciacion(parseDouble(tfVariable1.getText()));
			aeropuerto.setNumTrabajadores(parseInt(tfVariable2.getText()));
		}
		
		if (seleccionado != null) {
			aeropuerto.setId(seleccionado.getId());
			aeropuerto.getDireccion().setId(seleccionado.getDireccion().getId());
		}
		
		return aeropuerto;
	}
	
	
	private void rellenarEdicion() {
		//PRÁCTICAMENTE TODOS LOS CAMPOS SON NOT NULL, POR LO QUE NO DEBERÍA SALTAR NINGUNA EXCEPCIÓN
		if (seleccionado != null) {
			Direccion direccion = seleccionado.getDireccion();
			tfAno.setText(Integer.toString(seleccionado.getAnioInauguracion()));
			tfCalle.setText(direccion.getCalle());
			tfCapacidad.setText(Integer.toString(seleccionado.getCapacidad()));
			tfCiudad.setText(direccion.getCiudad());
			tfNombre.setText(seleccionado.getNombre());
			tfNumero.setText(Integer.toString(direccion.getNumero()));
			tfPais.setText(direccion.getPais());
			rbPrivado.setDisable(true);
			rbPublico.setDisable(true);
			if (rbPrivado.isSelected()) {
				tfVariable1.setText(Integer.toString(seleccionado.getNumeroSocios()));
			} else {
				tfVariable1.setText(String.format("%.2f", seleccionado.getFinanciacion()));
				tfVariable2.setText(Integer.toString(seleccionado.getNumTrabajadores()));
			}
		}
	}
	
	private void ponerImagen() {
		if (seleccionado != null && seleccionado.getImagen() != null) {
			ivImagen.setImage(new Image(new ByteArrayInputStream(seleccionado.getImagen())));
			try (OutputStream os = new FileOutputStream("/home/jiraiya/Descargas/imageprueba.jpg")){
				os.write(seleccionado.getImagen());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void checkCampoDouble(TextField tf) throws AeropuertosException {
		String strNum = tf.getText();
		Pattern doublePattern = Pattern.compile("\\d+([\\.,]\\d+)?");
		Matcher matcher = doublePattern.matcher(strNum);
		if (!matcher.matches()) {
			throw new AeropuertosException("El campo " + tf.getId() + " contiene un formato incorrecto o está vacío");
		}
	}

	public static void checkCampoInt(TextField tf) throws AeropuertosException {
		String strNum = tf.getText();
		Pattern intPattern = Pattern.compile("\\d+");
		Matcher matcher = intPattern.matcher(strNum);
		if (!matcher.matches()) {
			throw new AeropuertosException("El campo " + tf.getId() + " contiene un formato incorrecto o está vacío");
		}
	}

	public static void checkCampoStrNotNull(TextField tf) throws AeropuertosException {
		String str = tf.getText();
		if (str == null || str.isBlank()) {
			throw new AeropuertosException("El campo" + tf.getId() + " está vacío");
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
