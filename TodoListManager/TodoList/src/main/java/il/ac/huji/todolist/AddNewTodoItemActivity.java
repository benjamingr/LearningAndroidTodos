package il.ac.huji.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Date;

public class AddNewTodoItemActivity extends Activity {

    public static final int YEAR_DATE_OFFSET = 1900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_new_todo_item);
        this.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // end activity
            }
        });
        this.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et = (EditText)findViewById(R.id.edtNewItem);
                String text = et.getText().toString();
                DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
                Date chosenDate = new Date(datePicker.getYear() - YEAR_DATE_OFFSET,
                                            datePicker.getMonth(),
                                            datePicker.getDayOfMonth());

                if(text.isEmpty()) finish();
                Intent t = new Intent();
                t.putExtra("date",chosenDate.getTime());
                t.putExtra("text",text);
                et.setText("");
                setResult(RESULT_OK, t);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo_item_context, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

}
