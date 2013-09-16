package ru.study.core;


import java.awt.Dimension;
import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import org.apache.commons.collections15.Transformer;
import ru.study.core.entity.ComporatorInfo;
import ru.study.core.entity.NeuronEdge;
import ru.study.core.managers.ComporatorNetworkBuilder;
import ru.study.core.managers.NeuroNetworkSolver;
import ru.study.core.neurons.Neuron;

public class Jung {
    public static void main(String[] args) {
        ComporatorNetworkBuilder builder = new ComporatorNetworkBuilder();
        double[] vargs = {27.0, 16.0, 30.0, 13.0, 85.0, 63.0, 45.0, 69.0, 7.0, 8.0, 71.0, 76.0, 11.0, 19.0};
       ComporatorInfo ci =  builder.buildFromDoubleArray(vargs);
        DirectedSparseGraph<Neuron, NeuronEdge> g = builder.getNetwork();
        new NeuroNetworkSolver(g).solve();
        VisualizationImageServer vs =
                new VisualizationImageServer(new CircleLayout(g), new Dimension(800, 800));
        vs.getRenderContext().setVertexLabelTransformer(new Transformer<Neuron, String>() {
            @Override
            public String transform(Neuron n) {
                return "[" + n.getLabel() + ";" + n.getOut() + "]";
            }
        });
        vs.getRenderContext().setEdgeLabelTransformer(new Transformer<NeuronEdge, String>() {

            @Override
            public String transform(NeuronEdge neuronEdge) {
                return "" + neuronEdge.getWeight();
            }
        });
        JFrame frame = new JFrame();
        frame.getContentPane().add(vs);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        System.out.println("max value: " + ci.getMax().getOut());
    }
}
