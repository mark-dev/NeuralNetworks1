package ru.study.neuralnetworks.neurons;

import ru.study.neuralnetworks.entity.NeuroInput;

import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/16/13
 * Time: 6:27 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Neuron {
    public static byte NEURON_TYPE_OPENING = 1;
    public static final byte NEURON_TYPE_FINAL = 2;
    public static final byte NEURON_TYPE_MEDIUM = 3;
    public static final byte NEURON_TYPE_VIRTUAL = 4;

    private final HashSet<NeuroInput> in = new HashSet<NeuroInput>();
    private Double out = null;
    private byte type;
    private final int requiredInputs;
    private String label;
    private boolean isOutPassed = false;

    public void setOutPassed(boolean outPassed) {
        isOutPassed = outPassed;
    }

    public boolean isOutPassed() {
        return isOutPassed;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    protected Neuron(byte type, int requiredInputs) {
        this.type = type;
        this.requiredInputs = requiredInputs;
    }

    protected Neuron(byte type, int requiredInputs, String label) {
        this.type = type;
        this.requiredInputs = requiredInputs;
        setLabel(label);
    }

    public Double getOut() {
        return out;
    }

    public boolean hasOut() {
        return out != null;
    }

    public String getLabel() {
        return label;
    }

    public void setType(byte type) {
        this.type = type;
    }

    /*
    * @return true если достаточно входов для просчета результата
    * */
    public boolean addInput(NeuroInput p) {
        if (in.size() > requiredInputs) {
            return true;
        }
        in.add(p);
        if (in.size() == requiredInputs) {

            out = processInputs(sumInputs(in));
            return true;
        }
        return false;
    }

    public byte getType() {
        return type;
    }

    protected abstract double processInputs(double sum);

    private double sumInputs(HashSet<NeuroInput> inputs) {
        double sum = 0;
        for (NeuroInput input : inputs) {
            sum = sum + input.getValue() * input.getWeight();
        }
        return sum;
    }

    @Override
    public String toString() {
        return "\nNeuron{label=" + label + "," +
                "out=" + out +
                "}";
    }
}

