package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entity.Telefone;
import model.service.TelefoneService;

public class CadastroTelefonesController implements Initializable, DataChangeListener {

	private TelefoneService telefoneService;
	
	@FXML
	private TableView<Telefone> tableViewTelefones;

	@FXML
	private TableColumn<Telefone, String> tableColumnNumeroTelefone;

	@FXML
	private TableColumn<Telefone, Date> tableColumnDataDeCadastro;

	@FXML
	private Button btnNovo;
	
	private ObservableList<Telefone> obsList;

	@FXML
	public void onBtnNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Telefone obj = new Telefone();
		createDialogForm(obj, "/gui/TelefoneForm.fxml", parentStage);
	}

	public void updateTableView() {
		List<Telefone> lista = telefoneService.findAll();
		obsList = FXCollections.observableArrayList(lista);
		tableViewTelefones.setItems(obsList);
	}

	// Utilizamos o método para carregar os telefones
	public void setTelefoneService(TelefoneService telefoneService) {
		this.telefoneService = telefoneService;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		// Iniciando as colunas da tabela
		tableColumnDataDeCadastro.setCellValueFactory(new PropertyValueFactory<>("dataDeCadastro"));
		tableColumnNumeroTelefone.setCellValueFactory(new PropertyValueFactory<>("numero"));
		Utils.formatTableColumnDate(tableColumnDataDeCadastro, "dd/MM/yyyy");

		// Tabela terminando no final da tela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewTelefones.prefHeightProperty().bind(stage.heightProperty());
	}

	private void createDialogForm(Telefone obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			TelefoneFormController controller = loader.getController();
			controller.setTelefone(obj);
			controller.setTelefoneService(new TelefoneService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			// Para montar a janela criamos um stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Dados Telefone");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false); // A janela não pode ser redimensionada
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro ao carregar tela", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

}
