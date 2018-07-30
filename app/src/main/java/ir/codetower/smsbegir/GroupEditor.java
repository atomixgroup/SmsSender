package ir.codetower.smsbegir;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GroupEditor extends AppCompatActivity {
    private Group group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_editor);
        Intent intent=getIntent();

        if(intent.hasExtra("id")){
            int id=intent.getExtras().getInt("id");
            group=App.dbHelper.getGroup(id);
            if(group!=null){

            }
            else{
                finish();
            }
        }
        else{
            finish();
        }
    }
}
