package main;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    private static Bot bot = null;
    private Bot(){
        searchs = new ArrayList<>();
        statistic = new ArrayList<>();
    }
    public static Bot createBot(){
        if (bot == null){
            bot = new Bot();
        }
        return bot;
    }

    private ArrayList<Long> searchs;
    private ArrayList<Long> statistic;

    @Override
    public synchronized void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String message = update.getMessage().getText();
                AbonentWriter.write(update);
                for (int i = 0; i < searchs.size(); i++) {
                    if (searchs.get(i).equals(update.getMessage().getChatId())) {
                        searchs.remove(i);
                        try {
                            Integer.valueOf(message.split("/")[0]);
                            Integer.valueOf(message.split("/")[1]);
                            answerSearchSecondStep(update);
                            return;
                        } catch (Exception e) {
                            answerIncorrectEnterDate(update);
                            return;
                        }
                    }
                }
                for(int i = 0; i < statistic.size(); i++){
                    if(statistic.get(i).equals(update.getMessage().getChatId())){
                        statistic.remove(i);
                        try {
                            Integer.valueOf(message);
                            answerStatisticSecondStep(update);
                            return;
                        } catch (Exception e){
                            answerIncorrectEnterDateStatistic(update);
                            return;
                        }
                    }
                }
                if (message.equals("/start") || message.equals("Довідка")) {
                    answerStart(update);
                    return;
                }
                if (message.equals("/lastUpdate") || message.equals("Останнє оновлення")){
                    answerLastUpdate(update);
                    return;
                }
                if (message.equals("/searchMy") || message.equals("Відслідковувати мій номер досару")){
                    searchs.add(update.getMessage().getChatId());
                    answerSearchFirstStep(update);
                    return;
                }
                if (message.equals("/statistic") || message.equals("Показати статистику")){
                    statistic.add(update.getMessage().getChatId());
                    answerStatisticFirstStep(update);
                    return;
                }
                if (message.contains("/")) {
                    try {
                        Integer.valueOf(message.split("/")[0]);
                        Integer.valueOf(message.split("/")[1]);
                        answerNumberDosar(update);
                        return;
                    } catch (Exception e) {
                        answerIncorrectEnterDate(update);
                        return;
                    }
                }
                answerIncorrectEnterDate(update);
            }
        } catch (Exception e){}
    }

    private void markUp(Update update){
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId());
        sendMessage.setText("Ваші команди:");
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Довідка");
        keyboardRow.add("Останнє оновлення");
        rows.add(keyboardRow);
        keyboardRow = new KeyboardRow();
        keyboardRow.add("Відслідковувати мій номер досару");
        keyboardRow.add("Показати статистику");
        rows.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try {
            execute(sendMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized void sendMess(String line, long chatID){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText(line);
        try {
            execute(sendMessage);
        } catch (Exception e){}
    }

    private synchronized void sendMess(Update update, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private synchronized void answerStart(Update update) {
        sendMess(update, "Доброго дня!\nЩоб перевірити, чи вийшли Ви до наказу, введіть та " +
                "надішліть Ваш номер Досару та рік подачі документів у форматі: [цифри]/[рік подачі].\n\n" +
                "Приклад:  3245/2017\n\nДодаткові команди:\n-Довідка: /start\n-Останнє оновлення: /lastUpdate\n" +
                "-Відслідковування досару: /searchMy\n-Статистика за рік: /statistic");
        markUp(update);
    }

    private synchronized void answerNumberDosar(Update update) throws Exception{
        String[] dosarYear = update.getMessage().getText().split("/");
        String link = WorkWithDataBase.getWorker().containReturnLink(dosarYear[1], dosarYear[0]);
        if (!link.equals("")) {
            sendMess(update, "Так, Ви вийшли до наказу.");
            sendMess(update, "Посилання на Ваш наказ: " + link);
            return;
        }

//        int thousand = Integer.valueOf(dosarYear[0]) / 1000;
//        List<String> list = WorkWithDataBase.getWorker().read(new File("Ordine\\" + dosarYear[1] + "\\" + thousand + ".txt"));
//
//        for (int i = 0; i < list.size(); i++){
//            if(list.get(i).split("   ")[0].equals(dosarYear[0])) {
//                sendMess(update, "Так, Ви вийшли до наказу.");
//                sendMess(update, "Посилання на Ваш наказ: " + list.get(i).split("   ")[1]);
//                return;
//            }
//        }
        sendMess(update, "Ні, на жаль, Ви ще не вийшли до наказу. Будь ласка, очікуйте.");
    }

    private synchronized void answerIncorrectEnterDate(Update update){
        sendMess(update,"Вибачте, Ви ввели некоректні дані.\nСкористайтесь шаблоном: \n[цифри]/[рік подачі].\n\n" +
                "Приклад:  3245/2017");
    }

    private synchronized void answerLastUpdate(Update update){
        if (LastUpdatingDate.lastUpdating() != null )sendMess(update, LastUpdatingDate.lastUpdating());
        else sendMess(update, "Нажаль, наразі інформація відсутня.");
    }

    private synchronized void answerSearchFirstStep(Update update){
        sendMess(update, "Введіть номер вашого досару.\nСкористайтесь шаблоном: \n[цифри]/[рік подачі].\nПриклад:  3245/2017");
    }

    private synchronized void answerSearchSecondStep(Update update) {
        if (SearcherWriter.write(update.getMessage().getText(), update.getMessage().getChatId())) {
            sendMess(update, "Тепер номер вашого досару відслідковується.");
        } else {
            sendMess(update, "Щось пішло не так, спробуйте пізніше.");
        }
    }

    private synchronized void answerStatisticFirstStep(Update update){
        sendMess(update, "Вкажіть рік.\nПриклад: 2017");
    }

    private synchronized void answerStatisticSecondStep(Update update){
        StatisticCrater statisticCrater = StatisticCrater.crater();
        List<StatisticUnit> units = statisticCrater.getUnits();
        String line = "Статистика по місяцям:\n\n";
        int countYes = 0;
        int countNo = 0;
        for (int i = 0; i < units.size(); i++){
            if(units.get(i).getYear() == Integer.valueOf(update.getMessage().getText())){
                for(int l = 0; l < 12; l++){
                    countYes = countYes + units.get(i).getMonthYes()[l];
                    countNo = countNo + units.get(i).getMonthNo()[l];
                    line = line + " - " + (l + 1) +"." + update.getMessage().getText() + "  так: " + units.get(i).getMonthYes()[l] + " ні: " + units.get(i).getMonthNo()[l] + "\n";
                }
                line = line + "\n -За " + update.getMessage().getText() + " рік усього людей подали документи: " + (countNo + countYes);
                line = line + "\n -За " + update.getMessage().getText() + " рік усього людей вийшло до наказу: " + countYes;
                line = line + "\n -За " + update.getMessage().getText() + " рік усього людей очікує: " + countNo;
                sendMess(line, update.getMessage().getChatId());
                return;
            }
        }
        sendMess("Нажаль неможна надати статистику за цей рік", update.getMessage().getChatId());
    }

    private synchronized void answerIncorrectEnterDateStatistic(Update update){
        sendMess(update,"Вибачте, Ви ввели некоректні дані.\nСкористайтесь шаблоном: \n[рік подачі].\n\n" +
                "Приклад:  2016");
    }

    @Override
    public String getBotToken() {
        return "915051677:AAHQkHZBIAW2b9RQhHTYGJD-nPrxB42H0oM";
    }

    @Override
    public String getBotUsername() {
        return "CheckYourDosarBot";
    }
}
