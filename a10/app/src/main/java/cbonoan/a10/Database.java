package cbonoan.a10;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Database {

    public static void add(FirebaseFirestore db, String selectedCollection, ListItem item) {
        Map<String, Object> listItem = new HashMap<>();
        listItem.put("item", item);

        db.collection(selectedCollection)
                .add(listItem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("DATABASE", "Item added: " + documentReference);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("DATABASE", "Failed to add item: " + Arrays.toString(e.getStackTrace()));
                    }
                });

    }

    public static void getList(FirebaseFirestore db, String selectedCollection, ArrayList<ListItem> items,
                               ArrayAdapter<ListItem> itemsAdapter) {

        db.collection(selectedCollection)
                .orderBy("item.dateTime")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                long dateTime = doc.getLong("item.dateTime");
                                String item = doc.getString("item.item");
                                items.add(new ListItem(dateTime, item));
                            }
                            itemsAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("DATABASE", "Failed to get list: " + Arrays.toString(e.getStackTrace()));
                    }
                });

    }

    public static void removeItem(FirebaseFirestore db, String selectedCollection, ArrayList<ListItem> listItems,
                                  ArrayAdapter<ListItem> itemsAdapter, ListItem removedItem) {

        db.collection(selectedCollection).whereEqualTo("item.dateTime", removedItem.getDateTime())
                .whereEqualTo("item.item", removedItem.getItem())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots) {
                            db.collection(selectedCollection).document(doc.getId()).delete();
                        }
                    }
                });

    }

}
