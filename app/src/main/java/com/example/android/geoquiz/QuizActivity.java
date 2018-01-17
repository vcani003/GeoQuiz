package com.example.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;


    private int counter =  0;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia,true, false),
            new Question(R.string.question_oceans,true, false),
            new Question(R.string.question_mideast,false,false),
            new Question(R.string.question_africa,false, false),
            new Question(R.string.question_americas,true,false),
            new Question(R.string.question_asia, true, false)
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        updateQuestion();

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mPrevButton = (ImageButton) findViewById(R.id.previous_button);


        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();

            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                //startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);

            }
        });

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                checkAnswered();
                mIsCheater = false;
                updateQuestion();

            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                    checkAnswered();
                    updateQuestion();
                }catch(Exception e){
                    mCurrentIndex = mQuestionBank.length - 1; //when it reaches index[0], will update back to the last question.
                    checkAnswered();
                    updateQuestion();
                }

            }
        });

    }


    private void checkAnswered(){
        if(!mQuestionBank[mCurrentIndex].isAnswered()) {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
        else{
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }
    }

    /**
     * Overrides for log
     */
    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }




    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        boolean flag = false;
        mQuestionBank[mCurrentIndex].setAnswered(true);

        int messageResId = 0;

        if(mQuestionBank[mCurrentIndex].getHasCheated()){
            messageResId = R.string.judgment_toast;
        }else {

            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                if(!mQuestionBank[mCurrentIndex].getHasCheated())
                counter++;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
            Toast toast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 200);
            toast.show();

            for (int i = 0;i < mQuestionBank.length;i++){
                if(mQuestionBank[i].isAnswered() == false){
                    flag = true;
                }
            }
            if (flag != true){

                Toast toast2 = Toast.makeText(this, "Score: " + counter +  " out of 6", Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP, 0, 250);
                toast2.show();
            }

            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return;
            }
            mIsCheater= CheatActivity.wasAnswerShown(data);
            if(mIsCheater){
                mQuestionBank[mCurrentIndex].setHasCheated(true);
            }
        }
    }

}
