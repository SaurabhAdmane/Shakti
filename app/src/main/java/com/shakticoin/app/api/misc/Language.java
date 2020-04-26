package com.shakticoin.app.api.misc;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * The class wrap code and display name of a language and intended for using
 * with Spinner.
 */
public class Language {
    private String code;
    private String displayName;

    public Language(String code) {
        this.code = code;
        Locale locale = new Locale(code);
        this.displayName = locale.getDisplayName();
    }

    public String getCode() {
        return code;
    }

    @NotNull
    @Override
    public String toString() {
        return displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Language language = (Language) o;

        if (getCode() != null ? !getCode().equals(language.getCode()) : language.getCode() != null)
            return false;
        return displayName != null ? displayName.equals(language.displayName) : language.displayName == null;
    }

    @Override
    public int hashCode() {
        int result = getCode() != null ? getCode().hashCode() : 0;
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        return result;
    }
}
