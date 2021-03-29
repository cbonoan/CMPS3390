package cbonoan.a10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    private ListView listView;
    private EditText input;
    private TabLayout tabs;
    private ArrayList<ListItem> items;
    private ArrayAdapter<ListItem> itemsAdapter;
    private String selectedCollection = "Todo";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.lvItems);
        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, R.layout.list_item_layout, items);
        listView.setAdapter(itemsAdapter);
        setupLongClickHandler();

        input = findViewById(R.id.etInput);
        input.setOnEditorActionListener(this);


        tabs = findViewById(R.id.tabLayout);
        setupTabClickListener();

        updateList();
    }

    private void updateList() {
        showToast("Getting List", Toast.LENGTH_SHORT);
        Database.getList(db, selectedCollection, items, itemsAdapter);

    }

    private void setupTabClickListener() {
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Clear Items
                items.clear();

                // Set the selected collection
                selectedCollection = tab.getText().toString();

                // Load Items from collection
                updateList();

                // Notify data set changed
                itemsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupLongClickHandler() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem tmpItem = items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                Database.removeItem(db, selectedCollection, items, itemsAdapter, tmpItem);
                showToast("Removed Item", Toast.LENGTH_SHORT);
                return true;
            }
        });
    }

    private void showToast(String message, int length) {
        Toast toast = Toast.makeText(this, message, length);
        toast.setGravity(Gravity.CENTER, 0,-30);
        toast.show();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(event == null || event.getAction() == KeyEvent.ACTION_UP) {
            ListItem tmpItem  = new ListItem(input.getText().toString());
            items.add(tmpItem);
            input.setText("");
            itemsAdapter.notifyDataSetChanged();
            Database.add(db, selectedCollection, tmpItem);
            showToast("Item Added to List!", Toast.LENGTH_SHORT);
        }

        return true;
    }
}