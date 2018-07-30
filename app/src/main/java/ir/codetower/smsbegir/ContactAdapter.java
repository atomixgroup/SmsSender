package ir.codetower.smsbegir;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Mr-R00t on 1/19/2018.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
        public ArrayList<Contact> contacts;
    private DialogOpenListener dialogOpenListener;
    public ContactAdapter(ArrayList<Contact> contacts,DialogOpenListener dialogOpenListener) {
        this.contacts = contacts;
        this.dialogOpenListener=dialogOpenListener;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(App.context).inflate(R.layout.contact_adapter, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        holder.bind(contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AppCompatTextView item_name, item_family;
        private CheckBox item_check;
        private LinearLayout item_root;
        private Button btn_edit,btn_remove;
        public ContactViewHolder(View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            item_family = itemView.findViewById(R.id.item_family);
            item_check = itemView.findViewById(R.id.item_check);
            item_root = itemView.findViewById(R.id.item_root);
            item_root.setOnClickListener(this);
            item_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int pos = getLayoutPosition();
                    Contact contact = contacts.get(pos);
                    contact.setCheck(b);
                    contacts.set(pos, contact);
                }
            });

            btn_edit=itemView.findViewById(R.id.btn_edit);
            btn_remove=itemView.findViewById(R.id.btn_remove);


            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogOpenListener.editDialogOpen(getLayoutPosition(),contacts.get(getLayoutPosition()));
                }
            });
            btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogOpenListener.deleteDialogOpen(getLayoutPosition(),contacts.get(getLayoutPosition()));

                }
            });


        }

        public void bind(Contact contact) {
            item_name.setText(contact.getPhoneNumber());
            item_family.setText(contact.getName()+" "+contact.getFamily());
            item_check.setChecked(contact.isCheck());
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            int pos = getLayoutPosition();
            Contact contact = null;
            switch (id) {
                case R.id.item_root:
                    item_check.setChecked(!item_check.isChecked());
                    contact = contacts.get(pos);
                    contact.setCheck(item_check.isChecked());
                    contacts.set(pos, contact);
                    break;

            }
        }
    }
    public interface DialogOpenListener {
        public void editDialogOpen(int pos, Contact contact);
        public void deleteDialogOpen(int pos, Contact contact);
    }
}