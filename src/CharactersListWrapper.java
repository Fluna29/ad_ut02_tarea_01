import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlRootElement(name = "character")
public class CharactersListWrapper {

    private List<CharactersJson> characters;

    @XmlElementWrapper(name = "characters")
    @XmlElement(name = "character_data")
    public List<CharactersJson> getCharacters() {
        return characters;
    }

    public void setCharacters(List<CharactersJson> characters) {
        this.characters = characters;
    }
}