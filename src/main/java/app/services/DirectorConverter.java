package app.services;

import app.dtos.DirectorDTO;
import app.entities.Director;

public class DirectorConverter {

    public static Director dtoToEntity(DirectorDTO directorDTO) {
        return Director.builder()
                .name(directorDTO.getName())
                .adult(directorDTO.isAdult())
                .gender(directorDTO.getGender())
                .tmdbId(directorDTO.getTmdbId())
                .knownForDepartment(directorDTO.getKnownForDepartment())
                .originalName(directorDTO.getOriginalName())
                .popularity(directorDTO.getPopularity())
                .department(directorDTO.getDepartment())
                .job(directorDTO.getJob())
                .build();
    }
}
