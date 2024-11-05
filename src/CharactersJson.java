import com.google.gson.JsonElement;
import java.util.List;
public class CharactersJson{


    private String characterName;
    private String characterLink;
    private String characterImageThumb;
    private String characterImageFull;
    private JsonElement actorName;  // It can be a String or an Actor object array
    private String actorLink;
    private JsonElement houseName;  // It can be a String or a list of Strings
    private String nickname;
    private Boolean royal;
    private Boolean kingsguard;
    private List<String> parents;
    private List<String> parentOf;
    private List<String> guardianOf;
    private List<String> guardedBy;
    private List<String> siblings;
    private List<String> marriedEngaged;
    private List<String> allies;
    private List<String> abducted;
    private List<String> killed;
    private List<String> killedBy;
    private List<String> serves;
    private List<String> servedBy;

    // Getters y Setters
    public String getCharacterName() { return characterName; }
    public String getCharacterLink() { return characterLink; }
    public String getCharacterImageThumb() { return characterImageThumb; }
    public String getCharacterImageFull() { return characterImageFull; }
    public String getActorLink() { return actorLink; }
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

    // Get houseName as String whether is a list of Strings or a single String
    public String getHouseName() {
        if (houseName == null) return null;
        if (houseName.isJsonPrimitive()) {
            return houseName.getAsString();
        } else if (houseName.isJsonArray()) {
            StringBuilder houses = new StringBuilder();
            houseName.getAsJsonArray().forEach(element -> houses.append(element.getAsString()).append(", "));
            return houses.toString().replaceAll(", $", "");
        }
        return null;
    }

    // Get actorName as String whether is an Array or a single String
    public String getActorNameAsString() {
        if (actorName == null) return null;
        if (actorName.isJsonPrimitive()) {
            return actorName.getAsString();
        }
        return null;  // Returns null in case its is an array.
    }
}


