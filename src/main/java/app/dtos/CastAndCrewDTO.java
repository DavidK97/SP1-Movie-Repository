package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CastAndCrewDTO {
    private int id;

    @JsonProperty("cast")
    private List<ActorDTO> actors;

    @JsonProperty("crew")
    private List<DirectorDTO> crew;
}
