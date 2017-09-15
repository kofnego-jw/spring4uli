package at.ac.uibk.fiba.wang.spring4uli.rest.web.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PictureFW {

    public final String path;

    @JsonCreator
    public PictureFW(@JsonProperty("path") String path) {
        this.path = path;
    }

    public static PictureFW createPictureFW(String s) {
        return new PictureFW(s);
    }

    public static List<PictureFW> createPictureFWs(Collection<String> paths) {
        if (paths==null) return Collections.emptyList();
        return paths
                .stream()
                .map(x -> createPictureFW(x))
                .collect(Collectors.toList());
    }
}
