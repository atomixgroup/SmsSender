package ir.codetower.smsbegir;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mr-R00t on 1/19/2018.
 */

public class Contact {
    private int id;
    private String phoneNumber="0";
    private String name="";
    private String family="";
    private String address="";
    private String year="1360";
    private String mounth="1";
    private String day="1";
    private String description="";
    private String gender="male";
    private boolean check=true;
    private int g_id;

    public int getG_id() {
        return g_id;
    }

    public void setG_id(int g_id) {
        this.g_id = g_id;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMounth() {
        return mounth;
    }

    public void setMounth(String mounth) {
        this.mounth = mounth;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ArrayList<Contact> jsonToContact(String json) {
        ArrayList<Contact> contacts = new ArrayList<>();
        JSONArray array = null;
        try {
            array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonItem = array.getJSONObject(i);
                Contact contact = new Contact();

                contact.setId(jsonItem.getInt("id"));
                contact.setDescription(jsonItem.getString("description"));
                contact.setAddress(jsonItem.getString("address"));
                contact.setDay(jsonItem.getString("day"));
                contact.setMounth(jsonItem.getString("month"));
                contact.setYear(jsonItem.getString("year"));
                contact.setFamily(jsonItem.getString("family"));
                contact.setName(jsonItem.getString("name"));
                contact.setGender(jsonItem.getString("gender"));
                contact.setPhoneNumber(jsonItem.getString("phone"));
                contacts.add(contact);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return contacts;
    }


}
