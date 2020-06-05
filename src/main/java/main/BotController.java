package main;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BotController {

   public static void start() {

      ApiContextInitializer.init();
      TelegramBotsApi botapi = new TelegramBotsApi();
      try {
         botapi.registerBot(Bot.createBot());
      } catch (TelegramApiException e) {
         e.printStackTrace();
      }
   }

}
