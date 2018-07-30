package ir.codetower.smsbegir;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import ir.codetower.smsbegir.Calender.PersianDatePicker;
import ir.codetower.smsbegir.Calender.util.PersianCalendar;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {
    private int contactsCounts=0;
    private ContactAdapter adapter;
    private RecyclerView contact_list;
    private ContactAdapter.DialogOpenListener dialogOpenListener;
    private AppCompatTextView phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        final ArrayList<Contact> contacts=App.dbHelper.getContacts(App.defaultGroup.getId());

        final View editDialogView = LayoutInflater.from(ContactsActivity.this).inflate(R.layout.contact_editor_dialog, null);
        final AlertDialog editDialog = new AlertDialog.Builder(ContactsActivity.this).create();
        editDialog.setView(editDialogView);
        editDialog.setCancelable(true);
        final EditText name,family,address,description;
        final PersianDatePicker persianCalendar;
        final AppCompatRadioButton female,male;
        name=editDialogView.findViewById(R.id.name);
        family=editDialogView.findViewById(R.id.family);
        address=editDialogView.findViewById(R.id.address);
        description=editDialogView.findViewById(R.id.description);
        male=editDialogView.findViewById(R.id.male);
        female=editDialogView.findViewById(R.id.female);
        persianCalendar=editDialogView.findViewById(R.id.birth);
        phone=editDialogView.findViewById(R.id.result);
        final Button save=editDialogView.findViewById(R.id.btn_save);
        AppCompatButton btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn_clear;
        AppCompatImageView backspace;
        btn0 = editDialogView.findViewById(R.id.btn0);
        btn0.setOnClickListener(this);

        btn1 = editDialogView.findViewById(R.id.btn1);
        btn1.setOnClickListener(this);

        btn2 = editDialogView.findViewById(R.id.btn2);
        btn2.setOnClickListener(this);

        btn3 = editDialogView.findViewById(R.id.btn3);
        btn3.setOnClickListener(this);

        btn4 = editDialogView.findViewById(R.id.btn4);
        btn4.setOnClickListener(this);

        btn5 = editDialogView.findViewById(R.id.btn5);
        btn5.setOnClickListener(this);

        btn6 = editDialogView.findViewById(R.id.btn6);
        btn6.setOnClickListener(this);

        btn7 = editDialogView.findViewById(R.id.btn7);
        btn7.setOnClickListener(this);

        btn8 = editDialogView.findViewById(R.id.btn8);
        btn8.setOnClickListener(this);

        btn9 = editDialogView.findViewById(R.id.btn9);
        btn9.setOnClickListener(this);

        btn_clear = editDialogView.findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);

        backspace = editDialogView.findViewById(R.id.btn_backspace);
        backspace.setOnClickListener(this);


        dialogOpenListener=new ContactAdapter.DialogOpenListener() {
            @Override
            public void editDialogOpen(int pos, final Contact contact) {
                phone.setText(contact.getPhoneNumber());
                name.setText(contact.getName());
                family.setText(contact.getFamily());
                PersianCalendar calendar=new PersianCalendar();
                calendar.setPersianDate(Integer.parseInt(contact.getYear()),Integer.parseInt(contact.getMounth()),Integer.parseInt(contact.getDay()));
                persianCalendar.setDisplayPersianDate(calendar);
                address.setText(contact.getAddress());
                if(contact.getGender().equals("male"))
                {
                    male.setChecked(true);
                }
                else{
                    female.setChecked(true);
                }
                description.setText(contact.getDescription());

                editDialog.show();
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        contact.setPhoneNumber(phone.getText()+"");
                        if(male.isChecked()){
                            contact.setGender("male");
                        }
                        else{
                            contact.setGender("female");
                        }
                        contact.setName(name.getText()+"");
                        contact.setDescription(description.getText()+"");
                        contact.setFamily(family.getText()+"");
                        contact.setAddress(address.getText()+"");

                        PersianCalendar pCal = persianCalendar.getDisplayPersianDate();
                        contact.setYear(pCal.getPersianYear()+"");
                        contact.setMounth(pCal.getPersianMonth()+"");
                        contact.setDay(pCal.getPersianDay()+"");
                        App.dbHelper.update(contact);
                        Toast.makeText(App.context,"عملیات با موفقیت انجام شد",Toast.LENGTH_LONG).show();
                        editDialog.dismiss();
                        resetList();

                    }
                });
            }
            @Override
            public void deleteDialogOpen(final int pos, final Contact contact){
                final View dialogView = LayoutInflater.from(ContactsActivity.this).inflate(R.layout.confirm_dialog, null);
                final AlertDialog dialog = new AlertDialog.Builder(ContactsActivity.this).create();
                final AppCompatButton btn_accept = dialogView.findViewById(R.id.btn_accept);
                final AppCompatButton btn_decline = dialogView.findViewById(R.id.btn_decline);

                btn_decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        App.dbHelper.delete(App.dbHelper.contact_table, "id", contact.getId() + "");
                        resetList();
                        dialog.dismiss();
                    }
                });
                dialog.setView(dialogView);
                dialog.show();
            }
        };

        if(contacts==null){
            Toast.makeText(App.context,"مخاطبی در دفترچه شما موجود نیست",Toast.LENGTH_LONG).show();
        }
        else{
            contact_list=findViewById(R.id.contact_list);
            adapter=new ContactAdapter(contacts,dialogOpenListener);

            contact_list.setLayoutManager(new LinearLayoutManager(this));
            contact_list.setAdapter(adapter);
            final AppCompatButton btn_send=findViewById(R.id.btn_send);
            final LayoutInflater factory = LayoutInflater.from(this);
            final View dialogView = factory.inflate(R.layout.sms_dialog, null);
            final AlertDialog dialog = new AlertDialog.Builder(ContactsActivity.this).create();
            dialog.setView(dialogView);
            dialog.setCancelable(true);
            final AppCompatTextView price,sms_count,contact_count,inventory;
            price=dialogView.findViewById(R.id.price);
            sms_count=dialogView.findViewById(R.id.sms_count);
            contact_count=dialogView.findViewById(R.id.count);
            inventory=dialogView.findViewById(R.id.inventory);
            inventory.setText("موجودی :"+App.inventory+" تومان");
            final AppCompatEditText sms_text=dialogView.findViewById(R.id.sms_text);
            final AppCompatButton btn_send_sms=dialogView.findViewById(R.id.btn_send_sms);
            final AppCompatButton btn_calculate=dialogView.findViewById(R.id.btn_calculate);
            btn_calculate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    float smsPrice=22.0f;
                    String SMSText = sms_text.getText().toString();
                    String SMSLimit = "160.0f";
                    if(!isASCII(SMSText)) {
                        smsPrice=12.1f;
                        SMSLimit = "70.0f";}
                    final int count=(int) Math.ceil(SMSText.length()/Float.parseFloat(SMSLimit));
                    sms_count.setText("تعداد پیامک نسبت به متن : "+count+"");
                    price.setText("مبلغ کل : "+contactsCounts*count*smsPrice+" تومان");

                }
            });
            btn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int c=0;
                    for (Contact contact:adapter.contacts){
                        if(contact.isCheck()){
                            c++;
                        }
                    }
                    contactsCounts=c;
                    contact_count.setText("تعداد مخاطبین :"+contactsCounts+" نفر");
                    dialog.show();
                }
            });
            btn_send_sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    float smsPrice=22.0f;
                    String SMSText = sms_text.getText().toString();
                    String SMSLimit = "160.0f";
                    if(!isASCII(SMSText)) {
                        smsPrice=12.1f;
                        SMSLimit = "70.0f";}
                    final int count=(int) Math.ceil(SMSText.length()/Float.parseFloat(SMSLimit));
                    sms_count.setText("تعداد پیامک نسبت به متن : "+count+"");

                    float pr=contactsCounts*count*smsPrice;
                    price.setText("مبلغ کل : "+contactsCounts*count*smsPrice+" تومان");
                    if(App.inventory<pr){
                        Toast.makeText(App.context,"موجودی کافی نیست",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(ContactsActivity.this,InventoryActivity.class);
                        startActivity(intent);
                    }
                    else{
                        HashMap<String,String> params=new HashMap<>();
                        params.put("IMEI",App.IMEI);
                        params.put("text",SMSText);
                        StringBuilder phones= new StringBuilder();
                        boolean first=true;
                        for (Contact item:contacts){
                            if(item.isCheck()){
                                if(first) {
                                    first=false;
                                    phones.append(item.getPhoneNumber());
                                }
                                else{
                                    phones.append(",").append(item.getPhoneNumber());
                                }
                            }
                        }
                        params.put("phones",phones.toString());
                        App.webService.postRequest(params, App.api + "sendSms", new WebService.OnPostReceived() {
                            @Override
                            public void onReceived(String message) {
                                if(message.equals("E2")){
                                    Toast.makeText(App.context,"موجودی شما کافی نمیباشد",Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(ContactsActivity.this,InventoryActivity.class);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onReceivedError(String message) {

                            }
                        });
                    }
                }
            });

        }


    }
    private void resetList(){
        adapter=new ContactAdapter(App.dbHelper.getContacts(App.defaultGroup.getId()),dialogOpenListener);
        contact_list.swapAdapter(adapter,false);
        adapter.notifyDataSetChanged();
    }
    boolean isASCII(String str){
        for(int i=0;i<str.length();i++){
            if(str.charAt(i) >= 128)
                return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn0:
                phone.setText(phone.getText() + "0");
                App.contact.setPhoneNumber(phone.getText()+"");
                break;
            case R.id.btn1:
                phone.setText(phone.getText() + "1");
                App.contact.setPhoneNumber(phone.getText()+"");

                break;
            case R.id.btn2:
                phone.setText(phone.getText() + "2");
                App.contact.setPhoneNumber(phone.getText()+"");

                break;
            case R.id.btn3:
                phone.setText(phone.getText() + "3");
                App.contact.setPhoneNumber(phone.getText()+"");

                break;
            case R.id.btn4:
                phone.setText(phone.getText() + "4");
                App.contact.setPhoneNumber(phone.getText()+"");

                break;
            case R.id.btn5:
                phone.setText(phone.getText() + "5");
                App.contact.setPhoneNumber(phone.getText()+"");

                break;
            case R.id.btn6:
                phone.setText(phone.getText() + "6");
                App.contact.setPhoneNumber(phone.getText()+"");

                break;
            case R.id.btn7:
                phone.setText(phone.getText() + "7");
                App.contact.setPhoneNumber(phone.getText()+"");

                break;
            case R.id.btn8:
                phone.setText(phone.getText() + "8");
                App.contact.setPhoneNumber(phone.getText()+"");

                break;
            case R.id.btn9:
                phone.setText(phone.getText() + "9");
                App.contact.setPhoneNumber(phone.getText()+"");
                break;
            case R.id.btn_clear:
                phone.setText("");
                break;
            case R.id.btn_backspace:
                String txt=phone.getText()+"";
                if(!txt.equals("") && txt.length()>0){
                    txt=txt.substring(0,txt.length()-1);
                }
                phone.setText( txt);
                App.contact.setPhoneNumber(txt);
                break;
        }
    }
}
