package lk.ijse.GameCafe.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lk.ijse.GameCafe.dto.CustomerDto;
import lk.ijse.GameCafe.dto.EmployeeDto;
import lk.ijse.GameCafe.dto.tm.EmployeeTm;
import lk.ijse.GameCafe.model.CustomerModel;
import lk.ijse.GameCafe.model.EmployeeModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class EmployeeFormController {

        @FXML
        private TableColumn<?, ?> colEmpAddress;

        @FXML
        private TableColumn<?, ?> colEmpContactNum;

        @FXML
        private TableColumn<?, ?> colEmpName;

        @FXML
        private TableColumn<?, ?> colEmpId;

        @FXML
        private TableColumn<?, ?> colEmpSalary;

        @FXML
        private Pane pane;

        @FXML
        private TableView<EmployeeTm> tblEmployee;

        @FXML
        private TextField txtEmpContactNum;

        @FXML
        private TextField txtEmpName;

        @FXML
        private TextField txtEmpId;

        @FXML
        private TextField txtEmpSalary;

        @FXML
        private TextField txtEmpAddress;

        @FXML
        private TextField txtSearchBar;

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {

        txtEmpId.clear();
        txtEmpName.clear();
        txtEmpContactNum.clear();
        txtEmpSalary.clear();
        txtEmpAddress.clear();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {

        boolean isEmployeeValidated = ValidateEmployee();

        if (!isEmployeeValidated){
            return;
        }

        String id = txtEmpId.getText();
        String name = txtEmpName.getText();
        String contactNum = txtEmpContactNum.getText();
        String address = txtEmpAddress.getText();
        String salary = txtEmpSalary.getText();

        EmployeeDto dto = new EmployeeDto(id, name, contactNum, address, salary);
        EmployeeModel employeeModel = new EmployeeModel();

        try {
            boolean isSaved = employeeModel.saveEmployee(dto);

            if (isSaved){
                new Alert(Alert.AlertType.CONFIRMATION, "Employee Saved Successfully");
                loadAllEmployees();
            }
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }


    }

    public void initialize(){
        setCellValueFactory();
        loadAllEmployees();

    }

    private void setCellValueFactory() {
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colEmpName.setCellValueFactory(new PropertyValueFactory<>("empName"));
        colEmpContactNum.setCellValueFactory(new PropertyValueFactory<>("empContactNum"));
        colEmpSalary.setCellValueFactory(new PropertyValueFactory<>("empSalary"));
        colEmpAddress.setCellValueFactory(new PropertyValueFactory<>("empAddress"));
    }

    private void loadAllEmployees() {
        EmployeeModel model = new EmployeeModel();

        ObservableList<EmployeeTm> obList = FXCollections.observableArrayList();

        try {
            List<EmployeeDto> list = model.getAllEmployees();

            for (EmployeeDto dto : list){
                EmployeeTm  employeeTm = new EmployeeTm(dto.getEmpId(),
                        dto.getEmpName(),
                        dto.getEmpContactNum(),
                        dto.getEmpSalary(),
                        dto.getEmpAddress());

                obList.add(employeeTm);
            }
            tblEmployee.setItems(obList);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private boolean ValidateEmployee() {
        String empIdText = txtEmpId.getText();
        boolean isEmployeeIdValidated = Pattern.matches("[E][0-9]{4}", empIdText);
        if (!isEmployeeIdValidated) {
            new Alert(Alert.AlertType.ERROR, "invalid Employee Id!").show();
            return false;
        }

        String empNameText = txtEmpName.getText();
        boolean isEmployeeNameValidated = Pattern.matches("[A-Za-z]+", empNameText);
        if(!isEmployeeNameValidated){
            new Alert(Alert.AlertType.ERROR, "invalid Employee Name!").show();
            return false;
        }

        String empEmailText = txtEmpContactNum.getText();
        boolean isEmpEmailAddressValidated = Pattern.matches("[0-9]{10}", empEmailText  );
        if(!isEmpEmailAddressValidated){
            new Alert(Alert.AlertType.ERROR, "invalid Employee Contact Number!").show();
            return false;

        }

        String empSalaryText = txtEmpSalary.getText();
        boolean isEmpSalaryValidated = Pattern.matches("[0-9](.*)", empSalaryText);
        if(!isEmpSalaryValidated){
            new Alert(Alert.AlertType.ERROR, "invalid Employee Salary!").show();
            return false;

        }

        String empContactNumText = txtEmpAddress.getText();
        boolean isEmpContactNumValidated = Pattern.matches("[A-Za-z]+(.*)", empContactNumText);
        if(!isEmpContactNumValidated){
            new Alert(Alert.AlertType.ERROR, "invalid Employee Address!").show();
            return false;

        }

        return true;
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtEmpId.getText();

        EmployeeModel model = new EmployeeModel();

        try {
            boolean isDeleted = model.deleteEmployee(id);
            if(isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"Patient Deleted Successfully").show();
                loadAllEmployees();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtEmpId.getText();
        String name = txtEmpName.getText();
        String email = txtEmpContactNum.getText();
        String contactNo = txtEmpSalary.getText();
        String address = txtEmpAddress.getText();
        EmployeeDto dto = new EmployeeDto(id,name,email,contactNo,address);

        EmployeeModel model = new EmployeeModel();
        try {
            boolean isUpdated = model.updateEmployee(dto);
            if(isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"Employee Updated Successfully").show();
                loadAllEmployees();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String id = txtSearchBar.getText();
        var model = new EmployeeModel();
        try {

            EmployeeDto dto = model.SearchModel(id);
            if (dto != null){
                fillField(dto);
            }else {
                new Alert(Alert.AlertType.INFORMATION,"Employee not found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private void fillField(EmployeeDto dto) {
        txtEmpId.setText(dto.getEmpId());
        txtEmpName.setText(dto.getEmpName());
        txtEmpContactNum.setText(dto.getEmpContactNum());
        txtEmpSalary.setText(dto.getEmpSalary());
        txtEmpAddress.setText(dto.getEmpAddress());
    }

}
