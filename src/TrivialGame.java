import extensions.File;
import extensions.CSVFile;


class TrivialGame extends Program {
    final int IDX_CASE_VERTICALE = 9;
    final int IDX_CASE_HORIZONTAL = 16;
    final int THEME=0, TEXT=1, R=5,REPONSE=6,REPONDU=7;
    final double PROBA_QUESTIONS = 0.25;
    final int NOMBRE_QUESTION= 32;
    int scoreHistoire = 0;
    int scoreGeographie = 0;
    int scoreScience = 0;
    int scoreAnnimaux = 0;
    int scoreFrançais = 0;
    int scoreCultureG = 0;
    int scoreAnglais = 0;
    int scoreJeuxVideo = 0;
    int scoreNourriture = 0;
    boolean laReponse = false;
    Question [] tableauQuestions;
    int nbTour = 0;
    boolean jeuFini = false;
    boolean taperFin = false;

    //récupere le fichier Plateau.csv dans ressources puis le renvoie sous forme de chaine de caractére
    String initialiserPlateau(String chemin){
        String tp = "";
        CSVFile csv = loadCSV(chemin,';');
        for(int i = 0; i < 30; i++){
            tp += getCell(csv,i,0) + "\n";
            print(tp);
        }
        return tp;
    }

    //récupére le fichier Trivial.csv dans ressources puis le renvoie sous forme de chaine de caractére 
    void menuTrivial(String chemin){
        String tp = "";
        CSVFile csv = loadCSV(chemin, ';');
        for(int i = 0; i < 7; i ++){
            tp = getCell(csv, i, 0) + "\n";
            print(tp);
        }
    }

