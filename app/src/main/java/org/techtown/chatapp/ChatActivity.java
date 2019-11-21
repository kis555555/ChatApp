package org.techtown.chatapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

public class ChatActivity extends AppCompatActivity
{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String[] myDataset = {"안녕","오늘","뭐했어","영화볼래"};

    EditText etText;
    Button btnSend;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        etText = (EditText)findViewById(R.id.etText);
        btnSend = (Button)findViewById(R.id.btnSend);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
           email = user.getEmail();
        }



        btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String stText = etText.getText().toString();

                if(email.equals("") || stText.equals(""))
                {
                    Toast.makeText(ChatActivity.this, "내용 입력하셈" ,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ChatActivity.this, email + " " + stText ,Toast.LENGTH_SHORT).show();

                    Calendar c = Calendar.getInstance();

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());



                    // Write a message to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("chats").child(formattedDate);

                    Hashtable<String, String> chat = new Hashtable<String, String>();
                    chat.put("email", email);
                    chat.put("text", stText);
                    myRef.setValue(chat);
                }

            }
        });


        Button btnFinish = (Button)findViewById(R.id.btnFinish);

        btnFinish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);



        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);



        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

    }
}
