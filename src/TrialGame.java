import extensions.File;
import extensions.CSVFile;


class TrialGame extends Program {
    final int IDX_CASE_VERTICALE = 9;
    final int IDX_CASE_HORIZONTAL = 16;
    final int TEXT=0, R=4,REPONSE=5,REPONDU=6;
    final double PROBA_QUESTIONS = 0.25;
    final int NOMBRE_QUESTION= 32;
    int score = 0;
    boolean laReponse = false;
    Question [] tableauQuestions;

    String initialiserPlateau(String chemin){
        String tp = "";
        CSVFile csv = loadCSV(chemin,';');
        for(int i = 0; i < 30; i++){
            tp += getCell(csv,i,0) + "\n";
            print(tp);
        }
        return tp;
    }

    void menuTrivial(String chemin){
        String tp = "";
        CSVFile csv = loadCSV(chemin, ';');
        for(int i = 0; i < 7; i ++){
            tp = getCell(csv, i, 0) + "\n";
            print(tp);
        }
    }

    String [][] tabPlateaeu(String chemin){
        String [][]tp = new String[30][16];
        CSVFile csv = loadCSV(chemin,';');
        for(int i = 0; i < length(tp,1); i++){
            if(i == 2 || i == 27 || i == 6 || i == 9 || i == 12 || i == 15 || i == 18 || i == 21 || i == 24){
                for(int j = 0; j < length(tp,2); j ++){
                    tp[i][j] = getCell(csv, i, j);
                }
            }else{
                tp[i][0] = getCell(csv,i,0);
            }
        }
        return tp;
    }

    void afficherPlateau(String [][] tab, Case [][] cases){
        int idx = 1;
        int premierLancer = 0;
        for(int i = 0; i < length(tab,1);i++){
            for(int j = 0; j < length(tab,2);j++){
                if(i == 2){
                    print(toString(tab[i][j]) + toString(cases[0][j]));
                }else if(i == 27){
                    print(toString(tab[i][j]) + toString(cases[8][j]));
                }else if(i == 6 || i == 9 || i == 12 || i == 15 || i == 18 || i == 21 || i == 24){
                    print(toString(tab[i][j]) + toString(cases[idx][j]));
                }else if(tab[i][j] != null && cases[idx][j] != null){
                    print(toString(tab[i][0]));
                }
            }
            if(i == 6 || i == 9 || i == 12 || i == 15 || i == 18 || i == 21 || i == 24){
                idx++;
            }
            println();
        }
        if(!laReponse){
            if(premierLancer != 0){
               println("Mauvaise réponse"); 
               premierLancer++;
            }
            println("Votre score est de "+ score);
        }else{
            println("Bonne réponse");
            println("Votre score est de "+ score);
        }
    }

    Case [][] initialiserCase(){
        Plateau plt = newPlateau();
        for(int i = 0; i < length(plt.cases,1);i++){
            for(int j = 0; j < length(plt.cases,2);j++){
                if(i == 0 && j > 0 && j < 15 || i == 8 && j > 0 && j < 15){
                    plt.cases[i][j] = Case.QUESTIONS;
                }else if(j == 0 || j == 14 ){
                    plt.cases[i][j] = Case.QUESTIONS;
                }else{
                    plt.cases[i][j] = null;
                }
            }
        }
        plt.cases[8][0] = Case.JOUEUR;
        return plt.cases;
    }

    String toString(String m){
        if(m == null){
            return "";
        }else{
            return m;
        }
    }

    String toString(Case c){
        if(c == Case.QUESTIONS){
            return "?";
        }else if(c == Case.JOUEUR){
            return "J";
        }else{
            return " ";
        }
    }
    
    void deplacement(Case [][] cases, String [][] tab){
        print("Appuyer sur n'importe quelle touche pour lancer le dés");
        String tp = readString();
        int lancer = des(6);
        println("Vous avez fait "+lancer +" !");
        Case caseTP;
        int [] idxJoueur = idxJoueur(cases);
        int [] caseSuivante = idxQuestion(cases,idxJoueur);
        for(int i = 0; i < lancer; i++){
            idxJoueur = idxJoueur(cases);
            caseSuivante = idxQuestion(cases, idxJoueur);
            caseTP = cases[idxJoueur[0]][idxJoueur[1]];
            cases[idxJoueur[0]][idxJoueur[1]] = cases[caseSuivante[0]][caseSuivante[1]];
            cases[caseSuivante[0]][caseSuivante[1]] = caseTP;
        }
        afficherPlateau(tab, cases);
        initialiserQuestion(NOMBRE_QUESTION, tableauQuestions);
    }

    int [] idxQuestion(Case [][] cases, int [] idxJoueur){
        int [] idx = new int [2];
        if(idxJoueur[0] == 0){
            if(idxJoueur[1]+1 == 15){
                idx[0] = idxJoueur[0]+1;
                idx[1] = idxJoueur[1]; 
            }else{
                idx[0] = idxJoueur[0];
                idx[1] = idxJoueur[1]+1;
            }
        }else if(idxJoueur[0] == 8){
            if(idxJoueur[1]-1 == -1){
                idx[0] = idxJoueur[0]-1;
                idx[1] = idxJoueur[1]; 
            }else{
                idx[0] = idxJoueur[0];
                idx[1] = idxJoueur[1]-1;
            }            
        }else if(idxJoueur[1] == 0){
            idx[0] = idxJoueur[0]-1;
            idx[1] = idxJoueur[1];
        }else if(idxJoueur[1]+1 == 15){
            idx[0] = idxJoueur[0]+1;
            idx[1] = idxJoueur[1];
        }
        return idx;
    }