    //renvoie les cases du plateau depuis un fichier csv
    String [][] tabPlateau(String chemin){
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

    //affiche le plateau 
    void afficherPlateau(String [][] tab, Case [][] cases){
        int idx = 1;
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
    }

    //initilise les cases du plateau 
    Case [][] initialiserCase(){
        Plateau plt = newPlateau();
        for(int i = 0; i < length(plt.cases,1);i++){
            for(int j = 0; j < length(plt.cases,2);j++){
                if(i == 0 && j > 0 && j < 15 || i == 8 && j > 0 && j < 15){
                    plt.cases[i][j] = Case.QUESTIONS;
                }else if(j == 0 || j == 14 ){
                    plt.cases[i][j] = Case.QUESTIONS;
                }else if(i == length(plt.cases,1)/2 && j < 15){
                    plt.cases[i][j] = Case.QUESTIONS;
                    if(j== length(plt.cases,2)/2-1){
                        plt.cases[i][j] = Case.FIN;
                    }
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

    //Prend en paramétre une case et le renvoie sous une forme de chaine de caractére
    String toString(Case c){
        if(c == Case.QUESTIONS){
            return "?";
        }else if(c == Case.JOUEUR){
            return "\u001B[31m"+"J"+"\u001B[37m";
        }else if(c == Case.FIN){
            return "S";
        }else{
            return " ";
        }
    }
    
    //Permet le déplacement grâce à idxQuestion et idxJoueur
    void deplacement(Case [][] cases, String [][] tab){
        String tp = "";
        boolean lancerb = false;
        while(!lancerb && !taperFin){
            println("Appuyer sur entré pour lancer le dé ou écrivez score pour voir votre score");
            println("Écrivez fin pour finir la partie");
            tp = readString();
            if(equals(tp,"score")){
                println("Votre score : \nscore Histoire "+scoreHistoire + "\n" + "score Géographie " + scoreGeographie +"\n" + "score Science " + scoreScience +"\n" + "score Animaux " + scoreAnnimaux +"\n" + "score Francais " + scoreFrançais +"\n" + "score Culture G " + scoreCultureG +"\n" + "score Anglais " + scoreAnglais+"\n"+ "score Jeux Vidéo " + scoreJeuxVideo +"\n"+ "score Nourriture " + scoreNourriture);
            }else if(equals(tp,"fin")){
                taperFin = true;
            }else if(equals(tp,"")){
                lancerb = true;
                int lancer = des(6);
                println("Vous avez fait "+lancer +" !");
                delay(2000);
                Case caseTP;
                int [] idxJoueur = idxJoueur(cases);
                int [] caseSuivante = idxQuestion(cases,idxJoueur);
                int idx;
                for(int i = 0; i < lancer; i++){
                    idxJoueur = idxJoueur(cases);
                    caseSuivante = idxQuestion(cases, idxJoueur);
                    caseTP = cases[idxJoueur[0]][idxJoueur[1]];
                    if(idxJoueur[0] == 4 && idxJoueur[1] < 7){
                        if(cases[idxJoueur[0]][idxJoueur[1]+1] == Case.FIN){
                            jeuFini = true;
                            i = lancer;
                        }
                    }else if(idxJoueur[0] == 4 && idxJoueur[1] > 7){
                        if(cases[idxJoueur[0]][idxJoueur[1]-1] == Case.FIN){
                            jeuFini = true;
                            i = lancer;
                        }
                    }
                    if(cases[idxJoueur[0]][idxJoueur[1]+1] == Case.FIN){
                        jeuFini = true;
                        i = lancer;
                    }
                    cases[idxJoueur[0]][idxJoueur[1]] = cases[caseSuivante[0]][caseSuivante[1]];
                    cases[caseSuivante[0]][caseSuivante[1]] = caseTP;
                    if(jeuFini){
                        cases[idxJoueur[0]][idxJoueur[1]] = Case.QUESTIONS;
                    }
                    afficherPlateau(tab, cases);
                    println();
                    delay(800);
                }
                if(!jeuFini){
                    initialiserQuestion(NOMBRE_QUESTION, tableauQuestions);
                }
                if(!laReponse && !jeuFini){
                    println("Mauvaise réponse"); 
                }else if(laReponse && !jeuFini){
                    println("Bonne réponse");
                }
                delay(3000);
            }else{
                println("Veuillez rentrer une réponse valide");
            }
        }
    }

    //Déplace la case Joueur dans le plateau
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
        }else if(idxJoueur[1] == 0 || idxJoueur[1] >  0 && idxJoueur[1] < 7 && idxJoueur[0] == 4){
            if(idxJoueur[0] == 4 && fini() && idxJoueur[1] < 7){
                idx[0] = idxJoueur[0];
                idx[1] = idxJoueur[1]+1;
            }else{
                idx[0] = idxJoueur[0]-1;
                idx[1] = idxJoueur[1]; 
            }
        }else if(idxJoueur[1]+1 == 15 || idxJoueur[1] > 7 && idxJoueur[1] < 14 && idxJoueur[0] == 4){
            if(idxJoueur[0] == 4 && fini()){
                idx[0] = idxJoueur[0];
                idx[1] = idxJoueur[1]-1; 
            }else{
                idx[0] = idxJoueur[0]+1;
                idx[1] = idxJoueur[1];
            }
        }
        return idx;
    }

    //permet de trouver l'emplacement du Joueur dans le plateau
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

    //Créer un nouveau Plateau
    Plateau newPlateau(){
        Plateau t = new Plateau();
        t.cases = new Case [IDX_CASE_VERTICALE][IDX_CASE_HORIZONTAL];
        return t;
    }

    //Génére un dés aléatoire
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
            q.theme = getCell(csv,i,THEME);
            q.text = getCell(csv, i, TEXT);
            q.reponses = new String[R];
            idx = 0;
            for (int j = 2; j <= R; j++) {
                q.reponses[idx++] = getCell(csv, i, j);
            }
            q.reponse = getCell(csv, i, REPONSE);
            questions[i] = q;
        }
        return questions;
    }

    //Génére une question pour permettre au Joueur de répondre
    void initialiserQuestion(int nombreQuestions, Question [] questions){
        String reset = "\u001B[0m";
        String saisie;
        String reponse="";
        int idx = hasard(NOMBRE_QUESTION);
        boolean bonneSaisie=false;
        while(questions[idx].repondu){
            idx = hasard(NOMBRE_QUESTION);
        }
        print(questionsTheme(questions[idx].theme)+"Question : " + questions[idx].theme + questions[idx].text + reset + "\n");
        for(int j = 0; j < R-1; j++){
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
            if(equals(questions[idx].theme,"Histoire")){
                scoreHistoire++;
            }else if(equals(questions[idx].theme,"Science")){
                scoreScience++;
            }else if(equals(questions[idx].theme,"Animaux")){
                scoreAnnimaux++;
            }else if(equals(questions[idx].theme,"Français")){
                scoreFrançais++;
            }else if(equals(questions[idx].theme,"Culture G")){
                scoreCultureG++;
            }else if(equals(questions[idx].theme,"Anglais")){
                scoreAnglais++;
            }else if(equals(questions[idx].theme,"Jeux Vidéo")){
                scoreJeuxVideo++;
            }else if(equals(questions[idx].theme,"Nourriture")){
                scoreNourriture++;
            }else if(equals(questions[idx].theme,"Géographie")){
                scoreGeographie++;
            }
            laReponse = true;
            questions[idx].repondu = true;
        }else{
            laReponse=false;
        }
    }

    //Permet d'avoir une couleur pour chaque théme
    String questionsTheme(String theme){
        if(equals(theme,"Histoire")){
            return "\u001B[31m";
        }else if(equals(theme,"Géographie")){
            return "\u001B[33m";
        }else if(equals(theme,"Science")){
            return "\u001B[36m";
        }else if(equals(theme,"Animaux")){
            return "\u001B[32m";
        }else if(equals(theme,"Français")){
            return "\u001B[34m";
        }else if(equals(theme,"Culture G")){
            return "\u001B[35m";
        }else if(equals(theme,"Anglais")){
            return "\u001B[1m";
        }else if(equals(theme,"Jeux Vidéo")){
            return "\u001B[4m";
        }else if(equals(theme,"Nourriture")){
            return "";
        }else{
            return "";
        }
    }

    //Sauvegarde le nom, score et le chrono dans un csv 
    void saveJoueur(String scoreStr, Joueur joueur, double chrono, String chemin){
        CSVFile data = loadCSV(chemin,',');
        int nbligne = rowCount(data);
        String [][] tabuleur = new String [nbligne+1][3];
        String record;
        if(rowCount(data)>0){
            for(int i=0;i<=rowCount(data)-1;i++){
                for(int j=0;j<3;j++){
                    tabuleur[i][j]=getCell(data,i,j);
                }
            }
        }
        tabuleur [rowCount(data)][0] = joueur.nomPlayer + ":";
        tabuleur [rowCount(data)][1] = " score Histoire "+scoreHistoire + " " + "score Géographie " + scoreGeographie +" " + "score Science " + scoreScience +" " + "score Animaux " + scoreAnnimaux +" " + "score Francais " + scoreFrançais +" " + "score Culture G " + scoreCultureG +" " + "score Anglais " + scoreAnglais+" "+ "score Jeux Vidéo " + scoreJeuxVideo +" "+ "score Nourriture " + scoreNourriture +" ";
        tabuleur [rowCount(data)][2] = "Chrono : "+chrono;
        saveCSV(tabuleur,"./ressources/joueurs.csv");
    }

    //Fonction qui initialise la fin du jeux avec la sauvegarde du score
    void finDuJeux(double finTime, double debutTime, Joueur joueur){
        double chrono=(finTime-debutTime)/1000;
        String scoreStr="";
        joueur.score ="score Histoire "+scoreHistoire + "\n" + "score Géographie " + scoreGeographie +"\n" + "score Science " + scoreScience +"\n" + "score Animaux " + scoreAnnimaux +"\n" + "score Francais " + scoreFrançais +"\n" + "score Culture G " + scoreCultureG +"\n" + "score Anglais " + scoreAnglais+"\n"+ "score Jeux Vidéo " + scoreJeuxVideo +"\n"+ "score Nourriture " + scoreNourriture +"\n";
        println("FELICITATION " +joueur.nomPlayer+ "! Tu as fini le jeu avec : \n"+"score Histoire "+scoreHistoire + "\n" + "score Géographie " + scoreGeographie +"\n" + "score Science " + scoreScience +"\n" + "score Animaux " + scoreAnnimaux +"\n" + "score Francais " + scoreFrançais +"\n" + "score Culture G " + scoreCultureG +"\n" + "score Anglais " + scoreAnglais+"\n"+ "score Jeux Vidéo " + scoreJeuxVideo +"\n"+ "score Nourriture " + scoreNourriture +"\n"+chrono+" secondes!");
        scoreStr += joueur.score;
        saveJoueur(scoreStr, joueur, chrono,"./ressources/joueurs.csv");
    }

    //Choisie une question au hasard
    int hasard(int nombreQuestion) {
        return (int) (random() * nombreQuestion);
    }

    //Fonction qui détecte si le caractére est numérique
    boolean estCaractNum(String mot){
        if (equals(mot, "")) return false;
        return charAt(mot,0) >= '0' && charAt(mot,0) <= '4';
    }
    
    //Si le joueur à en moins 1 point de chaque catégorie le joueur peu accéder à la sortie
    boolean fini(){
        return scoreAnglais >= 1 && scoreAnnimaux >= 1 && scoreCultureG >= 1 && scoreFrançais >= 1 && scoreGeographie >= 1 && scoreHistoire >= 1
                && scoreJeuxVideo >= 1 && scoreNourriture >= 1 && scoreScience >= 1;
    }

    void algorithm(){
        String [][] tab = tabPlateau("./ressources/Plateau.csv") ;
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
        while (!jeuFini && !taperFin) {
            clearScreen();
            menuTrivial("./ressources/Trivial.csv");
            afficherPlateau(tab, p.cases);
            deplacement(p.cases, tab);
        }
        long finTime = getTime();
        finDuJeux(finTime,debutTime,joueur);
    }


////////////////////////////////////////////////////////////////////////
//TEST//
////////////////////////////////////////////////////////////////////////

    void testIdxJoueur(){
        int [] idx = new int[]{1,2};
        //int [] idx2 = new int []{0,1};
        Case [][] cases =  {{Case.QUESTIONS, Case.QUESTIONS, Case.QUESTIONS},
                     {Case.QUESTIONS, Case.QUESTIONS, Case.JOUEUR}};
        assertArrayEquals(idx, idxJoueur(cases));
        //assertArrayNotEquals(idx2, idxJoueur(cases));
    }

    void testToStringCase(){
        Case caseJoueur = Case.JOUEUR;
        Case caseQuestion = Case.QUESTIONS;
        Case fin = Case.FIN;
        assertEquals("\u001B[31m"+"J"+"\u001B[37m",toString(caseJoueur));
        assertEquals("?",toString(caseQuestion));
        assertNotEquals("\u001B[31m"+"J"+"\u001B[37m",toString(caseQuestion));
        assertEquals("S",toString(fin));
    }

    void testFini(){
        scoreAnglais++;
        scoreAnnimaux++;
        scoreCultureG++;
        scoreFrançais++;
        scoreGeographie++;
        scoreHistoire++;
        scoreJeuxVideo++;
        scoreNourriture++;
        scoreScience++;
        assertTrue(fini());
        scoreAnglais = scoreAnglais-1;
        assertFalse(fini());
    }


    void testChargerQuestion(){
        assertEquals(length(chargerQuestion("./ressources/Question.csv")),NOMBRE_QUESTION);
    }

    void testEstCaractNum(){
        String mot;
        mot="";
        assertFalse(estCaractNum(mot));
        mot="mot";
        assertFalse(estCaractNum(mot));
        mot="125";
        assertTrue(estCaractNum(mot));
        mot="é'(-_ç)àçè-é&é&";
        assertFalse(estCaractNum(mot));
        mot="1";
        assertTrue(estCaractNum(mot));
    }

    void testQuestionTheme(){
        String theme1 = "Histoire";
        String red = "\u001B[31m";
        String yellow = "\u001B[33m";
        assertEquals(red,questionsTheme(theme1));
        assertNotEquals(yellow,questionsTheme(theme1));
    }
}