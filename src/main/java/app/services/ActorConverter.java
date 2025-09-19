package app.services;

import app.dtos.ActorDTO;
import app.entities.Actor;

public class ActorConverter {

    public static Actor dtoToEntity (ActorDTO actorDTO) {
        Actor actor = Actor.builder()
                .name(actorDTO.getName())
                .adult(actorDTO.isAdult())
                .gender(actorDTO.getGender())
                .tmdbId(actorDTO.getTmdbId())
                .knownForDepartment(actorDTO.getKnownForDepartment())
                .originalName(actorDTO.getOriginalName())
                .popularity(actorDTO.getPopularity())
                .build();
        return actor;
    }
}
