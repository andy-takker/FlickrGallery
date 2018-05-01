package incorp.death.hikki.photogallerytest.database;

public class GalleryDbSchema {
    public static final class Gallery{
        public static final String NAME = "galleryItems";

        public static final class  Cols{
            public static final String URL_L = "url_l";
            public static final String TITLE = "title";
            public static final String POS = "pos";
            public static final String OWNER = "owner";
            public static final String ID = "id";
        }
    }
}
