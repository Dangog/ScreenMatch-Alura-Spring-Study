package br.com.estudos.danilo.ScreenMatch.model;

public enum Category {

    ACTION("Action"),
    ROMANCE("Romance"),
    DRAMA("Drama"),
    COMEDY("Comedy"),
    CRIME("Crime"),
    ADVENTURE("Adventure");
    private String omdbCategory;

    Category(String omdbCategory){
        this.omdbCategory = omdbCategory;
    }

    public static Category fromPortugues(String text) {
        for (Category categoria : Category.values()) {
            if (categoria.omdbCategory.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }


}
