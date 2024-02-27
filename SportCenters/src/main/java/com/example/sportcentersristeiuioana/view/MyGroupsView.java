package com.example.sportcentersristeiuioana.view;

import com.example.sportcentersristeiuioana.controller.ApplicationStartController;
import com.example.sportcentersristeiuioana.controller.MyGroupsController;
import com.example.sportcentersristeiuioana.controller.ReservationController;
import com.example.sportcentersristeiuioana.modelperson.GroupDetails;
import com.example.sportcentersristeiuioana.modelperson.MyGroupDetails;
import com.example.sportcentersristeiuioana.modelsportcenters.FieldDetails;
import com.example.sportcentersristeiuioana.repository.GroupRepository;
import com.example.sportcentersristeiuioana.repository.ReservationRepository;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.io.FileNotFoundException;

public class MyGroupsView extends ApplicationStartView{
    GroupRepository repository = new GroupRepository();
    TableView tableView = new TableView<>();
    TableColumn<MyGroupDetails, String> group_name = new TableColumn<>("Group Name");
    TableColumn<MyGroupDetails, String> status = new TableColumn<>("Status");
    TableColumn<MyGroupDetails, Void> exitGroup = new TableColumn<>("Exit");
    public MyGroupsView(ApplicationStartController controllerStart, MyGroupsController controller) throws FileNotFoundException {
        super(controllerStart);
        window.setTitle("My Groups");
        window.setWidth(centerX*2);
        window.setHeight(centerY*2);
        window.setX(0);
        window.setY(0);

        tableView.getColumns().addAll(group_name,status);
        ObservableList<TableColumn<FieldDetails, ?>> columns = tableView.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            TableColumn<FieldDetails, ?> col = columns.get(i);
            col.setResizable(true);
        }
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getItems().addAll(repository.getMyGroupsDetails(controllerStart.getPerson().getPersonId()));
        tableView.setPadding(new Insets(15));
        group_name.setCellValueFactory(cellData -> cellData.getValue().groupNameProperty());
        status.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        addButtonToTable(controllerStart, controller);

        container.getChildren().add(tableView);
    }

    private void addButtonToTable(ApplicationStartController applicationStartController, MyGroupsController controller) {

        Callback<TableColumn<MyGroupDetails, Void>, TableCell<MyGroupDetails, Void>> cellFactory = new Callback<TableColumn<MyGroupDetails, Void>, TableCell<MyGroupDetails, Void>>() {
            @Override
            public TableCell<MyGroupDetails, Void> call(final TableColumn<MyGroupDetails, Void> param) {
                final TableCell<MyGroupDetails, Void> cell = new TableCell<MyGroupDetails, Void>() {
                    private final Button btn = new Button("Exit Group");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            MyGroupDetails groupsAux = getTableView().getItems().get(getIndex());
                            controller.exitGroup(groupsAux.getGroupName(), applicationStartController.getPerson().getPersonId());
                        });
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

        exitGroup.setCellFactory(cellFactory);
        tableView.getColumns().add(exitGroup);

    }
}
