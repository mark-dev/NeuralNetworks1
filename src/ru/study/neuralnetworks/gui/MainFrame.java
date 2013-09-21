package ru.study.neuralnetworks.gui;

import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import org.apache.commons.collections15.Transformer;
import ru.study.neuralnetworks.entity.ComporatorInfo;
import ru.study.neuralnetworks.entity.NeuronEdge;
import ru.study.neuralnetworks.managers.ComporatorNetworkBuilder;
import ru.study.neuralnetworks.managers.NeuroNetworkSolver;
import ru.study.neuralnetworks.neurons.BlackNeuron;
import ru.study.neuralnetworks.neurons.Neuron;
import ru.study.neuralnetworks.neurons.VirtualNeuron;
import ru.study.neuralnetworks.neurons.WhiteNeuron;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.StringTokenizer;


/**
 * Created by Mark
 */
public class MainFrame extends JFrame {


    private MainFrame() {
        setTitle("comporator");
        initGUI();
    }

    private void initGUI() {
        int WIDTH = 500;
        int HEIGHT = 500;
        Color lightBlue = new Color(51, 204, 255);   // light blue
        Color lightYellow = new Color(255, 255, 215);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //init
        panelCenter = new JPanel();
        panelTop = new JPanel();
        panelBottom = new JPanel();
        buttonOk = new JButton("Ok");
        buttonSetRandom = new JButton("rnd");
        fieldInput = new JTextField("");
        resultLabel = new JLabel("");
        Font font = resultLabel.getFont();
        Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
        resultLabel.setFont(boldFont);
        //set bg Color
        panelTop.setBackground(lightBlue);
        panelCenter.setBackground(lightYellow);
        panelBottom.setBackground(lightBlue);
        buttonOk.setBackground(lightBlue);
        //preffered size
        panelTop.setPreferredSize(new Dimension(WIDTH, (int) (0.1 * HEIGHT)));
        panelCenter.setPreferredSize(new Dimension(WIDTH, (int) (0.8 * HEIGHT)));
        panelBottom.setPreferredSize(new Dimension(WIDTH, (int) (0.1 * HEIGHT)));
        fieldInput.setPreferredSize(new Dimension((int) (0.6 * WIDTH), (int) (0.08 * HEIGHT)));
        buttonOk.setPreferredSize(new Dimension((int) (0.12 * WIDTH), (int) (0.08 * HEIGHT)));
        buttonSetRandom.setPreferredSize(new Dimension((int) (0.12 * WIDTH), (int) (0.08 * HEIGHT)));
        // borders
        panelTop.setBorder(BorderFactory.createEtchedBorder());
        //layouts
        panelTop.setLayout(new FlowLayout());
        panelCenter.setLayout(new BorderLayout());
        // action listeners
        buttonOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                buttonOkActionPerformed();
            }
        });
        buttonSetRandom.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                buttonRandomActionPerformed();
            }
        });
        fieldInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buttonOkActionPerformed();
                }
            }
        });
        // add components to panels
        panelBottom.add(resultLabel);
        panelBottom.add(fieldInput);
        panelBottom.add(buttonOk);
        panelBottom.add(buttonSetRandom);
        //add panels to frame
        getContentPane().add(panelCenter, BorderLayout.CENTER);
        getContentPane().add(panelTop, BorderLayout.NORTH);
        getContentPane().add(panelBottom, BorderLayout.SOUTH);

        fieldInput.requestFocusInWindow();
        buttonRandomActionPerformed();
        buttonOkActionPerformed();
        pack();
    }

    private void buttonRandomActionPerformed() {
        StringBuilder sb = new StringBuilder();
        Random randomGenerator = new Random();
        int max = randomGenerator.nextInt(8) + 2;
        for (int i = 0; i < max; i++) {
            sb.append(randomGenerator.nextInt(100));
            if (!(i == (max - 1))) {
                sb.append(";");
            }
        }
        fieldInput.setText(sb.toString());
        clearResult();
    }

    private void buildNetwork(double[] values) throws Exception {
        boolean isFirstClick = true;
        if (sp != null) {
            panelCenter.remove(sp);
            isFirstClick = false;
        }
        ComporatorNetworkBuilder comporatorNetworkBuilder = new ComporatorNetworkBuilder();
        ComporatorInfo ci = comporatorNetworkBuilder.buildFromDoubleArray(values);
        DirectedSparseGraph<Neuron, NeuronEdge> neuroNetwork = comporatorNetworkBuilder.getNetwork();
        new NeuroNetworkSolver(neuroNetwork).solve();
        VisualizationViewer vs = getVIS(neuroNetwork);
        sp = new GraphZoomScrollPane(vs);
        panelCenter.add(sp, BorderLayout.CENTER);

        if (isFirstClick) {
            panelCenter.setPreferredSize(vs.getPreferredSize());
        }
        else
        {
            //Чтобы размер окна не увеличивался, если его уменьшили
            panelCenter.setPreferredSize(panelCenter.getSize());
        }
        setResult(ci);
        pack();
    }

    private double[] parseInput() {
        StringTokenizer st = new StringTokenizer(fieldInput.getText(), ";");
        int len = st.countTokens();
        double[] values = new double[len];
        int i = 0;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            values[i] = Double.parseDouble(token);
            i++;
        }
        return values;
    }

    private void buttonOkActionPerformed() {
        try {
            double[] values = parseInput();
            if (values.length > 1)
                buildNetwork(values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setResult(ComporatorInfo ci) {
        resultLabel.setText("max: " + ci.getMax().getOut());
    }

    private void clearResult() {
        resultLabel.setText("");
    }

    private VisualizationViewer getVIS(Graph g) {
        VisualizationViewer vv = new VisualizationViewer(new KKLayout(g), new Dimension(1000, 1000));
        vv.getRenderContext().setVertexLabelTransformer(new Transformer<Neuron, String>() {
            @Override
            public String transform(Neuron n) {
                return "[" + n.getLabel() + ";" + n.getOut() + "]";
            }
        });
        vv.getRenderContext().setEdgeLabelTransformer(new Transformer<NeuronEdge, String>() {
            @Override
            public String transform(NeuronEdge neuronEdge) {
                return "" + neuronEdge.getWeight();
            }
        });
        vv.getRenderContext().setVertexFillPaintTransformer(new Transformer<Neuron, Paint>() {

            @Override
            public Paint transform(Neuron neuron) {
                if (neuron instanceof BlackNeuron)
                    return new Color(101, 96, 96);
                else if (neuron instanceof WhiteNeuron)
                    return new Color(187, 247, 250);
                else if (neuron instanceof VirtualNeuron)
                    return new Color(240, 160, 65);
                else return Color.RED;
            }
        });
        return vv;
    }

    public static void main(String[] args) throws
            ClassNotFoundException,
            UnsupportedLookAndFeelException,
            InstantiationException,
            IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new MainFrame().setVisible(true);
    }

    //-----------------------
    private JPanel panelTop;
    private JPanel panelBottom;
    private JPanel panelCenter;
    private GraphZoomScrollPane sp;
    private JButton buttonOk;
    private JButton buttonSetRandom;
    private JTextField fieldInput;
    private JLabel resultLabel;
}
