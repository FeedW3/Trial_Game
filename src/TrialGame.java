import extensions.File;
import extensions.CSVFile;


class TrialGame extends Program {
    final int IDX_CASE_JOUEUR = 1;
    final int IDX_CASE_QESTIONS = 0;

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
        Plateau plt = newPlateau();
        for(int i = 0; i < 30; i++){
            if(i == 2 || i == 27){
                for(int j = 0; j < 16; j ++){
                    tp[i][j] = getCell(csv, i, j);
                    if(j < 15 && j !=0){
                        tp[i][j] += toString(plt.cases[IDX_CASE_QESTIONS]);
                    } 
                    if( i == 27 && j ==0){
                        tp[i][j] += toString(plt.cases[IDX_CASE_JOUEUR]);
                    }else if(i == 2 && j == 0){
                        tp[i][j] += toString(plt.cases[IDX_CASE_QESTIONS]);
                    }  
                }
            }else if(i == 6 || i == 9 || i == 12 || i == 15 || i == 18 || i == 21 || i == 24){
                for(int j = 0; j < 3; j++){
                    tp[i][j] = getCell(csv, i, j);
                    if(j < 2){
                        tp[i][j] += toString(Case.QUESTIONS);
                    }
                }
            }else{
                for(int j = 0; j < 16; j ++){
                    if(j == 0){
                        tp[i][j] = getCell(csv, i, j);
                    }else{
                        tp[i][j] = null;
                    }
                }
            }
        }
        return tp;
    }

    String toString(Case c){
        if(c == Case.QUESTIONS){
            return "?";
        }else{
            return "J";
        }
    }

    Plateau newPlateau(){
        Plateau t = new Plateau();
        t.cases [IDX_CASE_QESTIONS] = Case.QUESTIONS;
        t.cases [IDX_CASE_JOUEUR] = Case.JOUEUR;
        return t;
    }

    void algorithm(){
        //println(initialiserPlateau("./ressources/Plateau.csv"));
        String [][] tab = tabPlateaeu("./ressources/Plateau.csv") ;
            for(int i = 0; i < length(tab,1);i++){
                for(int j = 0; j < length(tab,2); j++){
                    if(tab[i][j] != null){
                        print(tab[i][j]);
                    }
                }
                println();

            }
    }
}