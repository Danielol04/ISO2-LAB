package com.nnm.nnm.presentacion;

import com.nnm.nnm.negocio.controller.GestorBusquedas;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.List;
import javax.imageio.ImageIO;

public class VentanaBusqueda extends JFrame {

    private JTextField tfDestino, tfBanos, tfHabitaciones, tfPrecioMin, tfPrecioMax;
    private JPanel panelResultados;
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

        panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(panelResultados);
        add(scroll, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
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

            panelResultados.removeAll();

            if (resultados.isEmpty()) {
                JLabel lblNoResultados = new JLabel("No se encontraron inmuebles con esos filtros.");
                lblNoResultados.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelResultados.add(lblNoResultados);
            } else {
                for (Inmueble i : resultados) {
                    JPanel panelInmueble = new JPanel();
                    panelInmueble.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    panelInmueble.setLayout(new BorderLayout(10, 10));

                    // Foto
                    if (i.getFoto() != null) {
                        try {
                            ByteArrayInputStream bais = new ByteArrayInputStream(i.getFoto());
                            BufferedImage img = ImageIO.read(bais);
                            if (img != null) {
                                ImageIcon icon = new ImageIcon(img.getScaledInstance(150, 100, Image.SCALE_SMOOTH));
                                JLabel lblFoto = new JLabel(icon);
                                panelInmueble.add(lblFoto, BorderLayout.WEST);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    // Información
                    JTextArea taInfo = new JTextArea(
                            i.getTitulo() + "\n" +
                            i.getLocalidad() + ", " + i.getProvincia() + "\n" +
                            i.getTipo_inmueble() + "\n" +
                            i.getHabitaciones() + " hab | " +
                            i.getNumero_banos() + " baños | " +
                            i.getPrecio_noche() + " €/noche\n" +
                            i.getDireccion()
                    );
                    taInfo.setEditable(false);
                    taInfo.setBackground(null);
                    panelInmueble.add(taInfo, BorderLayout.CENTER);

                    panelResultados.add(panelInmueble);
                }
            }

            panelResultados.revalidate();
            panelResultados.repaint();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Introduce valores numéricos correctos para baños, habitaciones y precios.",
                    "Error de entrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaBusqueda::new);
    }
}
