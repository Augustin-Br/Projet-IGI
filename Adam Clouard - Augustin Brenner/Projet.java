import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Projet {

    /// EXERCICE 1 ///

    /**
     * Calcule le nombre de pucerons mangés par une coccinelle
     * @param G (int[][]) : la grille de pucerons
     * @param d (int) : la colonne de départ
     * @return : le nombre de pucerons qu’une coccinelle ayant atterri sur la case (0, d) mangera sur son chemin glouton
     */
    public int glouton(int[][] G, int d) {
        int L = G.length; // Nombre de lignes
        int C = G[0].length; // Nombre de colonnes
        int puceronsManges = 0; // Nombre de pucerons mangés

        int i = L - 1; // La coccinelle commence sur la dernière ligne
        int j = d; // Colonne de départ

        while (i > 0) { // Tant qu'on n'est pas sur la première ligne
            puceronsManges += G[i][j]; // Mange les pucerons sur la ligne actuelle

            // Trouver la colonne avec le plus de pucerons parmi les 3 colonnes au-dessus
            int maxPucerons = -1; // Nombre de pucerons de la colonne avec le plus de pucerons
            int maxJ = j; // Colonne avec le plus de pucerons
            for (int k = -1; k <= 1; k++) { // Pour chaque colonne au-dessus
                int newJ = j + k;
                if (newJ >= 0 && newJ < C) { // Si la colonne est dans le tableau
                    if (G[i - 1][newJ] > maxPucerons) { // Si la colonne a plus de pucerons que la colonne avec le plus de pucerons (maxPucerons)
                        maxPucerons = G[i - 1][newJ]; // La colonne avec le plus de pucerons est la colonne actuelle
                        maxJ = newJ;
                    }
                }
            }

            // Déplacement
            i--; // Aller sur la ligne au-dessus
            j = maxJ; // Aller sur la colonne avec le plus de pucerons
        }

        // Ajouter les pucerons de la première ligne (ligne 0)
        puceronsManges += G[0][j]; // Mange les pucerons sur la ligne actuelle

        return puceronsManges;
    }


    /// EXERCICE 2 ///

    /**
     * Calcule le nombre de pucerons mangés par une coccinelle
     * @param G (int[][]) : la grille de pucerons
     * @return (int[]) : le nombre de pucerons qu’une coccinelle mangera sur son chemin glouton
     */
    public int[] glouton(int[][] G) {
        int C = G[0].length; // Nombre de colonnes
        int[] Ng = new int[C]; // Tableau pour stocker le nombre de pucerons mangés pour chaque colonne

        for (int d = 0; d < C; d++) { // Pour chaque colonne d
            Ng[d] = glouton(G, d); // Appeler la fonction glouton pour chaque colonne d
        }

        return Ng;
    }

    /// EXERCICE 4 ///

    /**
     * Calcule les tableaux M et A. Le tableau M[0:L][0:C] de terme genéral M[l][c] = m(l,c) et le tableau A[0:L][0:C] dont le terme genéral A[l][c] = a(l,c) est l’indice de la colonne qui precède la case (l,c) sur le chemin maximum.
     * @param G (int[][]) : la grille de pucerons
     * @param d (int) : la colonne de départ
     * @return (int[][][]) : un tableau 3D contenant les tableaux M et A
     */
    public static int[][][] calculerMA(int[][] G, int d) {
        int L = G.length; // Nombre de lignes
        int C = G[0].length; // Nombre de colonnes
        int[][] M = new int[L][C]; // Tableau pour stocker les valeurs de m
        int[][] A = new int[L][C]; // Tableau pour stocker les indices des colonnes précédentes


        // Pour le tableau M

        // Initialiser les valeurs de la dernière ligne
        for (int c = 0; c < C; c++) { // Pour chaque colonne c
            if (c == d) { // Si la colonne est la colonne de départ
                M[L - 1][c] = G[L - 1][c]; // Mettre la valeur de la case
            } else { // Si la colonne n'est pas la colonne de départ
                M[L - 1][c] = -1; // Mettre à -1 pour indiquer que la case n'est pas accessible
            }
        }

        // Calculer les valeurs de M en suivant l'équation de récurrence
        for (int l = L - 2; l >= 0; l--) { // Pour chaque ligne l en partant de l'avant-dernière ligne
            for (int c = 0; c < C; c++) { // Pour chaque colonne c
                if (c < d - (L - 1 - l) || c > d + (L - 1 - l)) { // Si la colonne n'est pas accessible
                    M[l][c] = -1; // Mettre à -1 pour indiquer que la case n'est pas accessible
                } else { // Si la colonne est accessible
                    // Calculer le maximum des valeurs suivantes selon l'équation de récurrence
                    int maxSuivant = M[l + 1][c];
                    int indiceMax = c; // Indice de la colonne avec le maximum
                    if (c > 0 && M[l + 1][c - 1] > maxSuivant) { // Si la colonne à gauche est accessible et a une valeur plus grande que le maximum
                        maxSuivant = M[l + 1][c - 1]; // Mettre à jour le maximum
                        indiceMax = c - 1; // Mettre à jour l'indice de la colonne avec le maximum
                    }
                    if (c < C - 1 && M[l + 1][c + 1] > maxSuivant) { // Si la colonne à droite est accessible et a une valeur plus grande que le maximum
                        maxSuivant = M[l + 1][c + 1]; // Mettre à jour le maximum
                        indiceMax = c + 1; // Mettre à jour l'indice de la colonne avec le maximum
                    }
                    // Mettre à jour les valeurs de M
                    M[l][c] = G[l][c] + maxSuivant; // Mettre à jour la valeur de M

                }
            }

        }

        // Pour le tableau A

        int max = M[0][0]; // Maximum de la première ligne
        int indice = 0; // Indice de la colonne avec le maximum dans la première ligne

        // Trouver la colonne avec le maximum dans la première ligne
        for (int k = 0; k < C; k++) {
            if (M[0][k] > max) { // Si la colonne a une valeur plus grande que le maximum
                max = M[0][k];
                indice = k;
            }
        }

        A[0][indice] = 1; // Mettre à 1 la case avec le maximum dans la première ligne

        // Construire le chemin optimal depuis la deuxième ligne jusqu'à la dernière
        for (int t = 1; t < L; t++) {
            max = -1;

            // Recherche de la colonne avec le maximum parmi les colonnes adjacentes
            for (int y = -1; y <= 1; y++) { // Pour chaque colonne adjacente
                int newIndice = indice + y; // Indice de la colonne adjacente
                if (newIndice >= 0 && newIndice < C && M[t][newIndice] > max) { // Si la colonne adjacente est dans le tableau et a une valeur plus grande que le maximum
                    max = M[t][newIndice]; // Mettre à jour le maximum
                    indice = newIndice; // Mettre à jour l'indice de la colonne avec le maximum
                }
            }

            A[t][indice] = 1; // Mettre à 1 la case avec le maximum dans la ligne t
        }
        // Retourner les tableaux M et A dans un tableau 3D
        return new int[][][]{M, A};

    }

    /// EXERCICE 5 ///

    /**
     * Affiche un chemin maximum
     * @param A (int[][]) : le tableau A
     */
    public void acnpm(int[][] A, int l, int c) {
        if (l >= 0 && c >= 0) { // Si la case existe
            if(A[l][c] == 1){ // Si la case est sur le chemin optimal
                System.out.print("(" + (A.length - 1 - l) + ", " + (c) + ")"); // Afficher la case
            }
            if (A[l][c] == 1) { // Si la case est sur le chemin optimal
                acnpm(A, l - 1, c); // Appel récursif sur la case du dessus
            } else if (c > 0 && A[l][c - 1] == 1) { // Si la case précédente est à gauche
                acnpm(A, l, c - 1); // Appel récursif sur la case de gauche
            } else if (c < A[0].length - 1 && A[l][c + 1] == 1) { // Si la case précédente est à droite
                acnpm(A, l, c + 1); // Appel récursif sur la case de droite
            }
        }
    }

    /// EXERCICE 6 ///

    /**
     * Fonction qui retourne le nombre de pucerons qu’une coccinelle ayant atterri sur la case (0, d) mangera sur le chemin a nombre de pucerons maximum
     * @param G (int[][]) : la grille de pucerons
     * @param d (int) : la colonne de départ
     * @return (int) : le nombre de pucerons mangés
     */
    public int optimal(int[][] G, int d) {
        int[][][] MA = calculerMA(G, d); // Calculer les tableaux M et A
        int[][] M = MA[0]; // Tableau des valeurs de m
        int[][] A = MA[1]; // Tableau des indices des colonnes précédentes

        // Trouver la colonne avec le maximum dans la première ligne
        int max = M[0][0];
        int indiceMax = 0;

        for (int k = 1; k < M[0].length; k++) {
            if (M[0][k] > max) {
                max = M[0][k];
                indiceMax = k;
            }
        }
        return max;
    }

    /// EXERCICE 7 ///

    /**
     * Fonction qui retourne le tableau Nmax[0 : C] de terme genéral Nmax[d] = nmax (d), nombre de pucerons que la coccinelle qui a atteri sur case (0,d) mangera sur le chemin a nombre de pucerons maximum
     * @param G (int[][]) : la grille de pucerons
     * @return (int[]) : le nombre de pucerons mangés
     */
    public int[] optimal(int[][] G) {
        int C = G[0].length; // Nombre de colonnes
        int[] Nmax = new int[C]; // Tableau pour stocker le nombre de pucerons maximum pour chaque colonne

        for (int d = 0; d < C; d++) { // Pour chaque colonne d
            Nmax[d] = optimal(G, d); // Appeler la fonction optimal pour chaque colonne d
        }

        return Nmax;
    }

    /// EXERCICE 8 ///

    /**
     * Fonction qui retourne un tableau float Gain[0:C] contenant, pour toute case (0, d) de depart, le gain relatif de la stratégie optimale sur la strategie gloutonne.
     * @param Nmax (int[]) : le tableau Nmax
     * @param Ng (int[]) : le tableau Ng
     * @return (float[]) : le gain relatif
     */
    public float[] gainRelatif(int[] Nmax, int[] Ng) {
        int C = Nmax.length; // Nombre de colonnes
        float[] Gain = new float[C]; // Tableau pour stocker le gain relatif pour chaque colonne

        for (int d = 0; d < C; d++) { // Pour chaque colonne d
            if (Ng[d] != 0) { // Si le nombre de pucerons mangés par la stratégie gloutonne est différent de zéro
                Gain[d] = (float) (Nmax[d] - Ng[d]) / Ng[d]; // Calculer le gain relatif
            } else {
                // En cas de division par zéro, définir le gain relatif à zéro
                Gain[d] = 0;
            }
        }

        return Gain;
    }


    /// EXERCICE 9 ///

    /**
     * fonction qui calcule une permutation aleatoire des valeurs d’un tableau.
     * @param T (int[]) : le tableau
     * @return (int[]) : le tableau avec les valeurs permutées
     */
    static int[] permutationAleatoire(int[] T){ int n = T.length;
    // Calcule dans T une permutation aléatoire de T et retourne T
        Random rand = new Random(); // bibliothèque java.util.Random
        for (int i = n; i > 0; i--){
            int r = rand.nextInt(i); // r est au hasard dans [0:i]
            permuter(T,r,i-1);
        }
        return T;
    }

    /**
     * Fonction qui permute les valeurs de deux cases d’un tableau
     * @param T (int[]) : le tableau
     * @param i (int) : l'indice de la première case
     * @param j (int) : l'indice de la deuxième case
     */
    static void permuter(int[] T, int i, int j){
        int ti = T[i];
        T[i] = T[j];
        T[j] = ti;
    }


    public static void main(String[] args) {
        // tableau de l'énoncé
        int[][] G = {
                {1, 1, 10, 1,1},
                {6, 5, 1, 2, 8},
                {2, 2, 3, 4, 2}
        };

        // tableau de la question 8
        int[][] G1 = {
                {3,1,2,4,5},
                {1,72,3,6,6},
                {89,27,10,12,3},
                {46,2,8,7,15},
                {36,34,1,13,30},
                {2,4,11,26,66},
                {1,10,15,1,2},
                {2,4,3,9,6}
        };

        Projet coccinelle = new Projet();

        // Afficher la grille G
        System.out.println("Grille G:");
        int vG = 0;
        for (int i = G.length - 1; i >= 0; i--) {
            System.out.println("G[" + i + "] : " + Arrays.toString(G[vG]));
            vG++;
        }



        // Calculer et afficher les valeurs des chemins gloutons depuis les cases (0,d)
        int[] Ng = coccinelle.glouton(G);
        System.out.println("Valeurs des chemins gloutons depuis les cases (0, d) : Ng = " + Arrays.toString(Ng) + "\n");

        // Calculer les tableaux M et A
        int d = 0; // Colonne de départ
        int[][][] MA = Projet.calculerMA(G, d); // Calculer les tableaux M et A
        System.out.println("Programmation dynamique, case de départ, (0, " + d + ")");
        System.out.println("Grille M : ");
        afficherGrille(MA[0]); // Afficher le tableau M correspondant au nombre de pucerons manger par la coccinelle sur chaque case
        //System.out.println("Grille A : ");
        //afficherGrille(MA[1]);
        System.out.print("un chemin maximum : ");
        coccinelle.acnpm(MA[1], MA[1].length - 1, d); // Afficher un chemin maximum
        System.out.println(" Valeur : " + valeurMax(MA) + "\n");

        //System.out.println("un chemin maximum : " + afficherChemin(MA) + " Valeur : " + valeurMax(MA) + "\n");

        d = 1;
        int[][][] MA2 = Projet.calculerMA(G, d);
        System.out.println("Programmation dynamique, case de départ, (0, " + d + ")");
        System.out.println("Grille M : ");
        afficherGrille(MA2[0]);
        //System.out.println("Grille A : ");
        //afficherGrille(MA2[1]);

        System.out.print("un chemin maximum : ");
        coccinelle.acnpm(MA2[1], MA2[1].length - 1, d);
        System.out.println(" Valeur : " + valeurMax(MA2) + "\n");

        //System.out.println("un chemin maximum : " + afficherChemin(MA2) + " Valeur : " + valeurMax(MA2) + "\n");

        // Exercice 5 //

        for(int k = 0; k <= G[0].length -1; k++){ // Pour chaque colonne k
            int[][][] MA3 = Projet.calculerMA(G, k); // Calculer les tableaux M et A
            System.out.print("un chemin maximum : ");
            coccinelle.acnpm(MA3[1], MA3[1].length - 1, k); // Afficher un chemin maximum
            System.out.println(" Valeur : " + valeurMax(MA3));
        }

        // Exercice 6 + 7 //

        System.out.println();

        System.out.println("Ng = " + Arrays.toString(Ng)); // Afficher le tableau Ng
        System.out.println("Nmax = " + Arrays.toString(coccinelle.optimal(G))); // Afficher le tableau Nmax

        // Exercice 8 //

        float[] resultGainRelatif = coccinelle.gainRelatif(coccinelle.optimal(G), Ng); // Calculer le gain relatif
        System.out.println("Gain relatifs = " + Arrays.toString(resultGainRelatif)); // Afficher le gain relatif

        System.out.println("\nTAB. 1 (Exercice 8): ");

        int[] Ng1 = coccinelle.glouton(G1); // Calculer le tableau Ng
        float[] resultGainRelatifG1 = coccinelle.gainRelatif(coccinelle.optimal(G1), Ng1); // Calculer le gain relatif
        System.out.println("Ng = " + Arrays.toString(Ng1));
        System.out.println("Gain relatifs = " + Arrays.toString(resultGainRelatifG1));

        // Exercice 9 //

        System.out.println("\nVALIDATION STATISTIQUE \n");

        int run = 10000; // Nombre de fois que l'on va générer une grille aléatoire et faire les calculs

        System.out.println("nruns = " + run);
        System.out.println("L au hasard dans [ 5, 16 ]");
        System.out.println("C au hasard dans [ 5, 16 ] \n");

        int[][] G2 = new int[0][0];
        int[] Ng2 = new int[0];
        float[] resultGainRelatifG2 = new float[0];
        double[][] gain = new double[run][];
        int[][] G3 = genererGrilleAleatoire();
        //System.out.println("Grille G3 : ");
        //afficherGrille(G3);

        int totalGainLength = 0;
        for(int i = 0; i < run; i++){ // Pour chaque run
            G2 = genererGrilleAleatoire(); // Générer une grille aléatoire
            int L = G2.length; // Nombre de lignes
            int C = G2[0].length; // Nombre de colonnes
            System.out.println("run " + (i+1) + "/" + run + ", (L,C) = (" + L + "," + C + ")");
            Ng2 = coccinelle.glouton(G2); // Calculer le tableau Ng
            resultGainRelatifG2 = coccinelle.gainRelatif(coccinelle.optimal(G2), Ng2); // Calculer le gain relatif
            gain[i] = new double[resultGainRelatifG2.length]; // Créer un tableau pour stocker les gains relatifs
            for(int j = 0; j < resultGainRelatifG2.length; j++){ // Pour chaque colonne j
                gain[i][j] = resultGainRelatifG2[j]; // Stocker le gain relatif dans le tableau
            }
            totalGainLength += resultGainRelatifG2.length; // Mettre à jour la longueur totale du tableau des gains relatifs
        }

        System.out.print("GAINS.length = " + totalGainLength + ", ");
        System.out.print("min = " + min(gain) + ", ");
        System.out.print("max = " + max(gain) + ", ");
        System.out.print("mean = " + mean(gain) + ", ");
        System.out.println("med = " + med(gain));

        writeCSV(gain, "gainsRelatifs_nruns=10000_Linf=5_Lsup=16_Cinf=5_Csup=16.csv");

    }

    /**
     * Fonction pour écrire dans un fichier CSV
     * @param data (double[][]) : le tableau de données
     * @param fileName (String) : le nom du fichier
     */
    public static void writeCSV(double[][] data, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) { // Création du fichier CSV
            writer.write(""); // Écriture d'une chaîne vide pour créer le fichier
            for (int i = 0; i < data.length; i++) { // Pour chaque ligne
                for (int j = 0; j < data[i].length; j++) { // Pour chaque colonne
                    writer.write(String.valueOf(data[i][j])); // Écrire la valeur
                    if (j < data[i].length - 1) { // Si ce n'est pas la dernière colonne
                        writer.write(",");
                    }
                }
                writer.newLine(); // Aller à la ligne
            }
            System.out.println("\nLes gains relatifs sont dans le fichier " + fileName);
        } catch (IOException e) { // En cas d'erreur
            e.printStackTrace();
        }
    }

    /**
     * Affiche un tableau 2D
     * @param G
     */
    public static void afficherGrille(final int[][] G) {
        int vG = 0;
        for (int i = G.length - 1; i >= 0; i--) { // Pour chaque ligne i
            System.out.println("M[" + i + "] : " + Arrays.toString(G[vG]));
            vG++;
        }
    }

    /**
     * Affiche la valeaur maximal du chemin
     * @param MA (int[][][]) : le tableau 3D
     * @return (int) : la valeur maximale du chemin
     */
    public static int valeurMax(final int[][][] MA){
        int[][] M = MA[0];
        int[][] A = MA[1];
        int max = M[0][0];
        int i = 0;
        while (A[0][i] != 1){
            i++;
        }
        max = M[0][i];
        return max;
    }

    /**
     * Genère une grille aléatoire avec entre 5 et 16 lignes et entre 5 et 16 colonnes
     * @return (int[][]) : la grille aléatoire
     */
    public static int[][] genererGrilleAleatoire() {
        Random random = new Random();
        int c = random.nextInt(5,16); // Nombre de colonnes aléatoire entre 5 et 16
        int l = random.nextInt(5,16);
        int[][] G = new int[l][c];
        // Remplissage de la grille G avec des valeurs aléatoires
        for(int i = 0;i < c;i++){
            for(int j = 0; j < l;j++){
                G[j][i] = random.nextInt(0,l*c);
            }
        }
        // Permutation aléatoire des éléments dans la grille
        int[] elements = new int[l * c]; // Tableau 1D pour stocker les éléments de la grille
        int k = 0;
        // Extraction des éléments de la grille dans un tableau 1D
        for (int i = 0; i < c; i++) {
            for (int j = 0; j < l; j++) {
                elements[k++] = G[j][i];
            }
        }
        // Permutation aléatoire des éléments
        Projet.permutationAleatoire(elements);
        // Remplissage de la grille avec les éléments permutés
        k = 0;
        for (int i = 0; i < c; i++) {
            for (int j = 0; j < l; j++) {
                G[j][i] = elements[k++];
            }
        }
        return G;
    }

    /**
     * Fonction qui retourne le minimum d'un tableau 2D
     * @param pTab (double[][]) : le tableau 2D
     * @return (double) : le minimum
     */
    public static double min(double[][] pTab){
        double min = pTab[0][0];
        for(int i = 0; i < pTab.length; i++){
            for(int j = 0; j < pTab[i].length; j++){
                if(pTab[i][j] < min){
                    min = pTab[i][j];
                }
            }
        }
        return min;
    }

    /**
     * Fonction qui retourne le maximum d'un tableau 2D
     * @param pTab (double[][]) : le tableau 2D
     * @return (double) : le maximum
     */
    public static double max(double[][] pTab){
        double max = pTab[0][0];
        for(int i = 0; i < pTab.length; i++){
            for(int j = 0; j < pTab[i].length; j++){
                if(pTab[i][j] > max){
                    max = pTab[i][j];
                }
            }
        }
        return max;
    }

    /**
     * Fonction qui retourne la moyenne d'un tableau 2D
     * @param pTab (double[][]) : le tableau 2D
     * @return (double) : la moyenne
     */
    public static double mean(double[][] pTab){
        double mean = 0;
        for(int i = 0; i < pTab.length; i++){
            for(int j = 0; j < pTab[i].length; j++){
                mean += pTab[i][j];
            }
        }
        mean = mean / (pTab.length * pTab[0].length);
        return mean;
    }

    /**
     * Fonction qui retourne la médiane d'un tableau 2D
     * @param pTab (double[][]) : le tableau 2D
     * @return (double) : la médiane
     */
    public static double med(double[][] pTab) {
        int n = pTab.length * pTab[0].length;  // Nombre d'éléments dans le tableau
        List<Double> values = new ArrayList<>(); // Créer une liste pour stocker les valeurs du tableau

        for (int i = 0; i < pTab.length; i++) {
            for (int j = 0; j < pTab[i].length; j++) {
                values.add(pTab[i][j]); // Ajouter les valeurs du tableau dans la liste
            }
        }

        double[] sortedValues = values.stream().mapToDouble(Double::doubleValue).sorted().toArray(); // Convertir la liste en tableau et trier les valeurs

        if (n % 2 == 0) { // Si le nombre d'éléments est pair
            return (sortedValues[n / 2] + sortedValues[(n / 2) - 1]) / 2; // Retourner la moyenne des deux valeurs du milieu
        } else {
            return sortedValues[(n - 1) / 2]; // Retourner la valeur du milieu
        }
    }
}
