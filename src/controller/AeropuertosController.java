package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import dao.DAOAeropuertos;
import dao.DAOAviones;
import enums.TipoAeropuerto;
import excepciones.AeropuertosException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Aeropuerto;
import model.Avion;
import model.Direccion;
import utilities.Utilidades;

public class AeropuertosController implements Initializable {

    @FXML
    private MenuItem miAnadirAeropuerto;

    @FXML
    private MenuItem miAnadirAvion;

    @FXML
    private MenuItem miEditarAeropuerto;
    
    @FXML
    private MenuItem miBorrarAeropuerto;

    @FXML
    private MenuItem miActivarAvion;
    
    @FXML
    private MenuItem miBorrarAvion;
    
    @FXML
    private MenuItem miInfoAeropuertos;

    @FXML
    private RadioButton rbPrivados;

    @FXML
    private RadioButton rbPublicos;

    @FXML
    private TableColumn<Aeropuerto, Integer> tcAno;

    @FXML
    private TableColumn<Aeropuerto, Integer> tcCapacidad;

    @FXML
    private TableColumn<Aeropuerto, String> tcCiudad;

    @FXML
    private TableColumn<Aeropuerto, Integer> tcId;

    @FXML
    private TableColumn<Aeropuerto, String> tcNombre;

    @FXML
    private TableColumn<Aeropuerto, Integer> tcNumSocios;
    
    @FXML
    private TableColumn<Aeropuerto, Double> tcFinanciacion;
    
    @FXML
    private TableColumn<Aeropuerto, Integer> tcNumTrabajadores;

    @FXML
    private TableColumn<Aeropuerto, Integer> tcNumero;

    @FXML
    private TableColumn<Aeropuerto, String> tcPais;

    @FXML
    private TextField tfNombre;

    @FXML
    private ToggleGroup tipoAeropuerto;

    @FXML
    private TableView<Aeropuerto> tvAeropuertos;

    @FXML
    void anadirAeropuerto(ActionEvent event) {
    	abrirEditor();
    }

    @FXML
    void anadirAvion(ActionEvent event) {
    	abrirEditorAvion();
    }

    @FXML
    void buscar(KeyEvent event) {
    	filtrarFilas();
    }

    @FXML
    void editarAeropuerto(ActionEvent event) {
    	Aeropuerto seleccionado = tvAeropuertos.getSelectionModel().getSelectedItem();
    	if (seleccionado != null) {    		
    		abrirEditor(seleccionado);
    	}
    }
    
    @FXML
    void borrarAeropuerto(ActionEvent event) {
    	Aeropuerto seleccionado = tvAeropuertos.getSelectionModel().getSelectedItem();
    	if (seleccionado != null) {    		
    		Alert alert = new Alert(AlertType.CONFIRMATION, "¿Desea borrar el aeropuerto " + seleccionado.getNombre() + "?", ButtonType.OK, ButtonType.CANCEL);
    		alert.showAndWait();
    		ButtonType eleccion = alert.getResult();
    		if (ButtonType.OK.equals(eleccion)) {
    			try {
    				//PRIMERO HAY QUE BORRAR LOS AVIONES ASOCIADOS PARA NO VIOLAR LA CONSTRAINT
    				List<Avion> avionesABorrar = DAOAviones.getAviones(seleccionado);
    				avionesABorrar.forEach(avion -> {
						try {
							DAOAviones.borrarAvion(avion);
						} catch (SQLException | AeropuertosException e) {
				    		Alert err = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
				    		err.show();
							e.printStackTrace();
						}
					});
					DAOAeropuertos.borrarAeropuerto(seleccionado);
					filtrarFilas();
				} catch (SQLException | AeropuertosException e) {
		    		Alert err = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
		    		err.show();
					e.printStackTrace();
				}
    		}
    	}
    }

    @FXML
    void activarAvion(ActionEvent event) {
    	abrirActivadorAvion();
    }
    
    @FXML
    void borrarAvion(ActionEvent event) {
    	abrirBorrarAvion();
    }
    

