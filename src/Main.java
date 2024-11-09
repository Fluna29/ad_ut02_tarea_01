import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;

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

}
