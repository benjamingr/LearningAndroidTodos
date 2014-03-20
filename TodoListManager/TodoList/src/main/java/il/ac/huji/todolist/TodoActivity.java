package il.ac.huji.todolist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

public class TodoActivity extends Activity {
    public static final int REQUEST_CODE_SECRET = 1337;
    public static final int INVALID = -1;
    ArrayList<TodoItem> defaultTodos;
    ArrayAdapter<TodoItem> todos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        defaultTodos = new ArrayList<>();
        todos = new ArrayWithDateAdapter(this, R.layout.todo,R.id.todoItem, defaultTodos);
        ListView list = (ListView)findViewById(R.id.lstTodoItems);
        list.setAdapter(todos);
        registerForContextMenu(list);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        
        getMenuInflater().inflate(R.menu.todo, menu);


        MenuItem item = menu.findItem(R.id.action_add);
        final TodoActivity that = this;
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent t = new Intent(that, AddNewTodoItemActivity.class);
                startActivityForResult(t, REQUEST_CODE_SECRET);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    // got result back
    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        boolean notOurRequest = (result != RESULT_OK) || (request != REQUEST_CODE_SECRET);
        if(notOurRequest){
            return;
        }
        TodoItem t = new TodoItem();
        t.date = new Date(data.getLongExtra("date", INVALID));
        t.text = data.getStringExtra("text");
        defaultTodos.add(t);
        todos.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu,v,menuInfo);
        if (v.getId() != R.id.lstTodoItems) {
            return;
        }
        ListView lv = (ListView) v;
        final AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final TodoItem itemClicked = (TodoItem) lv.getItemAtPosition(acmi.position);
        menu.setHeaderTitle(itemClicked.text);
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.todo_item_context,menu);
        boolean call = itemClicked.text.startsWith("call") || itemClicked.text.startsWith("Call");
        menu.getItem(1).setVisible(call);
        menu.getItem(1).setTitle(itemClicked.text);

        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                defaultTodos.remove(acmi.position);
                todos.notifyDataSetChanged();
                return true;
            }
        });
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String number = itemClicked.text.replaceAll("[a-zA-Z -]","");
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number)));

                return true;
            }
        });

    }

    static class ArrayWithDateAdapter extends ArrayAdapter<TodoItem>{

        List<TodoItem> data;
        LayoutInflater inf;
        public ArrayWithDateAdapter(Context context, int resource, int textViewResourceId, List<TodoItem> objects) {
            super(context, resource, textViewResourceId, objects);
            this.data = objects;
            this.inf = LayoutInflater.from(context);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View view = inf.inflate(R.layout.todo_item, null);
            TodoItem item = this.data.get(position); // model

            TextView content = (TextView)view.findViewById(R.id.txtTodoTitle);
            TextView date = (TextView)view.findViewById(R.id.txtTodoDueDate);

            warnIfDue(item, content, date);

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            content.setText(item.text);
            date.setText(dateFormat.format(item.date));
            return view;
        }

        private void warnIfDue(TodoItem item, TextView content, TextView date) {
            if(item.date.before(new Date())){
                content.setTextColor(Color.RED);
                date.setTextColor(Color.RED);
            }
        }
    }
    class TodoItem{
        String text;
        Date date;
    }

}
