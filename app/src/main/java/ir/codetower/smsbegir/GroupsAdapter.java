package ir.codetower.smsbegir;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
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
 * Created by Mr-R00t on 1/20/2018.
 */

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupsViewHolder> {
    public static ArrayList<Group> groups;
    private DialogOpenListener dialogOpenListener;

    public GroupsAdapter(ArrayList<Group> groups, DialogOpenListener dialogOpenListener) {
        this.groups = groups;
        this.dialogOpenListener = dialogOpenListener;
    }

    @Override
    public GroupsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(App.context).inflate(R.layout.group_adapter, parent, false);
        return new GroupsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupsViewHolder holder, int position) {
        holder.bind(groups.get(position));
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class GroupsViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView contact_count, item_title;
        private AppCompatButton btn_default;
        private Button btn_edit;
        private AppCompatImageView btn_delete;

        public GroupsViewHolder(View itemView) {
            super(itemView);
            contact_count = itemView.findViewById(R.id.contact_count);
            item_title = itemView.findViewById(R.id.item_title);
            btn_default = itemView.findViewById(R.id.btn_default);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogOpenListener.deleteDialogOpen(getLayoutPosition(),groups.get(getLayoutPosition()));
                }
            });
            btn_default.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < groups.size(); i++) {
                        if (groups.get(i).isDefaultGroup()) {
                            Group temp = groups.get(i);
                            temp.setDefaultGroup(false);
                            App.dbHelper.update(temp);
                            notifyItemChanged(i);
                            break;
                        }
                    }
                    int pos = getAdapterPosition();
                    Group group = groups.get(pos);
                    group.setDefaultGroup(true);
                    groups.set(pos, group);
                    App.dbHelper.update(group);
                    App.defaultGroup = group;
                    notifyItemChanged(pos);
                }
            });
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogOpenListener.dialogOpen(getLayoutPosition(), groups.get(getLayoutPosition()));
                }
            });

        }

        public void bind(Group group) {
            contact_count.setText("تعداد مخاطب : " + group.getContactCount() + "");
            item_title.setText(group.getTitle());
            if (group.isDefaultGroup()) {
                btn_default.setEnabled(false);
            } else {
                btn_default.setEnabled(true);
            }
        }


    }

    public interface DialogOpenListener {
        public void dialogOpen(int pos, Group group);
        public void deleteDialogOpen(int pos, Group group);
    }
}