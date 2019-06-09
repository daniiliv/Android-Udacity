package com.example.voiceassistant;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Send button.
    protected Button sendButton;

    // User's question text.
    protected EditText questionText;

    // Window that displays chat with the user.
    protected RecyclerView chatMessageList;

    // For text pronouncing.
    protected TextToSpeech tts;

    protected MessageListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding the xml view with java using its id.
        sendButton = findViewById(R.id.sendButton);
        questionText = findViewById(R.id.questionField);
        chatMessageList = findViewById(R.id.chatMessageList);

        adapter = new MessageListAdapter();
        chatMessageList.setLayoutManager(new LinearLayoutManager(this));
        chatMessageList.setAdapter(adapter);

        // Performs action when button is clicked.
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSendButton();
            }
        });

        // Set text listener to disable send button if question field is empty.
        questionText.addTextChangedListener(questionTextWatcher);

        // Instantiate Pronounce tool.
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(new Locale("ru"));
                }
            }
        });
    }

    /**
     * This method performs action when user pressed the button.
     */
    protected void onClickSendButton() {
        String text = questionText.getText().toString();

        //  Add question in the message list.
        adapter.messageList.add(new Message(text, true));
        adapter.notifyDataSetChanged();

        // Gets an answer user's question and adds it in the message list.
        AI.getAnswer(text, new Consumer<String>() {
            @Override
            public void accept(String answer) {
                adapter.messageList.add(new Message(answer, false));

                // Pronounce assistant answer.
                tts.speak(answer, TextToSpeech.QUEUE_FLUSH, null, null);

                adapter.notifyDataSetChanged();
                int lastMessageIndex = adapter.messageList.size() - 1;
                chatMessageList.scrollToPosition(lastMessageIndex);
            }
        });

        // Clears question field.
        questionText.setText("");
    }

    // User's input text listener.
    private TextWatcher questionTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = questionText.getText().toString();

            // If question field isn't empty, allow user to press the button.
            if (!text.isEmpty()) {
                sendButton.setEnabled(true);
            } else {
                sendButton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };
}
