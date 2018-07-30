package ir.codetower.smsbegir;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GroupsActivity extends AppCompatActivity {
    private GroupsAdapter adapter;
    private GroupsAdapter.DialogOpenListener dialogOpenListener;
    private RecyclerView group_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        group_list = findViewById(R.id.group_list);
        final ArrayList<Group> groups = App.dbHelper.getGroups();
        dialogOpenListener=new GroupsAdapter.DialogOpenListener() {
            @Override
            public void dialogOpen(int pos, final Group group) {
                final View editDialogView = LayoutInflater.from(GroupsActivity.this).inflate(R.layout.group_editor_dialog, null);
                final AlertDialog editDialog = new AlertDialog.Builder(GroupsActivity.this).create();
                editDialog.setView(editDialogView);
                editDialog.setCancelable(true);
                final AppCompatEditText name=editDialogView.findViewById(R.id.name);
                name.setText(group.getTitle());
                AppCompatButton save=editDialogView.findViewById(R.id.save);
                editDialog.show();
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        group.setTitle(name.getText()+"");
                        App.dbHelper.update(group);
                        Toast.makeText(App.context,"عملیات با موفقیت انجام شد",Toast.LENGTH_LONG).show();
                        editDialog.dismiss();
                       resetList();

                    }
                });
            }
            @Override
            public void deleteDialogOpen(final int pos, final Group group){
                final View dialogView = LayoutInflater.from(GroupsActivity.this).inflate(R.layout.confirm_dialog, null);
                final AlertDialog dialog = new AlertDialog.Builder(GroupsActivity.this).create();
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
                        int g_id = group.getId();
                        App.dbHelper.delete(App.dbHelper.contact_table, "g_id", g_id + "");
                        App.dbHelper.delete(App.dbHelper.group_table, "id", g_id + "");
                        resetList();
                        dialog.dismiss();
                    }
                });
                dialog.setView(dialogView);
                dialog.show();
            }
        };
        if (groups != null) {
            adapter = new GroupsAdapter(groups,dialogOpenListener);
            group_list.setLayoutManager(new LinearLayoutManager(this));
            group_list.setAdapter(adapter);

        }
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.group_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(GroupsActivity.this).create();
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        final AppCompatEditText groupTitle = dialogView.findViewById(R.id.title);
        final CheckBox defaultCheck = dialogView.findViewById(R.id.default_check);
        AppCompatButton save_group = dialogView.findViewById(R.id.add_group);
        save_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Group group = new Group();
                group.setTitle(groupTitle.getText() + "");
                if (defaultCheck.isChecked()) {
                    group.setDefaultGroup(true);
                }
                App.dbHelper.insert(group);
                resetList();
                dialog.dismiss();
                App.dbHelper.getDefaultGroup();

            }
        });

        AppCompatButton btn_add_group = findViewById(R.id.open_add_group);
        btn_add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }
    private void resetList(){
        adapter = new GroupsAdapter(App.dbHelper.getGroups(),dialogOpenListener);
        group_list.swapAdapter(adapter, false);
        adapter.notifyDataSetChanged();
    }
}
