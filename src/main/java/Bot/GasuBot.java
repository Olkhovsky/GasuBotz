package Bot;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import java.util.Arrays;
import java.util.Map;


public class GasuBot extends AbilityBot {
    private Dictionary current;
    private String dicName;
    private Exercise exercise;

    public GasuBot(DefaultBotOptions botOptions) {
        super(Token.BOT_TOKEN, Token.BOT_USERNAME, botOptions);
    }

    public Ability Langs() {
        return  Ability
                .builder()
                .name("langs")
                .info("shows all languages")
                .input(0)
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    Arrays.stream(Language.values()).forEach(l -> {
                       String msg = l.toString() + " index: " + l.ordinal();
                        silent.send(msg, ctx.chatId());
                    });
                })
                .build();
    }
    public Ability NewDics() {
        return  Ability
                .builder()
                .name("newdic")
                .input(3)
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                     Map<String, Dictionary> map = db.getMap("dics" + ctx.user().getId());
                     Dictionary dic = map.get(ctx.firstArg());
                     if (dic == null) {
                         Language lang1 = Language.values()[Integer.parseInt(ctx.secondArg())];
                         Language lang2 = Language.values()[Integer.parseInt(ctx.thirdArg())];
                         dic = new Dictionary(lang1, lang2);
                         map.put(ctx.firstArg(), dic);
                         current = dic;
                         Done(ctx.chatId());
                     }
                     else {
                         silent.send("You already have it.", ctx.chatId());
                     }
                })
                .build();
    }

    public Ability SetDic() {
        return  Ability
                .builder()
                .name("setdic")
                .input(1)
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    Map<String, Dictionary> map = db.getMap("dics" + ctx.user().getId());
                    Dictionary dic = map.get(ctx.firstArg());
                    if (dic != null) {
                        current = dic;
                        dicName = ctx.firstArg();
                        Done(ctx.chatId());
                    }
                    else {
                        silent.send("You do not have it.", ctx.chatId());
                    }
                })
                .build();
    }

    public Ability Show() {
        return  Ability
                .builder()
                .name("show")
                .input(0)
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    if (current != null) {
                        current.getWords().stream().forEach(w -> {
                            silent.send(w.getWord(current.GetFrom()) + " " +
                                    w.getWord(current.GetTo()), ctx.chatId());
                        });
                    }
                    else {
                        silent.send("Set dictionary first.", ctx.chatId());
                    }
                })
                .build();
    }

    public Ability Swap() {
        return  Ability
                .builder()
                .name("swap")
                .input(0)
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    if (current != null) {
                        current.Reverse();
                        Done(ctx.chatId());
                    }
                    else {
                        silent.send("Set dictionary first.", ctx.chatId());
                    }
                })
                .build();
    }

    public Ability Add() {
        return  Ability
                .builder()
                .name("add")
                .input(2)
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    if (current != null) {
                        current.Add(ctx.firstArg(), ctx.secondArg());
                        Done(ctx.chatId());
                    }
                    else {
                        silent.send("Set dictionary first.", ctx.chatId());
                    }
                })
                .build();
    }

    public Ability Edit() {
        return  Ability
                .builder()
                .name("edit")
                .input(2)
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    if (current != null) {
                        boolean result = current.ChangeWord(ctx.firstArg(), ctx.secondArg());
                        if (result) {
                            Done(ctx.chatId());
                        } else {
                            silent.send("You do not have that word.", ctx.chatId());
                        }
                    }
                    else {
                        silent.send("Set dictionary first.", ctx.chatId());
                    }
                })
                .build();
    }

    public Ability Delete() {
        return  Ability
                .builder()
                .name("delete")
                .input(1)
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    if (current != null) {
                        boolean result = current.DeleteWord(ctx.firstArg());
                        if (result) {
                            Done(ctx.chatId());
                        } else {
                            silent.send("You do not have that word.", ctx.chatId());
                        }
                    }
                    else {
                        silent.send("Set dictionary first.", ctx.chatId());
                    }
                })
                .build();
    }

    public Reply Translate() {
        return Reply.of( update -> {
            if (current != null) {
                String word = current.GetTranslation(update.getMessage().getText());
                if (word != null) {
                    silent.send(word, update.getMessage().getChatId());
                } else {
                    silent.send("You do not have that word.", update.getMessage().getChatId());
                }
            } else {
                silent.send("Set dictionary first.", update.getMessage().getChatId());
            }
        },
        Flag.MESSAGE,
                Flag.TEXT,
                update -> update.getMessage().getText().charAt(0) != '/',
                update -> exercise == null || !exercise._isSet
        );
    }

    public Ability StartExercise() {
        return  Ability
                .builder()
                .name("start")
                .input(0)
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    if (current != null) {

                        if (exercise == null) {
                            Map<String, Exercise> map = db.getMap("ex" + ctx.user().getId());
                            exercise = map.get(dicName);
                        }

                        if (exercise == null) {
                            exercise = new Exercise(current);
//                            map.put(dicName, ex);
                        }
                        exercise.Start(5);
                        silent.send(exercise.next(), ctx.chatId());
                    }
                    else {
                        silent.send("Set dictionary first.", ctx.chatId());
                    }
                })
                .build();
    }

    public Reply ContinueExercise() {
        return Reply.of( update -> {
                    exercise.WriteAnswer(update.getMessage().getText());
                    if (exercise.hasNext()){
                        silent.send(exercise.next(), update.getMessage().getChatId());
                    } else {
                        silent.send(exercise.Result(), update.getMessage().getChatId());
                    }
                },
                Flag.MESSAGE,
                Flag.TEXT,
                update -> update.getMessage().getText().charAt(0) != '/',
                update -> exercise != null && exercise._isSet
        );
    }
    private void Done(Long chatId) {
        db.commit();
        silent.send("Done!", chatId);
    }

    @Override
    public int creatorId() {
        return 0;
    }


}
