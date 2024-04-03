package co.edu.uptc.taller2.controllers;

import co.edu.uptc.taller2.services.ManagerFiles;
import co.edu.uptc.text.ManagerProperties;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
public class ShowManagerFiles {
    public ManagerFiles managerFiles = new ManagerFiles();

    public void Show () {
        ManagerProperties managerProperties = new ManagerProperties();
        managerProperties.setFileName("data.properties");
        try {
            managerFiles.setPath("people.xml");
            managerFiles.readFileXml();
            managerFiles.completePersonsXml();
            writeXml();
            managerFiles.setPath("people.json");
            managerFiles.readTxt();
            managerFiles.readFilejson();
            managerFiles.completePersonsJson();
            managerFiles.puntoA();
            managerFiles.createFileJson("puntoA.json", managerFiles.puntoA());
            managerFiles.puntoB();
            managerFiles.createFileJson("puntoB.json", managerFiles.puntoB());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void writeXml() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        NodeList persons = managerFiles.completePersonsXml();
        for (int i =0;i<persons.getLength();i++){
            Element person = (Element) persons.item(i);
            int age = Integer.parseInt(person.getElementsByTagName("edad").item(0).getTextContent());
            if(age<18){
                managerFiles.writeXml(person,"A.xml");
            }else{
                managerFiles.writeXml(person,"B.xml");
            }
        }
    }
}
