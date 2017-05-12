package com.example.android.notepad;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ZhouShiqiao on 2017/4/23 0023.
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<Note> list;

    public MyAdapter(Context context, List<Note> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0).getId();
    }

    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder holder = new ViewHolder();
        view = LayoutInflater.from(context).inflate(R.layout.noteslist_item,
                null);
        holder.note_title = (TextView) view.findViewById(R.id.title_text);
        holder.note_date = (TextView) view.findViewById(R.id.date_text);
        holder.note_edit = (ImageButton) view.findViewById(R.id.note_edit);
        holder.note_delete = (ImageButton) view.findViewById(R.id.note_delete);
        holder.note_type = (ImageView) view.findViewById(R.id.type_image);
        holder.lock_icon = (ImageView) view.findViewById(R.id.lock_icon);
        holder.time_icon = (ImageView) view.findViewById(R.id.time_icon);

        if (list.get(position).getTiming() == null || list.get(position).getTiming().equals("")) {
            holder.time_icon.setVisibility(View.GONE);
        }
        else{
            holder.time_icon.setVisibility(View.VISIBLE);
        }
        if (list.get(position).getLock() == 0) {
            holder.lock_icon.setVisibility(View.GONE);
        }
        else{
            holder.lock_icon.setVisibility(View.VISIBLE);
        }
        holder.note_title.setText(list.get(position).getTitle());
        holder.note_date.setText(list.get(position).getModificationdate());
        holder.note_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getLock() != 0) {
                    showdialog(position, 1);
                    return;
                }
                Uri uri = ContentUris.withAppendedId(NotesList.instance.getIntent().getData(), list.get(position).getId());
                String action = NotesList.instance.getIntent().getAction();
                if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) {
                    NotesList.instance.setResult(RESULT_OK, new Intent().setData(uri));
                } else {
                    NotesList.instance.startActivity(new Intent(Intent.ACTION_EDIT, uri));
                }
            }
        });
        holder.note_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getLock() != 0) {
                    showdialog(position, 2);
                    return;
                }
                Uri noteUri = ContentUris.withAppendedId(NotesList.instance.getIntent().getData(), list.get(position).getId());
                NotesList.instance.getContentResolver().delete(noteUri, null, null);
                list.remove(position);
                notifyDataSetChanged();

            }
        });

        return view;
        //view表示的是返回一行的view
    }

    class ViewHolder {
        TextView note_title;
        TextView note_date;
        ImageButton note_edit;
        ImageButton note_delete;
        ImageView note_type;
        ImageView time_icon;
        ImageView lock_icon;
    }

    private void showdialog(int p, int t) {
        final int position = p;
        final int type = t;
        final EditText et = new EditText(context);
        et.setHint("please enter a password");
        et.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        new AlertDialog.Builder(context).setTitle("Lock")
                .setIcon(R.drawable.app_notes)
                .setView(et)
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(context, "the password cannot be empty", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (!new Preferences(context, "note").getString("lock").equals(input)) {
                            Toast.makeText(context, "the password is error", Toast.LENGTH_LONG).show();
                        }
                        switch (type) {
                            case 1:
                                Uri uri = ContentUris.withAppendedId(NotesList.instance.getIntent().getData(), list.get(position).getId());
                                String action = NotesList.instance.getIntent().getAction();
                                if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) {
                                    NotesList.instance.setResult(RESULT_OK, new Intent().setData(uri));
                                } else {
                                    NotesList.instance.startActivity(new Intent(Intent.ACTION_EDIT, uri));
                                }
                                break;
                            case 2:
                                Uri noteUri = ContentUris.withAppendedId(NotesList.instance.getIntent().getData(), list.get(position).getId());
                                NotesList.instance.getContentResolver().delete(noteUri, null, null);
                                list.remove(position);
                                notifyDataSetChanged();
                                break;
                        }


                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
