package at.ac.uibk.fiba.wang.spring4uli.rest.web.message;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.PictureProjection;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PictureFW {

    public final Long id;

    public final String path;

    @JsonCreator
    public PictureFW(@JsonProperty("id") Long id,
                     @JsonProperty("path") String path) {
        this.id = id;
        this.path = path;
    }

    public static PictureFW createPictureFW(PictureProjection s) {
        if (s==null) return null;
        return new PictureFW(s.getId(), s.getPath());
    }

    public static List<PictureFW> createPictureFWs(Collection<PictureProjection> paths) {
        if (paths==null) return Collections.emptyList();
        return paths
                .stream()
                .map(x -> createPictureFW(x))
                .collect(Collectors.toList());
    }
}
