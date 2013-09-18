package ru.study.neuralnetworks.neurons;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/16/13
 * Time: 7:48 PM

 * To change this template use File | Settings | File Templates.
 */
public class WhiteNeuron extends Neuron {
    public WhiteNeuron(byte type, int requiredInputs) {
        super(type, requiredInputs);
    }

    public WhiteNeuron(byte type, int requiredInputs, String label) {
        super(type, requiredInputs, label);
    }

    @Override
    protected double processInputs(double sum) {
        return f2(sum);
    }

    private double f2(double arg) {
        if (arg < 0)
            return 0;
        else
            return arg;
    }

}
