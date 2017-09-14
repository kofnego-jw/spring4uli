package at.ac.uibk.fiba.wang.spring4uli.jpa.ontology;

public enum PictureType {

    JPEG,
    PNG,
    TIFF,
    UNKNOWN;

    public static PictureType guessType(String filename) {
        if (filename==null) return UNKNOWN;
        String ending = !filename.contains(".") ? filename.toLowerCase() :
                filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
        switch(ending) {
            case "jpg":
            case "jpeg": return JPEG;
            case "png": return PNG;
            case "tiff":
            case "tif": return TIFF;
        }
        return UNKNOWN;
    }

}
