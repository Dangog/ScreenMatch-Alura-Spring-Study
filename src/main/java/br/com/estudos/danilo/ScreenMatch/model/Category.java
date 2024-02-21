package br.com.estudos.danilo.ScreenMatch.model;

public enum Category {

    ACTION("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    DRAMA("Drama", "Drama"),
    COMEDY("Comedy","Comédia"),
    CRIME("Crime","Crime"),
    ADVENTURE("Adventure","Aventura");

    private String omdbCategory;
    private String omdbPortugueseCategory;

    Category(String omdbCategory, String omdbPortugueseCategory){
        this.omdbCategory = omdbCategory;
        this.omdbPortugueseCategory = omdbPortugueseCategory;
    }

    public static Category fromPortugues(String text) {
        for (Category categoria : Category.values()) {
            if (categoria.omdbCategory.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

    public static Category fromString(String text) {
        for (Category categoria : Category.values()) {
            if (categoria.omdbPortugueseCategory.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }


}