    @FXML
    void mostrarInfoAeropuerto(ActionEvent event) {
    	try {    		
    		Aeropuerto seleccionado = tvAeropuertos.getSelectionModel().getSelectedItem();
    		if (seleccionado != null) {    		
    			Alert alert = new Alert(AlertType.INFORMATION, Utilidades.infoAeropuerto(seleccionado, DAOAviones.getAviones(seleccionado)), ButtonType.OK);
    			alert.setTitle("Información");
    			alert.showAndWait();
    		}
    	} catch (AeropuertosException e) {
			Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			alert.showAndWait();
    	}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tcAno.setCellValueFactory(new PropertyValueFactory<Aeropuerto, Integer>("anioInauguracion"));
		tcCapacidad.setCellValueFactory(new PropertyValueFactory<Aeropuerto, Integer>("capacidad"));
		tcId.setCellValueFactory(new PropertyValueFactory<Aeropuerto, Integer>("id"));
		tcNombre.setCellValueFactory(new PropertyValueFactory<Aeropuerto, String>("nombre"));
		tcNumSocios.setCellValueFactory(new PropertyValueFactory<Aeropuerto, Integer>("numeroSocios"));
		tcFinanciacion.setCellValueFactory(new PropertyValueFactory<Aeropuerto, Double>("financiacion"));
		tcNumTrabajadores.setCellValueFactory(new PropertyValueFactory<Aeropuerto, Integer>("numTrabajadores"));
		
		MenuItem miEditar = new MenuItem("Editar");
		miEditar.setOnAction(ac -> abrirEditor(tvAeropuertos.getSelectionModel().getSelectedItem()));
		MenuItem miBorrar = new MenuItem("Borrar");
		miBorrar.setOnAction(ac -> borrarAeropuerto(ac));
		tvAeropuertos.setContextMenu(new ContextMenu(miEditar, miBorrar));
		
		//PARA ESTOS CAMPOS HAY QUE ACCEDER A LA PROPIEDAD DIRECCIÓN, POR LO QUE USO CALLBACKS
		tcCiudad.setCellValueFactory(param -> {
			Aeropuerto aeropuerto = param.getValue();
			Direccion direccion = aeropuerto.getDireccion();
			if (direccion != null && direccion.getCiudad() != null) {
				return new SimpleStringProperty(direccion.getCiudad());
			}
			return new SimpleStringProperty();
		});
		tcNumero.setCellValueFactory(param -> {
			Aeropuerto aeropuerto = param.getValue();
			Direccion direccion = aeropuerto.getDireccion();
			if (direccion != null && direccion.getNumero() > 0) {
				return new SimpleIntegerProperty(direccion.getNumero()).asObject();
			}
			return new SimpleIntegerProperty().asObject();
		});
		tcPais.setCellValueFactory(param -> {
			Aeropuerto aeropuerto = param.getValue();
			Direccion direccion = aeropuerto.getDireccion();
			if (direccion != null && direccion.getPais() != null) {
				return new SimpleStringProperty(direccion.getPais());
			}
			return new SimpleStringProperty();
		});
		
		tipoAeropuerto.selectedToggleProperty().addListener(e -> filtrarFilas());
		
		tvAeropuertos.setOnMouseClicked(ev -> {
			Aeropuerto aeropuerto = tvAeropuertos.getSelectionModel().getSelectedItem();
			//ABRIR INFO CON DOBLE CLICK
			if (aeropuerto != null &&
				MouseButton.PRIMARY.equals(ev.getButton()) &&
				ev.getClickCount() == 2) {
				mostrarInfoAeropuerto(null);
				//SACAR MENÚ CONTEXTUAL
			}
		});
		
		//PARA CARGAR POR PRIMERA VEZ
		filtrarFilas();
		
		
	}

	protected void filtrarFilas() {
		String busqueda = tfNombre.getText() != null ? tfNombre.getText().toLowerCase() : "";
		tvAeropuertos.getItems().clear();
		TipoAeropuerto tipo = rbPrivados == tipoAeropuerto.getSelectedToggle() ? TipoAeropuerto.PRIVADO : TipoAeropuerto.PUBLICO;
		
		if (TipoAeropuerto.PUBLICO.equals(tipo)) {
			tcNumSocios.setVisible(false);
			tcFinanciacion.setVisible(true);
			tcNumTrabajadores.setVisible(true);
		} else {
			tcNumSocios.setVisible(true);
			tcFinanciacion.setVisible(false);
			tcNumTrabajadores.setVisible(false);			
		}
		
		try {
			tvAeropuertos.getItems().addAll(DAOAeropuertos.getAeropuertos(tipo, busqueda));
			tvAeropuertos.refresh();
		} catch (AeropuertosException e) {
			e.printStackTrace();
		}
	}
	
	private void abrirEditor() {
		abrirEditor(null);
	}
	private void abrirEditor(Aeropuerto seleccionado) {
		FlowPane root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AnadirAeropuerto.fxml"));
			root = loader.load();
			cargarFondo(root);
			AnadirAeropuertoController controladorAnadirAeropuerto = loader.getController();
			
			controladorAnadirAeropuerto
			.setTablaAeropuertos(tvAeropuertos)
			.setSeleccionado(seleccionado)
			.setControladorPrincipal(this);
			
			Stage stage = new Stage();
			if (seleccionado != null) {
				stage.setTitle("AEROPUERTOS - EDITAR AEROPUERTO");
			} else {
				stage.setTitle("AEROPUERTOS - AÑADIR AEROPUERTO");
			}
			stage.initModality(Modality.WINDOW_MODAL);
			Scene scene = new Scene(root);
			controladorAnadirAeropuerto.setEscena(scene);
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void abrirEditorAvion() {
		FlowPane root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AnadirAvion.fxml"));
			root = loader.load();
			cargarFondo(root);
			AnadirAvionController controladorAnadirAvion= loader.getController();
			
			controladorAnadirAvion
			.setControladorPrincipal(this);
			
			Stage stage = new Stage();
			stage.setTitle("AVIONES - AÑADIR AVIÓN");
			stage.initModality(Modality.WINDOW_MODAL);
			Scene scene = new Scene(root);
			controladorAnadirAvion.setEscena(scene);
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void abrirActivadorAvion() {
		FlowPane root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ActivarAvion.fxml"));
			root = loader.load();
			cargarFondo(root);
			ActivarAvionController controladorActivarAvion= loader.getController();
			
			controladorActivarAvion
			.setControladorPrincipal(this);
			
			Stage stage = new Stage();
			stage.setTitle("AVIONES - ACTIVAR/DESACTIVAR AVIÓN");
			stage.initModality(Modality.WINDOW_MODAL);
			Scene scene = new Scene(root);
			controladorActivarAvion.setEscena(scene);
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void abrirBorrarAvion() {
		FlowPane root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BorrarAvion.fxml"));
			root = loader.load();
			cargarFondo(root);
			BorrarAvionController controladorBorrarAvion= loader.getController();
			
			controladorBorrarAvion
			.setControladorPrincipal(this);
			
			Stage stage = new Stage();
			stage.setTitle("AVIONES - LOGIN");
			stage.initModality(Modality.WINDOW_MODAL);
			Scene scene = new Scene(root);
			controladorBorrarAvion.setEscena(scene);
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void cargarFondo(Pane root) {
		Image img = new Image(Main.class.getResourceAsStream("/imagenes/fondo.png"));
		BackgroundImage bi = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
		root.setBackground(new Background(bi));
	}
	
}

