package com.example.android.geoquiz;

/**
 * Created by veron on 12/18/2017.
 */

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mIsAnswered;
    private boolean mhasCheated;


    public Question(int textResId, boolean answerTrue, boolean isAnswered) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mIsAnswered = isAnswered;
        mhasCheated = false;
    }

    public boolean getHasCheated() {
        return mhasCheated;
    }

    public void setHasCheated(boolean mhasCheated) {
        this.mhasCheated = mhasCheated;
    }

    public boolean isAnswered() {
        return mIsAnswered;
    }

    public void setAnswered(boolean isAnswered) {
        mIsAnswered = isAnswered;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
