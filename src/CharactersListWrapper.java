import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "character")
public class CharactersListWrapper {

    private List<CharactersJson> characters;

    @XmlElementWrapper(name = "characters")
    @XmlElement(name = "character")
    public List<CharactersJson> getCharacters() {
        return characters;
    }

    public void setCharacters(List<CharactersJson> characters) {
        this.characters = characters;
    }
}