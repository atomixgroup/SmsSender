package ir.codetower.smsbegir;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import java.util.HashMap;

public class InventoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        final AppCompatTextView inventory=findViewById(R.id.inventory);

        HashMap<String,String> params=new HashMap<>();
        params.put("IMEI",App.IMEI+"");
        App.webService.postRequest(params, App.api + "getInventory", new WebService.OnPostReceived() {
            @Override
            public void onReceived(String message) {
                try {
                    float inventoryPrice=Float.parseFloat(message);
                    inventory.setText("موجودی : "+inventoryPrice+" تومان");
                }
                catch (Exception e){
                    inventory.setText("موجودی : "+App.inventory+" تومان");
                }
            }

            @Override
            public void onReceivedError(String message) {

            }
        });
        findViewById(R.id.charj).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatEditText price=findViewById(R.id.price);
                String p=price.getText()+"";
                if(!p.equals("")){
                    String url = App.api+"request?sh2="+App.IMEI+"&price="+p;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });
    }
}
