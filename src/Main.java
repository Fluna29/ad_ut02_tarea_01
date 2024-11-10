import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.gson.stream.JsonReader;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class Main {

    public static void main(String[] args) {
        try {
            // Read the JSON from the URL
            String url = "https://raw.githubusercontent.com/jeffreylancaster/game-of-thrones/master/data/characters.json";
            List<CharactersJson> charactersList = readJSON(url);

            // Print each character's attributes
            charactersList.forEach(character -> {
                System.out.println(
                        "\nCharacter Name: " + character.getCharacterName() +
                                "\n- Character Link: " + character.getCharacterLink() +
                                "\n- Character Image Thumb: " + character.getCharacterImageThumb() +
                                "\n- Character Image Full: " + character.getCharacterImageFull() +
                                "\n- Actor name / Actors names: " + actorsNames(character) +
                                "\n- Actor Link: " + character.getActorLink() +
                                "\n- House Name: " + character.getHouseNameAsString() +
                                "\n- Nickname: " + character.getNickname() +
                                "\n- Royal: " + character.getRoyal() +
                                "\n- Kings Guard: " + character.getKingsguard() +
                                "\n- Parents: " + character.getParents() +
                                "\n- Parent Of: " + character.getParentOf() +
                                "\n- Guardian Of: " + character.getGuardianOf() +
                                "\n- Guarded By: " + character.getGuardedBy() +
                                "\n- Siblings: " + character.getSiblings() +
                                "\n- Married Engaged: " + character.getMarriedEngaged() +
                                "\n- Allies: " + character.getAllies() +
                                "\n- Abducted: " + character.getAbducted() +
                                "\n- Killed: " + character.getKilled() +
                                "\n- Killed By: " + character.getKilledBy() +
                                "\n- Serves: " + character.getServes() +
                                "\n- Served By: " + character.getServedBy()
                );
            });

            // Marshall each character into an XML file
            for (CharactersJson character : charactersList) {
                String filePath = "testJson/By_Character/" + character.getCharacterName().replaceAll("\\s+", "_") + ".xml";
                marshalling(character, filePath);
            }

            // Marshall the whole list of characters into an XML file
            CharactersListWrapper wrapper = new CharactersListWrapper();
            wrapper.setCharacters(charactersList);
            String filePath = "testJson/characters.xml";
            marshalling(wrapper, filePath);

            // Validate charactersList.xml against charactersList.xsd
            validateXml("testJson/characters.xml", "testJson/characters.xsd");

            // Validate characters_novalid.xml
            validateXml("testJson/characters_novalid.xml", "testJson/characters.xsd");

            // Parse the XML file to a DOM document
            parsingDom("testJson/characters.xml").forEach(character -> {
                System.out.println("\nCharacter Name: " + character.getCharacterName());
                System.out.println("Actor Name: " + (character.getActorNameAsString() != null ? character.getActorNameAsString() : "?"));
            });

            //Generate CSV file
            generateCsv(charactersList,"testJson/characters.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Read JSON from the URL using Gson
    public static List<CharactersJson> readJSON(String url) throws Exception {
        Gson gson = new GsonBuilder().create();
        try (InputStreamReader reader = new InputStreamReader(new URL(url).openStream())) {
            Map<String, List<CharactersJson>> jsonMap = gson.fromJson(reader, new TypeToken<Map<String, List<CharactersJson>>>(){}.getType());
            return jsonMap.get("characters");
        }
    }

    // Marshalling method with dynamic JAXBContext based on the object's class
    public static void marshalling(Object object, String filePath) throws Exception {
        JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(object, new File(filePath));
    }

    // Method to get actor names based on the presence of multiple actors
    public static String actorsNames(CharactersJson character) {
        if (character.getActors() == null || character.getActors().isEmpty()) {
            return character.getActorNameAsString();
        } else {
            StringBuilder actorNames = new StringBuilder();
            character.getActors().forEach(actor -> {
                actorNames.append("\nActor Name: ").append(actor.getActorName())
                        .append(", Actor Link: ").append(actor.getActorLink())
                        .append(", Seasons Active: ").append(actor.getSeasonsActive());
            });
            return actorNames.toString();
        }
    }

    public static void validateXml(String xmlPath, String xsdPath) {
        try {
            // Create the schema capable of understanding W3C XML Schema
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));

            //Create a validator for the schema
            Validator validator = schema.newValidator();

            // Validate the XML file
            validator.validate(new StreamSource(new File(xmlPath)));
            System.out.println("\nThe document " + xmlPath + " is valid.");
        } catch (Exception e) {
            System.out.println("\nThe document " + xmlPath + " isn't valid: " + e.getMessage());
        }
    }

    public static List<CharactersJson> parsingDom(String xmlfilePath) {
        List<CharactersJson> charactersList = new ArrayList<>();

        try {
            // Create a DocumentBuilderFactory and DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse the XML file to a DOM document
            Document document = builder.parse(xmlfilePath);
            document.getDocumentElement().normalize();

            // Obtain the list of nodes with the tag "character_data"
            NodeList nodeList = document.getElementsByTagName("character_data");

            // Iterate over the nodes
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Create a new CharactersJson object
                    CharactersJson character = new CharactersJson();

                    // Set the attributes of the character object
                    character.setCharacterName(getTextContent(element, "character_name"));
                    character.setCharacterLink(getTextContent(element, "character_link"));
                    character.setCharacterImageThumb(getTextContent(element, "character_image_thumb"));
                    character.setCharacterImageFull(getTextContent(element, "character_image_full"));

                    // Create a JSON element for the actor name
                    String actorName = getTextContent(element, "actor_name");
                    JsonElement actorNameJson = parseJsonLenient("\"" + (actorName != null ? actorName : "?") + "\"");
                    character.setActorName(actorNameJson);

                    character.setActorLink(getTextContent(element, "actor_link"));
                    character.setNickname(getTextContent(element, "nickname"));
                    character.setRoyal(Boolean.parseBoolean(getTextContent(element, "royal")));
                    character.setKingsguard(Boolean.parseBoolean(getTextContent(element, "kingsguard")));

                    // Add character to the list
                    charactersList.add(character);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return charactersList;
    }

    //Method to get the text content of an element
    private static String getTextContent(Element element , String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
                return node.getTextContent();
            }
        }
        return null;
    }

    // Method to parse JSON in lenient mode
    private static JsonElement parseJsonLenient(String jsonString) {
        JsonReader reader = new JsonReader(new StringReader(jsonString));
        reader.setLenient(true);
        return JsonParser.parseReader(reader);
    }

    // Function to generate the CSV file
    public static void generateCsv(List<CharactersJson> charactersList, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write the header (first line) with capitalized attribute names and spaces
            writer.write("Character Name;Actor Name;Actor Link;Character Image Full;Character Image Thumb;Nickname;Royal;Kings Guard\n");

            // Write data rows for each character
            for (CharactersJson character : charactersList) {
                writer.write(String.format("%s;%s;%s;%s;%s;%s;%b;%b\n",
                        character.getCharacterName(),
                        character.getActorNameAsString(),
                        character.getActorLink(),
                        character.getCharacterImageFull(),
                        character.getCharacterImageThumb(),
                        character.getNickname(),
                        character.getRoyal() != null ? character.getRoyal() : false,
                        character.getKingsguard() != null ? character.getKingsguard() : false
                ));
            }

            System.out.println("\nCSV file generated successfully at " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
