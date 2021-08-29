package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import application.Main;
import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entity.Telefone;
import model.exceptions.ValidacaoExceptions;
import model.service.TelefoneService;

public class TelefoneFormController implements Initializable {
	
	private Telefone entity;
	
	private TelefoneService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private DatePicker dpDataDeCadastro;
	
	@FXML
	private TextField txtNumeroTelefone;
	
	@FXML
	private Label lblErrorTelefone;
	
	@FXML
	private Button btnSalvar;
	
	@FXML
	private Button btnCancelar;
	
	@FXML
	private TableView<Telefone> tableViewTelefones;
	
	@FXML
	private TableColumn<Telefone, String> tableColumnNumeroTelefone;
	
	@FXML
	private TableColumn<Telefone, Date> tableColumnDataDeCadastro;
	
	@FXML
	private ObservableList<Telefone> obsList;
	
	
	public void setTelefone(Telefone entity) {
		this.entity = entity;
	}
	
	public void setTelefoneService(TelefoneService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtnSalvarAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity está null");
		}
		
		try {
			entity = getFormData();
			service.insert(entity);
			notifyDataChangeListeners();
			Alerts.showAlert("Sucesso", "Cadastro Telefone", "Número de telefone cadastrado com sucesso!", AlertType.INFORMATION);
			updateTableView();
		}
		catch(ValidacaoExceptions e) {
			setErrorsMessages(e.getErrors());
		}
		catch(DBException e) {
			Alerts.showAlert("Erro", "Erro ao Salvar.", e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtnCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@FXML
	public void onBtnFecharAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity está null");
		}
		
		if(service == null) {
			throw new IllegalStateException("Service está null");
		}
		
		dpDataDeCadastro.setValue(LocalDate.ofInstant(new Date(System.currentTimeMillis()).toInstant(), ZoneId.systemDefault()));	
		txtNumeroTelefone.setText(entity.getNumero());
		
	}
	
	//Pegando os dados do formulário
	private Telefone getFormData() {
		Telefone obj = new Telefone();
		Instant instant = Instant.from(dpDataDeCadastro.getValue().atStartOfDay(ZoneId.systemDefault()));
		
		ValidacaoExceptions exception = new ValidacaoExceptions("Erro de Validação");
		
		if(txtNumeroTelefone.getText() == null || txtNumeroTelefone.getText().trim().equals("")) {
			exception.addError("Telefone", "Campo não pode ser vazio");
		}
		
		obj.setNumero(txtNumeroTelefone.getText());
		obj.setDataDeCadastro(Date.from(instant));
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		tableColumnDataDeCadastro.setCellValueFactory(new PropertyValueFactory<>("dataDeCadastro"));
		tableColumnNumeroTelefone.setCellValueFactory(new PropertyValueFactory<>("numero"));
		Utils.formatTableColumnDate(tableColumnDataDeCadastro, "dd/MM/yyyy");
		
	}
	
	private void updateTableView() {
		List<Telefone> lista = service.findEquals(entity);
		obsList = FXCollections.observableArrayList(lista);
		tableViewTelefones.setItems(obsList);
	}
	
	private void setErrorsMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("Telefone")) {
			lblErrorTelefone.setText(errors.get("Telefone"));
		}
	} 
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

}
