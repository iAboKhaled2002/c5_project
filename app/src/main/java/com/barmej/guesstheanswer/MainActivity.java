package com.barmej.guesstheanswer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //-------------------------------------------------------
    private String[] QUESTIONS;
    private static final boolean[] ANSWERS = {
            false, true, true, false, true,
            false, false, false, false, true,
            true, false, true
    };
    private String[] ANSWERS_DETAILS;

    private TextView mTextViewQuestion;
    private String mCurrentQuestion, mCurrentAnswerDetail;
    private boolean mCurrentAnswer;
    //-------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("app_pref", MODE_PRIVATE);
        String appLang = sharedPreferences.getString("app_lang", "");
        if (!appLang.equals(""))
            LocaleHelper.setLocale(this, appLang);

        setContentView(R.layout.activity_main);
        mTextViewQuestion = findViewById(R.id.text_view_question);
        QUESTIONS = getResources().getStringArray(R.array.questions);
        ANSWERS_DETAILS = getResources().getStringArray(R.array.answers_details);
        showNewQuestion();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuChangeLang) {
            showLanguageDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showLanguageDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.change_lang_text)
                .setItems(R.array.Languages, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String language = "ar";
                        switch (which) {
                            case 0:
                                language = "en";
                                break;
                            case 1:
                                language = "ar";
                                break;
                        }
                        saveLanguage(language);
                        LocaleHelper.setLocale(MainActivity.this, language);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                }).create();
        alertDialog.show();
    }

    private void saveLanguage(String lang) {
        SharedPreferences sharedPreferences = getSharedPreferences("app_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("app_lang", lang);
        editor.apply();
    }

    private void showNewQuestion() {
        Random random = new Random();
        int randomIndex = random.nextInt(QUESTIONS.length);
        mCurrentQuestion = QUESTIONS[randomIndex];
        mCurrentAnswer = ANSWERS[randomIndex];
        mCurrentAnswerDetail = ANSWERS_DETAILS[randomIndex];
        mTextViewQuestion.setText(mCurrentQuestion);
    }

    public void changeQuestion(View view) {
        showNewQuestion();
    }

    public void shareQuestion(View view) {
        Intent intent = new Intent(MainActivity.this, ShareActivity.class);
        intent.putExtra("question", mCurrentQuestion);
        startActivity(intent);
    }

    public void trueClick(View view) {
        if (mCurrentAnswer == true) {
            showNewQuestion();
        } else {
            Intent intent = new Intent(MainActivity.this, AnswerActivity.class);
            intent.putExtra("question_answer", mCurrentAnswerDetail);
            startActivity(intent);
        }
    }

    public void falseClick(View view) {
        if (mCurrentAnswer == false) {
            showNewQuestion();
        } else {
            Intent intent = new Intent(MainActivity.this, AnswerActivity.class);
            intent.putExtra("question_answer", mCurrentAnswerDetail);
            startActivity(intent);
        }
    }

    public void exitButton(View view) {
        finish();
    }
}