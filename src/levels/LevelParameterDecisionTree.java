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

        switch (classValue) {
            case "lower" -> {
                System.out.println("ez");
                if (parameters[0] - 2 <= 26)
                    parameters[0] = 26;
                else parameters[0] -= 2;
                parameters[5]++;
                if (parameters[6] - 1 < 0)
                    parameters[6] = 0;
                else parameters[6]--;
            }
            case "equal" -> {
                System.out.println("med");
                parameters[1]++;
            }
            case "higher" -> {
                System.out.println("hard");
                parameters[0] += 5;
                parameters[1]++;
                if (parameters[5] - 1 < 0)
                    parameters[5] = 0;
                else parameters[5]--;
                parameters[6] += 3;
            }
        }
        return parameters;
    }
}
