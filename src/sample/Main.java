package sample;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Main extends Application{

    private DBConnector dataBase = new DBConnector();

    private boolean doesItCheat(String query){

        query = query.toUpperCase();
        boolean isOk=false;
        if(query.contains("DELETE")||query.contains("DROP")||query.contains("UPDATE")||query.contains("INSERT")||query.contains("SELECT"))
            isOk=true;

        return isOk;
    }

    private static String get_SHA_512_SecurePassword(String passwordToHash)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private boolean checkBossLogin(String login, String password){
        if(!doesItCheat(login)&&!doesItCheat(password)) {
            String hashedLogin = get_SHA_512_SecurePassword(login);
            String hashedPasswd = get_SHA_512_SecurePassword(password);
            try {
                ResultSet resultSet = dataBase.giveDatafromQuery("SELECT * FROM szefdane");
                while (resultSet.next()) {
                    String dbLogin = resultSet.getString("login");
                    String dbPassword = resultSet.getString("haslo");
                    if (hashedLogin.equals(dbLogin) && hashedPasswd.equals(dbPassword)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return false;
    }

    private boolean checkWorkerLogin(String login, String password){
        if(!doesItCheat(login)&&!doesItCheat(password)) {
            String hashedLogin = get_SHA_512_SecurePassword(login);
            String hashedPasswd = get_SHA_512_SecurePassword(password);
            try {
                ResultSet resultSet = dataBase.giveDatafromQuery("SELECT * FROM pracownicydane");
                while (resultSet.next()) {
                    String dbLogin = resultSet.getString("login");
                    String dbPassword = resultSet.getString("haslo");
                    if (hashedLogin.equals(dbLogin) && hashedPasswd.equals(dbPassword)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return false;
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {

        VBox tableListBox = new VBox(12);
        Label tableListLabel = new Label("Wybierz tabelę i kliknij DALEJ");
        ObservableList<String> tableListComboBoxOptions = FXCollections.observableArrayList(
                "Klienci",
                "Samochody",
                "Oferta",
                "Czesci",
                "Pracownicy",
                "Logizakupow",
                "Logiczynnosci"
        );
        ComboBox tableListComboBox = new ComboBox(tableListComboBoxOptions);
        tableListComboBox.getSelectionModel().selectFirst();
        Button tableListButton = new Button("DALEJ");
        Button tableListReturn = new Button("WRÓĆ");
        TextArea tableListArea = new TextArea();
        tableListArea.setEditable(false);
        tableListArea.setMinSize(1280,550);
        tableListBox.getChildren().addAll(tableListLabel, tableListComboBox, tableListButton,tableListArea, tableListReturn);

        ////

        VBox bossMenuBox = new VBox(12);
        Button bossMenuTables = new Button("PRZEGLĄDAJ TABELE");
        Button bossMenuBestWorker = new Button("POKAŻ NAJLEPSZEGO PRACOWNIKA");
        Button bossMenuFinances = new Button("SPRAWDŹ FINANSE WARSZTATU");
        Button bossMenuUsers = new Button("ZARZĄDZAJ UŻYTKOWNIKAMI");
        Button bossMenuAddCar = new Button("DODAJ POJAZD DO BAZY");
        Button bossMenuRepair = new Button("NAPRAW SAMOCHÓD");
        Button bossMenuParts = new Button("SPRAWDŹ MAGAZYN");
        Button bossMenuWorkers = new Button("ZARZĄDZAJ PRACOWNIKAMI");
        Button bossMenuRepairPart = new Button("DODAJ CZĘŚĆ DO NAPRAWY");
        Button bossMenuAddPart = new Button("DODAJ CZĘŚĆ DO LISTY CZĘŚCI");
        Button bossMenuBackUp = new Button("ZRÓB BACKUP BAZY");
        Button bossMenuAddQueue = new Button("DODAJ CZYNNOŚĆ");
        bossMenuBox.getChildren().addAll(bossMenuAddQueue, bossMenuTables, bossMenuBestWorker, bossMenuFinances, bossMenuUsers, bossMenuWorkers,bossMenuAddCar, bossMenuRepair, bossMenuParts, bossMenuRepairPart,bossMenuAddPart,bossMenuBackUp);

        ////

        VBox workerMenuBox = new VBox(12);
        Button workerMenuAddCar = new Button("DODAJ POJAZD DO BAZY");
        Button workerMenuRepair = new Button("NAPRAW SAMOCHÓD");
        Button workerMenuParts = new Button("SPRAWDŹ MAGAZYN");
        Button workerMenuRepairPart = new Button("DODAJ CZĘŚĆ DO NAPRAWY");
        Button workerMenuAddQueue = new Button("DODAJ CZYNNOŚĆ");
        workerMenuBox.getChildren().addAll(workerMenuAddQueue, workerMenuAddCar, workerMenuParts, workerMenuRepair, workerMenuRepairPart);

        ////

        VBox addCarBox = new VBox(12);
        Label vinLabel = new Label("Podaj VIN");
        TextField vinField = new TextField();
        Label brandLabel = new Label("Podaj markę");
        TextField brandField = new TextField();
        Label modelLabel = new Label("Podaj model");
        TextField modelField = new TextField();
        Label clientFirstName = new Label("Podaj imię właściciela");
        TextField clientFirstNameField = new TextField();
        Label clientSecondName = new Label("Podaj nazwisko właściciela");
        TextField clientSecondNameField = new TextField();
        Label clientPhoneNumber = new Label("Podaj numer telefonu");
        TextField clientPhoneNumberField = new TextField("");
        Button addCarButton = new Button("DODAJ");
        Button addCarReturn = new Button("WRÓĆ");
        addCarBox.getChildren().addAll(
                vinLabel,
                vinField,
                brandLabel,
                brandField,
                modelLabel,
                modelField,
                clientFirstName,
                clientFirstNameField,
                clientSecondName,
                clientSecondNameField,
                clientPhoneNumber,
                clientPhoneNumberField,
                addCarButton,
                addCarReturn);

        ////

        VBox beforeLoginBox = new VBox(12);
        Button showOffer = new Button("OFERTA");
        Button showLogin = new Button("LOGOWANIE");
        Button showCheckVin = new Button("SPRAWDŹ STAN POJAZDU");
        beforeLoginBox.getChildren().addAll(showOffer,showCheckVin,showLogin);

        ////

        VBox offerBox = new VBox(12);
        TextArea offerArea = new TextArea();
        offerArea.setEditable(false);
        offerArea.setMinSize(1280,600);
        Button offerReturn = new Button("WRÓĆ");

        offerBox.getChildren().addAll(offerArea, offerReturn);

        ////

        VBox checkVinBox = new VBox(12);
        Label checkVinLabel = new Label("Podaj VIN pojazdu");
        TextArea checkedVinArea = new TextArea();
        checkedVinArea.setEditable(false);
        TextField checkVinField = new TextField();
        Button checkVin = new Button("SPRAWDŹ");
        Button checkVinReturn = new Button("WRÓĆ");
        checkVinBox.getChildren().addAll(checkedVinArea,checkVinLabel,checkVinField,checkVin,checkVinReturn);

        ////

        final int[] permissions = {0};

        VBox loginBox = new VBox(12);
        ToggleGroup loginToggles = new ToggleGroup();
        ToggleButton workerToggle = new ToggleButton("PRACOWNIK");
        ToggleButton bossToggle = new ToggleButton("SZEF");
        workerToggle.setToggleGroup(loginToggles);
        bossToggle.setToggleGroup(loginToggles);
        Label typeName = new Label("Podaj nazwę użytkownika");
        Label typePassword = new Label("Podaj hasło");
        TextField enterNameField = new TextField();
        PasswordField enterPassword = new PasswordField();
        Button loginButton = new Button("ZALOGUJ");
        Button loginReturn = new Button("WRÓĆ");
        loginBox.getChildren().addAll(
                workerToggle,
                bossToggle,
                typeName,
                enterNameField,
                typePassword,
                enterPassword,
                loginButton,
                loginReturn
        );

        ////

        VBox addPartBox = new VBox(12);
        Label addPartNameLabel = new Label("Podaj nazwę dodawanej części:");
        TextField addPartField = new TextField();
        Label addPartOnStockLabel = new Label("Podaj ilość części na magazynie:");
        TextField addPartOnStockField = new TextField();
        Label addPartOnStockLabel2 = new Label("Podaj ilość części na stanie:");
        TextField addPartOnStockField2 = new TextField();
        Label addPartPriceLabel = new Label("Podaj cenę części:");
        TextField addPartPriceField = new TextField();
        Button addPartButton = new Button("DODAJ");
        Button addPartReturn = new Button("WRÓĆ");

        addPartBox.getChildren().addAll(addPartNameLabel,addPartField,addPartOnStockLabel,addPartOnStockField,addPartOnStockLabel2,addPartOnStockField2,addPartPriceLabel,addPartPriceField,addPartButton,addPartReturn);


        ////

        VBox orderBox = new VBox(12);
        Label orderListLabel = new Label("Części");
        ListView<String> orderListView = new ListView<String>();
        ObservableList<String> orderList;
        TextArea orderArea = new TextArea();
        orderArea.setEditable(false);
        Label orderNameLabel = new Label("Podaj nazwe artykułu(identyczna z nazwą na liście): ");
        TextField orderNameField = new TextField();
        Label orderCountLabel = new Label("Podaj ilość sztuk");
        TextField orderField = new TextField();
        TextField orderVeryfField = new TextField();
        Label orderVeryfLabel = new Label("Podaj swój PESEL w celu weryfikacji: ");
        Button orderOrder  = new Button("ZAMÓW");
        Button orderReturn = new Button("WRÓĆ");
        orderBox.getChildren().addAll(orderListLabel, orderArea,orderNameLabel,orderNameField, orderCountLabel, orderField,orderVeryfLabel,orderVeryfField,orderOrder, orderReturn);

        ////

        VBox bestWorkerBox = new VBox(12);
        TextArea bestWorkerArea = new TextArea();
        bestWorkerArea.setMinSize(800,100);
        bestWorkerArea.setEditable(false);
        Button bestWorkerReturn = new Button("WRÓC");
        bestWorkerBox.getChildren().addAll(bestWorkerArea,bestWorkerReturn);

        ////

        VBox financesBox = new VBox(12);
        TextArea financeArea = new TextArea();
        financeArea.setEditable(false);
        financeArea.setMaxSize(300,50);
        Button financesReturn = new Button("WRÓĆ");
        financesBox.getChildren().addAll(financeArea,financesReturn);

        ////

        VBox userBox = new VBox(12);
        Label deleteUserLabel = new Label("Podaj login użytkownika do usunięcia: ");
        TextField deleteUserField = new TextField();
        Button deleteUserButton = new Button("USUN");
        Label addUserLabel = new Label("Podaj login użytkownika do dodania: ");
        TextField addUserField = new TextField();
        Label addUserPassLabel = new Label("Podaj hasło użytkownika: ");
        TextField addUserPassField = new TextField();
        Label addUserPeselLabel = new Label("Podaj PESEL dodawanego użytkownika");
        TextField addUserPeselField = new TextField();
        Button addUserButton = new Button("DODAJ");
        Button userReturn = new Button("WRÓC");
        userBox.getChildren().addAll(deleteUserLabel,deleteUserField,deleteUserButton,addUserLabel,addUserField,addUserPassLabel,addUserPassField,addUserPeselLabel,addUserPeselField,addUserButton,userReturn);

        ////
        VBox addWorkerBox = new VBox(12);
        Label addWorkerNameLabel = new Label("Podaj imię pracownika: ");
        Label addWorkerSubameLabel = new Label("Podaj nazwisko pracownika: ");
        Label addWorkerPeselLabel = new Label("Podaj PESEL pracownika: ");
        Label addWorkerSalaryLabel = new Label("Podaj pensję pracownika: ");
        Label addWorkerPositionLabel = new Label("Podaj stanowisko pracownika: ");
        TextField addWorkerNameField = new TextField();
        TextField addWorkerSubnameField = new TextField();
        TextField addWorkerPeselField = new TextField();
        TextField addWorkerSalaryField = new TextField();
        TextField addWorkerPositionField = new TextField();
        Button addWorkerButton = new Button("DODAJ");
        Button addWorkerReturn = new Button("WRÓĆ");
        addWorkerBox.getChildren().addAll(addWorkerNameLabel,addWorkerNameField,addWorkerSubameLabel,addWorkerSubnameField,addWorkerPeselLabel,addWorkerPeselField,addWorkerSalaryLabel,addWorkerSalaryField,addWorkerPositionLabel,addWorkerPositionField,addWorkerButton,addWorkerReturn);

        ////

        VBox repairBox = new VBox(12);
        Button repairReturn = new Button("WRÓĆ");
        TextArea repairList = new TextArea();
        repairList.setEditable(false);
        repairList.setMinSize(1280,450);
        Label repairNumberLabel = new Label("Wybierz ID naprawy");
        TextField repairNumber = new TextField();
        Label repairDiagnosisLabel = new Label("Podaj wykonywaną czynność");
        TextField repairDiagnosis = new TextField();
        Label repairPESELLabel = new Label("Podaj swój PESEL");
        TextField repairPESELField = new TextField();
        Button repairRepair = new Button("NAPRAW");
        repairBox.getChildren().addAll(repairList, repairNumberLabel, repairNumber, repairDiagnosisLabel, repairDiagnosis,repairPESELLabel, repairPESELField, repairRepair, repairReturn);

        ////

        VBox repairPartBox = new VBox(12);
        Button repairPartReturn = new Button("WRÓĆ");
        TextArea repairPartList = new TextArea();
        repairPartList.setEditable(false);
        repairPartList.setMinSize(1280, 250);
        TextArea repairRepairList = new TextArea();
        repairRepairList.setEditable(false);
        repairRepairList.setMinSize(1280, 250);
        Label repairPartLabel = new Label("Wybierz id części, numer naprawy i kliknij DODAJ");
        TextField repairPartNumber = new TextField();
        TextField repairRepairNumber = new TextField();
        Button repairPartAdd = new Button("DODAJ");
        repairPartBox.getChildren().addAll(repairPartReturn, repairPartList, repairRepairList, repairPartLabel, repairPartNumber, repairRepairNumber, repairPartAdd);

        ////

        VBox addQueueBox = new VBox(12);
        Label addQueueVinLabel = new Label("Podaj VIN samochodu");
        TextField addQueueVin = new TextField();
        Label addQueueProblemLabel = new Label("Podaj problem");
        TextField addQueueProblem = new TextField();
        Button addQueue = new Button("DODAJ");
        Button addQueueReturn = new Button("WRÓĆ");
        addQueueBox.getChildren().addAll(addQueueVinLabel, addQueueVin, addQueueProblemLabel, addQueueProblem, addQueue, addQueueReturn);

        ////

        ////

        Scene addPartScene = new Scene(addPartBox,1280,720);
        Scene addWorkerScene = new Scene(addWorkerBox,1280,720);
        Scene checkVinScene = new Scene(checkVinBox,1280,720);
        Scene tableListScene = new Scene(tableListBox, 1280, 720);
        Scene bossMenuScene = new Scene(bossMenuBox, 1280, 720);
        Scene workerMenuScene = new Scene(workerMenuBox, 1280, 720);
        Scene addCarScene = new Scene(addCarBox, 1280, 720);
        Scene beforeLoginScene = new Scene(beforeLoginBox, 1280, 720);
        Scene offerScene = new Scene(offerBox, 1280, 720);
        Scene loginScene = new Scene(loginBox, 1280, 720);
        Scene orderScene = new Scene(orderBox, 1280, 720);
        Scene bestWorkerScene = new Scene(bestWorkerBox, 1280, 720);
        Scene financesScene = new Scene(financesBox, 1280, 720);
        Scene userScene = new Scene(userBox, 1280, 720);
        Scene repairScene = new Scene(repairBox, 1280, 720);
        Scene repairPartScene = new Scene(repairPartBox, 1280, 720);
        Scene addQueueScene = new Scene(addQueueBox, 1280, 720);

        ////

        addQueue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String vin = addQueueVin.getText();
                String problem = addQueueProblem.getText();

                addQueueVin.setText("");
                addQueueProblem.setText("");

                if(!doesItCheat(vin)&&!doesItCheat(problem))
                {
                    try {
                        dataBase.updateTable("insert into kolejkanapraw (samochod,problem) values ("+vin+",'" +problem+ "')");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        bossMenuBackUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String executeCmd = "C:/Program Files/MySQL/MySQL Workbench 6.3 CE/mysqldump -u root -p warsztacik > C:/Users/Piotrek/Desktop/backup2.sql";
                    Runtime.getRuntime().exec(new String[]{"cmd.exe","/c",executeCmd});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        addPartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = addPartField.getText();
                String price = addPartPriceField.getText();
                String onStock = addPartOnStockField.getText();
                String onStock2 = addPartOnStockField2.getText();
                int state=0;

                addPartField.setText("");
                addPartPriceField.setText("");
                addPartOnStockField.setText("");
                addPartOnStockField2.setText("");

                try{
                    Integer.parseInt(price);
                    Integer.parseInt(onStock);
                    Integer.parseInt(onStock2);
                }
                catch(NumberFormatException e){
                    System.out.println("Źle podana liczba");
                    state=1;
                }

                ArrayList<String> names = new ArrayList<>();
                try {
                    ResultSet resultSet = dataBase.giveDatafromQuery("SELECT Nazwa FROM czesci");
                    while(resultSet.next()){
                        names.add(resultSet.getString("Nazwa"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                for(String s:names){
                    if(name.equals(s)){
                        state=2;
                    }
                }

                if(state==0&&!doesItCheat(name)&&!doesItCheat(onStock2)&&!doesItCheat(onStock)&&!doesItCheat(price)) {
                    try {
                        dataBase.updateTable("INSERT INTO czesci VALUES('"+name+"',"+onStock2+","+onStock+","+price+")");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        bossMenuAddPart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(addPartScene);
            }
        });

        addPartReturn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addPartField.setText("");
                addPartPriceField.setText("");
                addPartOnStockField.setText("");
                addPartOnStockField2.setText("");
                primaryStage.setScene(bossMenuScene);
            }
        });

        repairRepair.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String number = repairNumber.getText();
                repairNumber.setText("");

                String diagnosis = repairDiagnosis.getText();
                repairDiagnosis.setText("");

                String pesel = repairPESELField.getText();
                repairPESELField.setText("");

                if(!doesItCheat(number)&&!doesItCheat(diagnosis)&&!doesItCheat(pesel)) {
                    try {
                        ResultSet resultSet = dataBase.giveDatafromQuery("SELECT * FROM kolejkanapraw WHERE ID="+number);
                        String problem="";
                        String vin="";
                        String price="";
                        while(resultSet.next()){
                            problem = resultSet.getString("Problem");
                            vin = resultSet.getString("Samochod");
                        }
                        ResultSet resultSet2 = dataBase.giveDatafromQuery("SELECT Cenamin from oferta WHERE Nazwa='"+diagnosis+"'");
                        while(resultSet2.next()){
                            price = resultSet2.getString("Cenamin");
                        }

                        System.out.println("Tutaj jestem jak co: "+problem+vin+number+diagnosis+pesel);

                        dataBase.updateTable("INSERT INTO logiczynnosci (Data,Czynnosc,Samochod,Pracownik,Problem,Cenadetal) VALUES (CURDATE(),'"+diagnosis+"',"+vin+","+pesel+",'"+problem+"',"+price+")");

                        dataBase.updateTable("delete from kolejkanapraw where id ="+number);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        bossMenuWorkers.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(addWorkerScene);
            }
        });

        addWorkerReturn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(bossMenuScene);
            }
        });

        addWorkerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = addWorkerNameField.getText();
                String subname = addWorkerSubnameField.getText();
                String pesel = addWorkerPeselField.getText();
                String salary = addWorkerSalaryField.getText();
                String position = addWorkerPositionField.getText();

                addWorkerNameField.setText("");
                addWorkerSubnameField.setText("");
                addWorkerPeselField.setText("");
                addWorkerSalaryField.setText("");
                addWorkerPositionField.setText("");

                if(!doesItCheat(name)&&!doesItCheat(subname)&&!doesItCheat(pesel)&&!doesItCheat(salary)&&!doesItCheat(position)) {
                    try {
                        dataBase.updateTable("INSERT INTO pracownicy VALUES ('" + pesel + "','" + name + "','" + subname + "'," + salary + ",'" + position + "')");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        showCheckVin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(checkVinScene);
            }
        });

        checkVinReturn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checkedVinArea.setText("");
                checkVinField.setText("");
                primaryStage.setScene(beforeLoginScene);
            }
        });

        deleteUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String login = deleteUserField.getText();
                deleteUserField.setText("");

                if (!doesItCheat(login)) {
                    try {
                        ResultSet resultSet = dataBase.giveDatafromQuery("select * from pracownicydane");
                        while (resultSet.next()) {
                            if (login.equals(resultSet.getString("login")))
                                dataBase.updateTable("DELETE FROM pracownicydane WHERE login='" + login + "'");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        addUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {


                String login = addUserField.getText();
                String password = addUserPassField.getText();
                String pesel = addUserPeselField.getText();
                addUserField.setText("");
                addUserPassField.setText("");
                addUserPeselField.setText("");
                if(!doesItCheat(login)&&!doesItCheat(password)&&!doesItCheat(pesel)) {
                    String hashedLogin = get_SHA_512_SecurePassword(login);
                    String hashedPasswd = get_SHA_512_SecurePassword(password);
                    try {
                        dataBase.updateTable("INSERT INTO pracownicydane VALUES ('" + hashedLogin + "','" + hashedPasswd + "','" + pesel + "')");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    System.out.println("Oszukuje!");
                }
            }
        });

        tableListButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String table = tableListComboBox.getValue().toString();
                ResultSet resultSet;
                try {
                    resultSet = dataBase.giveDatafromQuery("select * from "+table);

                    switch(table){
                        case("Klienci"):
                            tableListArea.setText("");
                            while(resultSet.next()){
                                String text = tableListArea.getText();
                                String id = resultSet.getString("ID");
                                String name = resultSet.getString("Imie");
                                String subname = resultSet.getString("Nazwisko");
                                String phone = resultSet.getString("Telefon");
                                tableListArea.setText(text + "\r\n"+"ID: "+ id + " Imię: "+name+" Nazwisko: "+subname+" Telefon: "+phone);
                            }
                            break;
                        case("Samochody"):
                            tableListArea.setText("");
                            while(resultSet.next()){
                                String text = tableListArea.getText();
                                String vin = resultSet.getString("VIN");
                                String model = resultSet.getString("Marka");
                                String owner = resultSet.getString("Właściciel");
                                String state = resultSet.getString("Status");
                                tableListArea.setText(text + "\r\n"+"VIN: "+ vin + " Marka: "+model+" Właściciel: "+owner+" Status: "+state);
                            }
                            break;
                        case("Oferta"):
                            tableListArea.setText("");
                            while(resultSet.next()){
                                String text = tableListArea.getText();
                                String repair = resultSet.getString("nazwa");
                                String price = resultSet.getString("cenamin");
                                tableListArea.setText(text + "\r\n"+"Nazwa naprawy: "+ repair + " Cena naprawy: "+price+"zł");
                            }
                            break;
                        case("Czesci"):
                            tableListArea.setText("");
                            while(resultSet.next()){
                                String text = tableListArea.getText();
                                String name = resultSet.getString("Nazwa");
                                String onstate = resultSet.getString("Nastanie");
                                String onmagazyn = resultSet.getString("Namagazynie");
                                String price = resultSet.getString("Cena");
                                tableListArea.setText(text + "\r\n"+"Nazwa: "+ name + " Na stanie: "+onstate+" Na magazynie: "+onmagazyn+" Cena: "+price);
                            }
                            break;
                        case("Pracownicy"):
                            tableListArea.setText("");
                            while(resultSet.next()){
                                String text = tableListArea.getText();
                                String pesel = resultSet.getString("PESEL");
                                String name = resultSet.getString("Imie");
                                String subname = resultSet.getString("Nazwisko");
                                String salary = resultSet.getString("Pensja");
                                String position = resultSet.getString("Stanowisko");
                                tableListArea.setText(text + "\r\n"+"Pesel: "+ pesel + " Imię: "+name+" Nazwisko: "+subname+" Pensja: "+salary+" Stanowisko: "+position);
                            }
                            break;
                        case("Logizakupow"):
                            tableListArea.setText("");
                            while(resultSet.next()){
                                String text = tableListArea.getText();
                                String id = resultSet.getString("ID");
                                String date = resultSet.getString("Data");
                                String part = resultSet.getString("Czesc");
                                String amount = resultSet.getString("Ilosc");
                                String worker = resultSet.getString("Pracownik");
                                tableListArea.setText(text + "\r\n"+"ID: "+ id + " Data: "+date+" Część: "+part+" Ilosć: "+amount+" Pracownik: "+worker);
                            }
                            break;
                        case("Logiczynnosci"):
                            tableListArea.setText("");
                            while(resultSet.next()){
                                String text = tableListArea.getText();
                                String id = resultSet.getString("ID");
                                String date = resultSet.getString("Data");
                                String action = resultSet.getString("Czynnosc");
                                String car = resultSet.getString("Samochod");
                                String worker = resultSet.getString("Pracownik");
                                String problem = resultSet.getString("Problem");
                                String price = resultSet.getString("Cenadetal");
                                tableListArea.setText(text + "\r\n"+"ID: "+ id + " Data: "+date+" Czynność: "+action+" Problem: "+problem+" Samochód: "+car+" Pracownik: "+worker+" Cena detal.: "+price);
                            }
                            break;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });

        workerToggle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                permissions[0] =1;
            }
        });

        bossToggle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                permissions[0]=2;
            }
        });

        showOffer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    ResultSet resultSet = dataBase.giveDatafromQuery("select * from oferta");


                    while(resultSet.next()){
                        String text = offerArea.getText();
                        String repair = resultSet.getString("nazwa");
                        String price = resultSet.getString("cenamin");
                        offerArea.setText(text + "\r\n"+"Nazwa naprawy: "+ repair + " Cena naprawy: "+price+"zł");
                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                }


                primaryStage.setScene(offerScene);
            }
        });

        showLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(loginScene);
            }
        });

        checkVin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String checked = checkVinField.getText();
                if(!doesItCheat(checked)) {
                    try {
                        ResultSet resultSet = dataBase.giveDatafromQuery("select * from samochody where vin=" + checked);
                        while (resultSet.next()) {
                            String vin = resultSet.getString("vin");
                            String brand = resultSet.getString("marka");
                            String model = resultSet.getString("model");
                            String state = resultSet.getString("status");
                            checkedVinArea.setText("VIN: " + vin + "\r\n" +
                                    "Marka: " + brand + "\r\n" +
                                    "Model: " + model + "\r\n" +
                                    "Stan: " + state);

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                checkVinField.setText("");
            }
        });

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch(permissions[0]){
                    case(0):
                        break;
                    case(1):
                        if(checkWorkerLogin(enterNameField.getText(),enterPassword.getText())){
                            primaryStage.setScene(workerMenuScene);
                        }
                        else
                            ////jakis komunikat
                            break;
                    case(2):
                        if(checkBossLogin(enterNameField.getText(),enterPassword.getText())){
                            primaryStage.setScene(bossMenuScene);
                        }
                        else
                            ///cos tam komunikat
                            break;

                }
            }
        });

        offerReturn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                primaryStage.setScene(beforeLoginScene);
            }
        });

        loginReturn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(beforeLoginScene);
            }
        });

        bossMenuParts.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    ResultSet resultSet = dataBase.giveDatafromQuery("select * from czesci");
                    orderArea.setText("");
                    while(resultSet.next()){
                        String text = orderArea.getText();
                        String name = resultSet.getString("Nazwa");
                        String onstate = resultSet.getString("Nastanie");
                        String onmagazyn = resultSet.getString("Namagazynie");
                        String price = resultSet.getString("Cena");
                        System.out.println(name+onmagazyn+onstate+price);
                        orderArea.setText(text + "\r\n"+"Nazwa: "+ name + " Na stanie: "+onstate+" Na magazynie: "+onmagazyn+" Cena: "+price);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                primaryStage.setScene(orderScene);
            }
        });

        orderOrder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = orderNameField.getText();
                String amount = orderField.getText();
                String pesel = orderVeryfField.getText();

                orderNameField.setText("");
                orderField.setText("");
                orderVeryfField.setText("");


                ArrayList<String> names = new ArrayList<>();
                try {
                    ResultSet resultSet = dataBase.giveDatafromQuery("Select * from czesci");
                    while(resultSet.next()){
                        names.add(resultSet.getString("Nazwa"));
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                boolean isItReal = false;
                for(String name1: names){
                    if(name1.equals(name)){
                        isItReal=true;
                    }
                }
                try{
                    int i = Integer.parseInt(amount);
                }
                catch(NumberFormatException e){
                    isItReal = false;
                }

                if(isItReal&&!doesItCheat(name)&&!doesItCheat(amount)&&!doesItCheat(pesel)){
                    try {
                        dataBase.updateTable("INSERT INTO logizakupow (Data,Czesc,Ilosc,Pracownik) VALUES (CURDATE(),'"+name+"',"+amount+","+pesel+")");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    ////////okrzycz za próbe hackowania
                }
            }
        });

        workerMenuParts.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(orderScene);
            }
        });

        bossMenuAddCar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(addCarScene);
            }
        });

        workerMenuAddCar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(addCarScene);
            }
        });

        addCarReturn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(permissions[0] == 1) primaryStage.setScene(workerMenuScene);
                else if(permissions[0] == 2) primaryStage.setScene(bossMenuScene);

            }
        });

        orderReturn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(permissions[0] == 1) primaryStage.setScene(workerMenuScene);
                else if(permissions[0] == 2) primaryStage.setScene(bossMenuScene);

            }
        });

        addQueueReturn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(permissions[0] == 1) primaryStage.setScene(workerMenuScene);
                else if(permissions[0] == 2) primaryStage.setScene(bossMenuScene);

            }
        });

        tableListReturn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(bossMenuScene);
            }
        });

        bossMenuTables.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(tableListScene);
            }
        });

        bossMenuBestWorker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    ResultSet resultSet = dataBase.giveDatafromQuery("CALL sprktonajwiecej()");
                    ArrayList<String> people = new ArrayList<>();
                    while(resultSet.next()){
                        people.add(resultSet.getString("Najlepszy"));
                    }
                    for(String pesel:people){
                        String text = bestWorkerArea.getText();
                        ResultSet resultSet1 = dataBase.giveDatafromQuery("SELECT * FROM pracownicy WHERE pesel="+pesel);
                        while(resultSet1.next()) {
                            String name = resultSet1.getString("Imię: ");
                            String subname = resultSet1.getString("Nazwisko: ");
                            bestWorkerArea.setText(text + "\r\n" + "Imię: " + name + " Nazwisko: " + subname + " Pesel: " + pesel);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                primaryStage.setScene(bestWorkerScene);
            }
        });

        bestWorkerReturn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(bossMenuScene);
            }
        });

        bossMenuFinances.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                financeArea.setText("");
                try {
                    ResultSet resultSet = dataBase.callSaldo();
                    if(resultSet.next()) {
                        String sum = resultSet.getString("suma");
                        financeArea.setText("Saldo warsztatu wynosi: " + sum);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                primaryStage.setScene(financesScene);
            }
        });

        financesReturn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(bossMenuScene);
            }
        });

        userReturn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addUserField.setText("");
                addUserPassField.setText("");
                addUserPeselField.setText("");
                deleteUserField.setText("");
                primaryStage.setScene(bossMenuScene);
            }
        });

        bossMenuUsers.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(userScene);
            }
        });

        bossMenuRepair.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(repairScene);
                try {
                    ResultSet resultSet = dataBase.giveDatafromQuery("select * from kolejkanapraw");
                    repairList.setText("");
                    while(resultSet.next()){
                        String text = repairList.getText();
                        String id = resultSet.getString("ID");
                        String repair = resultSet.getString("Problem");
                        String vehicle = resultSet.getString("Samochod");
                        repairList.setText(text + "\r\n"+"ID: "+ id + " Problem: "+repair+" VIN samochodu: "+vehicle);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        workerMenuRepair.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(repairScene);
            }
        });

        repairReturn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(permissions[0] == 1) primaryStage.setScene(workerMenuScene);
                else if(permissions[0] == 2) primaryStage.setScene(bossMenuScene);

            }
        });

        repairPartReturn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(permissions[0] == 1) primaryStage.setScene(workerMenuScene);
                else if(permissions[0] == 2) primaryStage.setScene(bossMenuScene);

            }
        });

        repairPartAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String repairNumber = repairRepairNumber.getText();
                String repairPart = repairPartNumber.getText();
                if(!doesItCheat(repairNumber)&&!doesItCheat(repairPart)) {
                    try {
                        dataBase.updateTable("insert into czescnaprawa values ('" + repairNumber + "','" + repairPart + "')");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        bossMenuRepairPart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(repairPartScene);
                try {
                    ResultSet resultSet = dataBase.giveDatafromQuery("select * from czesci");
                    repairPartList.setText("");
                    while (resultSet.next()) {
                        String text = repairPartList.getText();
                        String name = resultSet.getString("Nazwa");
                        String onstate = resultSet.getString("Nastanie");
                        String onmagazyn = resultSet.getString("Namagazynie");
                        String price = resultSet.getString("Cena");
                        repairPartList.setText(text + "\r\n" + "Nazwa: " + name + " Na stanie: " + onstate + " Na magazynie: " + onmagazyn + " Cena: " + price);
                    }
                    resultSet = dataBase.giveDatafromQuery("select * from logiczynnosci");
                    repairRepairList.setText("");
                    while (resultSet.next()) {
                        String text = repairRepairList.getText();
                        String id = resultSet.getString("ID");
                        String date = resultSet.getString("Data");
                        String thing = resultSet.getString("Czynnosc");
                        String vehicle = resultSet.getString("Samochod");
                        String worker = resultSet.getString("Pracownik");
                        String price = resultSet.getString("Cenadetal");
                        repairRepairList.setText(text + "\r\n" + "ID: " + id + " Data: " + date + " Czynność: " + thing + " Pojazd: " + vehicle +" Pracownik: " + worker + " Cena: " + price );
                    }
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
        });

        workerMenuRepairPart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(repairPartScene);
                try {
                    ResultSet resultSet = dataBase.giveDatafromQuery("select * from czesci");
                    repairPartList.setText("");
                    while (resultSet.next()) {
                        String text = repairPartList.getText();
                        String name = resultSet.getString("Nazwa");
                        String onstate = resultSet.getString("Nastanie");
                        String onmagazyn = resultSet.getString("Namagazynie");
                        String price = resultSet.getString("Cena");
                        repairPartList.setText(text + "\r\n" + "Nazwa: " + name + " Na stanie: " + onstate + " Na magazynie: " + onmagazyn + " Cena: " + price);
                    }
                    resultSet = dataBase.giveDatafromQuery("select * from logiczynnosci");
                    repairRepairList.setText("");
                    while (resultSet.next()) {
                        String text = repairRepairList.getText();
                        String id = resultSet.getString("ID");
                        String date = resultSet.getString("Data");
                        String thing = resultSet.getString("Czynnosc");
                        String vehicle = resultSet.getString("Samochod");
                        String worker = resultSet.getString("Pracownik");
                        String price = resultSet.getString("Cenadetal");
                        repairRepairList.setText(text + "\r\n" + "ID: " + id + " Data: " + date + " Czynność: " + thing + " Pojazd: " + vehicle +" Pracownik: " + worker + " Cena: " + price );
                    }
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
        });

        addCarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String clientFirstName = clientFirstNameField.getText();
                String clientSecondName = clientSecondNameField.getText();
                String clientPhoneNumber = clientPhoneNumberField.getText();

                String vin = vinField.getText();
                String brand = brandField.getText();
                String model = modelField.getText();

                clientFirstNameField.setText("");
                clientSecondNameField.setText("");
                clientPhoneNumberField.setText("");
                vinField.setText("");
                brandField.setText("");
                modelField.setText("");

                if(!doesItCheat(clientFirstName)&&!doesItCheat(clientSecondName)&&!doesItCheat(clientPhoneNumber)&&!doesItCheat(vin)&&!doesItCheat(brand)&&!doesItCheat(model)) {
                    try {

                        String isEmpty="";
                        ResultSet resultSet1 = dataBase.giveDatafromQuery("select id from klienci where imie ='" + clientFirstName + "' and nazwisko = '" + clientSecondName + "' and telefon = " + clientPhoneNumber);
                        while(resultSet1.next()){
                            System.out.println(resultSet1.getString("id"));
                            resultSet1.getString("id");
                        }
                        if(isEmpty.equals("")) {
                            dataBase.updateTable("insert into klienci (imie, nazwisko, telefon) values ('" + clientFirstName + "','" + clientSecondName + "','" + clientPhoneNumber + "')");
                        }
                        ResultSet resultSet = dataBase.giveDatafromQuery("select id from klienci where imie ='" + clientFirstName + "' and nazwisko = '" + clientSecondName + "' and telefon = " + clientPhoneNumber);
                        while (resultSet.next()) {
                            dataBase.updateTable("insert into samochody values (" + vin + ",'" + brand + "','" + model + "',"
                                    + resultSet.getString("id") + ",'zarejestrowane')");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if(permissions[0] == 1) primaryStage.setScene(workerMenuScene);
                else if(permissions[0] == 2) primaryStage.setScene(bossMenuScene);
            }
        });

        bossMenuAddQueue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(addQueueScene);
            }
        });

        bossMenuAddQueue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(addQueueScene);
            }
        });

        primaryStage.setScene(beforeLoginScene);
        primaryStage.show();



    }
}
