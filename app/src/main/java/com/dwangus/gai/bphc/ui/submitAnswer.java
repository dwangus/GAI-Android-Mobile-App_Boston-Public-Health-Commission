package com.dwangus.gai.bphc.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dwangus.gai.bphc.R;
import com.dwangus.gai.bphc.model.Answers;
import com.dwangus.gai.bphc.model.Questions;
import com.firebase.client.Firebase;

import butterknife.ButterKnife;
import butterknife.InjectView;
import java.util.HashMap;

public class submitAnswer extends AppCompatActivity {

    private long num;
    @InjectView(R.id.headerA) EditText mHeader;
    @InjectView(R.id.submitAnswer) Button mSubmitA;
    @InjectView(R.id.detailsAnswer) EditText mDetailsA;
    @InjectView(R.id.cancelAnswer) Button mCancelA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_answer);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        final String url = intent.getStringExtra("FIREBASE_URL");
        final String qid = intent.getStringExtra("Q_ID");
        num = intent.getLongExtra("NUMBER", 0L);
        final Firebase mRootRef = new Firebase(url);
        mSubmitA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase ARef = mRootRef.child("answers");
                String header = mHeader.getText().toString();
                String detailsA = mDetailsA.getText().toString();
                Toast.makeText(submitAnswer.this, "Answer received!", Toast.LENGTH_LONG).show();
                Answers new_Answer = new Answers();
                new_Answer.setAnswer(detailsA);
                new_Answer.setHeader(header);
                Long tsLong = System.currentTimeMillis()/1000;
                new_Answer.setTimeAnswered(tsLong);
                new_Answer.setQID(qid);
                ARef.push().setValue(new_Answer);

                num++;
                Firebase questionRef = mRootRef.child("questions").child(qid);
                HashMap<String, Object> newNumAns = new HashMap<String, Object>();
                newNumAns.put("numAnswers", num);
                questionRef.updateChildren(newNumAns);
                finish();
            }
        });

        mCancelA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
