package com.example.plecakowy;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class View {
    private Operations operations = new Operations();
    private Converter converter = new Converter();
    private TextField privateKeyContent = new TextField();
    private TextField publicKeyContent = new TextField();
    private TextField lengthOfKeyContent = new TextField();
    private TextArea message = new TextArea();
    private TextField result = new TextField();
    private DaoFile daoFile = new DaoFile();
    private int n;
    private int m;

    public VBox setKeyArea() {
        VBox vBox = new VBox();
        Label firstKeyText = new Label("Private key");
        Label secondKeyText = new Label("Public key");
        Label lengthOfKey = new Label("Enter length of private key");
        Button generateKeyButton = new Button("Generate private key");

        generateKeyButton.setOnAction(e -> {
            // JESLI UZYTKOWNIK CHCE PODAC SWOJ KLUCZ PRYWATNY TO MOZE LUB MOZE WYGENEROWAC NA PODSTAWIE DLUGOSCI KLUCZA KTORA PODAJE
            if (!Objects.equals(lengthOfKeyContent.getText(), "")) {
                privateKeyContent.setText(Arrays.toString(operations.generatePrivateKey(Integer.parseInt(lengthOfKeyContent.getText()))));
            }
            m = operations.getM(converter.convertStringToIntArray(privateKeyContent.getText()));
            n = operations.findRelativelyPrime(m);
            publicKeyContent.setText(Arrays.toString(operations.createPublicKey(converter.convertStringToIntArray(privateKeyContent.getText()),n,m)));
        });

        vBox.getChildren().addAll(firstKeyText, privateKeyContent, lengthOfKey, lengthOfKeyContent, generateKeyButton, secondKeyText, publicKeyContent);
        vBox.setAlignment(Pos.TOP_CENTER);
        return vBox;
    }

    public VBox setMessageArea() {
        VBox vBox = new VBox();
        Label messageText = new Label("Message to encipher");
        Label blank = new Label();
        HBox hBox = new HBox();
        Button cipher = new Button("Encode");
        Button decipher = new Button("Decode");

        cipher.setOnAction(e -> {
            result.setText(Arrays.toString(operations.encipher(converter.convertStringToByte(converter.asciiToBinaryString(message.getText())),
                    converter.convertStringToIntArray(publicKeyContent.getText()))));
        });

        decipher.setOnAction(e -> {
            byte[] temp = operations.decipher(n,m, converter.convertStringToIntArray(privateKeyContent.getText()), converter.convertStringToIntArray(message.getText()));
            result.setText(converter.BytesToAscii(converter.convertByteToString(temp)));
        });

        hBox.getChildren().addAll(cipher, decipher);
        hBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(blank, messageText, message, hBox);
        vBox.setAlignment(Pos.TOP_CENTER);
        return vBox;
    }

    public VBox setResultArea() {
        VBox vBox = new VBox();
        Label resultText = new Label("Result");
        Label blank = new Label();

        vBox.getChildren().addAll(blank, resultText, result);
        vBox.setAlignment(Pos.TOP_CENTER);
        return vBox;
    }

    public VBox setFileArea() {
        VBox vBox = new VBox();
        Label FileText = new Label("File to encipher");
        Label blank = new Label();
        Label blank1 = new Label();
        Label blank2 = new Label();
        Button encipher = new Button("Choose file to encipher");
        Button decipher = new Button("Choose file to decipher");

        encipher.setOnAction(e -> {
            String path = this.getFilePath();
            try {
                int[] temp = operations.encipher(converter.convertFileToBytes(daoFile.readFile(path)),
                        converter.convertStringToIntArray(publicKeyContent.getText()));
                daoFile.writeDataFile(temp, path);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        decipher.setOnAction(e -> {
            String path = this.getFilePath();
            try {
                byte[] temp = operations.decipher(n,m, converter.convertStringToIntArray(privateKeyContent.getText()),
                        daoFile.readDataFile(path));
                System.out.println(Arrays.toString(daoFile.readDataFile(path)));
                daoFile.writeFile(converter.convertBytesToFile(temp), path);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        vBox.getChildren().addAll(blank1, FileText, blank, encipher, blank2, decipher);
        vBox.setAlignment(Pos.TOP_CENTER);
        return vBox;
    }

    private String getFilePath() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        return file.getAbsolutePath();
    }
}
