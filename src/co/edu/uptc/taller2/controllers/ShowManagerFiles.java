package co.edu.uptc.taller2.controllers;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import co.edu.uptc.taller2.services.ManagerFiles;
import co.edu.uptc.text.ManagerProperties;

public class ShowManagerFiles {
    public void Show () {
        ManagerProperties managerProperties = new ManagerProperties();
        managerProperties.setFileName("data.properties");
        ManagerFiles managerFiles = new ManagerFiles();
        managerFiles.setPath("people.json");
        try {
            managerFiles.readTxt();
            managerFiles.readFilejson();
            managerFiles.completPersons();
            managerFiles.puntoA();
            managerFiles.createFileJson("puntoA.json", managerFiles.puntoA());
            managerFiles.puntoB();
            managerFiles.createFileJson("puntoB.json", managerFiles.puntoB());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
