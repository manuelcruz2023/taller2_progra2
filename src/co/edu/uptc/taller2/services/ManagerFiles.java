package co.edu.uptc.taller2.services;

import co.edu.uptc.taller2.models.Person;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerFiles {
    private String path;
    private BufferedReader br;

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> readTxt() throws IOException {
        List<String> personas = new ArrayList<>();
        File file = new File(path);
        FileReader fr = new FileReader(file);
        br = new BufferedReader(fr);
        String line = "";
        while (br.readLine() != null) {
            personas.add(line += br.readLine());
        }
        fr.close();
        br.close();
        return personas;
    }

    public JSONArray readFilejson() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(new FileReader(path));
        return (JSONArray) obj;
    }

    public NodeList readFileXml() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        File archivoXML = new File(path);
        Document doc = dBuilder.parse(archivoXML);
        doc.getDocumentElement().normalize();
        NodeList personList = doc.getElementsByTagName("persona");
        return personList;
    }

    public NodeList completePersonsXml() throws ParserConfigurationException, IOException, SAXException {
        NodeList personList = readFileXml();
        return personList;
    }

    public List<Person> completePersonsJson() throws IOException, ParseException {
        List<Person> persons = new ArrayList<>();
        JSONArray jsonArray = readFilejson();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject person = (JSONObject) jsonArray.get(i);
            Person person1 = new Person();
            person1.setName((String) person.get("nombre"));
            person1.setLastName((String) person.get("apellido"));
            person1.setAge(Integer.parseInt(person.get("edad").toString()));
            person1.setSalary(Integer.parseInt(person.get("salario").toString()));
            persons.add(person1);
        }
        return persons;
    }

    public void writeXml(Node person, String fileName)
            throws ParserConfigurationException, IOException, TransformerException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
        Element rootElement = doc.createElement("person");
        doc.appendChild(rootElement);
        Node importedNode = doc.importNode(person, true);
        rootElement.appendChild(importedNode);
        FileWriter writer = new FileWriter(fileName, true);
        javax.xml.transform.TransformerFactory.newInstance().newTransformer().transform(
                new javax.xml.transform.dom.DOMSource(doc),
                new javax.xml.transform.stream.StreamResult(writer));
        writer.close();
    }

    public long promedioSalarios() throws IOException, ParseException {
        List<Person> persons = completePersonsJson();
        long promedioSalarios = 0;
        for (Person person : persons) {
            promedioSalarios += person.getSalary();
        }
        promedioSalarios = promedioSalarios / persons.size();
        return promedioSalarios;
    }

    public List<Person> puntoA() throws IOException, ParseException {
        long promedioSalarios = promedioSalarios();
        List<Person> persons = completePersonsJson();
        List<Person> puntoA = new ArrayList<>();
        for (Person person : persons) {
            if (person.getSalary() > promedioSalarios) {
                puntoA.add(person);
            }
        }
        return puntoA;
    }

    public List<Person> puntoB() throws IOException, ParseException {
        List<Person> persons = completePersonsJson();
        List<Person> puntoB = new ArrayList<>();
        long promedioSalarios = promedioSalarios();
        for (Person person : persons) {
            if (person.getSalary() < promedioSalarios) {
                puntoB.add(person);
            }
        }
        return puntoB;
    }

    public List<String> personTojsonList(List<Person> personList) throws IOException, ParseException {
        List<String> jsonList = new ArrayList<>();
        char c = '"';
        for (Person person : personList) {
            if (personList.indexOf(person) != personList.size() - 1) {
                jsonList.add("{" + c + "nombre" + c + ":" + c + person.getName() + c + "," + c + "apellido" + c + ":"
                        + c
                        + person.getLastName() + c + "," + c + "salario" + c + ":" + person.getSalary() + "," + c
                        + "edad" + c
                        + ":" + person.getAge() + " }" + "," + "\n");
            } else {
                jsonList.add("{" + c + "nombre" + c + ":" + c + person.getName() + c + "," + c + "apellido" + c + ":"
                        + c
                        + person.getLastName() + c + "," + c + "salario" + c + ":" + person.getSalary() + "," + c
                        + "edad" + c
                        + ":" + person.getAge() + "}" + "\n");
            }
        }
        return jsonList;
    }

    public void createFileJson(String nombreArchivo, List<Person> personList) throws Exception {
        List<String> listaPersonasString = personTojsonList(personList);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            bw.write("[");
            for (String elemento : listaPersonasString) {
                bw.write(elemento);
            }
            bw.write("]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
