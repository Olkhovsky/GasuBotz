package Bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;


public class Program {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botApi = new TelegramBotsApi();
        try {
            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
            botOptions.setProxyHost("188.225.27.151");
            botOptions.setProxyPort(3128);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
            botApi.registerBot(new GasuBot(botOptions));
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
