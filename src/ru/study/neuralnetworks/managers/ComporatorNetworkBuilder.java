package ru.study.neuralnetworks.managers;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import ru.study.neuralnetworks.entity.ComporatorInfo;
import ru.study.neuralnetworks.entity.NeuronEdge;
import ru.study.neuralnetworks.neurons.BlackNeuron;
import ru.study.neuralnetworks.neurons.Neuron;
import ru.study.neuralnetworks.neurons.VirtualNeuron;
import ru.study.neuralnetworks.neurons.WhiteNeuron;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/16/13
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ComporatorNetworkBuilder {
    private final DirectedSparseGraph<Neuron, NeuronEdge> network;
    private int comporatorCount = 0;

    public ComporatorNetworkBuilder() {
        network = new DirectedSparseGraph<Neuron, NeuronEdge>();
    }

    public DirectedSparseGraph<Neuron, NeuronEdge> getNetwork() {
        return network;
    }

    ComporatorInfo addNewComporator(Neuron a, Neuron b) {
        comporatorCount++;
        //Если то к чему прицепляется компоратор раньше были финальными элементами
        // - то теперь нет, создаем новый компоратор - финальные элементы будут у него
        if (a.getType() == Neuron.NEURON_TYPE_FINAL) {
            a.setType(Neuron.NEURON_TYPE_MEDIUM);
        }
        if (b.getType() == Neuron.NEURON_TYPE_FINAL) {
            b.setType(Neuron.NEURON_TYPE_MEDIUM);
        }
        Neuron o1 = new WhiteNeuron(Neuron.NEURON_TYPE_MEDIUM, 2, comporatorId("o1"));
        Neuron o2 = new WhiteNeuron(Neuron.NEURON_TYPE_MEDIUM, 2, comporatorId("o2"));
        Neuron max = new WhiteNeuron(Neuron.NEURON_TYPE_FINAL, 4, comporatorId("max"));
        Neuron a1 = new BlackNeuron(Neuron.NEURON_TYPE_FINAL, 1, comporatorId("a'"));
        Neuron b1 = new BlackNeuron(Neuron.NEURON_TYPE_FINAL, 1, comporatorId("b'"));

        network.addVertex(a);
        network.addVertex(b);
        network.addVertex(o1);
        network.addVertex(o2);
        network.addVertex(a1);
        network.addVertex(b1);
        network.addVertex(max);

        /*
        *
        *  [a1] [max] [b1]
        *
        *  [o1]      [o2]
        *
        *  [a]       [b]
        * */
        network.addEdge(new NeuronEdge(1), o1, a1);
        network.addEdge(new NeuronEdge(0.5), o1, max);
        network.addEdge(new NeuronEdge(1), o2, b1);
        network.addEdge(new NeuronEdge(0.5), o2, max);

        network.addEdge(new NeuronEdge(1), a, o1);
        network.addEdge(new NeuronEdge(-1), a, o2);
        network.addEdge(new NeuronEdge(1), b, o2);
        network.addEdge(new NeuronEdge(-1), b, o1);

        network.addEdge(new NeuronEdge(0.5), b, max);
        network.addEdge(new NeuronEdge(0.5), a, max);
        ComporatorInfo ci = new ComporatorInfo();
        ci.setaStroke(a1);
        ci.setbStroke(b1);
        ci.setMax(max);
        return ci;
    }

    public ComporatorInfo buildFromDoubleArray(double[] values) {
        if (isPowOf2(values.length)) {
            return pow2ComporatorNetwork(values);
        } else {
            return non2powComparatorNetwork(values);
        }
    }
    //здесь строим сеть при случае если длина значений у массива не является степень двойки
    //Причем мы ее строим не с нуля, а имея уже какой-то компоратор в схемме
    private ComporatorInfo non2powComparatorNetwork(double[] values, ComporatorInfo topLevel) {
        //Если осталось 1 значение то его напрямую подключаем к уже существующему компоратору
        if (values.length == 1) {
            Neuron lastNeuron = new VirtualNeuron(values[0], "VALUE[direct!]");
            return addNewComporator(topLevel.getMax(), lastNeuron);
        }
        //Если длина массива значений = степень двойки, то создаем компоратор для нее и подключаем к уже существующему
        if (isPowOf2(values.length)) {
            //Вопрос решать умеем
            ComporatorInfo ci = pow2ComporatorNetwork(values);
            return addNewComporator(topLevel.getMax(), ci.getMax()); // а это последний компоратор
        }


        //Это значит что нечетная длина у массива..
        //Отделяем степень двойки
        int near = near2Pow(values.length);
        double[] pow2values = Arrays.copyOfRange(values, 0, near);
        double[] rest = Arrays.copyOfRange(values, near, values.length);
        //Соединяем его компоратор с нашим, и разбираемся с остатком
        ComporatorInfo powComporator = pow2ComporatorNetwork(pow2values);
        ComporatorInfo newTopLevel = addNewComporator(topLevel.getMax(), powComporator.getMax());
        return non2powComparatorNetwork(rest, newTopLevel);
    }

    private ComporatorInfo non2powComparatorNetwork(double[] values) {
        //Это ближайшее число степени двойки, для массива этой длины строим сеть, это мы уже умеем.
        int near = near2Pow(values.length);
        double[] pow2values = Arrays.copyOfRange(values, 0, near);
        double[] rest = Arrays.copyOfRange(values, near, values.length);
        //Построили сеть, терь рекурсивно повторяем для остатка
        ComporatorInfo pow2Comporator = pow2ComporatorNetwork(pow2values);
        return non2powComparatorNetwork(rest, pow2Comporator);
    }

    private double log2(int value) {
        return Math.log(value) / Math.log(2);
    }
     //Вернет ближайшую степень двойки к заданному числу, при этом не превышая это число
    private int near2Pow(int x) {
        double log = log2(x);
        int intlog = (int) log;
        return (int) Math.pow(2, intlog);
    }

    private boolean isPowOf2(int value) {
        return (value & (value - 1)) == 0;
    }

    private String comporatorId(String text) {
        return text + "{" + comporatorCount + "}";
    }

    private ComporatorInfo pow2ComporatorNetwork(double[] values) {
        //длина массива = степень двойки, попарно сравниваем пока не дойдем до верху
        int levelCount = (int) log2(values.length);
        ArrayList<ComporatorInfo> prevComporators = new ArrayList<ComporatorInfo>();
        ArrayList<ComporatorInfo> currentComporators = new ArrayList<ComporatorInfo>();
        ComporatorInfo topLevelComporator = null;
        for (int i = 0; i < levelCount; i++) {
            currentComporators.clear();
            //Первый проход, исходные данные для компораторов - значения из массива
            if (i == 0) {
                for (int q = 0; q < values.length - 1; q = q + 2) {
                    Neuron a = new VirtualNeuron(values[q], "INPUT[A]");
                    Neuron b = new VirtualNeuron(values[q + 1], "INPUT[B]" );
                    ComporatorInfo ci = addNewComporator(a, b);
                    prevComporators.add(ci);
                }
            }
            //Если не первый проход, и не последний
            else if (i != (levelCount - 1)) {
                //Берем данные для своего компоратора из ранее созданных компораторов
                for (int j = 0; j < prevComporators.size() - 1; j = j + 2) {
                    Neuron a = prevComporators.get(j).getMax();
                    Neuron b = prevComporators.get(j + 1).getMax();
                    currentComporators.add(addNewComporator(a, b));
                }
                prevComporators = (ArrayList<ComporatorInfo>) currentComporators.clone();
            }
            //Если последний
            if (i == (levelCount - 1)) {
                if (prevComporators.size() == 2) {
                    Neuron a = prevComporators.get(0).getMax();
                    Neuron b = prevComporators.get(1).getMax();
                    topLevelComporator = addNewComporator(a, b);
                } else if (prevComporators.size() == 1) {
                    //Это может быть если сравниваем всего 2 числа
                    return prevComporators.get(0);
                }

            }
        }
        return topLevelComporator;
    }
}
