package il.ac.huji.todolist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends Activity {
    PlaceholderFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        if (savedInstanceState == null) {
            fragment = new PlaceholderFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo, menu);
        final EditText currentTodo = (EditText)findViewById(R.id.edtNewItem);

        boolean shouldShow = !currentTodo.getText().toString().isEmpty();

        menu.getItem(0).setVisible(shouldShow);

        currentTodo.addTextChangedListener(new TextWatcher() {


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean shouldShow = !currentTodo.getText().toString().isEmpty();
                menu.getItem(0).setVisible(shouldShow);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }
        });

        MenuItem item = menu.findItem(R.id.action_add);

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                fragment.defaultTodos.add(currentTodo.getText().toString());
                currentTodo.setText("");
                fragment.todos.notifyDataSetChanged();
                return false;
            }
        });

        return true;
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
        if (v.getId() == R.id.lstTodoItems) {
            ListView lv = (ListView) v;
            final AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            final String itemClicked = (String) lv.getItemAtPosition(acmi.position);
            menu.setHeaderTitle(itemClicked);
            menu.add("Remove "+ itemClicked+"?");
            menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    fragment.defaultTodos.remove(acmi.position);
                    fragment.todos.notifyDataSetChanged();
                    return true;
                }
            });
        }
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        ArrayAdapter<String> todos;
        ArrayList<String> defaultTodos = new ArrayList<>();
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_todo, container, false);
            assert rootView != null;
            ListView list = (ListView)rootView.findViewById(R.id.lstTodoItems);
            registerForContextMenu(list);

            Activity cur = this.getActivity();
            assert cur != null;
            todos = new ArrayAlternatingColorAdapter(cur, R.layout.todo,R.id.todo_item, defaultTodos);
            list.setAdapter(todos);
            return rootView;
        }
    }
    static class ArrayAlternatingColorAdapter extends ArrayAdapter<String>{

        public ArrayAlternatingColorAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
            super(context, resource, textViewResourceId, objects);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View view = super.getView(position,convertView,parent);
            TextView item = (TextView)view.findViewById(R.id.todo_item);
            int color = (position%2==1) ? Color.BLUE : Color.RED;
            item.setTextColor(color);
            return view;
        }
    }

}
