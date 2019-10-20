import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;


import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


public class GasuBot extends TelegramLongPollingCommandBot {
    private static final String BOT_USERNAME = "Gasu2049_bot";
    private static final String BOT_TOKEN = "WhatTheHellAreYouLookingFor";

    public GasuBot(DefaultBotOptions botOptions) {
        super(botOptions, BOT_USERNAME);
        register(new HelloCommand());

        ReplyKeyboard keyBoard = getKeyboard();
        SendMessage sendMsg = new SendMessage().setReplyMarkup(keyBoard);
        try {
            execute(sendMsg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
    public GasuBot() {
        super(BOT_USERNAME);
        register(new HelloCommand());
    }
    @Override
    public void processNonCommandUpdate(Update update) {
        Message msg = update.getMessage();
        String str = new StringBuilder(msg.getText()).reverse().toString();
        SendMessage sendMsg = new SendMessage(msg.getChatId(), str);
        try {
            execute(sendMsg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }


    public ReplyKeyboard getKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyBoard = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add("/hello");

//        KeyboardRow secondRow = new KeyboardRow();
//        secondRow.add("hello");
//        secondRow.add("hello");
        keyBoard.add(firstRow);
//        keyBoard.add(secondRow);
        replyKeyboardMarkup.setKeyboard(keyBoard);
        return  replyKeyboardMarkup;
    }
}
