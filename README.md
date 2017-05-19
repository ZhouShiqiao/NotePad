# NotePad
 使用说明请跳转至 http://blog.csdn.net/dusk_angel/article/details/71759742
 
 新增功能：
 
 显示笔记更新时间
 
 笔记搜索框
 
 修改应用的背景颜色
 
 笔记加锁
 
 # 显示笔记更新时间
 
 - 自定义ListView的item布局
 
  修改布局文件notelist_item.xml,文件内容如下
```xml
 <?xml version="1.0" encoding="utf-8"?>
   <RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:padding="5dp"
    android:descendantFocusability="blocksDescendants">
     <ImageView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@drawable/app_notes"
        android:id="@+id/type_image"/>
     <TextView
        android:id="@+id/title_text"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="20sp"
        android:gravity="center_vertical"
        android:textColor="@color/black"
        android:layout_toRightOf="@+id/type_image"
        android:layout_toLeftOf="@+id/note_edit"
        android:layout_margin="5dp"
        />
     <ImageButton
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentRight="true"
        android:id="@+id/note_delete"
        android:src="@drawable/delete"
        android:background="@null"
        android:layout_margin="5dp"
        android:clickable="true"
        android:focusable="false" />
     <ImageButton
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_toLeftOf="@+id/note_delete"
        android:src="@drawable/edit_light"
        android:layout_margin="5dp"
        android:background="@null"
        android:id="@+id/note_edit"
        android:clickable="true"
        android:focusable="false" />
     <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@+id/title_text"
        android:orientation="horizontal"
        android:layout_toRightOf="@+id/type_image">
         <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/time_icon"
            android:src="@drawable/time"
            android:layout_margin="5dp"
            />
         <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/lock_icon"
            android:src="@drawable/lock_"
            android:layout_margin="5dp"/>
    </LinearLayout>
     <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/date_text"
        android:layout_below="@+id/note_delete"
        android:textColor="@color/black"
        android:gravity="right"/>
    </RelativeLayout>
 ```

- 修改查询

在首页获取笔记信息时，新增查询字段modification_date。

```java
    private static final String[] PROJECTION = new String[]{
            NotePad.Notes._ID, // 0
            NotePad.Notes.COLUMN_NAME_TITLE, // 1
            NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE,//2
            NotePad.Notes.COLUMN_NAME_LOCK
    };
```

同时将笔记的修改时间转化为yyyy-mm-dd hh:mm:ss 样式的时间格式字符串，存入表中

```java
  Long now = Long.valueOf(System.currentTimeMillis());
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  Date date = new Date(now);
  String datetime = formatter.format(date);
```
最后获取时间，将时间再控件上显示

因为需要自定义item，所以自定义了适配器，适配器如下
```java
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
```
