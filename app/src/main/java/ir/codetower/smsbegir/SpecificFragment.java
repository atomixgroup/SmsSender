package ir.codetower.smsbegir;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Date;

import ir.codetower.smsbegir.Calender.PersianDatePicker;
import ir.codetower.smsbegir.Calender.util.PersianCalendar;


public class SpecificFragment extends Fragment {
    private EditText name,family,address,description;
    private PersianDatePicker persianCalendar;
    private AppCompatRadioButton female,male;
    private static SpecificFragment instance;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public static SpecificFragment getInstance() {
        instance = instance == null ? new SpecificFragment() : instance;
        return instance;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_specific, container, false);
        name=view.findViewById(R.id.name);
        family=view.findViewById(R.id.family);
        address=view.findViewById(R.id.address);
        description=view.findViewById(R.id.description);
        male=view.findViewById(R.id.male);
        female=view.findViewById(R.id.female);
        persianCalendar=view.findViewById(R.id.birth);
        setData();
        return view;
    }
    public void setData(){
        Contact contact=App.contact;
        name.setText(contact.getName());
        family.setText(contact.getFamily());
        address.setText(contact.getAddress());
        description.setText(contact.getDescription());
        if(contact.getGender().equals("male")){
            male.setChecked(true);
        }
        else{
            female.setChecked(true);
        }
        PersianCalendar calendar=new PersianCalendar();
        calendar.setPersianDate(Integer.parseInt(contact.getYear()),Integer.parseInt(contact.getMounth()),Integer.parseInt(contact.getDay()));
        persianCalendar.setDisplayPersianDate(calendar);

    }
    public void getData(){
        App.contact.setName(name.getText()+"");
        App.contact.setFamily(family.getText()+"");
        App.contact.setAddress(address.getText()+"");
        App.contact.setDescription(description.getText()+"");
        if(male.isChecked()){
            App.contact.setGender("male");
        }
        else{
            App.contact.setGender("female");

        }
        PersianCalendar pCal = persianCalendar.getDisplayPersianDate();
        App.contact.setYear(pCal.getPersianYear()+"");
        App.contact.setMounth(pCal.getPersianMonth()+"");
        App.contact.setDay(pCal.getPersianDay()+"");

    }

    @Override
    public void onDestroy() {
        getData();
        super.onDestroy();

    }
}
