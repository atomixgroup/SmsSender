package ir.codetower.smsbegir;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.codetower.smsbegir.Calender.PersianDatePicker;
import ir.codetower.smsbegir.Calender.util.PersianCalendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    private AppCompatButton btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn_clear;
    private AppCompatImageView backspace;
    private AppCompatTextView result;
    private int flag=1;
    private boolean first=true;
    private EditText name,family,address,description;
    private PersianDatePicker persianCalendar;
    private AppCompatRadioButton female,male;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       setupViews();
        setupNavigations();

        if(checkPermissions()){
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            App.IMEI = telephonyManager.getDeviceId()+"";
        }
        AppCompatButton btn_send=findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersianCalendar pCal = persianCalendar.getDisplayPersianDate();

                HashMap<String,String> params=new HashMap<>();
                params.put("name",name.getText()+"");
                params.put("family",family.getText()+"");
                params.put("address",address.getText()+"");
                params.put("description",description.getText()+"");
                if(male.isChecked()){
                    params.put("gender","male");
                }
                else{
                    params.put("gender","female");
                }
                params.put("year",pCal.getPersianYear()+"");
                params.put("month",pCal.getPersianMonth()+"");
                params.put("day",pCal.getPersianDay()+"");
                params.put("phone",result.getText()+"");
                params.put("IMEI",App.IMEI);
                params.put("sms_default",App.prefManager.getPreference("sms_default"));

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
                App.contact.setYear(pCal.getPersianYear()+"");
                App.contact.setMounth(pCal.getPersianMonth()+"");
                App.contact.setDay(pCal.getPersianDay()+"");
                App.contact.setPhoneNumber(result.getText()+"");
                if(App.defaultGroup==null){
                    Toast.makeText(App.context,"گروه پیشفرض برای ذخیره مخاطب انتخاب نشده است",Toast.LENGTH_LONG).show();
                    Toast.makeText(App.context,"از منوی گروه بندی میتوانید این کار را انجام بدید",Toast.LENGTH_SHORT).show();
                }
                else{
                    App.contact.setG_id(App.defaultGroup.getId());
                    App.dbHelper.insert(App.contact);
                    Toast.makeText(App.context,"با موفقیت ثبت شد",Toast.LENGTH_LONG).show();
                    App.contact=new Contact();
                    result.setText("0");
                    App.webService.postRequest(params, App.api+"saveContact", new WebService.OnPostReceived() {
                        @Override
                        public void onReceived(String message) {
                            if(message.equals("ERROR:2")){
                                Toast.makeText(App.context,"موجودی شما کافی نمیباشد",Toast.LENGTH_LONG).show();
                            }
                            else if(message.equals("ERROR:3")){
                                Toast.makeText(App.context,"سرویس دهنده پیامک غیر فعال می باشد",Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onReceivedError(String message) {

                        }
                    });
                    resetForm();

                }

            }
        });
        HashMap<String,String> params=new HashMap<>();
        params.put("IMEI",App.IMEI+"");
        App.webService.postRequest(params, App.api + "getInventory", new WebService.OnPostReceived() {
            @Override
            public void onReceived(String message) {
                if(message.equals("ERROR:1")){
                    Toast.makeText(App.context,"مشکل در شبکه بوجود آمده است",Toast.LENGTH_LONG).show();

                }
                else {
                    try {
                        App.inventory = Float.parseFloat(message);
                    } catch (Exception e) {
                        App.inventory = 0.0f;
                    }
                }
            }

            @Override
            public void onReceivedError(String message) {

            }
        });
    }
    private void resetForm(){
        App.contact=new Contact();
        name.setText("");
        family.setText("");
        result.setText("");
        address.setText("");
        description.setText("");
        male.setChecked(true);
        PersianCalendar calendar=new PersianCalendar();
        calendar.setPersianDate(Integer.parseInt(App.contact.getYear()),Integer.parseInt(App.contact.getMounth()),Integer.parseInt(App.contact.getDay()));
        persianCalendar.setDisplayPersianDate(calendar);
    }
    private void setupViews() {

        btn0 = findViewById(R.id.btn0);
        btn0.setOnClickListener(this);

        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(this);

        btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(this);

        btn3 = findViewById(R.id.btn3);
        btn3.setOnClickListener(this);

        btn4 = findViewById(R.id.btn4);
        btn4.setOnClickListener(this);

        btn5 = findViewById(R.id.btn5);
        btn5.setOnClickListener(this);

        btn6 = findViewById(R.id.btn6);
        btn6.setOnClickListener(this);

        btn7 = findViewById(R.id.btn7);
        btn7.setOnClickListener(this);

        btn8 = findViewById(R.id.btn8);
        btn8.setOnClickListener(this);

        btn9 = findViewById(R.id.btn9);
        btn9.setOnClickListener(this);

        btn_clear = findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);

        backspace = findViewById(R.id.btn_backspace);
        backspace.setOnClickListener(this);

        result = findViewById(R.id.result);
        result.setText(App.contact.getPhoneNumber());

        name=findViewById(R.id.name);
        family=findViewById(R.id.family);
        address=findViewById(R.id.address);
        description=findViewById(R.id.description);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        persianCalendar=findViewById(R.id.birth);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void setupNavigations() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        drawer.closeDrawer(GravityCompat.START);

        switch (id){
            case R.id.contacts:
                Intent intent=new Intent(MainActivity.this,ContactsActivity.class);
                startActivity(intent);
                break;
            case R.id.charj:
                Intent intent2=new Intent(MainActivity.this,InventoryActivity.class);
                startActivity(intent2);
                break;
            case R.id.group:
                Intent intent3=new Intent(MainActivity.this,GroupsActivity.class);
                startActivity(intent3);
                break;
            case R.id.sms_text:
                final View edit_sms_text_dialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.sms_text_dialog, null);
                final AlertDialog edit_sms_dialog = new AlertDialog.Builder(MainActivity.this).create();
                edit_sms_dialog.setView(edit_sms_text_dialog);
                edit_sms_dialog.setCancelable(true);

                final AppCompatEditText sms_text=edit_sms_text_dialog.findViewById(R.id.sms_text);
                Button btn_save=edit_sms_text_dialog.findViewById(R.id.sms_default_save);
                sms_text.setText(App.prefManager.getPreference("sms_default"));
                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        App.prefManager.savePreference("sms_default",sms_text.getText()+"");
                        edit_sms_dialog.dismiss();
                    }
                });
                edit_sms_dialog.show();

                break;
        }
        return true;
    }

    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull final String[] permissions, @NonNull int[] grantResults) {
        boolean flag = false;
        for (int grant : grantResults) {
            if (grant != 0) {
                flag = true;
            }
        }
        if (flag) {
            checkPermissions();
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            App.IMEI = telephonyManager.getDeviceId()+"";
        }

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn0:
                result.setText(result.getText() + "0");
                App.contact.setPhoneNumber(result.getText() + "");
                break;
            case R.id.btn1:
                result.setText(result.getText() + "1");
                App.contact.setPhoneNumber(result.getText() + "");

                break;
            case R.id.btn2:
                result.setText(result.getText() + "2");
                App.contact.setPhoneNumber(result.getText() + "");

                break;
            case R.id.btn3:
                result.setText(result.getText() + "3");
                App.contact.setPhoneNumber(result.getText() + "");

                break;
            case R.id.btn4:
                result.setText(result.getText() + "4");
                App.contact.setPhoneNumber(result.getText() + "");

                break;
            case R.id.btn5:
                result.setText(result.getText() + "5");
                App.contact.setPhoneNumber(result.getText() + "");

                break;
            case R.id.btn6:
                result.setText(result.getText() + "6");
                App.contact.setPhoneNumber(result.getText() + "");

                break;
            case R.id.btn7:
                result.setText(result.getText() + "7");
                App.contact.setPhoneNumber(result.getText() + "");

                break;
            case R.id.btn8:
                result.setText(result.getText() + "8");
                App.contact.setPhoneNumber(result.getText() + "");

                break;
            case R.id.btn9:
                result.setText(result.getText() + "9");
                App.contact.setPhoneNumber(result.getText() + "");
                break;
            case R.id.btn_clear:
                result.setText("");
                break;
            case R.id.btn_backspace:
                String txt = result.getText() + "";
                if (!txt.equals("") && txt.length() > 0) {
                    txt = txt.substring(0, txt.length() - 1);
                }
                result.setText(txt);
                App.contact.setPhoneNumber(txt);
                break;
        }
    }
}
