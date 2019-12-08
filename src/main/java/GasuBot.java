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

    public GasuBot(DefaultBotOptions botOptions) {
        super(botOptions, Token.BOT_USERNAME);
    }
    public GasuBot() {
        super(Token.BOT_USERNAME);
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
        return Token.BOT_TOKEN;
    }



}
