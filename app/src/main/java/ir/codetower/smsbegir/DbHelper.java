package ir.codetower.smsbegir;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Mr R00t on 4/22/2017.
 */
//id 	name 	en_name
// scientific_name 	family 	nature
// specifications 	ingredients 	properties
// 	contraindications 	organs 	habitat 	construction
public class DbHelper
        extends SQLiteOpenHelper {
    public String contact_table = "contact";
    public String group_table = "contact_group";
    private final String SQL_CREATE_CONTACT = "CREATE TABLE IF NOT EXISTS " + contact_table + "( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT ," +
            "name VARCHAR(512),"+
            "family VARCHAR(512),"+
            "phone VARCHAR(512),"+
            "gender VARCHAR(20),"+
            "year INTEGER,"+
            "month INTEGER,"+
            "day INTEGER,"+
            "address TEXT,"+
            "description TEXT," +
            "g_id INTEGER)";
    private final String SQL_CREATE_GROUP = "CREATE TABLE IF NOT EXISTS " + group_table + "( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT ," +
            "title VARCHAR(512)," +
            "check_default VARCHAR(20))";
    private SQLiteDatabase db = getWritableDatabase();

    public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
        paramSQLiteDatabase.execSQL(SQL_CREATE_CONTACT);
        paramSQLiteDatabase.execSQL(SQL_CREATE_GROUP);
    }
    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {
        paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + contact_table);
        paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + group_table);
        onCreate(paramSQLiteDatabase);
    }

    public DbHelper(Context paramContext) {
        super(paramContext, "sms_begir.db", null, BuildConfig.VERSION_CODE);
    }
    private Contact parseContact(Cursor paramCursor) {
        ArrayList<Contact> contacts = parseContacts(paramCursor, true);
        if (contacts.isEmpty()) {
            return null;
        }
        return contacts.get(0);
    }
    public int contactCount(int g_id){
        HashMap<String,String> params=new HashMap<>();
        params.put("g_id",g_id+"");
        Cursor cursor = getQuery(contact_table, params);
        ArrayList<Contact> contacts = parseContacts(cursor, false);
        cursor.close();
        if (contacts.isEmpty()) {
            return 0;
        } else {
            return contacts.size();
        }
    }
    public ArrayList<Contact> getContacts(int g_id){
        HashMap<String,String> params=new HashMap<>();
        params.put("g_id",g_id+"");
        Cursor cursor = getQuery(contact_table, params);
        ArrayList<Contact> contacts = parseContacts(cursor, false);
        cursor.close();
        if (contacts.isEmpty()) {
            return null;
        } else {
            return contacts;
        }
    }
    private ArrayList<Contact> parseContacts(Cursor paramCursor, boolean single) {
        ArrayList<Contact> contacts = new ArrayList<>();
        if (paramCursor != null) {
            while (paramCursor.moveToNext()) {
                Contact contact = new Contact();
                contact.setId(paramCursor.getInt(0));
                contact.setName(paramCursor.getString(1));
                contact.setFamily(paramCursor.getString(2));
                contact.setPhoneNumber(paramCursor.getString(3));
                contact.setGender(paramCursor.getString(4));
                contact.setYear(paramCursor.getInt(5)+"");
                contact.setMounth(paramCursor.getInt(6)+"");
                contact.setDay(paramCursor.getInt(7)+"");
                contact.setAddress(paramCursor.getString(8));
                contact.setDescription(paramCursor.getString(9));
                contact.setG_id(paramCursor.getInt(10));
                contacts.add(contact);
                if (single) {
                    break;
                }
            }
            paramCursor.close();
        }
        return contacts;
    }

    private ContentValues parseToContent(Contact contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", contact.getName());
        contentValues.put("family", contact.getFamily());
        contentValues.put("phone", contact.getPhoneNumber());
        contentValues.put("gender", contact.getGender());
        contentValues.put("year", Integer.parseInt(contact.getYear()));
        contentValues.put("month", Integer.parseInt(contact.getMounth()));
        contentValues.put("day", Integer.parseInt(contact.getDay()));
        contentValues.put("address", contact.getAddress());
        contentValues.put("description", contact.getDescription());
        contentValues.put("g_id", contact.getG_id());

        return contentValues;
    }



    public Cursor customQuery(String table, String select, String where) {
        String sql = "SELECT " + select + " FROM " + table;
        if (where != null) {
            sql += " WHERE " + where;
        }
        return this.db.rawQuery(sql, null);
    }
    public int delete(String table, String field, String value){
        HashMap<String,String> params=new HashMap<>();
        params.put(field,value);
        return delete(table,params);
    }
    public int delete(String table, HashMap<String, String> where) {
        String keys = "";
        boolean flag = false;
        ArrayList<String> values = new ArrayList<>();
        String[] vals;
        if (where == null) {
            vals = null;
            keys = null;
        } else {

            for (Map.Entry<String, String> entry : where.entrySet()) {
                if (flag) {
                    keys += " and ";
                }
                keys += entry.getKey() + "=?";
                values.add(entry.getValue());
                flag = true;
            }
            vals = new String[values.size()];
            for (int i = 0; i < values.size(); i++) {
                vals[i] = values.get(i);
            }
        }
        return db.delete(table, keys, vals);
    }

    protected void finalize()
            throws Throwable {
        this.db.close();
        super.finalize();
    }

    public Cursor getQuery(String table, HashMap<String, String> where) { //get data from database in cursor
        String query = "SELECT * FROM " + table;
        String keys = "";
        boolean flag = false;
        ArrayList<String> values = new ArrayList<>();
        String[] vals;
        if (where == null) {
            vals = null;
        } else {
            query += " WHERE ";
            for (Map.Entry<String, String> entry : where.entrySet()) {
                if (flag) {
                    keys += " and ";
                }
                keys += entry.getKey() + "=?";
                values.add(entry.getValue());
                flag = true;
            }
            query += keys;
            vals = new String[values.size()];
            for (int i = 0; i < values.size(); i++) {
                vals[i] = values.get(i);
            }
        }
        query+=" ORDER BY id DESC";

        return db.rawQuery(query, vals);
    }
    public long insert(Contact contact) {
        return db.insert(contact_table, null, parseToContent(contact));
    }
    public int update(Contact contact) {
        ContentValues contentValues = parseToContent(contact);
        String keys = "id = ?";
        String[] vals = {contact.getId() + ""};
        return db.update(contact_table, contentValues, keys, vals);
    }

    public long insert(Group group) {
        if(group.isDefaultGroup()){
            ContentValues contentValues=new ContentValues();
            contentValues.put("check_default","no");
            db.update(group_table, contentValues,null,null);
        }
        return db.insert(group_table, null, parseToContent(group));
    }
    public int update(Group group) {
        ContentValues contentValues = parseToContent(group);
        String keys = "id = ?";
        String[] vals = {group.getId() + ""};
        return db.update(group_table, contentValues, keys, vals);
    }


    public ArrayList<Group> getGroups(){
        Cursor cursor = getQuery(group_table, null);
        ArrayList<Group> groups = parseGroups(cursor, false);
        cursor.close();
        if (groups.isEmpty()) {
            return null;
        } else {
            for (int i=0;i<groups.size();i++){
                Group group=groups.get(i);
                group.setContactCount(contactCount(group.getId()));
                groups.set(i,group);
            }
            return groups;
        }
    }
    private ArrayList<Group> parseGroups(Cursor paramCursor, boolean single) {
        ArrayList<Group> groups = new ArrayList<>();
        if (paramCursor != null) {
            while (paramCursor.moveToNext()) {
                Group group = new Group();
                group.setId(paramCursor.getInt(0));
                group.setTitle(paramCursor.getString(1));
                if(paramCursor.getString(2).equals("yes")){
                    group.setDefaultGroup(true);
                }
                else{
                    group.setDefaultGroup(false);
                }
                groups.add(group);
                if (single) {
                    break;
                }
            }
            paramCursor.close();
        }
        return groups;
    }

    private ContentValues parseToContent(Group group) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", group.getTitle());
        if(group.isDefaultGroup()){
            contentValues.put("check_default", "yes");
        }
        else{
            contentValues.put("check_default", "no");
        }
        return contentValues;
    }


    public void getDefaultGroup() {
        HashMap<String,String> params=new HashMap<>();
        params.put("check_default","yes");
        Cursor cursor = getQuery(group_table, params);
        ArrayList<Group> groups = parseGroups(cursor,true);
        cursor.close();
        if (groups.isEmpty()) {

        } else {
            Group  group=groups.get(0);
            group.setContactCount(contactCount(group.getId()));
            App.defaultGroup=group;
        }
    }
    public Group getGroup(int id) {
        HashMap<String,String> params=new HashMap<>();
        params.put("id",id+"");
        Cursor cursor = getQuery(group_table, params);
        ArrayList<Group> groups = parseGroups(cursor,true);
        cursor.close();
        if (groups.isEmpty()) {
            return null;
        } else {
            Group  group=groups.get(0);
            group.setContactCount(contactCount(group.getId()));
            return group;
        }
    }
}
