
package com.nnm.nnm.presentacion;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.nnm.nnm.negocio.controller.GestorBusquedas;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;

public class VentanaBusqueda extends JFrame {

    private JTextField tfDestino;
    private JTextField tfBanos;
    private JTextField tfHabitaciones;
    private JTextField tfPrecioMin;
    private JTextField tfPrecioMax;
    private JTextArea taResultados;

    private transient GestorBusquedas gestorBusquedas;

    public VentanaBusqueda(GestorBusquedas gestorBusquedas) {
        super("Búsqueda de Inmuebles");
        this.gestorBusquedas = gestorBusquedas;

        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));

        tfDestino = new JTextField();
        tfBanos = new JTextField();
        tfHabitaciones = new JTextField();
        tfPrecioMin = new JTextField();
        tfPrecioMax = new JTextField();

        panel.add(new JLabel("Destino"));
        panel.add(tfDestino);
        panel.add(new JLabel("Baños (mínimo)"));
        panel.add(tfBanos);
        panel.add(new JLabel("Habitaciones (mínimo)"));
        panel.add(tfHabitaciones);
        panel.add(new JLabel("Precio mínimo"));
        panel.add(tfPrecioMin);
        panel.add(new JLabel("Precio máximo"));
        panel.add(tfPrecioMax);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(this::buscar);

        panel.add(btnBuscar);
        add(panel, BorderLayout.NORTH);

        taResultados = new JTextArea();
        taResultados.setEditable(false);
        add(new JScrollPane(taResultados), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buscar(ActionEvent e) {
        try {
            String destino = tfDestino.getText();
            Integer banos = tfBanos.getText().isBlank() ? null : Integer.parseInt(tfBanos.getText());
            Integer habitaciones = tfHabitaciones.getText().isBlank() ? null : Integer.parseInt(tfHabitaciones.getText());
            Double precioMin = tfPrecioMin.getText().isBlank() ? null : Double.parseDouble(tfPrecioMin.getText());
            Double precioMax = tfPrecioMax.getText().isBlank() ? null : Double.parseDouble(tfPrecioMax.getText());

            List<Inmueble> resultados = gestorBusquedas.buscar(
                    destino, habitaciones, banos, precioMin, precioMax
            );

            taResultados.setText("");

            if (resultados.isEmpty()) {
                taResultados.setText("No se encontraron inmuebles.");
                return;
            }

            for (Inmueble i : resultados) {
                taResultados.append(
                        i.getLocalidad() + ", " + i.getProvincia() +
                        " | " + i.getHabitaciones() + " hab | " +
                        i.getNumero_banos() + " baños | " +
                        i.getPrecio_noche() + " €/noche\n"
                );
            }

        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this,
                    "Valores numéricos incorrectos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}



