package ru.study.core.tests;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import ru.study.core.entity.ComporatorInfo;
import ru.study.core.managers.ComporatorNetworkBuilder;
import ru.study.core.managers.NeuroNetworkSolver;
import ru.study.core.entity.NeuronEdge;
import ru.study.core.neurons.Neuron;

import java.util.Arrays;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/16/13
 * Time: 11:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) throws Exception {

        for (int i = 2; i < 10000; i++) {
            double[] array = fillRandom(i);
            try {

                ComporatorNetworkBuilder builder = new ComporatorNetworkBuilder();
                ComporatorInfo ci = builder.buildFromDoubleArray(array);
                DirectedSparseGraph<Neuron, NeuronEdge> g = builder.getNetwork();
                new NeuroNetworkSolver(g).solve();
                if (ci.getMax().getOut() == max(array)) {
                    System.out.println(i + "ok");
                } else {
                    throw new Exception("ebota");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("error array:" + Arrays.toString(array));
                System.exit(0);
            }
        }

    }

    private static double[] fillRandom(int i) {
        double[] arr = new double[i];
        Random randomGenerator = new Random();
        for (int q = 0; q < arr.length; q++) {
            arr[q] = randomGenerator.nextInt(100);
        }
        return arr;
    }

    private static double max(double[] values) {
        double max = values[0];
        for (int ktr = 0; ktr < values.length; ktr++) {
            if (values[ktr] > max) {
                max = values[ktr];
            }
        }
        return max;
    }
}
