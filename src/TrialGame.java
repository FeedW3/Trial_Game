import extensions.File;
import extensions.CSVFile;


class TrialGame extends Program {
    final int IDX_CASE_VERTICALE = 9;
    final int IDX_CASE_HORIZONTAL = 16;

    String initialiserPlateau(String chemin){
        String tp = "";
        CSVFile csv = loadCSV(chemin,';');
        for(int i = 0; i < 30; i++){
            tp += getCell(csv,i,0) + "\n";
        }
        return tp;
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
    
    void deplacement(Case [][] cases){
        int lancer = des(6);
        Case caseTP;
        int [] idxJoueur = idxJoueur(cases);
        int [] caseSuivante = idxQuestion(cases,idxJoueur);
        for(int i = 0; i < lancer+1; i++){
            caseSuivante = idxQuestion(cases, idxJoueur);
            caseTP = cases[idxJoueur[0]][idxJoueur[1]];
            cases[idxJoueur[0]][idxJoueur[1]] = cases[caseSuivante[0]][caseSuivante[1]];
            cases[caseSuivante[0]][caseSuivante[1]] = caseTP;
        }
    }

    int [] idxQuestion(Case [][] cases, int [] idxJoueur){
        int [] idx = new int [2];
        if(idxJoueur[0] == 0){
            if(idxJoueur[1]+1 == 16){
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
        }else if(idxJoueur[1] == 16){
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
        while(!trouve && i < length(cases,1) || j < length(cases,2)){
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
        return (int)random()*nbFace+1;
    }

    void algorithm(){
        //println(initialiserPlateau("./ressources/Plateau.csv"));
        String [][] tab = tabPlateaeu("./ressources/Plateau.csv") ;
        Plateau p = newPlateau();
        p.cases = initialiserCase();
        afficherPlateau(tab, p.cases);
        deplacement(p.cases);
        afficherPlateau(tab, p.cases);
    }
}