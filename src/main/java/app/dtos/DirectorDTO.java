package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectorDTO {
    private boolean adult;
    private int gender;

    @JsonProperty("id")
    private int tmdbId;

    @JsonProperty("know_for_department")
    private String knownForDepartment;

    private String name;

    @JsonProperty("original_name")
    private String originalName;

    private double popularity;

    private String department;

    private String job;
}
