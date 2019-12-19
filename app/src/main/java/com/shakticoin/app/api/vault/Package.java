package com.shakticoin.app.api.vault;

public class Package {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Vault getVault() {
        return vault;
    }

    public void setVault(Vault vault) {
        this.vault = vault;
    }

    public Integer getSpecificity() {
        return specificity;
    }

    public void setSpecificity(Integer specificity) {
        this.specificity = specificity;
    }

    public enum Specificity {General, Country, Subdivision}

    private Integer id;
    private String name;
    private String description;
    private Vault vault;
    private Integer specificity;
}
