import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        try {
            // We read the JSON from the URL
            String url = "https://raw.githubusercontent.com/jeffreylancaster/game-of-thrones/master/data/characters.json";
            List<CharactersJson> charactersList = readJSON(url);

            // For each character, we print one by one all the attributes
            charactersList.forEach(character -> {
                System.out.println(
                        "\nCharacter Name: " + character.getCharacterName() +
                                "\n- Character Link: " + character.getCharacterLink() +
                                "\n- Character Image Thumb: " + character.getCharacterImageThumb() +
                                "\n- Character Image Full: " + character.getCharacterImageFull() +
                                //Here we use the function actorsNames located in Main, to get the actor name or names
                                "\n- Actor name / Actors names: " + actorsNames(character)+
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
            for (CharactersJson character : charactersList) {
                String filePath = "testJson/" + character.getCharacterName().replaceAll("\\s+", "_") + ".xml";
                marshalling(character, filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    // We use the Gson library to read the JSON from the URL we have to give to the method
    public static List<CharactersJson> readJSON(String url) throws Exception {
        Gson gson = new GsonBuilder().create();
        try (InputStreamReader reader = new InputStreamReader(new URL(url).openStream())) {
            // Here we are using a TypeToken to specify that we are expecting a Map with a String as key and a List of CharactersJson as value
            Map<String, List<CharactersJson>> jsonMap = gson.fromJson(reader, new TypeToken<Map<String, List<CharactersJson>>>(){}.getType());

            // The JSON has a root element called "characters" that contains the list of characters
            return jsonMap.get("characters");
        }
    }

    // We use JAXB to marshall the CharactersJson object into an XML file
    public static void marshalling(Object character, String filePath) throws Exception {
        JAXBContext context = JAXBContext.newInstance(CharactersJson.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(character, new File(filePath));
    }

    //This method detects if it is a single actor or multiple actors and returns the names,
    //works when data returns in Console, not in XML.
    public static String actorsNames(CharactersJson character) {
        if (character.getActors() == null || character.getActors().isEmpty()) {
            return character.getActorNameAsString();
        } else {
            StringBuilder actorNames = new StringBuilder();
            character.getActors().forEach(actor -> {
                actorNames.append("Actor Name: ").append(actor.getActorName())
                        .append(", Actor Link: ").append(actor.getActorLink())
                        .append(", Seasons Active: ").append(actor.getSeasonsActive()).append("\n");
            });
            return actorNames.toString();
        }
    }
}