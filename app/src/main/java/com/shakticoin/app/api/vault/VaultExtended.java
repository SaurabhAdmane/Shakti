package com.shakticoin.app.api.vault;

import java.util.List;

public class VaultExtended {
    public static final String PACKAGE_VIEW         = "PACKAGE_VIEW";
    public static final String COMPANY_INFO_VIEW    = "COMPANY_INFO_VIEW";
    public static final String CONTACT_US_VIEW      = "CONTACT_US_VIEW";

    private Integer id;
    private String name;
    private String description;
    private List<String> features;
    private String transition_to_view;
    private String transition_button_label;
    private Integer order_no;
    private Bonus bonus;

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

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    public String getTransition_to_view() {
        return transition_to_view;
    }

    public void setTransition_to_view(String transition_to_view) {
        this.transition_to_view = transition_to_view;
    }

    public String getTransition_button_label() {
        return transition_button_label;
    }

    public void setTransition_button_label(String transition_button_label) {
        this.transition_button_label = transition_button_label;
    }

    public Integer getOrder_no() {
        return order_no;
    }

    public void setOrder_no(Integer order_no) {
        this.order_no = order_no;
    }
}
