import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KNNClassificateur {
    private List<EchantillonEau> donneesEntrainement;
    private int k;

    public KNNClassificateur(int k) {
        this.k = k;
        donneesEntrainement = new ArrayList<>();
    }

    public void ajouterEchantillon(EchantillonEau echantillon) {
        donneesEntrainement.add(echantillon);
    }

    public String classer(EchantillonEau entree) {
        List<DistanceLabelPair> distances = new ArrayList<>();

        for (EchantillonEau echantillon : donneesEntrainement) {
            double distance = calculerDistance(entree, echantillon);
            distances.add(new DistanceLabelPair(distance, echantillon.getLabel()));
        }

        Collections.sort(distances);

        // Obtenir les k plus proches voisins
        List<String> voisins = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            voisins.add(distances.get(i).getLabel());
        }

        // Effectuer un vote majoritaire
        String classePredite = obtenirLaPlusCommune(voisins);
        return classePredite;
    }

    private double calculerDistance(EchantillonEau a, EchantillonEau b) {

        double distancePH = Math.abs(a.getPH() - b.getPH());
        double distanceTSS = Math.abs(a.getTSS() - b.getTSS());

        // Utilisation de la distance euclidienne
        return Math.sqrt(distancePH * distancePH + distanceTSS * distanceTSS);
    }

    private String obtenirLaPlusCommune(List<String> etiquettes) {
        int compteMax = 0;
        String plusCommune = null;

        for (String etiquette : etiquettes) {
            int compte = Collections.frequency(etiquettes, etiquette);
            if (compte > compteMax) {
                compteMax = compte;
                plusCommune = etiquette;
            }
        }

        return plusCommune;
    }

    public static void main(String[] args) {
        KNNClassificateur knn = new KNNClassificateur(3);

        // Exemple de données d'entraînement
        knn.ajouterEchantillon(new EchantillonEau(7.0, 5, "Minérale"));
        knn.ajouterEchantillon(new EchantillonEau(6.5, 2, "Rivière"));
        knn.ajouterEchantillon(new EchantillonEau(7.2, 1, "Robinet"));
        knn.ajouterEchantillon(new EchantillonEau(7.5, 4, "Minérale"));
        knn.ajouterEchantillon(new EchantillonEau(6.8, 8, "Rivière"));
        knn.ajouterEchantillon(new EchantillonEau(7.3, 3, "Robinet"));
        knn.ajouterEchantillon(new EchantillonEau(6.9, 7, "Rivière"));
        knn.ajouterEchantillon(new EchantillonEau(6.6, 6, "Rivière"));
        knn.ajouterEchantillon(new EchantillonEau(7.8, 2, "Minérale"));
        knn.ajouterEchantillon(new EchantillonEau(7.1, 3, "Robinet"));
        knn.ajouterEchantillon(new EchantillonEau(6.7, 5, "Rivière"));
        knn.ajouterEchantillon(new EchantillonEau(7.4, 6, "Minérale"));
        knn.ajouterEchantillon(new EchantillonEau(7.2, 4, "Minérale"));
        knn.ajouterEchantillon(new EchantillonEau(6.5, 10, "Rivière"));
        knn.ajouterEchantillon(new EchantillonEau(7.0, 1, "Robinet"));
        knn.ajouterEchantillon(new EchantillonEau(7.7, 4, "Minérale"));
        knn.ajouterEchantillon(new EchantillonEau(6.8, 6, "Rivière"));
        knn.ajouterEchantillon(new EchantillonEau(7.6, 3, "Minérale"));
        knn.ajouterEchantillon(new EchantillonEau(6.4, 9, "Rivière"));
        knn.ajouterEchantillon(new EchantillonEau(7.1, 2, "Robinet"));


        // Données d'entrée pour la classification
        EchantillonEau entree = new EchantillonEau(7.1, 11, "");

        // Classification
        String classePredite = knn.classer(entree);
        System.out.println("Classe Prédite de l'echantillon est : " + classePredite);
    }
}

class EchantillonEau {
    private double pH;
    private int TSS; // Concentration de matières en suspension
    private String etiquette;

    public EchantillonEau(double pH, int TSS, String etiquette) {
        this.pH = pH;
        this.TSS = TSS;
        this.etiquette = etiquette;
    }

    public double getPH() {
        return pH;
    }

    public int getTSS() {
        return TSS;
    }

    public String getLabel() {
        return etiquette;
    }
}

class DistanceLabelPair implements Comparable<DistanceLabelPair> {
    private double distance;
    private String etiquette;

    public DistanceLabelPair(double distance, String etiquette) {
        this.distance = distance;
        this.etiquette = etiquette;
    }

    public double getDistance() {
        return distance;
    }

    public String getLabel() {
        return etiquette;
    }

    @Override
    public int compareTo(DistanceLabelPair autre) {
        return Double.compare(this.distance, autre.distance);
    }
}
