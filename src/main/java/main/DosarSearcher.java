package main;

import java.util.List;

public class DosarSearcher {


    public void start(){
        List<String> list = SearcherWriter.read();
        for(int i = list.size() - 1; i >= 0; i--){
            int index = list.get(i).indexOf("/");
//            int thousand = Integer.valueOf(list.get(i).substring(0, index)) / 1000;
            try {
                String link = WorkWithDataBase.getWorker().containReturnLink(list.get(i).substring(index + 1, index + 5), list.get(i).substring(0, index));
                if (!link.equals("")){
                    Bot.createBot().sendMess("Вітаємо, Ви вийшли до наказу!!!\nПосилання на ваш наказ: " + link, Long.valueOf(list.get(i).split(" ")[1]));
                    list.remove(i);
                }
//                List<String> numbers = WorkWithDataBase.getWorker().read(new File("Ordine\\" + list.get(i).substring(index + 1, index + 5) + "\\" + thousand + ".txt"));
//                for(int l = 0; l < numbers.size(); l++){
//                    if (numbers.get(l).split("   ")[0].equals(list.get(i).substring(0, index))){
//                        Bot.createBot().sendMess("Вітаємо, Ви вийшли до наказу!!!\nПосилання на ваш наказ: " + numbers.get(l).split("   ")[1], Long.valueOf(list.get(i).split(" ")[1]));
//                        list.remove(i);
//                    }
//                }
            } catch (Exception e){}
        }
        SearcherWriter.write(list);
    }
}