    int [] idxJoueur(Case [][] cases){
        int i = 0;
        int j = 0;
        int [] idx = new int[2];
        boolean trouve = false;
        while(!trouve && i < length(cases,1)){
            j = 0;
            while(!trouve && j < length(cases,2)){
                if(cases[i][j] == Case.JOUEUR){
                    trouve = true;
                    idx [0] = i;
                    idx [1] = j;
                }
                j++;
            }
            i++;
        }
        return idx;
    }

    Plateau newPlateau(){
        Plateau t = new Plateau();
        t.cases = new Case [IDX_CASE_VERTICALE][IDX_CASE_HORIZONTAL];
        return t;
    }

    int des(int nbFace){
        return (int)(random()*nbFace)+1;
    }

    //Charge les questions dans un tableau depuis un csv
    Question [] chargerQuestion(String chemin) {
        CSVFile csv = loadCSV(chemin, ';');
        int idx;
        Question[] questions = new Question[NOMBRE_QUESTION];
        Question q;
        for (int i = 0; i < NOMBRE_QUESTION; i++) {
            q = new Question();
            q.text = getCell(csv, i, TEXT);
            q.reponses = new String[R];
            idx = 0;
            for (int j = 1; j <= R; j++) {
                q.reponses[idx++] = getCell(csv, i, j);
            }
            q.reponse = getCell(csv, i, REPONSE);
            questions[i] = q;
        }
        return questions;
    }

    void initialiserQuestion(int nombreQuestions, Question [] questions){
        String saisie;
        String reponse="";
        int idx = hasard(NOMBRE_QUESTION);
        boolean bonneSaisie=false;
        while(questions[idx].repondu){
            idx = hasard(NOMBRE_QUESTION);
        }
        print("Question : " + questions[idx].text + "\n");
        for(int j = 0; j < R; j++){
            print("   "+(j+1)+" : "+questions[idx].reponses[j] + "\n");
        }
        print("\n"+"Quelle est votre reponse : ");
        while(!bonneSaisie){
            saisie = readString();
            if(estCaractNum(saisie) && stringToInt(saisie) <= R && stringToInt(saisie) > 0){
                for(int i = 1; i <= R; i++){
                    if(stringToInt(saisie) == i){
                        reponse = questions[idx].reponses[i-1];
                    }
                }
            bonneSaisie = true;
            }else{
                print("Veuillez rentrer une saisie valable : ");
                bonneSaisie = false;
            }
        }
        if(equals(reponse,questions[idx].reponse)){
            score += 1;
            laReponse = true;
            questions[idx].repondu = true;
            
        }else{
            laReponse=false;
        }
    }

    //Sauvegarde le nom, score et le chrono dans un csv 
    void saveJoueur(String scoreStr, Joueur joueur, double chrono, String chemin){
        CSVFile data = loadCSV(chemin,',');
        String [][] tabuleur = new String [rowCount(data)+1][3];
        String record;
        if(rowCount(data)>0){
            for(int i=0;i<=rowCount(data)-1;i++){
                for(int j=0;j<3;j++){
                    tabuleur[i][j]=getCell(data,i,j);
                }
            }
        }
        tabuleur [rowCount(data)][0] = joueur.nomPlayer;
        tabuleur [rowCount(data)][1] = ""+joueur.score;
        tabuleur [rowCount(data)][2] = ""+chrono;
        saveCSV(tabuleur,"./ressources/joueurs.csv");
    }

    //Fonction qui initialise la fin du jeux avec la sauvegarde du score
    void finDuJeux(double finTime, double debutTime, Joueur joueur){
        double chrono=(finTime-debutTime)/1000;
        String scoreStr="";
        joueur.score = score;
        println("FELICITATION " +joueur.nomPlayer+ "! Tu as fini le jeu avec "+joueur.score+" points en "+chrono+" secondes!");
        scoreStr += joueur.score;
        saveJoueur(scoreStr, joueur, chrono,"./ressources/joueurs.csv");
    }

    int hasard(int nombreQuestion) {
        return (int) (random() * nombreQuestion);
    }

    //Fonction qui détecte si le caractére est numérique
    boolean estCaractNum(String mot){
        if (equals(mot, "")) return false;
        return charAt(mot,0) >= '0' && charAt(mot,0) <= '9';
    }

    /*boolean estQuestion(Plateau p, int lig, int col){
        Case [][] tab= p.cases;
        if(tab [lig][col] == Case.QUESTIONS){
            return true;
        }else{
            return false;
        }
    } */
    
    boolean fini(int score){
        return score == 10;
    }

    void algorithm(){
        //println(initialiserPlateau("./ressources/Plateau.csv"));
        String [][] tab = tabPlateaeu("./ressources/Plateau.csv") ;
        Plateau p = newPlateau();
        String tp = "";
        Joueur joueur = new Joueur();
        tableauQuestions = chargerQuestion("./ressources/Question.csv");
        p.cases = initialiserCase();
        clearScreen();
        menuTrivial("./ressources/Trivial.csv");
        delay(2000);
        print("D'abord, quel est ton nom ?\t");
        joueur.nomPlayer = readString();
        long debutTime = getTime();
        while (!fini(score)) {
            clearScreen();
            menuTrivial("./ressources/Trivial.csv");
            afficherPlateau(tab, p.cases);
            deplacement(p.cases, tab);
        }
        long finTime = getTime();
        finDuJeux(finTime,debutTime,joueur);
    }
}