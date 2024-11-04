// Babble (c) 2014/24 Baltasar MIT License <baltasarq@gmail.com>


package core;


public class I18n {
    /// Available language codes as in ISO 639
    public enum Language {
        ES,
        EN,
        IT,
        DE,
        FR,
        NL,
        SL
    };

    public static final String[] LanguageES = {
            "Español",
            "Inglés",
            "Italiano",
            "Alemán",
            "Francés",
            "Holandés",
            "Esloveno"
    };

    public static final String[] LanguageEN = {
            "Spanish",
            "English",
            "Italian",
            "German",
            "French",
            "Dutch",
            "Slovenian"
    };

    public static Language getLanguageByIso639Name(String str)
    {
        Language toret = Language.EN;
        str = str.trim().toUpperCase();

        for(Language v : Language.values() ) {
            if ( v.toString().compareToIgnoreCase( str ) == 0 ) {
                toret = v;
                break;
            }
        }

        return toret;
    }

    public static String[] getStrLanguages(Language language)
    {
        String[] toret = LanguageES;

        if ( language == Language.EN ) {
            toret = LanguageEN;
        }

        return toret;
    }
}
