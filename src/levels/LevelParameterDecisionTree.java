package levels;

import main.MainClass;
import utilz.Constants;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class LevelParameterDecisionTree {

    private final Classifier decisionTree;
    private Instances data;

    public LevelParameterDecisionTree() {
        decisionTree = new J48();
    }

    public void train() throws Exception {
        String data_path = "/training_data/";
        switch (Constants.getDIFFICULTY()){
            case "Easy" -> data_path += "data_easy.csv";
            case "Hard" -> data_path += "data_hard.csv";
            case "Medium" -> data_path += "data_medium.csv";
        }

        CSVLoader loader = new CSVLoader();
        loader.setSource(MainClass.class.getResourceAsStream(data_path));
        data = loader.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);

        decisionTree.buildClassifier(data);
    }

    public int[] decideLevelParameters(int deaths, int completionTime, int[] parameters) throws Exception {
        Instance newInstance = new DenseInstance(3);
        newInstance.setValue(0, deaths);
        newInstance.setValue(1, completionTime);

        newInstance.setDataset(data);

        double classIndex = decisionTree.classifyInstance(newInstance);
        String classValue = data.attribute(data.numAttributes() - 1).value((int) classIndex);

        System.out.println(Constants.getDIFFICULTY() + ", " + deaths + ", " + completionTime + ", " + classValue);

        // 0-width, 1-enemy, 2-potion, 5-box, 6-spikes
        switch (classValue) {
            case "lower" -> {
                switch (Constants.getDIFFICULTY()) {
                    case "Easy" -> {
                        if (parameters[1] - 2 <= 0)
                            parameters[1] = 0;
                        else parameters[1] -= 2;

                        parameters[2]++;

                        if (parameters[6] - 2 <= 0)
                            parameters[6] = 0;
                        else parameters[6] -= 2;
                    }
                    case "Medium" -> {
                        if (parameters[1] - 1 <= 0)
                            parameters[1] = 0;
                        else parameters[1]--;

                        parameters[2]++;

                        if (parameters[6] - 1 <= 0)
                            parameters[6] = 0;
                        else parameters[6]--;
                    }
                    case "Hard" -> {
                        if (parameters[1] - 1 <= 0)
                            parameters[1] = 0;
                        else parameters[1]--;

                        parameters[5]++;
                    }
                }
            }
            case "equal" -> {
                switch (Constants.getDIFFICULTY()) {
                    case "Easy" -> parameters[0]++;
                    case "Medium" -> parameters[1]++;
                    case "Hard" -> {
                        parameters[0] += 5;
                        parameters[1] += 2;
                        parameters[6] += 3;
                    }
                }
            }
            case "higher" -> {
                switch (Constants.getDIFFICULTY()) {
                    case "Easy" -> {
                        parameters[0] += 4;
                        parameters[1]++;
                        parameters[6]++;
                    }
                    case "Medium" -> {
                        parameters[0] += 3;
                        parameters[1]++;

                        if (parameters[2] - 1 <= 0)
                            parameters[2] = 0;
                        else parameters[1]--;

                        parameters[6] += 2;
                    }
                    case "Hard" -> {
                        parameters[0] += 5;
                        parameters[1] += 2;
                        parameters[6] += 5;
                    }
                }
            }
        }
        return parameters;
    }
}
