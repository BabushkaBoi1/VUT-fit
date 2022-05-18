package uml.ija.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Trida pro praci se souborem (ulozeni a nacteni json)
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class ReadWriteFile {
    /**
     * Konstruktor
     */
    public ReadWriteFile() {
    }

    /**
     * Trida pro cteni ze souboru
     *
     * @param filename Soubor ze ktereho se ma cist
     * @return ClassDiagram Vytvoreny diagram
     */
    public ClassDiagram readFromFile(String filename) {
        // Docasna cesta
        String path = "C:\\Users\\tomas\\Desktop\\škola\\Brno\\4 semestr\\IJA\\git_verze\\IJA\\test.json";

        StringBuilder builder = new StringBuilder();

        try (BufferedReader buffer = new BufferedReader(new FileReader(filename))) {
            String str;
            while ((str = buffer.readLine()) != null) {

                builder.append(str).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Systém nemůže nalézt uvedený soubor");
            return null;
        }

        String json = builder.toString();

        ClassDiagram classDiagram = null;
        try {
            JSONObject root = new JSONObject(json);

            // Vytvoreni diagramu s nazvem
            classDiagram = new ClassDiagram(root.getString("name"));

            // Naplneni klasifikatoru
            JSONArray classifiers = root.getJSONArray("classifiers");
            for (int i = 0; i < classifiers.length(); i++) {
                JSONObject row = classifiers.getJSONObject(i);
                if (row.getString("name").equals("string") || row.getString("name").equals("bool") || row.getString("name").equals("void") || row.getString("name").equals("int")) {
                    continue;
                }
                UMLClassifier classifier = new UMLClassifier(row.getString("name"), row.getBoolean("isUserDefined"));
                classDiagram.addClassifier(classifier);
            }

//            // Naplneni modifikatory pristupnosti
//            JSONArray accessClassifiers = root.getJSONArray("accessClassifiers");
//            for (int i = 0; i < accessClassifiers.length(); i++)
//            {
//                JSONObject row = accessClassifiers.getJSONObject(i);
//                UMLClassifier accessClassifier = new UMLClassifier(row.getString("name"), row.getBoolean("isUserDefined"));
//                classDiagram.addAccessClassifier(accessClassifier);
//            }

            // Naplneni trid
            JSONArray classes = root.getJSONArray("classes");
            for (int i = 0; i < classes.length(); i++) {
                JSONObject jsonClass = classes.getJSONObject(i);
                UMLClass umlClass = new UMLClass(jsonClass.getString("name"));

                // Napleni pozice
                GraphicPosition position = new GraphicPosition();
                JSONObject jsonPosition = jsonClass.getJSONObject("position");
                position.setX(jsonPosition.getDouble("x"));
                position.setY(jsonPosition.getDouble("y"));
                position.setWidth(jsonPosition.getDouble("width"));
                position.setHeight(jsonPosition.getDouble("height"));
                umlClass.setPosition(position);

                GraphicPosition positionSq = new GraphicPosition();
                JSONObject jsonPositionSq = jsonClass.getJSONObject("sequencePosition");
                positionSq.setX(jsonPositionSq.getDouble("x"));
                positionSq.setY(jsonPositionSq.getDouble("y"));
                positionSq.setWidth(jsonPositionSq.getDouble("width"));
                positionSq.setHeight(jsonPositionSq.getDouble("height"));
                umlClass.setSequencePosition(positionSq);

                // Naplneni operaci
                JSONArray jsonOperations = jsonClass.getJSONArray("operations");
                for (int j = 0; j < jsonOperations.length(); j++) {
                    JSONObject jsonOperation = jsonOperations.getJSONObject(j);

                    JSONObject jsonAccessType = jsonOperation.getJSONObject("accessType");
                    JSONObject jsonType = jsonOperation.getJSONObject("type");
                    JSONArray jsonArgs = jsonOperation.getJSONArray("args");

                    // argumenty operace
                    java.util.List<UMLAttribute> args = new ArrayList<>();
                    for (int k = 0; k < jsonArgs.length(); k++) {
                        JSONObject jsonArg = jsonArgs.getJSONObject(k);
                        JSONObject jsonArgType = jsonArg.getJSONObject("type");
                        UMLAttribute att = new UMLAttribute(jsonArg.getString("name"), classDiagram.classifierForName(jsonArgType.getString("name")));
                        args.add(att);
                    }

                    // Vytvoreni operace
                    UMLOperation operation = UMLOperation.create(jsonOperation.getString("name"), classDiagram.classifierForName(jsonType.getString("name")), UMLClassifier.forName(jsonAccessType.getString("name")), args);

                    umlClass.addOperation(operation);
                }

                // Naplneni atributu
                JSONArray jsonAttributes = jsonClass.getJSONArray("attributes");
                for (int j = 0; j < jsonAttributes.length(); j++) {
                    JSONObject jsonAttribute = jsonAttributes.getJSONObject(j);
                    JSONObject jsonAccessType = jsonAttribute.getJSONObject("accessType");
                    JSONObject jsonType = jsonAttribute.getJSONObject("type");
                    UMLAttribute attribute = new UMLAttribute(jsonAttribute.getString("name"), classDiagram.classifierForName(jsonType.getString("name")), UMLClassifier.forName(jsonAccessType.getString("name")));

                    attribute.setList(jsonAttribute.getBoolean("isList"));
                    umlClass.addAttribute(attribute);
                }

                // Pridani tridy do diagramu
                classDiagram.addClass(umlClass);
            }

            // Naplneni vazeb
            JSONArray relations = root.getJSONArray("relations");
            for (int i = 0; i < relations.length(); i++) {
                JSONObject row = relations.getJSONObject(i);
                UMLRelation relation = new UMLRelation();

                // Nastaveni class
                UMLClass classFrom = classDiagram.findClass(row.getString("classNameFrom"));
                UMLClass classTo = classDiagram.findClass(row.getString("classNameTo"));
                UMLClassifier type = classDiagram.findRelationsClassifier(row.getString("type"));

                if (type.getName().equals("generalization")) {
                    classFrom.setExtendFrom(classTo);
                }

                String valueFrom = row.getString("valueFrom");
                String valueTo = row.getString("valueTo");

                relation.setClassFrom(classFrom);
                relation.setClassTo(classTo);
                relation.setTypeOfRelation(type);
                relation.setValueTo(valueTo);
                relation.setValueFrom(valueFrom);

                classDiagram.addRelation(relation);
            }

            // Napleneni sekvencnich diagramu
            JSONArray sequenceDiagrams = root.getJSONArray("sequenceDiagrams");
            for (int i = 0; i < sequenceDiagrams.length(); i++) {
                JSONObject sequenceRow = sequenceDiagrams.getJSONObject(i);
                SequenceDiagram sequenceDiagram = new SequenceDiagram(sequenceRow.getString("name"));

                // Pridani trid
                JSONArray sequenceClasses = sequenceRow.getJSONArray("classes");
                for (int j = 0; j < sequenceClasses.length(); j++) {
                    String className = sequenceClasses.getString(j);
                    sequenceDiagram.addClass(classDiagram.findClass(className));
                }

                // Pridani zprav
                JSONArray sequenceMessages = sequenceRow.getJSONArray("messages");
                for (int j = 0; j < sequenceMessages.length(); j++) {
                    JSONObject jsonMessage = sequenceMessages.getJSONObject(j);

                    UMLClass umlClassFrom = classDiagram.findClass(jsonMessage.getString("from"));
                    UMLClass umlClassTo = classDiagram.findClass(jsonMessage.getString("to"));
                    String typeOfMessage = jsonMessage.getString("typeOfMessage");
                    String operation = jsonMessage.getString("operation");
                    SequenceMessage message = new SequenceMessage(umlClassFrom, umlClassTo, typeOfMessage, operation);
                    message.setMessage(jsonMessage.getString("message"));

                    sequenceDiagram.addMessage(message);
                }
                classDiagram.addSequenceDiagram(sequenceDiagram);
            }

        } catch (JSONException e) {
            System.out.println(e.getMessage());
            System.out.println("Spatny format JSON nebo chybi nektere potrebne informace");
            return null;
        }

        return classDiagram;

    }

    /**
     * Trida pro zapsani souboru
     *
     * @param classDiagram třída
     * @param filename     soubor
     * @return True - pokud se podarilo nacist soubor uspesne <br> False - nepodarilo se nacist soubor
     */
    public boolean printToFile(ClassDiagram classDiagram, String filename) {
        // Jmeno modelu
        JSONObject jsonDiagram = new JSONObject();
        jsonDiagram.put("name", classDiagram.getName());

        // Klasifikatory
        JSONArray listClassifier = new JSONArray();
        for (UMLClassifier classifier : classDiagram.getClassifiers()) {
            JSONObject jsonClassifier = new JSONObject();
            jsonClassifier.put("name", classifier.getName());
            jsonClassifier.put("isUserDefined", classifier.isUserDefined());
            listClassifier.put(jsonClassifier);
        }
        jsonDiagram.put("classifiers", listClassifier);

        // Tridy
        JSONArray listClass = new JSONArray();
        for (UMLClass umlClass : classDiagram.getClasses()) {
            // Jmeno
            JSONObject jsonClass = new JSONObject();
            jsonClass.put("name", umlClass.getName());

            // Pozice
            JSONObject jsonPosition = new JSONObject();
            GraphicPosition position = umlClass.getPosition();
            jsonPosition.put("x", position.getX());
            jsonPosition.put("y", position.getY());
            jsonPosition.put("width", position.getWidth());
            jsonPosition.put("height", position.getHeight());

            jsonClass.put("position", jsonPosition);

            // Pozice sekvecniho diagramu
            JSONObject jsonPositionSq = new JSONObject();
            GraphicPosition positionSq = umlClass.getSequencePosition();
            if (positionSq != null) {
                jsonPositionSq.put("x", positionSq.getX());
                jsonPositionSq.put("y", positionSq.getY());
                jsonPositionSq.put("width", positionSq.getWidth());
                jsonPositionSq.put("height", positionSq.getHeight());
            } else {
                jsonPositionSq.put("x", 0);
                jsonPositionSq.put("y", 0);
                jsonPositionSq.put("width", 0);
                jsonPositionSq.put("height", 0);
            }

            jsonClass.put("sequencePosition", jsonPositionSq);

            // Operace
            JSONArray listOperation = new JSONArray();
            for (UMLOperation operation : umlClass.getOperations()) {
                // Nazev
                JSONObject jsonOperation = new JSONObject();
                jsonOperation.put("name", operation.getName());

                // Navratovy typ
                JSONObject jsonClassifier = new JSONObject();
                jsonClassifier.put("name", operation.getType().getName());
                jsonClassifier.put("isUserDefined", operation.getType().isUserDefined());
                jsonOperation.put("type", jsonClassifier);

                // Modifikator pristupu
                JSONObject jsonClassifierAccess = new JSONObject();
                jsonClassifierAccess.put("name", operation.getAccessType().getName());
                jsonClassifierAccess.put("isUserDefined", operation.getAccessType().isUserDefined());
                jsonOperation.put("accessType", jsonClassifierAccess);

                // argumenty
                JSONArray listArgs = new JSONArray();
                for (UMLAttribute arg : operation.getArguments()) {
                    JSONObject jsonArg = new JSONObject();
                    jsonArg.put("name", arg.getName());

                    // Typ argumentu
                    JSONObject jsonClassifierArg = new JSONObject();
                    jsonClassifierArg.put("name", arg.getType().getName());
                    jsonClassifierArg.put("isUserDefined", arg.getType().isUserDefined());
                    jsonArg.put("type", jsonClassifierArg);

                    listArgs.put(jsonArg);
                }
                jsonOperation.put("args", listArgs);

                listOperation.put(jsonOperation);

            }
            jsonClass.put("operations", listOperation);

            //Atributy
            JSONArray listAttributes = new JSONArray();
            for (UMLAttribute att : umlClass.getAttributes()) {
                JSONObject jsonAtt = new JSONObject();
                jsonAtt.put("name", att.getName());
                jsonAtt.put("isList", att.isList());

                // Typ atributu
                JSONObject jsonClassifierAtt = new JSONObject();
                jsonClassifierAtt.put("name", att.getType().getName());
                jsonClassifierAtt.put("isUserDefined", att.getType().isUserDefined());
                jsonAtt.put("type", jsonClassifierAtt);

                // Modifikator pristupnosti atributu
                JSONObject jsonAccessAtt = new JSONObject();
                jsonAccessAtt.put("name", att.getAccessType().getName());
                jsonAccessAtt.put("isUserDefined", att.getAccessType().isUserDefined());
                jsonAtt.put("accessType", jsonAccessAtt);

                listAttributes.put(jsonAtt);
            }
            jsonClass.put("attributes", listAttributes);

            listClass.put(jsonClass);
        }
        jsonDiagram.put("classes", listClass);

        // Vazby
        JSONArray listRelations = new JSONArray();
        for (UMLRelation umlRelation : classDiagram.getRelations()) {
            JSONObject jsonRelation = new JSONObject();

            jsonRelation.put("classNameFrom", umlRelation.getClassFrom().getName());
            jsonRelation.put("classNameTo", umlRelation.getClassTo().getName());
            jsonRelation.put("type", umlRelation.getTypeOfRelation().getName());

            jsonRelation.put("valueFrom", umlRelation.getValueFrom());
            jsonRelation.put("valueTo", umlRelation.getValueTo());

            listRelations.put(jsonRelation);
        }
        jsonDiagram.put("relations", listRelations);


        // Sekvecni diagramy
        JSONArray sequenceDiagrams = new JSONArray();
        for (SequenceDiagram sequenceDiagram : classDiagram.getSequenceDiagrams()) {
            JSONObject jsonSequenceDiagram = new JSONObject();
            jsonSequenceDiagram.put("name", sequenceDiagram.getName());

            // Tridy
            JSONArray sequnceClasses = new JSONArray();
            for (UMLClass umlClass : sequenceDiagram.getClasses()) {
                sequnceClasses.put(umlClass.getName());
            }
            jsonSequenceDiagram.put("classes", sequnceClasses);

            // Zpravy
            JSONArray messages = new JSONArray();
            for (SequenceMessage message : sequenceDiagram.getMessages()) {
                JSONObject jsonMessage = new JSONObject();
                jsonMessage.put("from", message.getFrom().getName());
                jsonMessage.put("to", message.getTo().getName());
                jsonMessage.put("typeOfMessage", message.getTypeOfMessage());
                jsonMessage.put("operation", message.getOperation());
                if (message.getMessage().equals("")) {
                    jsonMessage.put("message", "Return");
                } else {
                    jsonMessage.put("message", message.getMessage());
                }

                messages.put(jsonMessage);
            }
            jsonSequenceDiagram.put("messages", messages);

            sequenceDiagrams.put(jsonSequenceDiagram);
        }

        jsonDiagram.put("sequenceDiagrams", sequenceDiagrams);

        try (FileWriter file = new FileWriter(filename)) {
            file.write(jsonDiagram.toString(4));
            return true;
        } catch (IOException e) {
            System.out.println("Systém nemůže nalézt uvedenou cestu");
            return false;
        }
    }
}
