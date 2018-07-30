package ir.codetower.smsbegir;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PhoneFragment extends Fragment implements View.OnClickListener {
    private static PhoneFragment instance;
    private AppCompatButton btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn_clear;
    private AppCompatImageView backspace;
    private AppCompatTextView result;
    public static PhoneFragment getInstance() {
        instance = instance == null ? new PhoneFragment() : instance;
        return instance;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_phone, container, false);
        btn0 = view.findViewById(R.id.btn0);
        btn0.setOnClickListener(this);

        btn1 = view.findViewById(R.id.btn1);
        btn1.setOnClickListener(this);

        btn2 = view.findViewById(R.id.btn2);
        btn2.setOnClickListener(this);

        btn3 = view.findViewById(R.id.btn3);
        btn3.setOnClickListener(this);

        btn4 = view.findViewById(R.id.btn4);
        btn4.setOnClickListener(this);

        btn5 = view.findViewById(R.id.btn5);
        btn5.setOnClickListener(this);

        btn6 = view.findViewById(R.id.btn6);
        btn6.setOnClickListener(this);

        btn7 = view.findViewById(R.id.btn7);
        btn7.setOnClickListener(this);

        btn8 = view.findViewById(R.id.btn8);
        btn8.setOnClickListener(this);

        btn9 = view.findViewById(R.id.btn9);
        btn9.setOnClickListener(this);

        btn_clear = view.findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);

        backspace = view.findViewById(R.id.btn_backspace);
        backspace.setOnClickListener(this);

        result = view.findViewById(R.id.result);
        result.setText(App.contact.getPhoneNumber());
        return view;
    }

    public String getResultString() {
        return result.getText() + "";
    }
    public void setResultString(String txt){
        result.setText(txt);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn0:
                result.setText(result.getText() + "0");
                App.contact.setPhoneNumber(result.getText()+"");
                break;
            case R.id.btn1:
                result.setText(result.getText() + "1");
                App.contact.setPhoneNumber(result.getText()+"");

                break;
            case R.id.btn2:
                result.setText(result.getText() + "2");
                App.contact.setPhoneNumber(result.getText()+"");

                break;
            case R.id.btn3:
                result.setText(result.getText() + "3");
                App.contact.setPhoneNumber(result.getText()+"");

                break;
            case R.id.btn4:
                result.setText(result.getText() + "4");
                App.contact.setPhoneNumber(result.getText()+"");

                break;
            case R.id.btn5:
                result.setText(result.getText() + "5");
                App.contact.setPhoneNumber(result.getText()+"");

                break;
            case R.id.btn6:
                result.setText(result.getText() + "6");
                App.contact.setPhoneNumber(result.getText()+"");

                break;
            case R.id.btn7:
                result.setText(result.getText() + "7");
                App.contact.setPhoneNumber(result.getText()+"");

                break;
            case R.id.btn8:
                result.setText(result.getText() + "8");
                App.contact.setPhoneNumber(result.getText()+"");

                break;
            case R.id.btn9:
                result.setText(result.getText() + "9");
                App.contact.setPhoneNumber(result.getText()+"");
                break;
            case R.id.btn_clear:
                result.setText("");
                break;
            case R.id.btn_backspace:
                String txt=result.getText()+"";
                if(!txt.equals("") && txt.length()>0){
                    txt=txt.substring(0,txt.length()-1);
                }
                result.setText( txt);
                App.contact.setPhoneNumber(txt);
                break;
        }
    }
}
