package co.edu.uptc.taller2.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import co.edu.uptc.taller2.models.Person;

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
        JSONArray jsonArray = (JSONArray) obj;
        return jsonArray;
    }

    public List<Person> completPersons() throws IOException, ParseException {
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
        System.out.println("Se cargaron " + persons.size() + " personas");
        return persons;
    }

    public long promedioSalarios() throws IOException, ParseException {
        List<Person> persons = completPersons();
        long promedioSalarios = 0;
        for (Person person : persons) {
            promedioSalarios += person.getSalary();
        }
        promedioSalarios = promedioSalarios / persons.size();
        return promedioSalarios;
    }

    public List<Person> puntoA() throws IOException, ParseException {
        long promedioSalarios = promedioSalarios();
        List<Person> persons = completPersons();
        List<Person> puntoA = new ArrayList<>();
        for (Person person : persons) {
            if (person.getSalary() > promedioSalarios) {
                puntoA.add(person);
            }
        }
        return puntoA;
    }

    public List<Person> puntoB() throws IOException, ParseException {
        List<Person> persons = completPersons();
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
                jsonList.add("{" + c + "nombre" + c + ":" + c + person.getName() + c + "," + c + "apellido" + c + ":" + c
                        + person.getLastName() + c + "," + c + "salario" + c + ":" + person.getSalary() + "," + c
                        + "edad" + c
                        + ":" + person.getAge() +" }" + "," + "\n");
            }else{
                jsonList.add("{" + c + "nombre" + c + ":" + c + person.getName() + c + "," + c + "apellido" + c + ":" + c
                        + person.getLastName() + c + "," + c + "salario" + c + ":" + person.getSalary() + "," + c
                        + "edad" + c
                        + ":" + person.getAge() +" }" + "\n");
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
