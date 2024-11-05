import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        try {
            //We read the JSON from the URL
            String url = "https://raw.githubusercontent.com/jeffreylancaster/game-of-thrones/master/data/characters.json";
            List<CharactersJson> charactersList = readJSON(url);

            // For each character, we print one by one all the attributes
            charactersList.forEach(character -> System.out.println(
                    "Character Name: " + character.getCharacterName() +
                            ", Character Link: " + character.getCharacterLink() +
                            ", Character Image Thumb: " + character.getCharacterImageThumb() +
                            ", Character Image Full: " + character.getCharacterImageFull() +
                            ", Actor name / Actors names: " + character.getActorNameAsString() +
                            ", Actor Link: " + character.getActorLink() +
                            ", House Name: " + character.getHouseName() +
                            ", Nickname: " + character.getNickname() +
                            ", Royal: " + character.getRoyal() +
                            ", Kingsguard: " + character.getKingsguard() +
                            ", Parents: " + character.getParents() +
                            ", Parent Of: " + character.getParentOf() +
                            ", Guardian Of: " + character.getGuardianOf() +
                            ", Guarded By: " + character.getGuardedBy() +
                            ", Siblings: " + character.getSiblings() +
                            ", Married Engaged: " + character.getMarriedEngaged() +
                            ", Allies: " + character.getAllies() +
                            ", Abducted: " + character.getAbducted() +
                            ", Killed: " + character.getKilled() +
                            ", Killed By: " + character.getKilledBy() +
                            ", Serves: " + character.getServes() +
                            ", Served By: " + character.getServedBy()
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //We use the Gson library to read the JSON from the URL we have to give to the method
    public static List<CharactersJson> readJSON(String url) throws Exception {
        Gson gson = new GsonBuilder().create();
        try (InputStreamReader reader = new InputStreamReader(new URL(url).openStream())) {
            //Here we are using a TypeToken to specify that we are expecting a Map with a String as key and a List of CharactersJson as value
            Map<String, List<CharactersJson>> jsonMap = gson.fromJson(reader, new TypeToken<Map<String, List<CharactersJson>>>(){}.getType());

            //The JSON has a root element called "characters" that contains the list of characters
            return jsonMap.get("characters");
        }
    }
}