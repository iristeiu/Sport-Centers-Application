package com.example.sportcentersristeiuioana.view;

import com.example.sportcentersristeiuioana.controller.ApplicationStartController;
import com.example.sportcentersristeiuioana.controller.FieldController;
import com.example.sportcentersristeiuioana.controller.ReservationController;
import com.example.sportcentersristeiuioana.enumeration.StatusType;
import com.example.sportcentersristeiuioana.modelsportcenters.FieldDetails;
import com.example.sportcentersristeiuioana.repository.FieldRepository;
import com.example.sportcentersristeiuioana.repository.ReservationRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.FileNotFoundException;

public class FieldView extends ApplicationStartView{
    TableView tableView = new TableView<>();
    TableColumn<FieldDetails, Number> fieldIdColumn = new TableColumn<>("Field ID");
    TableColumn<FieldDetails, String> country = new TableColumn<>("Country");
    TableColumn<FieldDetails, String> county = new TableColumn<>("County");
    TableColumn<FieldDetails, String> city = new TableColumn<>("City");
    TableColumn<FieldDetails, String> details = new TableColumn<>("Details");
    TableColumn<FieldDetails, Number> priceHour = new TableColumn<>("Price");
    TableColumn<FieldDetails, String> sportName = new TableColumn<>("Sport Name");
    TableColumn<FieldDetails, Number> maxPlayers = new TableColumn<>("Max. Players");
    TableColumn<FieldDetails, String> inOutLoc = new TableColumn<>("Indoor/Outdoor");
    TableColumn<FieldDetails, Boolean> ventLight = new TableColumn<>("Ventilation/Night Lights");
    TableColumn<FieldDetails, String> floorSurf = new TableColumn<>("Floor/Surface");
    TableColumn<FieldDetails, Void> reservation = new TableColumn<>("Make Reservation");
    Label searchLabel = new Label("City: ");
    Label locationLabel = new Label("Location: ");
    Label sportLabel = new Label("Sport: ");
    TextField searchField = new TextField();
    TextField searchLocation = new TextField();
    String[] sports;
    ComboBox comboBox;
    Button search = new Button("Search");
    Button clear = new Button("Clear");
    HBox searchBox = new HBox(searchLabel, searchField, locationLabel, searchLocation, sportLabel);
    VBox box = new VBox(searchBox, tableView);
    FieldRepository repository = new FieldRepository();
    public FieldView(FieldController controller, ApplicationStartController controllerStart) throws FileNotFoundException {
        super(controllerStart);
        window.setTitle("Fields");
        window.setWidth(centerX*2);
        window.setHeight(centerY*2);
        window.setX(0);
        window.setY(0);

        sports = repository.getSportsList();
        comboBox = new ComboBox(FXCollections.observableArrayList(sports));
        searchBox.getChildren().addAll(comboBox, search, clear);

        searchField.setPromptText("Search...");
        searchLocation.setPromptText("Search...");
        searchBox.setPadding(new Insets(20));
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setSpacing(20);
        tableView.getColumns().addAll(fieldIdColumn,country,county,city,details,sportName,maxPlayers,priceHour, inOutLoc,ventLight,floorSurf);
        ObservableList<TableColumn<FieldDetails, ?>> columns = tableView.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            TableColumn<FieldDetails, ?> col = columns.get(i);
            col.setResizable(true);
        }
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        fieldIdColumn.setCellValueFactory(cellData -> cellData.getValue().fieldIdProperty());
        country.setCellValueFactory(cellData -> cellData.getValue().countryProperty());
        county.setCellValueFactory(cellData -> cellData.getValue().countyProperty());
        city.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
        details.setCellValueFactory(cellData -> cellData.getValue().detailsProperty());
        priceHour.setCellValueFactory(cellData -> cellData.getValue().pricePerHourProperty());
        sportName.setCellValueFactory(cellData -> cellData.getValue().sportNameProperty());
        maxPlayers.setCellValueFactory(cellData -> cellData.getValue().maxPlayersProperty());
        inOutLoc.setCellValueFactory(cellData -> cellData.getValue().fieldTypeProperty());
        ventLight.setCellValueFactory(cellData -> cellData.getValue().ventLightProperty());
        floorSurf.setCellValueFactory(cellData -> cellData.getValue().floorSurfProperty());

        tableView.getItems().addAll(repository.getAllFieldDetails());
        search.setOnAction(e->{
            String searchCity = getSearchFieldText();
            String searchLoc = getSearchLocationText();
            String searchSport = getSportText();
            controller.setTableData(searchCity,searchLoc,searchSport);
        });
        clear.setOnAction(e->controller.pressClearButton());
        tableView.refresh();
        addButtonToTable(controllerStart);
        box.setPadding(new Insets(5));
        container.getChildren().add(box);
    }



    public String getSearchFieldText() {
        return searchField.getText();
    }
    public TextField getSearchField() {
        return searchField;
    }

    public void setSearchField(TextField searchField) {
        this.searchField = searchField;
    }

    public String getSearchLocationText() {
        return searchLocation.getText();
    }
    public TextField getSearchLocation() {
        return searchLocation;
    }

    public void setSearchLocation(TextField searchLocation) {
        this.searchLocation = searchLocation;
    }

    public String getSportText() {
        return (String) comboBox.getValue();
    }
    public ComboBox getSport() {
        return comboBox;
    }

    public void setComboBox(ComboBox comboBox) {
        this.comboBox = comboBox;
    }

    public TableView getTableView() {
        return  tableView;
    }
    private void addButtonToTable(ApplicationStartController applicationStartController) {

        Callback<TableColumn<FieldDetails, Void>, TableCell<FieldDetails, Void>> cellFactory = new Callback<TableColumn<FieldDetails, Void>, TableCell<FieldDetails, Void>>() {
            @Override
            public TableCell<FieldDetails, Void> call(final TableColumn<FieldDetails, Void> param) {
                final TableCell<FieldDetails, Void> cell = new TableCell<FieldDetails, Void>() {
                    private final Button btn = new Button();
                    {
                        if(applicationStartController.getPerson().getStatus() == StatusType.ADMIN){
                            btn.setText("Delete");
                            btn.setOnAction(e->{
                                FieldDetails fieldAux = getTableView().getItems().get(getIndex());
                                applicationStartController.deleteField(fieldAux.getFieldId());
                            });
                        }
                        else {
                            btn.setText("Reserve");
                            btn.setOnAction((ActionEvent event) -> {
                                ReservationController reservationController = new ReservationController();
                                ReservationView reservationView = null;
                                try {
                                    reservationView = new ReservationView(window, applicationStartController, reservationController);
                                } catch (FileNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                                ReservationRepository reservationRepository = new ReservationRepository();
                                reservationController.setRepository(reservationRepository);
                                reservationController.setView(reservationView);
                                FieldDetails fieldAux = getTableView().getItems().get(getIndex());
                                reservationView.setFieldId(fieldAux.getFieldId());
                                reservationView.setSportType(fieldAux.getSportName());
                                reservationView.setCity(fieldAux.getCity());
                                reservationView.setAddress(fieldAux.getDetails());
                            });
                        }
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        reservation.setCellFactory(cellFactory);

        tableView.getColumns().add(reservation);

    }
}
