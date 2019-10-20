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
            botOptions.setProxyHost("185.234.217.6");
            botOptions.setProxyPort(3128);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
            //botApi.registerBot(new GasuBot(botOptions));
            botApi.registerBot(new GasuBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
