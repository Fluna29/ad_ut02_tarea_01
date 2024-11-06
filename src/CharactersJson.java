import com.google.gson.JsonElement;
import java.util.List;
import java.util.ArrayList;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "character")
//We use XmlType to define the order of the elements in the XML file
@XmlType(propOrder = {
        "characterName", "characterLink", "characterImageThumb", "characterImageFull",
        "actorNameAsString", "actorLink", "actors", "houseNameAsString", "nickname", "royal", "kingsguard",
        "parents", "parentOf", "guardianOf", "guardedBy", "siblings",
        "marriedEngaged", "allies", "abducted", "killed", "killedBy", "serves", "servedBy"
})
public class CharactersJson {

    @XmlElement(name = "character_name")
    private String characterName;

    @XmlElement(name = "character_link")
    private String characterLink;

    @XmlElement(name = "character_image_thumb")
    private String characterImageThumb;

    @XmlElement(name = "character_image_full")
    private String characterImageFull;

    private JsonElement actorName;  // It can be a single String or an array of Strings

    @XmlElement(name = "actor_name")
    //We use this function to get the actor name when it is only one actor.
    //If it is an array of actors, we will make use of the inner class Actor.
    public String getActorNameAsString() {
        if (actorName == null) return null;
        if (actorName.isJsonPrimitive()) {
            return actorName.getAsString();
        }
        return null;  // Returns null in case it's an array.
    }

    @XmlElement(name = "actor_link")
    private String actorLink;

    @XmlElementWrapper(name = "actors")
    @XmlElement(name = "actor")
    //This is used for the case when there are more than one actor for a character.
    private List<Actor> actors;

    private JsonElement houseName;  // It can be a single String or an array of Strings

    @XmlElementWrapper(name = "houses")
    @XmlElement(name = "house_name")
    //We use this function to get the house name in a String or turn it in an Array of Strings called "houses".
    public List<String> getHouseNameAsString() {
        if (houseName == null) return null;
        if (houseName.isJsonPrimitive()) {
            return List.of(houseName.getAsString());  // Wrap the single string in a list
        } else if (houseName.isJsonArray()) {
            List<String> houses = new ArrayList<>();
            houseName.getAsJsonArray().forEach(element -> houses.add(element.getAsString()));
            return houses;
        }
        return null;
    }

    @XmlElement(name = "nickname")
    private String nickname;

    @XmlElement(name = "royal")
    private Boolean royal;

    @XmlElement(name = "kingsguard")
    private Boolean kingsguard;

    @XmlElementWrapper(name = "parents")
    @XmlElement(name = "name")
    private List<String> parents;

    @XmlElementWrapper(name = "parent_of")
    @XmlElement(name = "name")
    private List<String> parentOf;

    @XmlElementWrapper(name = "guardian_of")
    @XmlElement(name = "name")
    private List<String> guardianOf;

    @XmlElementWrapper(name = "guarded_by")
    @XmlElement(name = "name")
    private List<String> guardedBy;

    @XmlElementWrapper(name = "siblings")
    @XmlElement(name = "name")
    private List<String> siblings;

    @XmlElementWrapper(name = "married_engaged")
    @XmlElement(name = "name")
    private List<String> marriedEngaged;

    @XmlElementWrapper(name = "allies")
    @XmlElement(name = "name")
    private List<String> allies;

    @XmlElementWrapper(name = "abducted")
    @XmlElement(name = "name")
    private List<String> abducted;

    @XmlElementWrapper(name = "killed")
    @XmlElement(name = "name")
    private List<String> killed;

    @XmlElementWrapper(name = "killed_by")
    @XmlElement(name = "name")
    private List<String> killedBy;

    @XmlElementWrapper(name = "serves")
    @XmlElement(name = "name")
    private List<String> serves;

    @XmlElementWrapper(name = "served_by")
    @XmlElement(name = "name")
    private List<String> servedBy;

    // Getters & Setters
    public String getCharacterName() { return characterName; }
    public String getCharacterLink() { return characterLink; }
    public String getCharacterImageThumb() { return characterImageThumb; }
    public String getCharacterImageFull() { return characterImageFull; }
    public String getActorLink() { return actorLink; }
    //We only put this getter for the case when there are more than one actor for a character.
    //Becuase, in the case that is just one actor, we will use the function getActorNameAsString.
    public List<Actor> getActors() { return actors; }
    public String getNickname() { return nickname; }
    public Boolean getRoyal() { return royal; }
    public Boolean getKingsguard() { return kingsguard; }
    public List<String> getParents() { return parents; }
    public List<String> getParentOf() { return parentOf; }
    public List<String> getGuardianOf() { return guardianOf; }
    public List<String> getGuardedBy() { return guardedBy; }
    public List<String> getSiblings() { return siblings; }
    public List<String> getMarriedEngaged() { return marriedEngaged; }
    public List<String> getAllies() { return allies; }
    public List<String> getAbducted() { return abducted; }
    public List<String> getKilled() { return killed; }
    public List<String> getKilledBy() { return killedBy; }
    public List<String> getServes() { return serves; }
    public List<String> getServedBy() { return servedBy; }

    // Inner class for Actor
    public static class Actor {
        @XmlElement(name = "actor_name")
        private String actorName;

        @XmlElement(name = "actor_link")
        private String actorLink;

        @XmlElementWrapper(name = "seasons_active")
        @XmlElement(name = "season")
        private List<Integer> seasonsActive;

        // Getters & Setters
        public String getActorName() { return actorName; }
        public String getActorLink() { return actorLink; }
        public List<Integer> getSeasonsActive() { return seasonsActive; }
    }
}