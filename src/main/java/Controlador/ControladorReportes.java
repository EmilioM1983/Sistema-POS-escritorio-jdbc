package Controlador;

import Configuracion.Conexion;
import Modelo.Cliente;
import Modelo.Producto;
import com.toedter.calendar.JDateChooser;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Emilio Mayer
 */
public class ControladorReportes {

    Conexion conexion = new Conexion();
    Cliente cliente = new Cliente();
    Producto productos = new Producto();

    public void datosClientesComprobantes(JTextField nComprobante, JLabel nComprobanteEncontrado, JLabel fecha, JLabel nombreCliente, JLabel apellidoCliente) {
        try {
            String sql = "SELECT comprobante.idComprobante, comprobante.fechaComprobante, cliente.nombres, cliente.apellido "
                    + "FROM comprobante INNER JOIN cliente ON cliente.idCliente = comprobante.fkCliente WHERE comprobante.idComprobante = ?";

            PreparedStatement ps = conexion.establecerConexion().prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(nComprobante.getText()));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                nComprobanteEncontrado.setText(String.valueOf(rs.getInt("idComprobante")));
                fecha.setText(rs.getDate("fechaComprobante").toString());
                nombreCliente.setText(rs.getString("nombres"));
                apellidoCliente.setText(rs.getString("apellido"));
            } else {
                nComprobanteEncontrado.setText("");
                fecha.setText("");
                nombreCliente.setText("");
                apellidoCliente.setText("");

                JOptionPane.showMessageDialog(null, "No se hallo el comprobante");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el comprobante: " + e.toString());
        } finally {
            conexion.cerrarConexion();
        }
    }

    public void mostrarComprobantesProductos(JTextField nComprobante, JTable tbProductos, JLabel total) {

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Nombre");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Precio");
        modelo.addColumn("Subtotal");

        tbProductos.setModel(modelo);

        try {
            String sql = "SELECT producto.nombre, detalle.cantidad, detalle.precioVenta "
                    + "FROM detalle "
                    + "INNER JOIN comprobante ON comprobante.idComprobante = detalle.fkComprobante "
                    + "INNER JOIN producto ON producto.idProducto = detalle.fkProducto "
                    + "WHERE comprobante.idComprobante = ?";

            PreparedStatement ps = conexion.establecerConexion().prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(nComprobante.getText()));

            ResultSet rs = ps.executeQuery();

            double totalVenta = 0.0;
            DecimalFormat formato = new DecimalFormat("#.##");

            while (rs.next()) {
                String nombreProducto = rs.getString("nombre");
                int cantidad = rs.getInt("cantidad");
                double precioVenta = rs.getDouble("precioVenta");
                double subtotal = cantidad * precioVenta;

                totalVenta = Double.parseDouble(formato.format(totalVenta + subtotal));

                modelo.addRow(new Object[]{nombreProducto, cantidad, precioVenta, subtotal});
            }

            total.setText(String.valueOf(totalVenta));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar productos: " + e.toString());
        } finally {
            conexion.cerrarConexion();
        }
        
        for (int column = 0; column < tbProductos.getColumnCount(); column++) {
            Class<?> columnClass = tbProductos.getColumnClass(column);
            tbProductos.setDefaultEditor(columnClass, null);
        }
    }

    public void mostrarReportesFechas(JDateChooser desde, JDateChooser hasta, JTable tbVentas, JLabel total) {

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("idComprobante");
        modelo.addColumn("fecha comprobante");
        modelo.addColumn("Nombre Producto");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Precio de venta");
        modelo.addColumn("Subtotal");

        tbVentas.setModel(modelo);

        try {

            String sql = "SELECT comprobante.idComprobante, comprobante.fechaComprobante, producto.nombre, detalle.cantidad, detalle.precioVenta "
                    + "FROM detalle "
                    + "INNER JOIN comprobante ON comprobante.idComprobante = detalle.fkComprobante "
                    + "INNER JOIN producto ON producto.idProducto = detalle.fkProducto "
                    + "WHERE comprobante.fechaComprobante BETWEEN ? AND ?";

            PreparedStatement ps = conexion.establecerConexion().prepareStatement(sql);

            // Obtener fechas de JDateChooser y convertirlas a java.sql.Date
            java.util.Date fechaDesde = desde.getDate();
            java.util.Date fechaHasta = hasta.getDate();

            if (fechaDesde == null || fechaHasta == null) {
                JOptionPane.showMessageDialog(null, "Por favor, seleccione ambas fechas.");
                return;
            }

            java.sql.Date fechaDesdeSQL = new java.sql.Date(fechaDesde.getTime());
            java.sql.Date fechaHastaSQL = new java.sql.Date(fechaHasta.getTime());

            // Asignar las fechas al PreparedStatement
            ps.setDate(1, fechaDesdeSQL);
            ps.setDate(2, fechaHastaSQL);

            ResultSet rs = ps.executeQuery();

            double totalVenta = 0.0;
            DecimalFormat formato = new DecimalFormat("#.##");

            while (rs.next()) {
                int IdComprobande = rs.getInt("idComprobante");
                Date fechaComprobante = rs.getDate("fechaComprobante");
                String nombre = rs.getString("nombre");
                int cantidad = rs.getInt("cantidad");
                double precio = rs.getDouble("precioVenta");
                double subtotal = cantidad * precio;

                totalVenta += subtotal;

                modelo.addRow(new Object[]{IdComprobande, fechaComprobante, nombre, cantidad, precio, subtotal});
            }

            total.setText(String.format("%.2f", totalVenta));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar los ingresos por fecha: " + e.toString());
        } finally {
            conexion.cerrarConexion();
        }

        // Configurar tabla como no editable
        for (int column = 0; column < tbVentas.getColumnCount(); column++) {
            Class<?> columnClass = tbVentas.getColumnClass(column);
            tbVentas.setDefaultEditor(columnClass, null);
        }
    }

}
