# NotePad
 使用说明请跳转至 http://blog.csdn.net/dusk_angel/article/details/71759742
 
 新增功能：
 
 显示笔记更新时间
 
 笔记搜索框
 
 修改应用的背景颜色
 
 笔记加锁
 
 # 显示笔记更新时间
 
 ## 自定义ListView的item布局
 
 修改布局文件notelist_item.xml,文件内容如下
 
 <xml>
  <?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:padding="5dp"
    android:descendantFocusability="blocksDescendants"
    >
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
 </xml>
 
