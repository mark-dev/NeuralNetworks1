package ru.study.core.neurons;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/16/13
 * Time: 7:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class BlackNeuron extends Neuron {
    public BlackNeuron(byte type, int requiredInputs) {
        super(type, requiredInputs);
    }

    public BlackNeuron(byte type, int requiredInputs, String label) {
        super(type, requiredInputs, label);
    }

    @Override
    protected double processInputs(double sum) {
        if (sum < 0.5)
            return 0;
        else
            return 1;
    }
}
