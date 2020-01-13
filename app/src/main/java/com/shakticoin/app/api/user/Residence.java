package com.shakticoin.app.api.user;

public class Residence {
    private String address_line_1;
    private String address_line_2;
    private String city;
    private Integer subdivision_id;
    private String subdivision_name;
    private String zip_code;
    private String country_code;
    private String country_name;

    public String getAddress_line_1() {
        return address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public String getAddress_line_2() {
        return address_line_2;
    }

    public void setAddress_line_2(String address_line_2) {
        this.address_line_2 = address_line_2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getSubdivision_id() {
        return subdivision_id;
    }

    public void setSubdivision_id(Integer subdivision_id) {
        this.subdivision_id = subdivision_id;
    }

    public String getSubdivision_name() {
        return subdivision_name;
    }

    public void setSubdivision_name(String subdivision_name) {
        this.subdivision_name = subdivision_name;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }
}
