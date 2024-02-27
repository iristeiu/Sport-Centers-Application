package com.example.sportcentersristeiuioana.view;

import com.example.sportcentersristeiuioana.controller.AllGroupsController;
import com.example.sportcentersristeiuioana.controller.ApplicationStartController;
import com.example.sportcentersristeiuioana.controller.ConfController;
import com.example.sportcentersristeiuioana.modelperson.GroupDetails;
import com.example.sportcentersristeiuioana.modelsportcenters.FieldDetails;
import com.example.sportcentersristeiuioana.repository.GroupRepository;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.FileNotFoundException;
import java.util.Date;

public class AllGroupsView extends ApplicationStartView{
    TableView tableView = new TableView<>();
    TableColumn<GroupDetails, Number> reservationId = new TableColumn<>("ID");
    TableColumn<GroupDetails, String> groupName = new TableColumn<>("Group Name");
    TableColumn<GroupDetails, Date> date = new TableColumn<>("Date");
    TableColumn<GroupDetails, String> hours = new TableColumn<>("Hours");
    TableColumn<GroupDetails, String> sportName = new TableColumn<>("Hours");
    TableColumn<GroupDetails, String> country = new TableColumn<>("Country");
    TableColumn<GroupDetails, String> city = new TableColumn<>("City");
    TableColumn<GroupDetails, String> addressDetails = new TableColumn<>("Details");
    TableColumn<GroupDetails, Number> groupsPlayers = new TableColumn<>("Nr Of Members");
    TableColumn<GroupDetails, Boolean> availableGroup = new TableColumn<>("Available");
    TableColumn<GroupDetails, Void> joinGroup = new TableColumn<>("Join");

    Label searchLabel = new Label("Group Name: ");
    TextField searchField = new TextField();
    Button search = new Button("Search");
    Button clear = new Button("Clear");
    HBox searchBox = new HBox(searchLabel, searchField, search, clear);
    VBox box = new VBox(searchBox, tableView);
    GroupRepository repository = new GroupRepository();
    public AllGroupsView(ApplicationStartController controllerStart, AllGroupsController controller) throws FileNotFoundException {
        super(controllerStart);
        window.setTitle("Fields");
        window.setWidth(centerX*2);
        window.setHeight(centerY*2);
        window.setX(0);
        window.setY(0);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        tableView.getStyleClass().add("table-view");
        reservationId.getStyleClass().add("table-column");
        groupName.getStyleClass().add("table-column");
        date.getStyleClass().add("table-column");
        hours.getStyleClass().add("table-column");
        sportName.getStyleClass().add("table-column");
        country.getStyleClass().add("table-column");
        city.getStyleClass().add("table-column");
        addressDetails.getStyleClass().add("table-column");
        groupsPlayers.getStyleClass().add("table-column");
        availableGroup.getStyleClass().add("table-column");
        joinGroup.getStyleClass().add("button-column");

        tableView.setStyle("-fx-background-color: #f2f2f2;");


        searchField.setPromptText("Search...");
        searchBox.setPadding(new Insets(20));
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setSpacing(20);


        tableView.getColumns().addAll(reservationId,groupName,date, hours, sportName, country, city, addressDetails,availableGroup,groupsPlayers);
        ObservableList<TableColumn<FieldDetails, ?>> columns = tableView.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            TableColumn<FieldDetails, ?> col = columns.get(i);
            col.setResizable(true);
        }
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        reservationId.setCellValueFactory(cellData -> cellData.getValue().reservationIdProperty());
        groupName.setCellValueFactory(cellData -> cellData.getValue().groupNameProperty());
        date.setCellValueFactory(cellData -> cellData.getValue().getReserDateProperty());
        hours.setCellValueFactory(cellData -> cellData.getValue().hoursProperty());
        sportName.setCellValueFactory(cellData -> cellData.getValue().sportNameProperty());
        country.setCellValueFactory(cellData -> cellData.getValue().countryProperty());
        city.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
        addressDetails.setCellValueFactory(cellData -> cellData.getValue().addressDetailsProperty());
        groupsPlayers.setCellValueFactory(cellData -> cellData.getValue().nrOfPlayersProperty());
        availableGroup.setCellValueFactory(cellData -> cellData.getValue().availabilityProperty());

        tableView.getItems().addAll(repository.getAllGroupsDetails(controllerStart.getPerson().getPersonId()));

        search.setOnAction(e->{
            String searchGroup = getSearchField();
            controller.setTableData(searchGroup, controllerStart);
        });
        clear.setOnAction(e->controller.pressClearButton());
        tableView.setStyle("-fx-background-color: #3498db;");


        tableView.refresh();
        addButtonToTable(controller, controllerStart);
        box.setPadding(new Insets(5));
        container.getChildren().add(box);

    }

    private void addButtonToTable(AllGroupsController controller, ApplicationStartController applicationStartController) {

        Callback<TableColumn<GroupDetails, Void>, TableCell<GroupDetails, Void>> cellFactory = new Callback<TableColumn<GroupDetails, Void>, TableCell<GroupDetails, Void>>() {
            @Override
            public TableCell<GroupDetails, Void> call(final TableColumn<GroupDetails, Void> param) {
                final TableCell<GroupDetails, Void> cell = new TableCell<GroupDetails, Void>() {
                    private final Button btn = new Button("Join");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            GroupDetails groupsAux = getTableView().getItems().get(getIndex());
                            ConfController confController = new ConfController();
                            ConfView reservationView = null;
                            try {
                                reservationView = new ConfView(confController, applicationStartController, groupsAux );
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            GroupRepository groupRepository = new GroupRepository();
                            confController.setConfView(reservationView);
                            confController.setRepository(groupRepository);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            GroupDetails groupDetails = getTableView().getItems().get(getIndex());
                            boolean isAvailable = groupDetails.isAvailability();

                            btn.setDisable(!isAvailable);
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        joinGroup.setCellFactory(cellFactory);

        tableView.getColumns().add(joinGroup);

    }

    public String getSearchField() {
        return searchField.getText();
    }
    public TextField getSearchTextField(){
        return searchField;
    }

    public void setSearchField(TextField searchField) {
        this.searchField = searchField;
    }

    public TableView getTableView() {
        return tableView;
    }
}
