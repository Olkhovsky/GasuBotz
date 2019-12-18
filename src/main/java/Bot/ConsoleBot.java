package Bot;

import java.util.Scanner;


public class ConsoleBot {
    public static void main(String[] args) {
        Dictionary rusEnd = new Dictionary(Language.Russian, Language.English);
        rusEnd.Add("russWord1","cat");
        rusEnd.Add("russWord2","dog");
        rusEnd.Add("russWord3","circle");
        rusEnd.Add("russWord4","human");
        rusEnd.Add("russWord5","table");
        rusEnd.Add("russWord6","hand");
        rusEnd.Add("russWord7","work");
        rusEnd.Add("russWord8","day");
        rusEnd.Add("russWord9","wall");
        rusEnd.Add("russWord10","ocean");

        Exercise ex = new Exercise(rusEnd);
        ex.Start(10);

        Scanner in = new Scanner(System.in);
        while (ex.hasNext()) {
            String word = ex.next();
            System.out.println(word + " : ");
            String answer = in.nextLine();
            ex.WriteAnswer(answer);
        }
        System.out.println(ex.Result());
    }
}
