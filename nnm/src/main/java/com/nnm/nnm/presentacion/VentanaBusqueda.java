package com.nnm.nnm.presentacion;

import com.nnm.nnm.negocio.controller.GestorBusquedas;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class VentanaBusqueda extends JFrame {

    private JTextField tfDestino, tfBanos, tfHabitaciones, tfPrecioMin, tfPrecioMax;
    private JTextArea taResultados;
    private GestorBusquedas gestor;

    public VentanaBusqueda() {
        super("Búsqueda de Inmuebles");
        gestor = new GestorBusquedas();

        setLayout(new BorderLayout());

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 5, 5));

        tfDestino = new JTextField();
        tfBanos = new JTextField("1");
        tfHabitaciones = new JTextField("1");
        tfPrecioMin = new JTextField("0");
        tfPrecioMax = new JTextField("1000");

        panelFormulario.add(new JLabel("Destino:"));
        panelFormulario.add(tfDestino);

        panelFormulario.add(new JLabel("Baños (mínimo):"));
        panelFormulario.add(tfBanos);

        panelFormulario.add(new JLabel("Habitaciones (mínimo):"));
        panelFormulario.add(tfHabitaciones);

        panelFormulario.add(new JLabel("Precio mínimo:"));
        panelFormulario.add(tfPrecioMin);

        panelFormulario.add(new JLabel("Precio máximo:"));
        panelFormulario.add(tfPrecioMax);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(this::buscarInmuebles);
        panelFormulario.add(btnBuscar);

        add(panelFormulario, BorderLayout.NORTH);

        taResultados = new JTextArea(15, 50);
        taResultados.setEditable(false);
        add(new JScrollPane(taResultados), BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buscarInmuebles(ActionEvent e) {
        try {
            String destino = tfDestino.getText();
            int banos = Integer.parseInt(tfBanos.getText());
            int habitaciones = Integer.parseInt(tfHabitaciones.getText());
            double precioMin = Double.parseDouble(tfPrecioMin.getText());
            double precioMax = Double.parseDouble(tfPrecioMax.getText());

            List<Inmueble> resultados = gestor.buscar(destino, 0, banos, habitaciones, precioMin, precioMax);

            taResultados.setText("");
            for (Inmueble i : resultados) {
                taResultados.append(i.getTitulo() + " - " + i.getLocalidad() + ", " + i.getProvincia()
                        + " | " + i.getHabitaciones() + " hab | " + i.getNumero_banos() + " baños | "
                        + i.getPrecio_noche() + "€/noche\n");
            }

            if (resultados.isEmpty()) {
                taResultados.setText("No se encontraron inmuebles con esos filtros.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Introduce valores numéricos correctos para baños, habitaciones y precios.",
                    "Error de entrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaBusqueda::new);
    }
}
